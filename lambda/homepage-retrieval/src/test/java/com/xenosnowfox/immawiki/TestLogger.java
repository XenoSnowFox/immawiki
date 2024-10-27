package com.xenosnowfox.immawiki;

import com.amazonaws.services.lambda.runtime.LambdaLogger;
import java.util.logging.Logger;

public class TestLogger implements LambdaLogger {
  private static final Logger logger = Logger.getLogger(TestLogger.class.getName());

  public void log(String message) {
    logger.info(message);
  }

  public void log(byte[] message) {
    logger.info(new String(message));
  }
}
