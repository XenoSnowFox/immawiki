package com.xenosnowfox.immawiki.api;

import java.io.IOException;
import lombok.Data;
import software.amazon.awscdk.Duration;
import software.amazon.awscdk.Environment;
import software.amazon.awscdk.Stack;
import software.amazon.awscdk.StackProps;
import software.amazon.awscdk.services.apigateway.LambdaRestApi;
import software.amazon.awscdk.services.apigateway.Resource;
import software.amazon.awscdk.services.apigateway.StageOptions;
import software.amazon.awscdk.services.lambda.Code;
import software.amazon.awscdk.services.lambda.Function;
import software.amazon.awscdk.services.lambda.Runtime;
import software.amazon.awscdk.services.logs.RetentionDays;
import software.constructs.Construct;

public class ApiStack extends Stack {

  @lombok.Builder
  @Data
  public static class ApiStackProps implements StackProps {
    private String description;
    private String alertEmail;
    private Boolean deployPackagingApi;
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
        Function.Builder.create(this, "HomepageRetrievalFunction")
            .runtime(Runtime.JAVA_21)
            .code(
                Code.fromAsset(
                    "./build/modules/homepage-retrieval/libs/immawiki-homepage-retrieval.jar"))
            .handler("com.xenosnowfox.immawiki.ApiGatewayHandler::handleRequest")
            .memorySize(512)
            .timeout(Duration.seconds(300))
            .logRetention(RetentionDays.ONE_WEEK)
            .build();

    // Define the API Gateway resource
    LambdaRestApi api =
        LambdaRestApi.Builder.create(this, "HelloWorldApi")
            .handler(helloWorldFunction)
            .proxy(false) // Turn off default proxy integration
            .deploy(true)
            .deployOptions(StageOptions.builder().stageName("production").build())
            .build();

    // Define the '/hello' resource and its GET method
    Resource helloResource = api.getRoot().addResource("hello");
    helloResource.addMethod("GET");
  }
}
