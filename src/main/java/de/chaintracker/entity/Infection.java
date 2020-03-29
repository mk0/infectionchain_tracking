/**
 *
 */
package de.chaintracker.entity;

import java.time.OffsetDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.OneToOne;
import javax.persistence.PrePersist;
import javax.persistence.Table;
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
@Table(indexes = {
    @Index(name = "IDX_INFECTION_TYPE", columnList = "infectionType"),
})
public class Infection {

  @Id
  @GeneratedValue(generator = "uuid")
  @GenericGenerator(name = "uuid", strategy = "uuid")
  @Column(updatable = false, nullable = false, unique = true, length = 32)
  private String id;

  @OneToOne(fetch = FetchType.LAZY, optional = false)
  private User user;

  @Column(length = 8, columnDefinition = "varchar(8) default 'HEALTHY' NOT NULL", nullable = false)
  @Enumerated(EnumType.STRING)
  private InfectionType infectionType;

  @Column(nullable = false)
  private int stage;

  @Column(columnDefinition = "TIMESTAMP WITH TIME ZONE", nullable = false)
  private OffsetDateTime timestamp;

  @PrePersist
  void onPrePersist() {

    setTimestamp(OffsetDateTime.now());

    switch (this.infectionType) {
      case INFECTED:
        setStage(0);
        break;
      case HEALTHY:
        setStage(-1);
        break;
      case AT_RISK:
        if (this.stage < 1)
          setStage(1);
        break;
      default:
        break;
    }
  }
}
