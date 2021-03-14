package clustered.data.warehouse.model.helpers;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.univocity.parsers.annotations.Parsed;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@Getter
@Setter
public class DealBean {
	
	@Parsed(index = 0)
	private String dealId;
	@Parsed(index = 1)
	private String fromCurrencyCode;
	@Parsed(index = 2)
	private String toCurrencyCode;
	@Parsed(index = 3)
	private String stringTimestamp;
	@Parsed(index = 4)
	private String stringAmount;
	
	private boolean valid = true;
	private BigDecimal amount;
	private LocalDateTime dealTimestamp;
}
