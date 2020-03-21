package de.chaintracker.dto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LocationEventDto {

  @Size(max = 32, min = 32)
  private String id;

  @NotNull
  private Double latitude;

  @NotNull
  private Double longitude;

  @Size(max = 120)
  private String externalId;

  @Size(max = 250)
  private String name;

}
