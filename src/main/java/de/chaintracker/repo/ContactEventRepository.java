/**
 *
 */
package de.chaintracker.repo;

import java.util.List;
import org.springframework.data.repository.CrudRepository;
import de.chaintracker.entity.ContactEvent;
import de.chaintracker.entity.User;

/**
 * @author Marko Voß
 *
 */
public interface ContactEventRepository extends CrudRepository<ContactEvent, String> {

  List<ContactEvent> findByUser1OrUser2(User user1, User user2);

}
