/**
 *
 */
package de.chaintracker.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

/**
 * @author Marko Voß
 *
 */
@Data
@Builder
public class UserCreateDto {

  @NotBlank
  // TODO length?
  private String password;

  @NotNull
  @Size(max = 250)
  private String email;

  @NotNull
  @Size(max = 120)
  private String firstName;

  @NotNull
  @Size(max = 250)
  private String lastName;

  @NotNull
  @Size(max = 120)
  private String userName;
}
