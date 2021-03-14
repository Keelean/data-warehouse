package clustered.data.warehouse.model.helpers;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@Getter
@Setter
public class DealBean {
	private String id;
	private String fromCurrencyCode;
	private String toCurrencyCode;
	private String timestamp;
	private String amount;
	private boolean valid = true;
}
