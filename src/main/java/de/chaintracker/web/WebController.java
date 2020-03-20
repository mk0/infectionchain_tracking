/**
 *
 */
package de.chaintracker.web;

import java.time.OffsetDateTime;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import de.chaintracker.entity.Location;
import de.chaintracker.repo.LocationRepository;

/**
 * @author Marko Voß
 *
 */
@RestController()
public class WebController {

  @Autowired
  private LocationRepository locationRepository;

  @PutMapping("/{userId}")
  public void putLocation(
      @PathVariable("userId") final UUID userId,
      @RequestParam("lat") final Double latitude,
      @RequestParam("lon") final Double longitude) {

    this.locationRepository.save(Location.builder()
        .userId(userId.toString())
        .timestamp(OffsetDateTime.now())
        .latitude(Math.toRadians(latitude))
        .longitude(Math.toRadians(longitude))
        .build());
  }
}
