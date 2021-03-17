package clustered.data.warehouse.entities;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@MappedSuperclass
@Builder
public class DealEntity extends BaseEntity {

	private static final long serialVersionUID = 1L;
	
	@Column
	private String dealId;

	@Column
	private String fromCurrencyCode;
	@Column
	private String toCurrencyCode;
	
	@Column
	protected String dealTimestamp;
	@Column
	private String amount;
	
}
