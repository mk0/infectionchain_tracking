/**
 *
 */
package de.chaintracker.web;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import de.chaintracker.dto.LocationEventDto;
import de.chaintracker.repo.LocationEventRepository;
import de.chaintracker.util.Geocoord;

/**
 * @author Marko Voß
 *
 */
@RestController
@RequestMapping(path = "/locations")
public class LocationController {

  @Autowired
  private LocationEventRepository locationEventRepository;

  @RequestMapping
  public List<LocationEventDto> getLocations(
      @RequestParam("distance")
      final Double distance) {

    final double distanceAlpha = (distance / Geocoord.EARTH_RADIUS) * (distance / Geocoord.EARTH_RADIUS);
    // return
    // this.locationEventRepository.findByDistanceAlpha(distanceAlpha).stream().map(LocationEventDto::fromEntity)
    // .collect(Collectors.toList());
    return null;
  }

  public static void main(final String[] args) {
    final double distance = 5;
    System.out.println((distance / Geocoord.EARTH_RADIUS) * (distance / Geocoord.EARTH_RADIUS));
  }
}
