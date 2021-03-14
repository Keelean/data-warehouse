package clustered.data.warehouse.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Entity
@Table
@Builder
public class CurrencyDeal extends BaseEntity{

	
	private static final long serialVersionUID = 1L;
	
	@Column
	private String currencyCode;
	@Column
	private Long countOfDeals;

}
