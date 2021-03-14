package clustered.data.warehouse.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import clustered.data.warehouse.entities.CurrencyDeal;

@Repository
public interface CurrencyDealRepository extends JpaRepository<CurrencyDeal, Long> {
	
	Optional<CurrencyDeal> findBycurrencyCode(String currencyCode);
}
