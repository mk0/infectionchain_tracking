/**
 *
 */
package de.chaintracker.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import de.chaintracker.entity.User;
import de.chaintracker.repo.UserRepository;

/**
 * @author Marko Voß
 *
 */
@Service
public class MainService {

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private PasswordEncoder passwordEncoder;

  @EventListener
  public void onApplicationEvent(final ApplicationStartedEvent event) {

    if (!this.userRepository.existsByEmail("markovoss@arcor.de"))
      this.userRepository.save(User.builder()
          .userName("admin")
          .email("markovoss@arcor.de")
          .emailVerificationStatus(true)
          .encryptedPassword(this.passwordEncoder.encode("admin123!"))
          .firstName("Marko")
          .lastName("Voss")
          .isEnabled(true)
          .build());
  }

}
