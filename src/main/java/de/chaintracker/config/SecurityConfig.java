/**
 *
 */
package de.chaintracker.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import de.chaintracker.repo.UserRepository;
import de.chaintracker.security.AuthenticationFilter;
import de.chaintracker.security.AuthorizationFilter;
import de.chaintracker.security.CustomAuthenticationProvider;
import de.chaintracker.security.SecurityConstants;

/**
 * @author Marko Voß
 *
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

  @Autowired
  private UserRepository userRepository;

  @Value("${app.token.secret}")
  private String tokenSecret;

  @Autowired
  public void configureGlobal(final AuthenticationManagerBuilder auth) throws Exception {
    auth.authenticationProvider(authenticationProvider(passwordEncoder()));
  }

  @Override
  protected void configure(final HttpSecurity http) throws Exception {
    http.cors().and().csrf().disable().authorizeRequests()
        .antMatchers(HttpMethod.POST, SecurityConstants.SIGN_UP_URL)
        .permitAll()
        .antMatchers(HttpMethod.GET, SecurityConstants.VERIFICATION_EMAIL_URL)
        .permitAll()
        .antMatchers(HttpMethod.GET, "/*.html", "/*.js", "/*.css", "/*.ico", "/assets/**")
        .permitAll()
        .antMatchers("/v2/api-docs", "/configuration/**", "/swagger*/**", "/webjars/**")
        .permitAll()
        .anyRequest().authenticated()
        .and().addFilter(getAuthenticationFilter())
        .addFilter(new AuthorizationFilter(authenticationManager(), this.tokenSecret)).sessionManagement()
        .sessionCreationPolicy(SessionCreationPolicy.STATELESS);
  }

  public AuthenticationFilter getAuthenticationFilter() throws Exception {
    final AuthenticationFilter filter = new AuthenticationFilter(
        authenticationManager(),
        this.userRepository,
        this.tokenSecret);
    filter.setFilterProcessesUrl("/users/login");
    return filter;
  }

  @Bean
  CustomAuthenticationProvider authenticationProvider(final PasswordEncoder passwordEncoder) {
    return new CustomAuthenticationProvider(passwordEncoder);
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }
}
