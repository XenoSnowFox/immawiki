package com.xenosnowfox.immawiki.lambda.homepageretrieval;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayV2HTTPEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayV2HTTPResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.xenosnowfox.immawiki.library.thymeleafutilities.TemplateEngineFactory;
import java.util.HashMap;
import java.util.Map;

public class ApiGatewayHandler
    implements RequestHandler<APIGatewayV2HTTPEvent, APIGatewayV2HTTPResponse> {

  private static final ObjectMapper OBJECT_MAPPER =
      new ObjectMapper().disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);

  @Override
  public APIGatewayV2HTTPResponse handleRequest(
      final APIGatewayV2HTTPEvent event, final Context context) {

    APIGatewayV2HTTPResponse response = new APIGatewayV2HTTPResponse();
    response.setIsBase64Encoded(false);
    response.setStatusCode(200);

    HashMap<String, String> headers = new HashMap<>();
    headers.put("Content-Type", "text/html");
    response.setHeaders(headers);

    try {
      final Map<String, Object> contextMap = new HashMap<>();
      contextMap.put("url", new UrlFormatter(event.getStageVariables()));
      contextMap.putAll(event.getStageVariables());

      final String html = TemplateEngineFactory.parse("homepage", contextMap);
      response.setBody(html);
    } catch (Exception ex) {
      System.out.println(ex.getMessage());
      ex.printStackTrace();
      throw ex;
    }

    Util.logEnvironment(event, context);
    return response;
  }
}
