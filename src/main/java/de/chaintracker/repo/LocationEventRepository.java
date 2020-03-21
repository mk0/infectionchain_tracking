/**
 *
 */
package de.chaintracker.repo;

import org.springframework.data.repository.CrudRepository;
import de.chaintracker.entity.LocationEvent;

/**
 * @author Marko Voß
 *
 */
public interface LocationEventRepository extends CrudRepository<LocationEvent, String> {

}
