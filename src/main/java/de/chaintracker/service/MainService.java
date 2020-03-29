/**
 *
 */
package de.chaintracker.service;

import java.time.OffsetDateTime;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

import de.chaintracker.kafka.events.UserUpdated;
import de.chaintracker.kafka.producers.UserProducer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.kafka.KafkaAutoConfiguration;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import de.chaintracker.entity.ContactEvent;
import de.chaintracker.entity.Infection;
import de.chaintracker.entity.LocationEvent;
import de.chaintracker.entity.User;
import de.chaintracker.entity.UserAddress;
import de.chaintracker.repo.ContactEventRepository;
import de.chaintracker.repo.InfectionRepository;
import de.chaintracker.repo.LocationEventRepository;
import de.chaintracker.repo.UserAddressRepository;
import de.chaintracker.repo.UserRepository;
import de.chaintracker.util.Geocoord;

/**
 * @author Marko Voß
 *
 */
@Service
@SpringBootApplication(exclude = KafkaAutoConfiguration.class)
public class MainService {

  private static final Logger LOG = LoggerFactory.getLogger(MainService.class);

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private UserAddressRepository addressRepository;

  @Autowired
  private LocationEventRepository locationEventRepository;

  @Autowired
  private ContactEventRepository contactEventRepository;

  @Autowired
  private InfectionRepository infectionRepository;

  @Autowired
  private PasswordEncoder passwordEncoder;

  @Autowired
  private ObjectMapper objectMapper;

  @Autowired
  private UserProducer userProducer;

  @EventListener
  public void onApplicationEvent(final ApplicationStartedEvent event) throws JsonProcessingException {
    createExampleData();
  }

