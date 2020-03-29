/**
 *
 */
package de.chaintracker.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.CommonsRequestLoggingFilter;

/**
 * @author Marko Voß
 *
 */
@Configuration
public class LoggingFilterConfig {

  @Bean
  public CommonsRequestLoggingFilter logFilter() {
    final CommonsRequestLoggingFilter filter = new CommonsRequestLoggingFilter();
    filter.setIncludeQueryString(true);
    filter.setIncludePayload(false);
    filter.setMaxPayloadLength(10000);
    filter.setIncludeHeaders(false);
    return filter;
  }
}
