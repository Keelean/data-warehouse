package clustered.data.warehouse.services.impl;

import java.util.Optional;

import org.springframework.stereotype.Service;

import clustered.data.warehouse.entities.CurrencyDeal;
import clustered.data.warehouse.repositories.CurrencyDealRepository;
import clustered.data.warehouse.services.CurrencyDealService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class CurrencyDealServiceImpl implements CurrencyDealService {
	
	private final CurrencyDealRepository currencyDealRepository;

	@Override
	public void incrementDeals(String currencyCode, long count) {
		Optional<CurrencyDeal> optionalCurrencyDeal = currencyDealRepository.findBycurrencyCode(currencyCode);
		if(optionalCurrencyDeal.isPresent()) {
			CurrencyDeal currencyDeal = optionalCurrencyDeal.get();
			currencyDeal.setCountOfDeals(currencyDeal.getCountOfDeals() + count);
			return;
		}
		
		currencyDealRepository.save(CurrencyDeal.builder().currencyCode(currencyCode).countOfDeals(count).build());
		return;
	}

}
