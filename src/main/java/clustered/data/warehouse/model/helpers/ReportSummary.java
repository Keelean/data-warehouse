package clustered.data.warehouse.model.helpers;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import clustered.data.warehouse.model.helpers.DealBean.DealBeanBuilder;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReportSummary {
	
	private Long processDuration;
	private Integer noOfDeals;
	private Integer noOfInvalidDeals;
	
	public String toString() {
		return "[Process Duration="+processDuration +", No of Deals=" + noOfDeals + ", No of Invalid Deals="+noOfInvalidDeals+"]";
	}
}
