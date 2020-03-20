/**
 *
 */
package de.chaintracker.util;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author Marko Voß
 *
 */
@Data
@AllArgsConstructor
public class Geocoord {

  private final double latitudeDecimal;
  private final double longitudeDecimal;

  public double latitude() {
    return Math.toRadians(this.latitudeDecimal);
  }

  public double longitude() {
    return Math.toRadians(this.longitudeDecimal);
  }
}
