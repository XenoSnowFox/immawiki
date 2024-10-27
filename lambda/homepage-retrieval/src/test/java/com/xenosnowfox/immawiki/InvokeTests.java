package com.xenosnowfox.immawiki;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.events.APIGatewayV2HTTPEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayV2HTTPResponse;
import java.util.logging.Logger;
import org.junit.jupiter.api.Test;

public class InvokeTests {
  private static final Logger logger = Logger.getLogger(TestLogger.class.getName());

  @Test
  void invokeTest() {
    logger.info("Invoke TEST");
    APIGatewayV2HTTPEvent event = new APIGatewayV2HTTPEvent();
    Context context = new TestContext();
    String requestId = context.getAwsRequestId();
    ApiGatewayHandler handler = new ApiGatewayHandler();
    APIGatewayV2HTTPResponse result = handler.handleRequest(event, context);
    assertEquals(result.getStatusCode(), 200);
  }
}
