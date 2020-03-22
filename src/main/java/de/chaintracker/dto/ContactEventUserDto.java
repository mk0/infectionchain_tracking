package de.chaintracker.dto;

import java.time.OffsetDateTime;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import de.chaintracker.entity.ContactEvent;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ContactEventUserDto {

  @NotBlank
  private String username1;

  @NotBlank
  private String username2;

  @NotNull
  private OffsetDateTime timestamp;

  @NotNull
  private LocationEventDto locationEvent;

  public static final ContactEventUserDto fromEntity(final ContactEvent event) {
    return ContactEventUserDto.builder()
        .locationEvent(LocationEventDto.fromEntity(event.getLocationEvent()))
        .username1(event.getUser1().getUserName())
        .username2(event.getUser2().getUserName())
        .timestamp(event.getTimestampCreate())
        .build();
  }
}