  private final void createExampleData() throws JsonProcessingException {

    final ObjectWriter writer = this.objectMapper.writerWithDefaultPrettyPrinter();

    /*
     * User A
     */

    final User userA = this.userRepository.save(User.builder()
        .userName("userA")
        .email("user.a@example.com")
        .emailVerificationStatus(true)
        .emailVerificationToken("SYSTEM")
        .encryptedPassword(this.passwordEncoder.encode("userA"))
        .firstName("User")
        .lastName("A")
        .isEnabled(true)
        .build());

    final UserAddress addressA1 = this.addressRepository.save(UserAddress.builder()
        .addressExternalId(UUID.randomUUID().toString())
        .city("Berlin")
        .country("Germany")
        .postalCode("10178")
        .streetname("Alexanderplatz 1")
        .type("HOME")
        .user(userA)
        .userCreate(userA)
        .build());

    final UserAddress addressA2 = this.addressRepository.save(UserAddress.builder()
        .addressExternalId(UUID.randomUUID().toString())
        .city("Berlin")
        .country("Germany")
        .postalCode("10178")
        .streetname("Sophienstraße 23")
        .type("WORK")
        .user(userA)
        .userCreate(userA)
        .build());

    LOG.info("Created User A:\n{}", writer.writeValueAsString(userA));
    LOG.info("Created User A Address HOME:\n{}", writer.writeValueAsString(addressA1));
    LOG.info("Created User A Address WORK:\n{}", writer.writeValueAsString(addressA2));

    /*
     * User B
     */

    final User userB = this.userRepository.save(User.builder()
        .userName("userB")
        .email("user.b@example.com")
        .emailVerificationStatus(true)
        .emailVerificationToken("SYSTEM")
        .encryptedPassword(this.passwordEncoder.encode("userB"))
        .firstName("User")
        .lastName("B")
        .isEnabled(true)
        .build());

    final UserAddress addressB1 = this.addressRepository.save(UserAddress.builder()
        .addressExternalId(UUID.randomUUID().toString())
        .city("Berlin")
        .country("Germany")
        .postalCode("10407")
        .streetname("Syringenweg 20")
        .type("HOME")
        .user(userB)
        .userCreate(userB)
        .build());

    final UserAddress addressB2 = this.addressRepository.save(UserAddress.builder()
        .addressExternalId(UUID.randomUUID().toString())
        .city("Berlin")
        .country("Germany")
        .postalCode("10407")
        .streetname("Prenzlauer Berg")
        .type("WORK")
        .user(userB)
        .userCreate(userB)
        .build());

    LOG.info("Created User B:\n{}", writer.writeValueAsString(userB));
    LOG.info("Created User B Address HOME:\n{}", writer.writeValueAsString(addressB1));
    LOG.info("Created User B Address WORK:\n{}", writer.writeValueAsString(addressB2));

    /*
     * User C
     */

    final User userC = this.userRepository.save(User.builder()
        .userName("userC")
        .email("user.c@example.com")
        .emailVerificationStatus(true)
        .emailVerificationToken("SYSTEM")
        .encryptedPassword(this.passwordEncoder.encode("userC"))
        .firstName("User")
        .lastName("C")
        .isEnabled(true)
        .build());

    final UserAddress addressC1 = this.addressRepository.save(UserAddress.builder()
        .addressExternalId(UUID.randomUUID().toString())
        .city("Berlin")
        .country("Germany")
        .postalCode("10437")
        .streetname("Stubbenkammerstraße 10")
        .type("HOME")
        .user(userB)
        .userCreate(userB)
        .build());

    final UserAddress addressC2 = this.addressRepository.save(UserAddress.builder()
        .addressExternalId(UUID.randomUUID().toString())
        .city("Berlin")
        .country("Germany")
        .postalCode("10409")
        .streetname("Storkower Str. 15")
        .type("WORK")
        .user(userB)
        .userCreate(userB)
        .build());

    LOG.info("Created User C:\n{}", writer.writeValueAsString(userC));
    LOG.info("Created User C Address HOME:\n{}", writer.writeValueAsString(addressC1));
    LOG.info("Created User C Address WORK:\n{}", writer.writeValueAsString(addressC2));

    /*
     * User D
     */

    final User userD = this.userRepository.save(User.builder()
        .userName("userD")
        .email("user.d@example.com")
        .emailVerificationStatus(true)
        .emailVerificationToken("SYSTEM")
        .encryptedPassword(this.passwordEncoder.encode("userD"))
        .firstName("User")
        .lastName("D")
        .isEnabled(true)
        .build());

    final UserAddress addressD1 = this.addressRepository.save(UserAddress.builder()
        .addressExternalId(UUID.randomUUID().toString())
        .city("Berlin")
        .country("Germany")
        .postalCode("10437")
        .streetname("Stubbenkammerstraße 10")
        .type("HOME")
        .user(userB)
        .userCreate(userB)
        .build());

    final UserAddress addressD2 = this.addressRepository.save(UserAddress.builder()
        .addressExternalId(UUID.randomUUID().toString())
        .city("Berlin")
        .country("Germany")
        .postalCode("10409")
        .streetname("Storkower Str. 15")
        .type("WORK")
        .user(userB)
        .userCreate(userB)
        .build());

    LOG.info("Created User D:\n{}", writer.writeValueAsString(userD));
    LOG.info("Created User D Address HOME:\n{}", writer.writeValueAsString(addressD1));
    LOG.info("Created User D Address WORK:\n{}", writer.writeValueAsString(addressD2));

    /*
     * Location Event User A -> User B on 52.527338, 13.430731: User A scanning User B
     */

    final LocationEvent locationEventAB = this.locationEventRepository.save(LocationEvent.builder()
        .externalId(UUID.randomUUID().toString())
        .latitude(Math.toRadians(52.527338))
        .longitude(Math.toRadians(13.430731))
        .name("SYSTEM")
        .userCreate(userA)
        .build());

    final ContactEvent contactEventAB = this.contactEventRepository.save(ContactEvent.builder()
        .externalId(UUID.randomUUID().toString())
        .locationEvent(locationEventAB)
        .user1(userA)
        .user2(userB)
        .userCreate(userA)
        .build());

    LOG.info("Created ContactEvent User A scanning User B:\n{}", writer.writeValueAsString(contactEventAB));

    /*
     * Location Event User C -> User D on 52.527467, 13.431369: User C scanning User D
     */

    final LocationEvent locationEventCD = this.locationEventRepository.save(LocationEvent.builder()
        .externalId(UUID.randomUUID().toString())
        .latitude(Math.toRadians(52.527467))
        .longitude(Math.toRadians(13.431369))
        .name("SYSTEM")
        .userCreate(userC)
        .build());

    final ContactEvent contactEventCD = this.contactEventRepository.save(ContactEvent.builder()
        .externalId(UUID.randomUUID().toString())
        .locationEvent(locationEventCD)
        .user1(userC)
        .user2(userD)
        .userCreate(userC)
        .build());

    LOG.info("Created ContactEvent User C scanning User D:\n{}", writer.writeValueAsString(contactEventCD));

    final OffsetDateTime infectionTime = OffsetDateTime.now().plus(1, ChronoUnit.WEEKS);
    final Infection infection = this.infectionRepository.save(Infection.builder()
        .isInfected(true)
        .timestamp(infectionTime)
        .user(userC)
        .build());

    LOG.info("Created Infection of User C one week later:\n{}", writer.writeValueAsString(infection));

    final Geocoord a = new Geocoord(48.16726, 11.49628);
    final Geocoord b = new Geocoord(48.16726, 11.49632);

    this.locationEventRepository.save(LocationEvent.builder()
        .externalId(UUID.randomUUID().toString())
        .latitude(a.latitude())
        .longitude(a.longitude())
        .name("A")
        .userCreate(userC)
        .build());

    this.locationEventRepository.save(LocationEvent.builder()
        .externalId(UUID.randomUUID().toString())
        .latitude(b.latitude())
        .longitude(b.longitude())
        .name("B")
        .userCreate(userC)
        .build());

    final double distance = a.distanceTo(b); // in meters

    LOG.info("Created locations with distance of {} meters", distance);

    LOG.info("Found close locations for this distance:");
    this.locationEventRepository
        .findLocations((distance / Geocoord.EARTH_RADIUS) * (distance / Geocoord.EARTH_RADIUS)).stream()
        .forEach(t -> {
          t.getFirst().setUserCreate(null);
          t.getSecond().setUserCreate(null);
          try {
            LOG.info(writer.writeValueAsString(t));
          } catch (final JsonProcessingException e) {
            e.printStackTrace();
          }
        });

    var update = new UserUpdated();
    update.id = userA.getId();
    update.email = userA.getEmail();
    userProducer.userUpdated(update);
  }

}
