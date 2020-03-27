/**
 *
 */
package de.chaintracker.web;

import java.util.List;
import java.util.Optional;
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
  private ContactEventRepository contactEventRepository;

  @Autowired
  private UserRepository userRepository;

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

    /*
     * Validate scanned userId
     */

    final String scannedUserId = Jwts.parser().setSigningKey(this.tokenSecret)
        .parseClaimsJws(contactEvent.getScannedQrCode())
        .getBody().get(SecurityConstants.JWT_CLAIM_USERID, String.class);

    final Optional<User> scannedUserOp = this.userRepository.findById(scannedUserId);
    if (scannedUserOp.isEmpty())
      throw new IllegalArgumentException("Scanned userId not found.");

    /*
     * Get current user
     */

    final User currentUser = (User) authentication.getDetails();

    /*
     * Store data
     */

    final LocationEvent locationEvent = this.locationEventRepository.save(LocationEvent.builder()
        .externalId(UUID.randomUUID().toString())
        .latitude(Math.toRadians(contactEvent.getLocationEvent().getLatitude()))
        .longitude(Math.toRadians(contactEvent.getLocationEvent().getLongitude()))
        .name(contactEvent.getLocationEvent().getName())
        .userCreate(currentUser)
        .build());

    this.contactEventRepository.save(ContactEvent.builder()
        .externalId(UUID.randomUUID().toString())
        .locationEvent(locationEvent)
        .user1(currentUser)
        .user2(scannedUserOp.get())
        .userCreate(currentUser)
        .build());
  }

  @Secured
  @RequestMapping(method = RequestMethod.GET)
  public List<ContactEventUserDto> getContacts(final Authentication authentication) {

    final User user = (User) authentication.getDetails();
    return this.contactEventRepository.findByUser1OrUser2(user, user).stream().map(c -> {

      if (user.getId().equals(c.getUser1().getId()))
        return ContactEventUserDto.fromEntity(c);

      return ContactEventUserDto.builder()
          .locationEvent(LocationEventDto.fromEntity(c.getLocationEvent()))
          .username1(c.getUser2().getUserName())
          .username2(c.getUser1().getUserName())
          .timestamp(c.getTimestampCreate())
          .build();
    }).collect(Collectors.toList());
  }
}
