package clustered.data.warehouse.model.helpers;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Builder
public class ReportSummary {
	
	private Long processDuration;
	private Integer noOfDeals;
	private Integer noOfInvalidDeals;
	
	public String toString() {
		return "[Process Duration="+processDuration +", No of Deals=" + noOfDeals + ", No of Invalid Deals="+noOfInvalidDeals+"]";
	}
}
