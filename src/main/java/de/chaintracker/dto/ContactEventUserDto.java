package de.chaintracker.dto;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import de.chaintracker.entity.ContactEvent;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ContactEventUserDto {

  @Valid
  @NotNull
  private UserDto user1;

  @Valid
  @NotNull
  private UserDto user2;

  @NotNull
  private LocationEventDto locationEvent;

  public static final ContactEventUserDto fromEntity(final ContactEvent event) {
    return ContactEventUserDto.builder()
        .locationEvent(LocationEventDto.fromEntity(event.getLocationEvent()))
        .user1(UserDto.fromEntity(event.getUser1()))
        .user2(UserDto.fromEntity(event.getUser2()))
        .build();
  }
}
