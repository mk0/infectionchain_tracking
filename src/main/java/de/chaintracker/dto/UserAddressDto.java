package de.chaintracker.dto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import de.chaintracker.entity.UserAddress;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserAddressDto {

  @NotNull
  @Size(max = 250)
  private String city;

  @NotNull
  @Size(max = 250)
  private String country;

  @NotNull
  @Size(max = 120)
  private String postalCode;

  @NotNull
  @Size(max = 250)
  private String streetname;

  @NotNull
  @Size(max = 120)
  private String type;

  public static final UserAddressDto fromEntity(final UserAddress address) {
    return UserAddressDto.builder()
        .city(address.getCity())
        .country(address.getCountry())
        .postalCode(address.getPostalCode())
        .streetname(address.getStreetname())
        .type(address.getType())
        .build();
  }
}
