package com.xenosnowfox.immawiki.config;

import com.xenosnowfox.immawiki.PropertiesUtils;
import java.io.IOException;
import java.nio.file.Path;

class Properties {
  static final java.util.Properties deploymentProperties;
  static final java.util.Properties siteProperties;
  static final java.util.Properties homepageProperties;

  static {
    try {
      deploymentProperties =
          PropertiesUtils.fromResources(Path.of("./config/deployment.properties"));
      siteProperties = PropertiesUtils.fromResources(Path.of("./config/site.properties"));
      homepageProperties = PropertiesUtils.fromResources(Path.of("./config/homepage.properties"));
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }
}
