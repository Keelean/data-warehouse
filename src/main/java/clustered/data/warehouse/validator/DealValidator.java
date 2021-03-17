package clustered.data.warehouse.validator;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import clustered.data.warehouse.model.helpers.DealBean;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class DealValidator {

	public DealBean validate(DealBean bean) {
				
		if(Objects.isNull(bean.getDealId()) || !bean.getDealId().matches("^\\d+$")) {
			bean.setValid(false);
			return bean;
		}
				
		if (Objects.isNull(bean.getAmount()) || Objects.isNull(bean.getFromCurrencyCode())
				|| Objects.isNull(bean.getToCurrencyCode()) || Objects.isNull(bean.getDealTimestamp())) {
			bean.setValid(false);
			return bean;
		}
		
		if (bean.getFromCurrencyCode().isEmpty() || bean.getToCurrencyCode().isEmpty()
				|| bean.getDealTimestamp().isEmpty() || bean.getAmount().isEmpty()) {
			bean.setValid(false);
			return bean;
		}
		
		if (bean.getFromCurrencyCode().equals(bean.getToCurrencyCode())) {
			bean.setValid(false);
			return bean;
		}
		
		if (!bean.getFromCurrencyCode().matches("(USD|EUR|GBP|JOD|BHD)")) {
			bean.setValid(false);
			return bean;
		}
		

		if (!bean.getToCurrencyCode().matches("(USD|EUR|GBP|JOD|BHD)")) {
			bean.setValid(false);
			return bean;
		}

		if (!bean.getAmount().matches("^\\d*\\.?\\d*$")) {
			bean.setValid(false);
			return bean;
		}
		
		
		// matches this format 2018-05-31T12:19:23
		//(\\d{4}-\\d{2}-\\d{2})[A-Z]+(\\d{2}:\\d{2}:\\d{2}).
		//^(?:[1-9]\d{3}-(?:(?:0[1-9]|1[0-2])-(?:0[1-9]|1\d|2[0-8])|(?:0[13-9]|1[0-2])-(?:29|30)|(?:0[13578]|1[02])-31)|(?:[1-9]\d(?:0[48]|[2468][048]|[13579][26])|(?:[2468][048]|[13579][26])00)-02-29)T(?:[01]\d|2[0-3]):[0-5]\d:[0-5]\d(?:Z|[+-][01]\d:[0-5]\d)$
		if (!bean.getDealTimestamp().matches("^(?:[1-9]\\d{3}-(?:(?:0[1-9]|1[0-2])-(?:0[1-9]|1\\d|2[0-8])|(?:0[13-9]|1[0-2])-(?:29|30)|(?:0[13578]|1[02])-31)|(?:[1-9]\\d(?:0[48]|[2468][048]|[13579][26])|(?:[2468][048]|[13579][26])00)-02-29)T(?:[01]\\d|2[0-3]):[0-5]\\d:[0-5]\\d(?:\\.\\d{1,9})?$")) {
			bean.setValid(false);
			return bean;
		}

		return bean;
	}

	public List<DealBean> validate(List<DealBean> dealBeans) {
		return dealBeans.parallelStream().map(deal -> {
			return validate(deal);
		}).collect(Collectors.toList());

	}
}
