package com.xenosnowfox.immawiki.config;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum SiteProperties {
  // SITE PROPERTIES
  IMMAWIKI_SITE_NAME(Properties.siteProperties.getProperty("NAME", "")),
  IMMAWIKI_SITE_DOMAIN(Properties.siteProperties.getProperty("DOMAIN", "")),
  IMMAWIKI_SITE_PATH_PREFIX(Properties.siteProperties.getProperty("PATH_PREFIX", "/")),
  // HOMEPAGE PROPERTIES
  IMMAWIKI_HOMEPAGE_TITLE(Properties.homepageProperties.getProperty("TITLE", "")),
  IMMAWIKI_HOMEPAGE_SUMMARY(Properties.homepageProperties.getProperty("SUMMARY", ""));
  ;

  @Getter private final String value;
}
