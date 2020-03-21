/**
 *
 */
package de.chaintracker.repo;

import org.springframework.data.repository.CrudRepository;
import de.chaintracker.entity.Country;

/**
 * @author Marko Voß
 *
 */
public interface CountryRepository extends CrudRepository<Country, String> {

}
