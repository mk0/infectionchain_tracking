/**
 *
 */
package de.chaintracker.repo;

import org.springframework.data.repository.CrudRepository;
import de.chaintracker.entity.CountryTravelHistory;

/**
 * @author Marko Voß
 *
 */
public interface CountryTravelHistoryRepository extends CrudRepository<CountryTravelHistory, String> {

}
