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
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import org.hibernate.annotations.GenericGenerator;
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
public class CountryTravelHistory {

  @Id
  @GeneratedValue(generator = "uuid")
  @GenericGenerator(name = "uuid", strategy = "uuid")
  @Column(updatable = false, nullable = false, unique = true, length = 32)
  private String id;

  @Column(name = "timestamp_create", columnDefinition = "TIMESTAMP WITH TIME ZONE")
  private OffsetDateTime timestampCreate;

  @Column(name = "timestamp_update", columnDefinition = "TIMESTAMP WITH TIME ZONE")
  private OffsetDateTime timestampUpdate;

  private boolean isDeleted;
  private boolean isEnabled;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  private User userCreate;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  private User userUpdate;

  @Column(length = 32)
  private String cityName;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  private Country country;

  @Column(name = "date_travel_from", columnDefinition = "TIMESTAMP WITH TIME ZONE")
  private OffsetDateTime timestampTravelFrom;

  @Column(name = "date_travel_to", columnDefinition = "TIMESTAMP WITH TIME ZONE")
  private OffsetDateTime timestampTravelTo;

  @Column(length = 250)
  private String note;

  @PrePersist
  void onPrePersist() {
    if (this.id == null)
      setTimestampCreate(OffsetDateTime.now());
  }
}
