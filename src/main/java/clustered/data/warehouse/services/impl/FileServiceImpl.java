package clustered.data.warehouse.services.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.univocity.parsers.common.processor.BeanListProcessor;
import com.univocity.parsers.csv.CsvParser;
import com.univocity.parsers.csv.CsvParserSettings;

import clustered.data.warehouse.entities.FileImportInfo;
import clustered.data.warehouse.entities.InvalidDeal;
import clustered.data.warehouse.entities.ValidDeal;
import clustered.data.warehouse.model.helpers.DealBean;
import clustered.data.warehouse.model.helpers.ReportSummary;
import clustered.data.warehouse.repositories.FileImportInfoRepository;
import clustered.data.warehouse.repositories.InvalidDealRepository;
import clustered.data.warehouse.repositories.ValidDealRepository;
import clustered.data.warehouse.services.BaseService;
import clustered.data.warehouse.services.CurrencyDealService;
import clustered.data.warehouse.services.FileService;
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
	private final FileImportInfoRepository fileImportRepository;
	private final CurrencyDealService currencyDealService;

	@Override
	public ReportSummary processCSV(Path importPath, String filename, InputStream inpuStream) {

		try {
			createDirectory(importPath.toString());
	        Files.copy(inpuStream, importPath, StandardCopyOption.REPLACE_EXISTING);
			
			Long startTime = System.currentTimeMillis();
			List<DealBean> deals = convertCSVToList(importPath.toString());
			Long endTime = System.currentTimeMillis();
			Long duration = ((endTime - startTime));
			log.info("Processing Time after CSV Loading: [{}]", duration);

			// validate Beans
			List<DealBean> validatedBeans = validator.validate(deals);
			
			endTime = System.currentTimeMillis();
			duration = ((endTime - startTime));
			log.info("Processing Time after validation: [{}]", duration);

			// Group Valid and Invalid Deals
			Map<Boolean, List<DealBean>> groupedValidatedBeans = validatedBeans.parallelStream()
					.collect(Collectors.groupingBy(DealBean::isValid));
			
			endTime = System.currentTimeMillis();
			duration = ((endTime - startTime));
			log.info("Processing Time after grouping: [{}]", duration);
			
			// Extract valid deals
			List<DealBean> validDealBeans = groupedValidatedBeans.get(Boolean.TRUE);
			// Convert to Valid Deal Entity
			List<ValidDeal> convertedValidDeals = convertCollections(validDealBeans, ValidDeal.class);
			// Extract valid deals
			List<DealBean> invalidDealBeans = groupedValidatedBeans.get(Boolean.FALSE);
			// Convert to Valid Deal Entity
			List<InvalidDeal> convertedInvalidDeals = convertCollections(invalidDealBeans, InvalidDeal.class);
			FileImportInfo fileInfo = fileImportRepository.save(FileImportInfo.builder().filename(filename).build());
			
			if (!convertedValidDeals.isEmpty()) {
				List<ValidDeal> validDealsList = convertedValidDeals.parallelStream().map(deal -> {
					deal.setFileInfo(fileInfo);
					return deal;
				}).collect(Collectors.toList());
				
				endTime = System.currentTimeMillis();
				duration = ((endTime - startTime));
				log.info("Processing Time after setting file info valid deals: [{}]", duration);
				
				
				validDealRepository.saveAll(validDealsList);
				Map<String, Long> countDeals = validDealBeans.parallelStream()
						.collect(Collectors.groupingBy(DealBean::getFromCurrencyCode, Collectors.counting()));
				countDeals.forEach((currencyCode, countOfDeals) -> currencyDealService.incrementDeals(currencyCode, countOfDeals));
				
				endTime = System.currentTimeMillis();
				duration = ((endTime - startTime));
				log.info("Processing Time after saving valid deals: [{}]", duration);
			}

			if (!convertedInvalidDeals.isEmpty()) {
				List<InvalidDeal> invalidDealsList = convertedInvalidDeals.parallelStream().map(deal -> {
					deal.setFileInfo(fileInfo);
					return deal;
				}).collect(Collectors.toList());
				
				endTime = System.currentTimeMillis();
				duration = ((endTime - startTime));
				log.info("Processing Time after setting file info invalid deals: [{}]", duration);
				
				invalidDealRepository.saveAll(invalidDealsList);
				
				endTime = System.currentTimeMillis();
				duration = ((endTime - startTime));
				log.info("Processing Time after saving invalid deals: [{}]", duration);
			}

			 endTime = System.currentTimeMillis();
			 duration = ((endTime - startTime)/1000);
			log.info("Processing Time: [{}]", duration);
			
			return ReportSummary.builder().processDuration(duration).noOfInvalidDeals(convertedInvalidDeals.size()).noOfDeals(convertedValidDeals.size()).build();

		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return ReportSummary.builder().build();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return ReportSummary.builder().processDuration(0l).noOfInvalidDeals(0).noOfDeals(0).build();
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
	
	protected static void createDirectory(String filePath) {
		File targetDir = new File(filePath);
		if (!targetDir.exists()) {
			targetDir.mkdirs();
		}
	}

}
