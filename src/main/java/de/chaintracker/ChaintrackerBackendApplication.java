package de.chaintracker;

import java.time.OffsetDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import de.chaintracker.entity.Location;
import de.chaintracker.repo.LocationRepository;
import de.chaintracker.util.Geocoord;

@SpringBootApplication
public class ChaintrackerBackendApplication {

  private static final Logger log = LoggerFactory.getLogger(ChaintrackerBackendApplication.class);

  public static void main(final String[] args) {
    SpringApplication.run(ChaintrackerBackendApplication.class, args);
  }

  @Bean
  public CommandLineRunner demo(final LocationRepository repository) {
    return (args) -> {

      final UUID userIdA = UUID.randomUUID();
      final UUID userIdB = UUID.randomUUID();

      log.info("UserID A: " + userIdA);
      log.info("UserID B: " + userIdB);

      final OffsetDateTime now = OffsetDateTime.now();

      // User A movement
      final Geocoord gA1 = new Geocoord(48.968914, 8.418054);
      final Geocoord gA2 = new Geocoord(48.969914, 8.416632);
      final Geocoord gA3 = new Geocoord(48.969857, 8.414079);
      final Geocoord gA4 = new Geocoord(48.968583, 8.413006);
      final Geocoord gA5 = new Geocoord(48.967604, 8.413628);

      // User B movement
      final Geocoord gB1 = new Geocoord(48.966920, 8.412437);
      final Geocoord gB2 = new Geocoord(48.967568, 8.413596);
      final Geocoord gB3 = new Geocoord(48.968907, 8.413038);
      final Geocoord gB4 = new Geocoord(48.968893, 8.411912);
      final Geocoord gB5 = new Geocoord(48.969019, 8.410345);

      // Save locations for User A
      repository.save(Location.builder().userId(userIdA.toString()).timestamp(now.minus(25, ChronoUnit.MINUTES))
          .latitude(gA1.latitude()).longitude(gA1.longitude()).build());
      repository.save(Location.builder().userId(userIdA.toString()).timestamp(now.minus(20, ChronoUnit.MINUTES))
          .latitude(gA2.latitude()).longitude(gA2.longitude()).build());
      repository.save(Location.builder().userId(userIdA.toString()).timestamp(now.minus(15, ChronoUnit.MINUTES))
          .latitude(gA3.latitude()).longitude(gA3.longitude()).build());
      repository.save(Location.builder().userId(userIdA.toString()).timestamp(now.minus(10, ChronoUnit.MINUTES))
          .latitude(gA4.latitude()).longitude(gA4.longitude()).build());
      repository.save(Location.builder().userId(userIdA.toString()).timestamp(now.minus(5, ChronoUnit.MINUTES))
          .latitude(gA5.latitude()).longitude(gA5.longitude()).build());

      // Save locations for User B
      repository.save(Location.builder().userId(userIdB.toString()).timestamp(now.minus(28, ChronoUnit.MINUTES))
          .latitude(gB1.latitude()).longitude(gB1.longitude()).build());
      repository.save(Location.builder().userId(userIdB.toString()).timestamp(now.minus(23, ChronoUnit.MINUTES))
          .latitude(gB2.latitude()).longitude(gB2.longitude()).build());
      repository.save(Location.builder().userId(userIdB.toString()).timestamp(now.minus(18, ChronoUnit.MINUTES))
          .latitude(gB3.latitude()).longitude(gB3.longitude()).build());
      repository.save(Location.builder().userId(userIdB.toString()).timestamp(now.minus(13, ChronoUnit.MINUTES))
          .latitude(gB4.latitude()).longitude(gB4.longitude()).build());
      repository.save(Location.builder().userId(userIdB.toString()).timestamp(now.minus(8, ChronoUnit.MINUTES))
          .latitude(gB5.latitude()).longitude(gB5.longitude()).build());


      log.info("Current data in DB:");
      log.info("-------------------------------");
      repository.findAll().forEach(c -> log.info(c.toString()));
      log.info("");

      // Fetch matches of locations for distance of 20 meters within the last 20 minutes
      final List<Location> locations = repository.findByUserIdAndDistanceAndTimestamp(userIdA.toString(), .02,
          now,
          now.minus(20, ChronoUnit.MINUTES));
      log.info("Locations found:");
      log.info("--------------------------------");
      locations.forEach(c -> log.info(c.toString()));
      log.info("");
    };
  }
}
