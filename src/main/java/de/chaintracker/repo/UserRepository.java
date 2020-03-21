/**
 *
 */
package de.chaintracker.repo;

import java.util.Optional;
import org.springframework.data.repository.CrudRepository;
import de.chaintracker.entity.User;

/**
 * @author Marko Voß
 *
 */
public interface UserRepository extends CrudRepository<User, String> {

  Optional<User> findByEmail(String email);

  boolean existsByEmail(String email);

}
