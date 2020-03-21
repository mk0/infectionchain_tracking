/**
 *
 */
package de.chaintracker.dto;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

/**
 * @author Marko Voß
 *
 */
@Data
@Builder
public class UserCreateDto {

  @NotNull
  @Valid
  private UserDto user;

  @NotBlank
  // TODO length?
  private String password;
}
