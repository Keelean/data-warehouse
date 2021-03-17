package clustered.data.warehouse.validator;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import clustered.data.warehouse.model.helpers.DealBean;

@SpringBootTest
public class DealValidatorTest {

	@Autowired
	private DealValidator dealValidator;

	@Test
	@DisplayName("Test Invalid Amount")
	void testInvalidAmount() {

		DealBean dealBean = DealBean.builder().amount("5.5H").dealId("89").fromCurrencyCode("EUR")
				.toCurrencyCode("USD").dealTimestamp("2019-03-04T23:09:06").build();
		
		DealBean validated = dealValidator.validate(dealBean);
		Assertions.assertFalse(validated.isValid());

	}

	@Test
	@DisplayName("Test invalid Currency Code")
	void testInvalidCurrencyCode() {
		DealBean dealBean = DealBean.builder().amount("5.5").dealId("89").fromCurrencyCode("USD")
				.toCurrencyCode("USD").dealTimestamp("2019-03-04T23:09:06").build();
		
		DealBean validated = dealValidator.validate(dealBean);
		Assertions.assertFalse(validated.isValid());
		
		dealBean = DealBean.builder().amount("5.5").dealId("89").fromCurrencyCode("NGN")
				.toCurrencyCode("USD").dealTimestamp("2019-03-04T23:09:06").build();
		
		validated = dealValidator.validate(dealBean);
		Assertions.assertFalse(validated.isValid());
		
		dealBean = DealBean.builder().amount("5.5").dealId("89").fromCurrencyCode("USD")
				.toCurrencyCode("HHH").dealTimestamp("2019-03-04T23:09:06").build();
		
		validated = dealValidator.validate(dealBean);
		Assertions.assertFalse(validated.isValid());
	}
	
	@Test
	@DisplayName("Test Invalid Deal ID")
	void testInvalidDealId() {

		DealBean dealBean = DealBean.builder().amount("5.5").dealId("8U9").fromCurrencyCode("EUR")
				.toCurrencyCode("USD").dealTimestamp("2019-03-04T23:09:06").build();
		
		DealBean validated = dealValidator.validate(dealBean);
		Assertions.assertFalse(validated.isValid());

	}
	
	@Test
	@DisplayName("Test Invalid Deal Timestamp")
	void testInvalidDealTimestamp() {

		DealBean dealBean = DealBean.builder().amount("5.5").dealId("89").fromCurrencyCode("EUR")
				.toCurrencyCode("USD").dealTimestamp("2019-03-04T23:09:06,999").build();
		
		DealBean validated = dealValidator.validate(dealBean);
		Assertions.assertFalse(validated.isValid());

	}
	
	@Test
	@DisplayName("Test Valid Deal")
	void testValidDeal() {

		DealBean dealBean = DealBean.builder().amount("55").dealId("89").fromCurrencyCode("EUR")
				.toCurrencyCode("USD").dealTimestamp("2019-03-04T23:09:06").build();
		
		DealBean validated = dealValidator.validate(dealBean);
		Assertions.assertTrue(validated.isValid());

	}
}
