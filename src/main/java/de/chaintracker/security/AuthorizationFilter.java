/**
 *
 */
package de.chaintracker.security;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Optional;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import de.chaintracker.entity.User;
import de.chaintracker.repo.UserRepository;
import io.jsonwebtoken.Jwts;

public class AuthorizationFilter extends BasicAuthenticationFilter {

  private final String tokenSecret;
  private final UserRepository userRepository;

  public AuthorizationFilter(final AuthenticationManager authManager, final String tokenSecret,
      final UserRepository userRepository) {
    super(authManager);
    this.tokenSecret = tokenSecret;
    this.userRepository = userRepository;
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

  /**
   *
   * @param request
   * @return
   */
  private UsernamePasswordAuthenticationToken getAuthentication(final HttpServletRequest request) {

    String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);

    if (authHeader == null)
      return null;

    authHeader = authHeader.replace(SecurityConstants.TOKEN_PREFIX, "");

    final String email = Jwts.parser()
        .setSigningKey(this.tokenSecret)
        .parseClaimsJws(authHeader)
        .getBody()
        .getSubject();

    if (email == null)
      return null;

    final Optional<User> userOp = this.userRepository.findByEmail(email);
    if (userOp.isEmpty())
      return null;

    final UsernamePasswordAuthenticationToken token =
        new UsernamePasswordAuthenticationToken(email, null, new ArrayList<>());
    token.setDetails(userOp.get());

    return token;
  }

}
