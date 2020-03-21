/**
 *
 */
package de.chaintracker.repo;

import static org.assertj.core.api.Assertions.assertThat;
import java.nio.charset.StandardCharsets;
import javax.persistence.EntityManager;
import javax.sql.DataSource;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import com.google.common.hash.Hashing;
import de.chaintracker.entity.User;

/**
 * @author Marko Voß
 *
 */
@ExtendWith(SpringExtension.class)
@DataJpaTest
public class UserRepositoryTest {

  @Autowired
  private DataSource dataSource;
  @Autowired
  private JdbcTemplate jdbcTemplate;
  @Autowired
  private EntityManager entityManager;
  @Autowired
  private UserRepository userRepository;

  @Test
  void injectedComponentsAreNotNull() {
    assertThat(this.dataSource).isNotNull();
    assertThat(this.jdbcTemplate).isNotNull();
    assertThat(this.entityManager).isNotNull();
    assertThat(this.userRepository).isNotNull();
  }

  public static void main(final String[] args) {
    System.out.println(Hashing.sha256().hashString("Hallo123!", StandardCharsets.UTF_8).toString());
  }

  @Test
  void test_userCreation() {

    final User createdUser = this.userRepository.save(User.builder()
        .email("max.mustermann@example.com")
        .encryptedPassword(Hashing.sha256().hashString("Hallo123!", StandardCharsets.UTF_8).toString())
        .firstName("Max")
        .lastName("Mustermann")
        .userId("@Max")
        .userName("max.mustermann")
        .build());

    assertThat(this.userRepository.findById(createdUser.getId())).isNotNull();
    assertThat(this.userRepository.findByEmail(createdUser.getEmail())).isNotNull();
    assertThat(createdUser.getEmailVerificationToken()).isNotNull();
    assertThat(createdUser.getTimestampCreate()).isNotNull();
    assertThat(createdUser.getTimestampUpdate()).isNull();
  }
}

