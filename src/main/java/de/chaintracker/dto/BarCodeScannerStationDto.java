/**
 *
 */
package de.chaintracker.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import de.chaintracker.entity.BarCodeScannerStation;
import lombok.Builder;
import lombok.Data;

/**
 * @author Marko Voß
 *
 */
@Data
@Builder
public class BarCodeScannerStationDto {

  @Size(max = 32, min = 32)
  private String id;

  @NotBlank
  @Size(max = 250)
  private String name;

  @NotNull
  private Double latitude;

  @NotNull
  private Double longitude;

  public static final BarCodeScannerStationDto fromEntity(final BarCodeScannerStation station) {
    return BarCodeScannerStationDto.builder()
        .id(station.getId())
        .latitude(station.getLatitude())
        .longitude(station.getLongitude())
        .name(station.getName())
        .build();
  }
}
