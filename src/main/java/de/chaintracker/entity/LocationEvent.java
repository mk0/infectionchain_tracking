/**
 *
 */
package de.chaintracker.entity;

import java.time.OffsetDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.ManyToOne;
import javax.persistence.NamedNativeQueries;
import javax.persistence.NamedNativeQuery;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import org.hibernate.annotations.GenericGenerator;
import com.github.davidmoten.geo.GeoHash;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Marko Voß
 *
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@NamedNativeQueries({
    @NamedNativeQuery(
        name = "LocationEvent.findByDistanceAlpha",
        query = "SELECT p1.* FROM location_event p1, location_event p2 "
            + "WHERE p1.id <> p2.id"
            + " AND p1.geohash LIKE LEFT(p2.geohash)"
            + " AND (p1.latitude - p2.latitude) * (p1.latitude - p2.latitude) + "
            + " COS((p1.latitude + p2.latitude) / 2) * COS((p1.latitude + p2.latitude) / 2) *"
            + " (p1.longitude - p2.longitude) * (p1.longitude - p2.longitude) <= :distanceAlpha",
        resultClass = LocationEvent.class)
})
// @SqlResultSetMapping(name = "DistanceSearchResult", classes = {
// @ConstructorResult(targetClass = DistanceSearchResult.class,
// columns = {@ColumnResult(name = "name"), @ColumnResult(name = "age")})
// })
@Table(indexes = {
    @Index(name = "IDX_BOUNDARY7", columnList = "boundary7"),
    @Index(name = "IDX_BOUNDARY8", columnList = "boundary8"),
    @Index(name = "IDX_GEOHASH", columnList = "geoHash")
})
public class LocationEvent {

  @Id
  @GeneratedValue(generator = "uuid")
  @GenericGenerator(name = "uuid", strategy = "uuid")
  @Column(updatable = false, nullable = false, unique = true, length = 32)
  private String id;

  private Double latitude;
  private Double longitude;

  @Column(length = 11)
  private String geoHash;

  @Column(length = 8)
  private String boundary8;

  @Column(length = 7)
  private String boundary7;

  @Column(name = "timestamp_create", columnDefinition = "TIMESTAMP WITH TIME ZONE")
  private OffsetDateTime timestampCreate;

  @Column(length = 120)
  private String externalId;

  @Column(length = 250)
  private String name;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  private User userCreate;

  @PrePersist
  void onPrePersist() {
    if (this.id == null)
      setTimestampCreate(OffsetDateTime.now());

    final String geohash = GeoHash.encodeHash(Math.toDegrees(this.latitude), Math.toDegrees(this.longitude), 11);
    setGeoHash(geohash);
    setBoundary7(geohash.substring(0, 7));
    setBoundary8(geohash.substring(0, 8));
  }
}
