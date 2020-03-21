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
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class UserAddress {

  @Id
  @GeneratedValue(generator = "uuid")
  @GenericGenerator(name = "uuid", strategy = "uuid")
  @Column(updatable = false, nullable = false, unique = true, length = 32)
  private String id;

  @Column(length = 120)
  private String addressExternalId;

  @Column(length = 250)
  private String city;

  @Column(length = 250)
  private String country;

  @Column(name = "timestamp_create", columnDefinition = "TIMESTAMP WITH TIME ZONE")
  private OffsetDateTime timestampCreate;

  @Column(name = "timestamp_update", columnDefinition = "TIMESTAMP WITH TIME ZONE")
  private OffsetDateTime timestampUpdate;

  private boolean isDeleted;
  private boolean isEnabled;

  @Column(length = 120)
  private String postalCode;

  @Column(length = 250)
  private String streetname;

  @Column(length = 120)
  private String type;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  private User userCreate;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  private User userUpdate;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  private User user;
}
