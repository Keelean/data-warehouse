package clustered.data.warehouse.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import clustered.data.warehouse.entities.InvalidDeal;


@Repository
public interface InvalidDealRepository extends JpaRepository<InvalidDeal, Long>{

}
