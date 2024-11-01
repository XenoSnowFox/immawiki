package com.xenosnowfox.immawiki.api;

import com.xenosnowfox.immawiki.config.DeploymentProperties;
import com.xenosnowfox.immawiki.config.SiteProperties;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.Data;
import software.amazon.awscdk.Duration;
import software.amazon.awscdk.Environment;
import software.amazon.awscdk.RemovalPolicy;
import software.amazon.awscdk.Stack;
import software.amazon.awscdk.StackProps;
import software.amazon.awscdk.services.apigateway.AwsIntegration;
import software.amazon.awscdk.services.apigateway.IntegrationOptions;
import software.amazon.awscdk.services.apigateway.IntegrationResponse;
import software.amazon.awscdk.services.apigateway.LambdaIntegration;
import software.amazon.awscdk.services.apigateway.LambdaRestApi;
import software.amazon.awscdk.services.apigateway.MethodOptions;
import software.amazon.awscdk.services.apigateway.MethodResponse;
import software.amazon.awscdk.services.apigateway.MockIntegration;
import software.amazon.awscdk.services.apigateway.Model;
import software.amazon.awscdk.services.apigateway.Resource;
import software.amazon.awscdk.services.apigateway.StageOptions;
import software.amazon.awscdk.services.iam.Effect;
import software.amazon.awscdk.services.iam.PolicyDocument;
import software.amazon.awscdk.services.iam.PolicyStatement;
import software.amazon.awscdk.services.iam.Role;
import software.amazon.awscdk.services.iam.ServicePrincipal;
import software.amazon.awscdk.services.lambda.Code;
import software.amazon.awscdk.services.lambda.Function;
import software.amazon.awscdk.services.lambda.Runtime;
import software.amazon.awscdk.services.logs.RetentionDays;
import software.amazon.awscdk.services.s3.Bucket;
import software.amazon.awscdk.services.s3.BucketAccessControl;
import software.amazon.awscdk.services.s3.deployment.BucketDeployment;
import software.amazon.awscdk.services.s3.deployment.Source;
import software.constructs.Construct;

public class ApiStack extends Stack {

  @lombok.Builder
  @Data
  public static class ApiStackProps implements StackProps {
    private String description;
    private Environment env;
  }

  public ApiStack(final Construct scope, final String id, final ApiStackProps props)
      throws IOException {
    super(scope, id, props);

    createApiGateway(props);
  }

