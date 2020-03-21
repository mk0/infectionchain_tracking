/**
 *
 */
package de.chaintracker.security;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Optional;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.chaintracker.dto.UserLoginRequestDto;
import de.chaintracker.entity.User;
import de.chaintracker.repo.UserRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

public class AuthenticationFilter extends UsernamePasswordAuthenticationFilter {

  private final AuthenticationManager authenticationManager;
  private final UserRepository userRepository;
  private final String tokenSecret;

  public AuthenticationFilter(final AuthenticationManager authenticationManager, final UserRepository userRepository,
      final String tokenSecret) {
    this.authenticationManager = authenticationManager;
    this.userRepository = userRepository;
    this.tokenSecret = tokenSecret;

  }

  @Override
  public Authentication attemptAuthentication(final HttpServletRequest req, final HttpServletResponse res)
      throws AuthenticationException {

    try {
      final UserLoginRequestDto creds = new ObjectMapper().readValue(req.getInputStream(),
          UserLoginRequestDto.class);

      return this.authenticationManager.authenticate(
          new UsernamePasswordAuthenticationToken(creds.getEmail(), creds.getPassword(), new ArrayList<>()));

    } catch (final IOException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  protected void successfulAuthentication(final HttpServletRequest req, final HttpServletResponse res,
      final FilterChain chain,
      final Authentication auth) throws IOException, ServletException {

    final String userEmail = (auth.getPrincipal() instanceof String) ? (String) auth.getPrincipal()
        : ((org.springframework.security.core.userdetails.User) auth.getPrincipal()).getUsername();

    final Optional<User> userOp = this.userRepository.findByEmail(userEmail);
    final String userId = userOp.get().getId();

    final Claims claims = Jwts.claims().setSubject(userEmail);
    claims.put("userId", userId);

    final String token = Jwts.builder().setSubject(userEmail)
        .setExpiration(new Date(System.currentTimeMillis() + SecurityConstants.EXPIRATION_TIME))
        .signWith(SignatureAlgorithm.HS512, this.tokenSecret).setClaims(claims)
        .compact();

    res.addHeader(SecurityConstants.HEADER_STRING, SecurityConstants.TOKEN_PREFIX + token);
  }

}
