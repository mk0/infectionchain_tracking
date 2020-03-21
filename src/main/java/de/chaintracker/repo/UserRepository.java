/**
 *
 */
package de.chaintracker.repo;

import org.springframework.data.repository.CrudRepository;
import de.chaintracker.entity.User;

/**
 * @author Marko Voß
 *
 */
public interface UserRepository extends CrudRepository<User, String> {

}
