package clustered.data.warehouse.validator;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import clustered.data.warehouse.model.helpers.DealBean;


@Component
public class DealValidator {

	public DealBean validate(DealBean bean) {
		if (Objects.isNull(bean.getAmount()) || Objects.isNull(bean.getFromCurrencyCode())
				|| Objects.isNull(bean.getToCurrencyCode()) || Objects.isNull(bean.getTimestamp())) {
			bean.setValid(false);
			return bean;
		}

		if (bean.getFromCurrencyCode().isEmpty() || bean.getToCurrencyCode().isEmpty() || bean.getTimestamp().isEmpty()
				|| bean.getAmount().isEmpty()) {
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
