package de.chaintracker.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ContactEventDto {

  @NotBlank
  private String scannedQrCode;

  @NotNull
  private LocationEventDto locationEvent;
}
