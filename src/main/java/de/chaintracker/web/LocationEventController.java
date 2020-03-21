/**
 *
 */
package de.chaintracker.web;

import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import de.chaintracker.dto.LocationEventDto;
import de.chaintracker.entity.LocationEvent;
import de.chaintracker.repo.LocationEventRepository;
import de.chaintracker.repo.UserRepository;
import de.chaintracker.security.aspect.Secured;

/**
 * @author Marko Voß
 *
 */
@RestController
@RequestMapping("/location-event")
public class LocationEventController {

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private LocationEventRepository locationEventRepository;

  @Secured
  @RequestMapping(method = RequestMethod.POST)
  public void post(@RequestBody final LocationEventDto locationEvent, final Authentication authentication) {

    this.locationEventRepository.save(LocationEvent.builder()
        .externalId(UUID.randomUUID().toString())
        .latitude(Math.toRadians(locationEvent.getLatitude()))
        .longitude(Math.toRadians(locationEvent.getLongitude()))
        .name(locationEvent.getName())
        .userCreate(this.userRepository.findByEmail(authentication.getName()).get())
        .build());
  }
}
