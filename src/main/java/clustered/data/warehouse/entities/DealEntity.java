package clustered.data.warehouse.entities;

import javax.persistence.MappedSuperclass;

import lombok.AllArgsConstructor;
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
public class DealEntity extends BaseEntity {

	private static final long serialVersionUID = 1L;

	private String filename;
	private String fromCurrency;
	private String toCurrency;
}
