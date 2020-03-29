/**
 *
 */
package de.chaintracker.domain;

import de.chaintracker.entity.LocationEvent;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

/**
 * @author Marko Voß
 *
 */
@Data
@Builder
@AllArgsConstructor
public class DistanceSearchResult {

  private LocationEvent locationA;
  private LocationEvent locationB;

  private double distance;
  private int timeDiff;
}
