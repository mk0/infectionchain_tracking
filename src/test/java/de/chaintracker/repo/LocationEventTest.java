/**
 *
 */
package de.chaintracker.repo;

import static org.assertj.core.api.Assertions.assertThat;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import javax.persistence.EntityManager;
import javax.sql.DataSource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import de.chaintracker.config.TestConfig;
import de.chaintracker.entity.LocationEvent;
import de.chaintracker.entity.User;
import de.chaintracker.util.Geocoord;

/**
 * @author Marko Voß
 *
 */
@ExtendWith(SpringExtension.class)
@DataJpaTest
@Import(TestConfig.class)
public class LocationEventTest {

  private static final Random RANDOM = new Random();
  private static final Logger LOG = LoggerFactory.getLogger(LocationEventTest.class);

  @Autowired
  private DataSource dataSource;
  @Autowired
  private JdbcTemplate jdbcTemplate;
  @Autowired
  private EntityManager entityManager;
  @Autowired
  private LocationEventRepository locationEventRepository;
  @Autowired
  private UserRepository userRepository;
  @Autowired
  private PasswordEncoder passwordEncoder;
  @Autowired
  private TestConfig config;

  @BeforeEach
  void injectedComponentsAreNotNull() {
    assertThat(this.dataSource).isNotNull();
    assertThat(this.jdbcTemplate).isNotNull();
    assertThat(this.entityManager).isNotNull();
    assertThat(this.locationEventRepository).isNotNull();
    assertThat(this.userRepository).isNotNull();
    assertThat(this.passwordEncoder).isNotNull();
    assertThat(this.config).isNotNull();
  }

  @Test
  void stressSearch() {
    // create user
    final User user = this.userRepository.save(DataHelper.getUserMaxMustermann());

    // create center
    final Geocoord coord = new Geocoord(48.16726, 11.49628);
    // create match (distance 3 meters)
    final Geocoord coord2 = new Geocoord(48.16726, 11.49641);

    // create at least one match

    this.locationEventRepository.save(LocationEvent.builder()
        .externalId(UUID.randomUUID().toString())
        .latitude(coord.latitude())
        .longitude(coord.longitude())
        .name("STATIC")
        .userCreate(user)
        .build());

    this.locationEventRepository.save(LocationEvent.builder()
        .externalId(UUID.randomUUID().toString())
        .latitude(coord2.latitude())
        .longitude(coord2.longitude())
        .name("STATIC")
        .userCreate(user)
        .build());

    createLocations(coord, user);

    LOG.info("Performing search");
    final double distance = 10; // meters
    final double distanceAlpha = (distance / Geocoord.EARTH_RADIUS) * (distance / Geocoord.EARTH_RADIUS);

    this.locationEventRepository.findByDistanceAlpha(distanceAlpha).forEach(c -> {
      LOG.info(c.toString());
    });
  }

  /**
   * Create random location around specified {@link Geocoord}.
   *
   * @param center
   */
  private void createLocations(final Geocoord center, final User user) {

    final int amount = this.config.getAmountLocations();
    final int bufferSize = this.config.getBufferSize();

    LOG.info("Creating {} amount of LocationEvents around location {}", amount, center);

    final List<LocationEvent> buffer = new ArrayList<>(bufferSize);

    final OffsetDateTime start = OffsetDateTime.now();

    for (int i = 1; i <= amount; i++) {

      final Geocoord randCoord = new Geocoord(
          getRandom(center.getLatitudeDecimal()),
          getRandom(center.getLongitudeDecimal()));

      buffer.add(LocationEvent.builder()
          .externalId(UUID.randomUUID().toString())
          .latitude(randCoord.latitude())
          .longitude(randCoord.longitude())
          .name("RANDOM")
          .userCreate(user)
          .build());

      // Cancel operation after n seconds
      if (OffsetDateTime.now().isAfter(start.plusSeconds(this.config.getTimeout()))) {
        LOG.info("Cancelling (timeout after {} seconds). Storing {} into DB.", this.config.getTimeout(), buffer.size());
        this.locationEventRepository.saveAll(buffer);
        buffer.clear();
        break;
      }

      // Store entries at once for faster transactions
      if (i % bufferSize == 0) {
        LOG.info("Storing {} into DB.", buffer.size());
        this.locationEventRepository.saveAll(buffer);
        buffer.clear();
      }

      // print percentage every 10%
      if ((i * 100. / amount) % 10 == 0 && (i * 100. / amount) != 0)
        LOG.info("{}% done", (int) (i * 100. / amount));
    }
  }

  /**
   *
   * @param position
   * @return
   */
  private static final double getRandom(final double position) {
    return position + RANDOM.nextInt(100000) / 1000000.0;
  }
}
