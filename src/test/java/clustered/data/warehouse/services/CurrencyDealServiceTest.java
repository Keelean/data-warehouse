package clustered.data.warehouse.services;

import static org.mockito.Mockito.doReturn;

import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import clustered.data.warehouse.entities.CurrencyDeal;
import clustered.data.warehouse.repositories.CurrencyDealRepository;
import lombok.extern.slf4j.Slf4j;

@SpringBootTest
@Slf4j
public class CurrencyDealServiceTest {

	@Autowired
	private CurrencyDealService currencyDealService;

	@MockBean
	private CurrencyDealRepository currencyDealRepository;

	@Test
	public void testCurrencyDeal() {
		CurrencyDeal currencyDeal = CurrencyDeal.builder().currencyCode("EUR").countOfDeals(0L).build();
		doReturn(Optional.of(currencyDeal)).when(currencyDealRepository).findBycurrencyCode("EUR");
		doReturn(currencyDeal).when(currencyDealRepository).save(currencyDeal);
		currencyDealService.incrementDeals("EUR", 5);
		Assertions.assertEquals(5, currencyDeal.getCountOfDeals());
		currencyDealService.incrementDeals("EUR", 5);
		Assertions.assertEquals(10, currencyDeal.getCountOfDeals());

	}
}
