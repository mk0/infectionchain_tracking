/**
 *
 */
package de.chaintracker.security;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
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

/**
 * @author Marko Voß
 *
 */
@Component
public class CustomAuthenticationProvider implements AuthenticationProvider {

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private PasswordEncoder passwordEncoder;

  @Override
  public Authentication authenticate(final Authentication authentication) throws AuthenticationException {

    final String email = authentication.getName();
    final String password = authentication.getCredentials().toString();

    final Optional<User> user = this.userRepository.findByEmail(email);
    if (user.isEmpty() || !this.passwordEncoder.matches(password, user.get().getEncryptedPassword()))
      throw new BadCredentialsException("Invalid credentials");

    final List<GrantedAuthority> authorities = new ArrayList<>();

    return new UsernamePasswordAuthenticationToken(email, password, authorities);
  }

  @Override
  public boolean supports(final Class<?> authentication) {
    return authentication.equals(UsernamePasswordAuthenticationToken.class);
  }
}
