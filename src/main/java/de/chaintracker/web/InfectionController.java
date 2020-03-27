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
import de.chaintracker.security.aspect.Secured;
import de.chaintracker.util.Geocoord;
import io.swagger.annotations.ApiParam;

/**
 * @author Marko Voß
 *
 */
@RestController
@RequestMapping("/infect")
public class InfectionController {

  @Autowired
  private InfectionRepository infectionRepository;

  @Secured
  @RequestMapping(method = RequestMethod.POST)
  public void setInfected(final InfectionDto infection, final Authentication authentication) {

    final User user = (User) authentication.getDetails();

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
      @ApiParam(value = "The maximum distance in meters between two locations to search for. (max. 100 meters)")
      @RequestParam(value = "distance", required = false)
      final Double distance,
      @ApiParam(value = "The maximum amount of days to search backwards from now.")
      @RequestParam(value = "maxTime", required = false)
      final Integer maxTime,
      @ApiParam(value = "The maximum amount of minutes between different users did enter the almost same location.")
      @RequestParam(value = "deltaTime", required = false)
      final Integer deltaTime,
      final Authentication authentication) {

    final User user = (User) authentication.getDetails();

    final double distanceX = distance == null ? 10 : distance > 100 ? 100 : distance;
    final int deltaTimeX = deltaTime == null ? 10 : deltaTime;
    final OffsetDateTime minTimeX =
        maxTime == null ? OffsetDateTime.now().minusDays(14) : OffsetDateTime.now().minusDays(maxTime);

    final double distanceAlpha =
        (distanceX / Geocoord.EARTH_RADIUS) * (distanceX / Geocoord.EARTH_RADIUS);

    return this.infectionRepository.findContactEvents(user.getId(), distanceAlpha, deltaTimeX, minTimeX).stream()
        .map(ContactEventUserDto::fromEntity).collect(Collectors.toList());
  }
}
