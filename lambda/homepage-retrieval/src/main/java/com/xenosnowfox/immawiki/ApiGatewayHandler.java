package com.xenosnowfox.immawiki;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayV2HTTPEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayV2HTTPResponse;
import java.util.HashMap;

public class ApiGatewayHandler
    implements RequestHandler<APIGatewayV2HTTPEvent, APIGatewayV2HTTPResponse> {
  @Override
  public APIGatewayV2HTTPResponse handleRequest(
      final APIGatewayV2HTTPEvent event, final Context context) {
    LambdaLogger logger = context.getLogger();
    APIGatewayV2HTTPResponse response = new APIGatewayV2HTTPResponse();
    response.setIsBase64Encoded(false);
    response.setStatusCode(200);
    HashMap<String, String> headers = new HashMap<String, String>();
    headers.put("Content-Type", "text/html");
    response.setHeaders(headers);
    response.setBody(
        "<!DOCTYPE html><html><head><title>Hello World</title></head><body>"
            + "<h1>Hello World</h1>"
            + "</body></html>");
    Util.logEnvironment(event, context);
    return response;
  }
}
