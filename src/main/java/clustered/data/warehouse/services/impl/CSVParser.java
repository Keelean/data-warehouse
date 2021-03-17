package clustered.data.warehouse.services.impl;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.univocity.parsers.common.processor.BeanListProcessor;
import com.univocity.parsers.csv.CsvParser;
import com.univocity.parsers.csv.CsvParserSettings;

import clustered.data.warehouse.model.helpers.DealBean;
import clustered.data.warehouse.repositories.DealImportInfoRepository;
import clustered.data.warehouse.repositories.InvalidDealRepository;
import clustered.data.warehouse.repositories.ValidDealRepository;
import clustered.data.warehouse.services.CurrencyDealService;
import clustered.data.warehouse.validator.DealValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class CSVParser {

	public List<DealBean> convertCSVToList(final String filePath) throws FileNotFoundException {
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
	
	private static Reader getReader(String relativePath) throws FileNotFoundException {
		try {
			return new InputStreamReader(new FileInputStream(relativePath), "UTF-8");
		} catch (UnsupportedEncodingException e) {
			throw new IllegalStateException("Unable to read input", e);
		}
	}
}
