package clustered.data.warehouse.services;

public interface CurrencyDealService {
	
	void incrementDeals(String currencyCode, long count);
}
