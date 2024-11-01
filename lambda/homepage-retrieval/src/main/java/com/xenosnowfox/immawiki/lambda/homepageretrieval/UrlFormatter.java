package com.xenosnowfox.immawiki.lambda.homepageretrieval;

import java.util.Map;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class UrlFormatter {

  @NonNull private final Map<String, String> stageVariables;

  public String format(final String withUrl) {
    return this.stageVariables.getOrDefault("IMMAWIKI_SITE_PATH_PREFIX", "")
        + (withUrl.startsWith("/") ? "" : "/")
        + withUrl;
  }
}
