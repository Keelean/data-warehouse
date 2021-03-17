package clustered.data.warehouse.model.helpers;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.univocity.parsers.annotations.Parsed;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Builder.Default;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@ToString
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DealBean {
	
	@Parsed(index = 0)
	private String dealId;
	@Parsed(index = 1)
	private String fromCurrencyCode;
	@Parsed(index = 2)
	private String toCurrencyCode;
	@Parsed(index = 3)
	private String dealTimestamp;
	@Parsed(index = 4)
	private String amount;
	
	@Default
	private boolean valid = true;

}
