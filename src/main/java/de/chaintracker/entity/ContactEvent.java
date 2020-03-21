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
import javax.persistence.OneToOne;
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
public class ContactEvent {

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

  @ManyToOne(fetch = FetchType.LAZY, optional = true)
  private User userUpdate;

  @Column(length = 120)
  private String externalId;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  private User user1;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  private User user2;

  @OneToOne(fetch = FetchType.LAZY, optional = true)
  private LocationEvent locationEvent;

  @PrePersist
  void onPrePersist() {
    if (this.id == null)
      setTimestampCreate(OffsetDateTime.now());
  }
}
