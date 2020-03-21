/**
 *
 */
package de.chaintracker.repo;

import de.chaintracker.entity.User;
import de.chaintracker.entity.UserAddress;
import lombok.Builder;
import lombok.Data;

/**
 * @author Marko Voß
 *
 */
@Data
@Builder
public class DBHelper {

  public static User getUserMaxMustermann() {
    return User.builder()
        .email("max.mustermann@example.com")
        .encryptedPassword("f9e18e5428fcd197c72869375154f7c830be2729e163def13e0e3574ba6fd3d7")
        .firstName("Max")
        .lastName("Mustermann")
        .userName("@Max")
        .build();
  }

  public static User getUserTimFourir() {
    return User.builder()
        .email("tim.fourir@example.com")
        .encryptedPassword("f9e18e5428fcd197c72869375154f7c830be2729e163def13e0e3574ba6fd3d7")
        .firstName("Tim")
        .lastName("Fourir")
        .userName("@Tim")
        .build();
  }

  public static User getUserJohnDoe() {
    return User.builder()
        .email("john.doe@example.com")
        .encryptedPassword("f9e18e5428fcd197c72869375154f7c830be2729e163def13e0e3574ba6fd3d7")
        .firstName("John")
        .lastName("Doe")
        .userName("@John")
        .build();
  }

  public static UserAddress getAddressAlexanderplatz() {
    return UserAddress.builder()
        .addressExternalId("ExternalId")
        .city("Berlin")
        .country("Germany")
        .postalCode("10178")
        .streetname("Alexanderplatz")
        .type("Anschrift")
        .build();
  }

  public static UserAddress getAddressSophienkirche() {
    return UserAddress.builder()
        .addressExternalId("ExternalId")
        .city("Berlin")
        .country("Germany")
        .postalCode("10178")
        .streetname("Alexanderplatz")
        .type("Arbeit")
        .build();
  }
}
