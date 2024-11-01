package com.xenosnowfox.immawiki;

import com.xenosnowfox.immawiki.api.ApiStack;
import com.xenosnowfox.immawiki.config.DeploymentProperties;
import com.xenosnowfox.immawiki.config.SiteProperties;
import java.io.IOException;
import software.amazon.awscdk.App;

/**
 * The entry point of CDK application. This class creates a CDK App with two stacks 1. DbStack
 * contains network resources (e.g. VPC, subnets), MySQL DB, DB Proxy, and secrets 2. ApiStack
 * contains API Gateway and Lambda functions for compute
 *
 * <p>We separate the two stacks from each other as they have different life cycles. The ApiStack
 * will be updated more frequently while the DbStack should be rarely updated. This also allows us
 * to put different permission settings for each stack (e.g. prevent an innocent intern deleting
 * your DB accidentally).
 */
public class Application {

  /**
   * Entry point of the CDK CLI.
   *
   * @param args Not used
   * @throws IOException can be thrown from ApiStack as it read and build Lambda package
   */
  public static void main(final String[] args) throws IOException {
    final App app = new App();
    Application.synth(app);
    app.synth();
  }

  private static void synth(final App app) throws IOException {
    new ApiStack(
        app,
        DeploymentProperties.IDENTIFIER.getValue() + "-stack",
        ApiStack.ApiStackProps.builder()
            .description(SiteProperties.IMMAWIKI_SITE_NAME.getValue() + " Application Stack")
            .build());
  }
}
