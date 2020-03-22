/**
 *
 */
package de.chaintracker.web;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import de.chaintracker.dto.ContactEventUserDto;
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

  @Secured
  @RequestMapping(method = RequestMethod.GET, path = "/events")
  public List<ContactEventUserDto> getInfectedLocations(
      @RequestParam(value = "distance", required = false) final Double distance,
      @RequestParam(value = "minTime", required = false) final OffsetDateTime minTime,
      final Authentication authentication) {

    final User user = this.userRepository.findByEmail(authentication.getName()).get();

    final Double distanceX = distance == null ? .6 : distance;
    final OffsetDateTime minTimeX = minTime == null ? OffsetDateTime.now().minusWeeks(2) : minTime;

    return this.infectionRepository.findContactEvents(user.getId(), minTimeX, distanceX).stream()
        .map(ContactEventUserDto::fromEntity).collect(Collectors.toList());
  }
}