  private void createApiGateway(ApiStackProps props) throws IOException {
    final String apiStageName = "production";

    Function helloWorldFunction =
        Function.Builder.create(
                this, DeploymentProperties.IDENTIFIER.getValue() + "-lambda-homepage-retrieval")
            .runtime(Runtime.JAVA_21)
            .code(
                Code.fromAsset(
                    "./build/modules/lambda-homepage-retrieval/libs/immawiki-homepage-retrieval.jar"))
            .handler(
                "com.xenosnowfox.immawiki.lambda.homepageretrieval.ApiGatewayHandler::handleRequest")
            .memorySize(512)
            .timeout(Duration.seconds(300))
            .logRetention(RetentionDays.ONE_WEEK)
            .build();

    // List all config files
    Map<String, String> stageVariables = new HashMap<>();
    Arrays.stream(SiteProperties.values())
        .forEach(property -> stageVariables.put(property.name(), property.getValue()));

    // Define the API Gateway resource
    LambdaRestApi api =
        LambdaRestApi.Builder.create(
                this, DeploymentProperties.IDENTIFIER.getValue() + "-api-gateway")
            .handler(helloWorldFunction)
            .proxy(false) // Turn off default proxy integration
            .deploy(true)
            .deployOptions(
                StageOptions.builder().stageName("production").variables(stageVariables).build())
            .build();

    // Define the '/' resource and its GET method
    api.getRoot()
        .addMethod(
            "GET",
            LambdaIntegration.Builder.create(helloWorldFunction)
                .proxy(true)
                .integrationResponses(
                    List.of(IntegrationResponse.builder().statusCode("200").build()))
                .build(),
            MethodOptions.builder()
                .methodResponses(List.of(MethodResponse.builder().statusCode("200").build()))
                .build());

    // Set up S3 bucket for storing static resources
    final Bucket bucket =
        Bucket.Builder.create(this, DeploymentProperties.IDENTIFIER.getValue() + "-static-resource")
            .bucketName(DeploymentProperties.IDENTIFIER.getValue() + "-static-resources")
            .publicReadAccess(false)
            .accessControl(BucketAccessControl.PRIVATE)
            .removalPolicy(RemovalPolicy.DESTROY)
            .autoDeleteObjects(true)
            .build();
    Role role =
        Role.Builder.create(this, DeploymentProperties.IDENTIFIER.getValue() + "-api-gateway-role")
            .assumedBy(ServicePrincipal.fromStaticServicePrincipleName("apigateway.amazonaws.com"))
            .inlinePolicies(
                Map.of(
                    "s3read",
                    PolicyDocument.Builder.create()
                        .statements(
                            List.of(
                                PolicyStatement.Builder.create()
                                    .actions(List.of("s3:GetObject"))
                                    .effect(Effect.ALLOW)
                                    .resources(List.of("*"))
                                    .build()))
                        .build()))
            .build();

    api.getRoot()
        .addResource("fonts")
        .addResource("{object}")
        .addMethod(
            "GET",
            AwsIntegration.Builder.create()
                .service("s3")
                .integrationHttpMethod("GET")
                .path(
                    DeploymentProperties.IDENTIFIER.getValue() + "-static-resources/fonts/{object}")
                .options(
                    IntegrationOptions.builder()
                        .requestParameters(
                            Map.of("integration.request.path.object", "method.request.path.object"))
                        .integrationResponses(
                            List.of(IntegrationResponse.builder().statusCode("200").build()))
                        .credentialsRole(role)
                        .build())
                .build(),
            MethodOptions.builder()
                .requestParameters(Map.of("method.request.path.object", true))
                .methodResponses(List.of(MethodResponse.builder().statusCode("200").build()))
                .build());

    api.getRoot()
        .addResource("styles")
        .addResource("{object}")
        .addMethod(
            "GET",
            AwsIntegration.Builder.create()
                .service("s3")
                .integrationHttpMethod("GET")
                .path(
                    DeploymentProperties.IDENTIFIER.getValue()
                        + "-static-resources/styles/{object}")
                .options(
                    IntegrationOptions.builder()
                        .requestParameters(
                            Map.of("integration.request.path.object", "method.request.path.object"))
                        .integrationResponses(
                            List.of(
                                IntegrationResponse.builder()
                                    .statusCode("200")
                                    .responseParameters(
                                        Map.of(
                                            "method.response.header.Content-Type",
                                            "integration.response.header.Content-Type"))
                                    .build()))
                        .credentialsRole(role)
                        .build())
                .build(),
            MethodOptions.builder()
                .requestParameters(Map.of("method.request.path.object", true))
                .methodResponses(
                    List.of(
                        MethodResponse.builder()
                            .statusCode("200")
                            .responseParameters(Map.of("method.response.header.Content-Type", true))
                            .build()))
                .build());

    // Upload fonts to S3 bucket
    BucketDeployment.Builder.create(
            this, DeploymentProperties.IDENTIFIER.getValue() + "-static-resources-deployment")
        .sources(List.of(Source.asset("./build/modules/immawiki-static-resources")))
        .destinationBucket(bucket)
        .prune(true)
        .build();
  }

  private void appendStaticCSSResource(final Resource resource, final String content) {

    MockIntegration mockIntegration =
        MockIntegration.Builder.create()
            .requestTemplates(Map.of("application/json", "{ \"statusCode\": 200 }"))
            .integrationResponses(
                List.of(
                    IntegrationResponse.builder()
                        .statusCode("200")
                        .responseTemplates(Map.of("text/css", content))
                        .build()))
            .build();

    MethodOptions methodOptions =
        MethodOptions.builder()
            .methodResponses(
                List.of(
                    MethodResponse.builder()
                        .statusCode("200")
                        .responseModels(Map.of("text/css", Model.EMPTY_MODEL))
                        .build()))
            .build();

    resource.addMethod("HEAD", mockIntegration, methodOptions);
    resource.addMethod("GET", mockIntegration, methodOptions);
  }
}
