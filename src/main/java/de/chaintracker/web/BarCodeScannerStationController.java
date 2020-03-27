/**
 *
 */
package de.chaintracker.web;

import java.time.OffsetDateTime;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.SortDefault;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import de.chaintracker.dto.BarCodeScannerStationDto;
import de.chaintracker.entity.BarCodeScannerStation;
import de.chaintracker.entity.User;
import de.chaintracker.repo.BarCodeScannerStationRepository;
import de.chaintracker.security.aspect.Secured;

/**
 * @author Marko Voß
 *
 */
@RestController
@RequestMapping("/stations")
public class BarCodeScannerStationController {

  @Autowired
  private BarCodeScannerStationRepository stationRepository;

  /**
   *
   * @param station
   * @param authentication
   * @return
   */
  @Secured
  @RequestMapping(method = RequestMethod.POST)
  public String createStation(final BarCodeScannerStationDto station, final Authentication authentication) {

    return this.stationRepository.save(BarCodeScannerStation.builder()
        .latitude(station.getLatitude())
        .longitude(station.getLongitude())
        .name(station.getName())
        .userCreate((User) authentication.getDetails())
        .build()).getId();
  }

  /**
   *
   * @param id
   * @param station
   * @param authentication
   */
  @Secured
  @RequestMapping(method = RequestMethod.POST, path = "/{id}")
  public void updateStation(
      @PathVariable("id")
      final String id,
      final BarCodeScannerStationDto station,
      final Authentication authentication) {

    // Get existing station
    final Optional<BarCodeScannerStation> existingStationOp = this.stationRepository.findById(id);
    // Validate if exists
    if (existingStationOp.isEmpty())
      throw new IllegalArgumentException("No station found by id: " + id);

    final BarCodeScannerStation existingStation = existingStationOp.get();

    existingStation.setLatitude(station.getLatitude());
    existingStation.setLongitude(station.getLongitude());
    existingStation.setName(station.getName());
    existingStation.setTimestampUpdate(OffsetDateTime.now());
    existingStation.setUserUpdate((User) authentication.getDetails());

    this.stationRepository.save(existingStation);
  }

  /**
   *
   * @param pageable
   * @return
   */
  @RequestMapping(method = RequestMethod.GET)
  public Page<BarCodeScannerStationDto> getStations(
      @PageableDefault(page = 0, size = 20)
      @SortDefault.SortDefaults({
          @SortDefault(sort = "name", direction = Sort.Direction.ASC),
      })
      final Pageable pageable) {

    final Page<BarCodeScannerStation> result = this.stationRepository.findAll(pageable);

    return new PageImpl<>(
        result.stream().map(BarCodeScannerStationDto::fromEntity).collect(Collectors.toList()),
        pageable,
        result.getTotalElements());
  }
}
