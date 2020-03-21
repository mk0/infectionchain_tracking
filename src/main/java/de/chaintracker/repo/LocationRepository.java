/**
 *
 */
package de.chaintracker.repo;

import java.time.OffsetDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import de.chaintracker.entity.Location;

/**
 * @author Marko Voß
 * @deprecated Can be deleted.
 */
@Deprecated
public interface LocationRepository extends CrudRepository<Location, Long> {


  /**
   * Using fast equirectangular distance approximation since the distances are small. (e.g. 20 meter)
   *
   * Note: Cannot make use of POW function as this function is named differently in different
   * databases.
   *
   * @param distance in kilometers
   * @param maxTime maximum time
   * @param minTime minimum time
   * @return
   */
  @Query("SELECT DISTINCT p2 FROM Location p1, Location p2 "
      + "WHERE p1.userId = :userId"
      + " AND p2.userId <> :userId"
      + " AND p1.timestamp <= :maxTime "
      + " AND p1.timestamp >= :minTime "
      + " AND p2.timestamp <= :maxTime "
      + " AND p2.timestamp >= :minTime "
      + " AND 111.319 * SQRT((p2.latitude - p1.latitude)*(p2.latitude - p1.latitude) + ((p2.longitude - p1.longitude) * COS((p2.latitude + p1.latitude) * 0.00872664626))*((p2.longitude - p1.longitude) * COS((p2.latitude + p1.latitude) * 0.00872664626))) <= :distance")
  List<Location> findByUserIdAndDistanceAndTimestamp(
      @Param("userId") String userId,
      @Param("distance") Double distance,
      @Param("maxTime") OffsetDateTime maxTime,
      @Param("minTime") OffsetDateTime minTime);

  @Query("SELECT DISTINCT 1000 * 111.319 * SQRT((p2.latitude - p1.latitude)*(p2.latitude - p1.latitude) + ((p2.longitude - p1.longitude) * COS((p2.latitude + p1.latitude) * 0.00872664626))*((p2.longitude - p1.longitude) * COS((p2.latitude + p1.latitude) * 0.00872664626))) FROM Location p1, Location p2 "
      + "WHERE p1.userId = :userId"
      + " AND p2.userId <> :userId"
      + " AND p1.timestamp <= :maxTime "
      + " AND p1.timestamp >= :minTime "
      + " AND p2.timestamp <= :maxTime "
      + " AND p2.timestamp >= :minTime "
      + " AND 111.319 * SQRT((p2.latitude - p1.latitude)*(p2.latitude - p1.latitude) + ((p2.longitude - p1.longitude) * COS((p2.latitude + p1.latitude) * 0.00872664626))*((p2.longitude - p1.longitude) * COS((p2.latitude + p1.latitude) * 0.00872664626))) <= :distance")
  List<Double> findByUserIdAndDistanceAndTimestamp2(
      @Param("userId") String userId,
      @Param("distance") Double distance,
      @Param("maxTime") OffsetDateTime maxTime,
      @Param("minTime") OffsetDateTime minTime);
}
