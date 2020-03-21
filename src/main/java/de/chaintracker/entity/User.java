/**
 *
 */
package de.chaintracker.entity;

import java.time.OffsetDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
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
public class User {

  @Id
  @GeneratedValue(generator = "uuid")
  @GenericGenerator(name = "uuid", strategy = "uuid")
  @Column(updatable = false, nullable = false, unique = true, length = 32)
  private String id;

  @Column(columnDefinition = "TIMESTAMP WITH TIME ZONE", nullable = false, updatable = false)
  private OffsetDateTime timestampCreate;

  @Column(columnDefinition = "TIMESTAMP WITH TIME ZONE")
  private OffsetDateTime timestampUpdate;

  @Column(length = 250, nullable = false, unique = true)
  private String email;

  @Column(nullable = false)
  private boolean emailVerificationStatus;

  @Column(length = 255, nullable = false)
  private String emailVerificationToken;

  @Column(length = 256, nullable = false)
  private String encryptedPassword;

  @Column(length = 120, nullable = false)
  private String firstName;

  @Column(length = 250, nullable = false)
  private String lastName;

  @Column(nullable = false)
  private boolean isDeleted;

  @Column(nullable = false)
  private boolean isEnabled;

  @Column(length = 120, nullable = false)
  private String userName;

  @Column(length = 256)
  private String qrCode;

  @PrePersist
  void onPrePersist() {
    if (this.id == null) {
      setTimestampCreate(OffsetDateTime.now());
    }
  }

}
