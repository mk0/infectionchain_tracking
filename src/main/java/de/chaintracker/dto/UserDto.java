<<<<<<< HEAD
/**
 *
 */
package de.chaintracker.dto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import de.chaintracker.entity.User;
import lombok.Builder;
import lombok.Data;

/**
 * @author Marko Voß
 *
 */
@Data
@Builder
public class UserDto {

  @Size(max = 32, min = 32)
  private String id;

  @NotNull
  @Size(max = 250)
  private String email;

  @NotNull
  private Boolean emailVerificationStatus;

  @NotNull
  @Size(max = 120)
  private String firstName;

  @NotNull
  @Size(max = 250)
  private String lastName;

  @NotNull
  @Size(max = 120)
  private String userName;

  public static final UserDto fromEntity(final User entity) {
    return UserDto.builder()
        .email(entity.getEmail())
        .emailVerificationStatus(entity.isEmailVerificationStatus())
        .firstName(entity.getFirstName())
        .id(entity.getId())
        .lastName(entity.getLastName())
        .userName(entity.getUserName())
        .build();
  }
}
=======
package de.chaintracker.dto;

public class UserDto {

}
>>>>>>> branch 'master' of https://github.com/mk0/infectionchain_tracking
