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
import javax.persistence.PreUpdate;
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
public class LocationEvent {

  @Id
  @GeneratedValue(generator = "uuid")
  @GenericGenerator(name = "uuid", strategy = "uuid")
  @Column(updatable = false, nullable = false, unique = true, length = 32)
  private String id;

  private Double latitude;
  private Double longitude;

  @Column(name = "timestamp_create", columnDefinition = "TIMESTAMP WITH TIME ZONE")
  private OffsetDateTime timestampCreate;

  @Column(name = "timestamp_update", columnDefinition = "TIMESTAMP WITH TIME ZONE")
  private OffsetDateTime timestampUpdate;

  @Column(length = 120)
  private String externalId;

  private boolean isDeleted;
  private boolean isEnabled;

  @Column(length = 250)
  private String name;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  private User userCreate;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  private User userUpdate;

  @PrePersist
  void onPrePersist() {
    setTimestampCreate(OffsetDateTime.now());
  }

  @PreUpdate
  void onPreUpdate() {
    setTimestampUpdate(OffsetDateTime.now());
  }
}
