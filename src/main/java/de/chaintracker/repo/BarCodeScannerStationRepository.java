/**
 *
 */
package de.chaintracker.repo;

import org.springframework.data.repository.PagingAndSortingRepository;
import de.chaintracker.entity.BarCodeScannerStation;

/**
 * @author Marko Voß
 *
 */
public interface BarCodeScannerStationRepository extends PagingAndSortingRepository<BarCodeScannerStation, String> {

}
