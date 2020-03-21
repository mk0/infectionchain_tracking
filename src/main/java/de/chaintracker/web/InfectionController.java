/**
 *
 */
package de.chaintracker.web;

import java.time.OffsetDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import de.chaintracker.dto.InfectionDto;
import de.chaintracker.entity.Infection;
import de.chaintracker.entity.User;
import de.chaintracker.repo.InfectionRepository;
import de.chaintracker.repo.UserRepository;
import de.chaintracker.security.aspect.Secured;

/**
 * @author Marko Voß
 *
 */
@RestController
@RequestMapping("/infect")
public class InfectionController {

  @Autowired
  private InfectionRepository infectionRepository;

  @Autowired
  private UserRepository userRepository;

  @Secured
  @RequestMapping(method = RequestMethod.POST)
  public void setInfected(final InfectionDto infection, final Authentication authentication) {

    final User user = this.userRepository.findByEmail(authentication.getName()).get();

    if (!infection.isInfected() && this.infectionRepository.findByUserId(user.getId())) {
      this.infectionRepository.deleteByUserId(user.getId());
      return;
    }

    this.infectionRepository.save(Infection.builder()
        .isInfected(true)
        .timestamp(OffsetDateTime.now())
        .user(user)
        .build());

    // TODO: Trigger push notifications to other users, who got in contact with this current user
  }
}
