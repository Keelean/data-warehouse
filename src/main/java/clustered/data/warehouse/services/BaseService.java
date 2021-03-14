package clustered.data.warehouse.services;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;

public class BaseService {

	@Autowired
	protected ModelMapper mapper;

	protected <T, S> List<T> convertCollections(Collection<S> entities, Class<T> targetClass) {

		List<T> convertedEntities = new ArrayList<>();

		if (Objects.isNull(entities)) {
			return convertedEntities;
		}

		for (S entity : entities) {
			convertedEntities.add(mapper.map(entity, targetClass));
		}

		return convertedEntities;
	}
}
