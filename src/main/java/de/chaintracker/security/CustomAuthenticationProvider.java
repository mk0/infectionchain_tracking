/**
 *
 */
package de.chaintracker.security;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import de.chaintracker.entity.User;
import de.chaintracker.repo.UserRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

/**
 * @author Marko Voß
 *
 */
@Component
public class CustomAuthenticationProvider implements AuthenticationProvider {

  @Autowired
  private UserRepository userRepository;

  private final PasswordEncoder passwordEncoder;

  @Value("${app.token.secret}")
  private String tokenSecret;

  public CustomAuthenticationProvider(final PasswordEncoder passwordEncoder) {
    this.passwordEncoder = passwordEncoder;
  }

  @Override
  public Authentication authenticate(final Authentication authentication) throws AuthenticationException {

    final String email = authentication.getName();
    final String password = authentication.getCredentials().toString();

    final Optional<User> userOp = this.userRepository.findByEmail(email);
    if (userOp.isEmpty() || !this.passwordEncoder.matches(password, userOp.get().getEncryptedPassword()))
      throw new BadCredentialsException("Invalid credentials");

    // Create qr code
    final User user = userOp.get();
    user.setQrCode(generateQrCode(user));
    this.userRepository.save(user);

    final List<GrantedAuthority> authorities = new ArrayList<>();

    return new UsernamePasswordAuthenticationToken(email, password, authorities);
  }

  private String generateQrCode(final User user) {

    final Claims claims = Jwts.claims().setSubject(user.getEmail());
    claims.put("userId", user.getId());

    final String token = Jwts.builder().setSubject(user.getEmail())
        .setExpiration(new Date(System.currentTimeMillis() + SecurityConstants.EXPIRATION_TIME))
        .signWith(SignatureAlgorithm.HS256, this.tokenSecret).setClaims(claims)
        .compact();

    return token;
  }

  @Override
  public boolean supports(final Class<?> authentication) {
    return authentication.equals(UsernamePasswordAuthenticationToken.class);
  }
}
