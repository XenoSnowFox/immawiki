package com.xenosnowfox.immawiki.config;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum DeploymentProperties {
  // SITE PROPERTIES
  IDENTIFIER(Properties.deploymentProperties.getProperty("IDENTIFIER", "immawiki"));

  @Getter private final String value;
}
