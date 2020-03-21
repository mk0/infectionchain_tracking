/**
 *
 */
package de.chaintracker.security;

import java.io.IOException;
import java.util.ArrayList;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import io.jsonwebtoken.Jwts;

public class AuthorizationFilter extends BasicAuthenticationFilter {

  private final String tokenSecret;

  public AuthorizationFilter(final AuthenticationManager authManager, final String tokenSecret) {
    super(authManager);
    this.tokenSecret = tokenSecret;
  }

  @Override
  protected void doFilterInternal(final HttpServletRequest req, final HttpServletResponse res, final FilterChain chain)
      throws IOException, ServletException {

    final String header = req.getHeader(SecurityConstants.HEADER_STRING);

    if (header == null || !header.startsWith(SecurityConstants.TOKEN_PREFIX)) {
      chain.doFilter(req, res);
      return;
    }

    final UsernamePasswordAuthenticationToken authentication = getAuthentication(req);
    SecurityContextHolder.getContext().setAuthentication(authentication);
    chain.doFilter(req, res);

  }

  private UsernamePasswordAuthenticationToken getAuthentication(final HttpServletRequest request) {

    String token = request.getHeader(SecurityConstants.HEADER_STRING);

    if (token != null) {

      token = token.replace(SecurityConstants.TOKEN_PREFIX, "");

      final String user = Jwts.parser()
          .setSigningKey(this.tokenSecret)
          .parseClaimsJws(token)
          .getBody()
          .getSubject();

      if (user != null) {
        return new UsernamePasswordAuthenticationToken(user, null, new ArrayList<>());
      }
    }

    return null;
  }

}
