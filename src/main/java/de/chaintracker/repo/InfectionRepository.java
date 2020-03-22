/**
 *
 */
package de.chaintracker.repo;

import java.time.OffsetDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import de.chaintracker.entity.ContactEvent;
import de.chaintracker.entity.Infection;

/**
 * @author Marko Voß
 *
 */
public interface InfectionRepository extends CrudRepository<Infection, String> {

  void deleteByUserId(String id);

  boolean findByUserId(String id);

  /**
   * Using euclidean distance approximation for fast distance calculation since the target distances
   * are very small.
   *
   * @param userId
   * @param minTime
   * @param distanceKm
   * @return
   */
  // u2 = infected, p2 = locations of u2
  @Query("SELECT DISTINCT c1 FROM LocationEvent p1, LocationEvent p2, User u1, User u2, Infection i2, ContactEvent c1, ContactEvent c2 "
      + "WHERE u1.id = :userId"
      + " AND u2.id <> :userId"
      + " AND u2.id = i2.user.id"
      + " AND i2.isInfected = true"
      + " AND (c1.user1.id = :userId OR c1.user2.id = :userId)"
      + " AND (c1.locationEvent.id = p1.id)"
      + " AND (c2.user1.id = u2.id OR c2.user2.id = u2.id)"
      + " AND (c2.locationEvent.id = p2.id)"
      + " AND p1.timestampCreate <= i2.timestamp"
      + " AND p1.timestampCreate >= :minTime"
      + " AND p2.timestampCreate <= i2.timestamp"
      + " AND p2.timestampCreate >= :minTime"
      + " AND (p1.timestampCreate >= DATEADD('MINUTE', -:deltaTime, p2.timestampCreate)"
      + " OR p1.timestampCreate <= DATEADD('MINUTE',  :deltaTime, p2.timestampCreate))"
      + " AND 111.319 * SQRT("
      + "(p2.latitude - p1.latitude)*(p2.latitude - p1.latitude) + "
      + "((p2.longitude - p1.longitude) * COS((p2.latitude + p1.latitude) * 0.00872664626))*"
      + "((p2.longitude - p1.longitude) * COS((p2.latitude + p1.latitude) * 0.00872664626))) <= :distanceKm")
  List<ContactEvent> findContactEvents(String userId, double distanceKm, int deltaTime, OffsetDateTime minTime);
}
