/**
 *
 */
package de.chaintracker.entity;

import java.time.OffsetDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
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
public class Location {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  private String userId;

  @Column(name = "timestamp", columnDefinition = "TIMESTAMP WITH TIME ZONE")
  private OffsetDateTime timestamp;

  private Double latitude;
  private Double longitude;
}
