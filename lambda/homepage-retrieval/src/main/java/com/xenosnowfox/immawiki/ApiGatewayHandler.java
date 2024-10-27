package com.xenosnowfox.immawiki;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayV2HTTPEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayV2HTTPResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import java.util.HashMap;

public class ApiGatewayHandler
    implements RequestHandler<APIGatewayV2HTTPEvent, APIGatewayV2HTTPResponse> {

  private static final ObjectMapper OBJECT_MAPPER =
      new ObjectMapper().disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);

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
    try {
      response.setBody(
          "<!DOCTYPE html><html><head><title>Hello World</title></head><body>"
              + "<h1>Hello World</h1>"
              + "<p>ENVIRONMENT: <pre>"
              + OBJECT_MAPPER.writerWithDefaultPrettyPrinter().writeValueAsString(System.getenv())
              + "</pre></p>"
              + "<p>CONTEXT: <pre>"
              + OBJECT_MAPPER.writerWithDefaultPrettyPrinter().writeValueAsString(context)
              + "</pre></p>"
              + "<p>EVENT: <pre>"
              + OBJECT_MAPPER.writerWithDefaultPrettyPrinter().writeValueAsString(event)
              + "</pre></p>"
              + "</body></html>");
    } catch (JsonProcessingException e) {
      throw new RuntimeException(e);
    }
    Util.logEnvironment(event, context);
    return response;
  }
}
