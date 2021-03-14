package clustered.data.warehouse.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import clustered.data.warehouse.entities.ValidDeal;


@Repository
public interface ValidDealRepository extends JpaRepository<ValidDeal, Long>{

}
