package com.xenosnowfox.immawiki;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Properties;

public class PropertiesUtils {

  /**
   * Loads the properties from a specified resource file.
   *
   * @param withFilePath Name of the properties resource file to load
   * @return project properties.
   * @throws IOException if an error occurred when reading from the resource file
   */
  public static Properties fromResources(final Path withFilePath) throws IOException {
    try (InputStream input = Files.newInputStream(withFilePath)) {
      Properties properties = new Properties();
      properties.load(input);
      return properties;
    }
  }

  private PropertiesUtils() {}
}
