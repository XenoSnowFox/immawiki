package com.xenosnowfox.immawiki.library.thymeleafutilities;

import java.util.Locale;
import java.util.Map;
import org.thymeleaf.ITemplateEngine;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.context.IContext;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;

public class TemplateEngineFactory {
  public static final ITemplateEngine TEMPLATE_ENGINE = buildTemplateEngine();

  private static ITemplateEngine buildTemplateEngine() {
    final ClassLoaderTemplateResolver templateResolver = new ClassLoaderTemplateResolver();

    //  HTML is the default mode, but we will set it anyway for better understanding of code
    templateResolver.setTemplateMode(TemplateMode.HTML);

    // This will convert "home" to "/WEB-INF/templates/home.html"
    templateResolver.setPrefix("/templates/");
    templateResolver.setSuffix(".html");

    // Set template cache TTL to 1 hour. If not set, entries would live in cache until expelled by
    // LRU
    templateResolver.setCacheTTLMs(3600000L);
    templateResolver.setCacheable(true);

    final TemplateEngine templateEngine = new TemplateEngine();
    templateEngine.setTemplateResolver(templateResolver);
    return templateEngine;
  }

  public static String parse(final String withTemplate, final Map<String, Object> withContext) {
    final IContext context = new Context(Locale.ENGLISH, withContext);
    return TemplateEngineFactory.TEMPLATE_ENGINE.process(withTemplate, context);
  }
}
