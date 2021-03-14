package clustered.data.warehouse.services;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.univocity.parsers.common.processor.BeanListProcessor;
import com.univocity.parsers.csv.CsvParser;
import com.univocity.parsers.csv.CsvParserSettings;

import clustered.data.warehouse.entities.InvalidDeal;
import clustered.data.warehouse.entities.ValidDeal;
import clustered.data.warehouse.model.helpers.DealBean;
import clustered.data.warehouse.repositories.InvalidDealRepository;
import clustered.data.warehouse.repositories.ValidDealRepository;
import clustered.data.warehouse.validator.DealValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class FileServiceImpl extends BaseService implements FileService {

	private final ValidDealRepository validDealRepository;
	private final InvalidDealRepository invalidDealRepository;
	private final DealValidator validator;

	@Override
	public void processCSV(String importPath, String filename) {

		try {
			List<DealBean> deals = convertCSVToList(importPath);

			// validate Beans
			List<DealBean> validatedBeans = validator.validate(deals);
			
			//Group Valid and Invalid Deals
			Map<Boolean, List<DealBean>> groupedValidatedBeans = validatedBeans.stream()
					  .collect(Collectors.groupingBy(DealBean::isValid));
			
			List<DealBean> validDealBeans = groupedValidatedBeans.get(Boolean.TRUE);
			List<ValidDeal> validDeals = convertCollections(validDealBeans, ValidDeal.class);
			List<DealBean> invalidDealBeanss = groupedValidatedBeans.get(Boolean.FALSE);
			List<InvalidDeal> invalidDeals = convertCollections(invalidDealBeanss, InvalidDeal.class);
			
			if (!validDeals.isEmpty()) {
				validDealRepository.saveAll(validDeals);
			}
			if (!invalidDeals.isEmpty()) {
				invalidDealRepository.saveAll(invalidDeals);
			}
			

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	private static Reader getReader(String relativePath) throws FileNotFoundException {
		try {
			return new InputStreamReader(new FileInputStream(relativePath), "UTF-8");
		} catch (UnsupportedEncodingException e) {
			throw new IllegalStateException("Unable to read input", e);
		}
	}

	private List<DealBean> convertCSVToList(final String filePath) throws FileNotFoundException {
		BeanListProcessor<DealBean> rowProcessor = new BeanListProcessor<DealBean>(DealBean.class);

		CsvParserSettings parserSettings = new CsvParserSettings();
		parserSettings.getFormat().setLineSeparator("\n");
		parserSettings.getFormat().setQuoteEscape('\\');
		parserSettings.setProcessor(rowProcessor);
		parserSettings.setHeaderExtractionEnabled(true);
		parserSettings.setLineSeparatorDetectionEnabled(true);

		CsvParser parser = new CsvParser(parserSettings);
		parser.parse(getReader(filePath));

		List<DealBean> beans = rowProcessor.getBeans();
		return beans;
	}

	

}
