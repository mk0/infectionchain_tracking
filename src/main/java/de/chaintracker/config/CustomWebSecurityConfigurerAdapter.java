/**
 *
 */
package de.chaintracker.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import de.chaintracker.security.CustomAuthenticationProvider;

/**
 * @author Marko Voß
 *
 */
@Configuration
@EnableWebSecurity
public class CustomWebSecurityConfigurerAdapter extends WebSecurityConfigurerAdapter {

  @Autowired
  private CustomAuthenticationProvider authProvider;

  @Autowired
  public void configureGlobal(final AuthenticationManagerBuilder auth) throws Exception {
    auth.authenticationProvider(this.authProvider);
  }

  @Override
  protected void configure(final HttpSecurity http) throws Exception {
    http.authorizeRequests()
        .antMatchers("/frontend").permitAll()
        .anyRequest().authenticated()
        .and()
        .httpBasic();
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }
}