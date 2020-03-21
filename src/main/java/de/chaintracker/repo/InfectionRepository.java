/**
 *
 */
package de.chaintracker.repo;

import org.springframework.data.repository.CrudRepository;
import de.chaintracker.entity.Infection;

/**
 * @author Marko Voß
 *
 */
public interface InfectionRepository extends CrudRepository<Infection, String> {

  void deleteByUserId(String id);

  boolean findByUserId(String id);

}
