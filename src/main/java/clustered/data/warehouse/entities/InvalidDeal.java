package clustered.data.warehouse.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Entity
@Table
@NoArgsConstructor
@AllArgsConstructor
public class InvalidDeal extends BaseEntity {

	private static final long serialVersionUID = 1L;

	@Column
	private String dealId;
	@Column
	private String fromCurrencyCode;
	@Column
	private String toCurrencyCode;
	@Column(name = "deal_timestamp")
	private String stringTimestamp;
	@Column(name = "amount")
	private String stringAmount;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "file_info_id")
	private FileImportInfo fileInfo;

}
