/**
 *
 */
package de.chaintracker.repo;

import org.springframework.data.repository.CrudRepository;
import de.chaintracker.entity.ContactEvent;

/**
 * @author Marko Voß
 *
 */
public interface ContactEventRepository extends CrudRepository<ContactEvent, String> {

}
