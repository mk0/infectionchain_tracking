/**
 *
 */
package de.chaintracker.repo;

import org.springframework.data.repository.CrudRepository;
import de.chaintracker.entity.UserAddress;

/**
 * @author Marko Voß
 *
 */
public interface UserAddressRepository extends CrudRepository<UserAddress, String> {

}
