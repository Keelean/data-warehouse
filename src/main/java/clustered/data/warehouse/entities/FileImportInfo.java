package clustered.data.warehouse.entities;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
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
@Builder
public class FileImportInfo extends BaseEntity {

	private static final long serialVersionUID = 1L;

	@Column
	private String filename;

	
	@OneToMany(mappedBy = "fileInfo", cascade = CascadeType.ALL)
	private Set<InvalidDeal> invalidDeals;
	
	@OneToMany(mappedBy = "fileInfo", cascade = CascadeType.ALL)
	private Set<ValidDeal> validDeals;

}
