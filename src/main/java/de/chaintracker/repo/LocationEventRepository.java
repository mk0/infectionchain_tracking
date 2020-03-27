/**
 *
 */
package de.chaintracker.repo;

import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import de.chaintracker.domain.Pair;
import de.chaintracker.entity.LocationEvent;

/**
 * @author Marko Voß
 *
 */
public interface LocationEventRepository extends CrudRepository<LocationEvent, String> {

  @Query("SELECT DISTINCT NEW de.chaintracker.domain.Pair(p1, p2) FROM LocationEvent p1, LocationEvent p2 "
      + "WHERE p1.id <> p2.id"
      + " AND (p1.latitude - p2.latitude) * (p1.latitude - p2.latitude) + "
      + " COS((p1.latitude + p2.latitude) / 2) * COS((p1.latitude + p2.latitude) / 2) *"
      + " (p1.longitude - p2.longitude) * (p1.longitude - p2.longitude) <= :distanceAlpha")
  List<Pair<LocationEvent, LocationEvent>> findLocations(double distanceAlpha);
}
