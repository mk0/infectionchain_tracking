/**
 *
 */
package de.chaintracker.web;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import de.chaintracker.dto.ContactEventDto;
import de.chaintracker.dto.ContactEventUserDto;
import de.chaintracker.dto.LocationEventDto;
import de.chaintracker.entity.ContactEvent;
import de.chaintracker.entity.LocationEvent;
import de.chaintracker.entity.User;
import de.chaintracker.repo.ContactEventRepository;
import de.chaintracker.repo.LocationEventRepository;
import de.chaintracker.repo.UserRepository;
import de.chaintracker.security.SecurityConstants;
import de.chaintracker.security.aspect.Secured;
import io.jsonwebtoken.Jwts;

/**
 * @author Marko Voß
 *
 */
@RestController
@RequestMapping("/contact-event")
public class ContactEventController {

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private ContactEventRepository contactEventRepository;

  /**
   * At this point, we really could utilize the @Service level :)
   */
  @Autowired
  private LocationEventRepository locationEventRepository;

  @Value("${app.token.secret}")
  private String tokenSecret;

  @Secured
  @RequestMapping(method = RequestMethod.POST)
  public void post(@RequestBody final ContactEventDto contactEvent, final Authentication authentication) {

    final LocationEvent locationEvent =
        this.locationEventRepository.save(LocationEvent.builder()
            .externalId(UUID.randomUUID().toString())
            .latitude(Math.toRadians(contactEvent.getLocationEvent().getLatitude()))
            .longitude(Math.toRadians(contactEvent.getLocationEvent().getLongitude()))
            .name(contactEvent.getLocationEvent().getName())
            .userCreate(this.userRepository.findByEmail(authentication.getName()).get())
            .build());

    final String scannedUserId = Jwts.parser().setSigningKey(this.tokenSecret)
        .parseClaimsJws(contactEvent.getScannedQrCode())
        .getBody().get(SecurityConstants.JWT_CLAIM_USERID, String.class);

    final User creator = this.userRepository.findByEmail(authentication.getName()).get();

    this.contactEventRepository.save(ContactEvent.builder()
        .externalId(UUID.randomUUID().toString())
        .locationEvent(locationEvent)
        .user1(creator)
        .user2(this.userRepository.findById(scannedUserId).get())
        .userCreate(creator)
        .build());
  }

  @Secured
  @RequestMapping(method = RequestMethod.GET)
  public List<ContactEventUserDto> getContacts(final Authentication authentication) {

    final User user = this.userRepository.findByEmail(authentication.getName()).get();
    return this.contactEventRepository.findByUser1OrUser2(user, user).stream().map(c -> {

      if (user.getId().equals(c.getUser1().getId()))
        return ContactEventUserDto.fromEntity(c);

      return ContactEventUserDto.builder()
          .locationEvent(LocationEventDto.fromEntity(c.getLocationEvent()))
          .username1(c.getUser2().getUserName())
          .username2(c.getUser1().getUserName())
          .build();
    }).collect(Collectors.toList());
  }
}
