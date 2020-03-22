package de.chaintracker.dto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import de.chaintracker.entity.LocationEvent;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LocationEventDto {

  @NotNull
  private Double latitude;

  @NotNull
  private Double longitude;

  @Size(max = 120)
  private String externalId;

  @Size(max = 250)
  private String name;

  public static final LocationEventDto fromEntity(final LocationEvent event) {
    return LocationEventDto.builder()
        .externalId(event.getExternalId())
        .latitude(Math.toDegrees(event.getLatitude()))
        .longitude(Math.toDegrees(event.getLongitude()))
        .name(event.getName())
        .build();
  }
}
