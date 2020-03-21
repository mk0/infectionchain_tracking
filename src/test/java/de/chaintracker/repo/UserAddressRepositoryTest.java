/**
 *
 */
package de.chaintracker.repo;

import static org.assertj.core.api.Assertions.assertThat;
import java.time.OffsetDateTime;
import java.util.Optional;
import javax.persistence.EntityManager;
import javax.sql.DataSource;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import de.chaintracker.entity.User;
import de.chaintracker.entity.UserAddress;

/**
 * @author Marko Voß
 *
 */
@ExtendWith(SpringExtension.class)
@DataJpaTest
public class UserAddressRepositoryTest {

  @Autowired
  private DataSource dataSource;
  @Autowired
  private JdbcTemplate jdbcTemplate;
  @Autowired
  private EntityManager entityManager;
  @Autowired
  private UserAddressRepository addressRepository;
  @Autowired
  private UserRepository userRepository;

  @Test
  void injectedComponentsAreNotNull() {
    assertThat(this.dataSource).isNotNull();
    assertThat(this.jdbcTemplate).isNotNull();
    assertThat(this.entityManager).isNotNull();
    assertThat(this.addressRepository).isNotNull();
    assertThat(this.userRepository).isNotNull();
  }

  @Test
  void test_addressCreation() {

    this.userRepository.save(DBHelper.getUserMaxMustermann());
    final Optional<User> userMax = this.userRepository.findByEmail(DBHelper.getUserMaxMustermann().getEmail());
    assertThat(userMax).isNotEmpty();

    final UserAddress address1 = DBHelper.getAddressAlexanderplatz();
    address1.setUser(userMax.get());
    address1.setUserCreate(userMax.get());

    final UserAddress createdAddress1 = this.addressRepository.save(address1);

    assertThat(this.addressRepository.findById(createdAddress1.getId())).isNotNull();
    assertThat(createdAddress1.getTimestampCreate()).isNotNull();
    assertThat(createdAddress1.getTimestampUpdate()).isNull();

    // Check ManyToOne
    this.userRepository.save(DBHelper.getUserTimFourir());
    final Optional<User> userTim = this.userRepository.findByEmail(DBHelper.getUserTimFourir().getEmail());
    assertThat(userTim).isNotEmpty();

    final UserAddress address2 = DBHelper.getAddressSophienkirche();
    address2.setUser(userMax.get());
    address2.setUserCreate(userTim.get());

    final UserAddress createdAddress2 = this.addressRepository.save(address2);

    assertThat(this.addressRepository.findById(createdAddress2.getId())).isNotNull();
  }

  /**
   * Test behavior when referenced users get deleted.
   */
  @Test
  void test_addressUpdateAndDeletion() {

    /*
     * Create users
     */

    this.userRepository.save(DBHelper.getUserMaxMustermann());
    final Optional<User> userMax = this.userRepository.findByEmail(DBHelper.getUserMaxMustermann().getEmail());
    assertThat(userMax).isNotEmpty();

    this.userRepository.save(DBHelper.getUserTimFourir());
    final Optional<User> userTim = this.userRepository.findByEmail(DBHelper.getUserTimFourir().getEmail());
    assertThat(userTim).isNotEmpty();

    this.userRepository.save(DBHelper.getUserJohnDoe());
    final Optional<User> userJohn = this.userRepository.findByEmail(DBHelper.getUserJohnDoe().getEmail());
    assertThat(userTim).isNotEmpty();

    /*
     * Create and update address
     */

    final UserAddress address = DBHelper.getAddressAlexanderplatz();
    address.setUser(userMax.get());
    address.setUserCreate(userTim.get());

    final UserAddress createdAddress = this.addressRepository.save(address);
    assertThat(createdAddress.getTimestampCreate()).isNotNull();
    assertThat(createdAddress.getTimestampUpdate()).isNull();
    assertThat(createdAddress.getUserCreate()).isNotNull();
    assertThat(createdAddress.getUserUpdate()).isNull();

    try {
      // Wait one millisecond to ensure update timestamp is greater than creation timestamp
      Thread.sleep(1);
    } catch (final InterruptedException e) {
      throw new RuntimeException(e);
    }

    createdAddress.setUserUpdate(userJohn.get());
    createdAddress.setTimestampUpdate(OffsetDateTime.now());

    final UserAddress updatedAddress = this.addressRepository.save(createdAddress);
    assertThat(updatedAddress.getId()).isEqualTo(createdAddress.getId());
    assertThat(updatedAddress.getTimestampCreate()).isEqualTo(createdAddress.getTimestampCreate());
    assertThat(updatedAddress.getTimestampUpdate()).isNotNull();
    assertThat(updatedAddress.getTimestampUpdate()).isAfter(createdAddress.getTimestampCreate());
    assertThat(updatedAddress.getUserCreate()).isEqualTo(createdAddress.getUserCreate());
    assertThat(updatedAddress.getUserUpdate()).isNotNull();

    /*
     * Test deletion behavior
     */

    // Delete updater
    this.userRepository.delete(userJohn.get());
    assertThat(this.userRepository.findById(userJohn.get().getId())).isEmpty();

    final Optional<UserAddress> currentAddressOp = this.addressRepository.findById(createdAddress.getId());
    assertThat(currentAddressOp).isNotEmpty();
    final User user = currentAddressOp.get().getUserUpdate();
    // TODO
  }
}
