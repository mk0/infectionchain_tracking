/**
 *
 */
package de.chaintracker.repo;

import java.util.List;
import java.util.Optional;
import org.springframework.data.repository.CrudRepository;
import de.chaintracker.entity.UserAddress;

/**
 * @author Marko Voß
 *
 */
public interface UserAddressRepository extends CrudRepository<UserAddress, String> {

  Optional<UserAddress> findByUserIdAndType(String userId, String type);

  List<UserAddress> findByUserId(String userId);

  void deleteByType(String type);
}
