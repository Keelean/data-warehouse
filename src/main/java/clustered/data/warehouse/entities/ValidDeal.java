package clustered.data.warehouse.entities;

import javax.persistence.Entity;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Entity
@Table
public class ValidDeal extends BaseEntity {
	
	private static final long serialVersionUID = 1L;

}
