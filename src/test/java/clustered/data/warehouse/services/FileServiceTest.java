package clustered.data.warehouse.services;

import static org.mockito.Mockito.doReturn;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import clustered.data.warehouse.entities.DealEntity;
import clustered.data.warehouse.entities.FileImportInfo;
import clustered.data.warehouse.entities.InvalidDeal;
import clustered.data.warehouse.entities.ValidDeal;
import clustered.data.warehouse.model.helpers.DealBean;
import clustered.data.warehouse.model.helpers.ReportSummary;
import clustered.data.warehouse.repositories.FileImportInfoRepository;
import clustered.data.warehouse.repositories.InvalidDealRepository;
import clustered.data.warehouse.repositories.ValidDealRepository;
import clustered.data.warehouse.services.impl.CSVParser;
import clustered.data.warehouse.validator.DealValidator;
import lombok.extern.slf4j.Slf4j;

@SpringBootTest
@Slf4j
public class FileServiceTest {

	@Autowired
	private FileService fileService;

	@MockBean
	private ValidDealRepository validDealRepository;
	@MockBean
	private InvalidDealRepository invalidDealRepository;
	@MockBean
	private DealValidator validator;
	@MockBean
	private FileImportInfoRepository fileImportRepository;
	@MockBean
	private CurrencyDealService currencyDealService;
	@MockBean
	private CSVParser csvParser;

	@Test
	void doProcessCSVService() {

		DealBean dealOne = DealBean.builder().dealId("1").stringAmount("1000.25").fromCurrencyCode("USD")
				.toCurrencyCode("EUR").stringTimestamp("2019-03-04T23:09:06,1005").valid(true).build();
		DealBean dealTwo = DealBean.builder().dealId("2").stringAmount("1005").fromCurrencyCode("USD")
				.toCurrencyCode("EUR").stringTimestamp("2019-03-04T23:09:06,1005").build();
		DealBean dealThree = DealBean.builder().dealId("3").stringAmount("1100").valid(true).fromCurrencyCode("USD")
				.toCurrencyCode("EUR").stringTimestamp("2019-03-04T23:09:06,1005").build();
		DealBean dealFour = DealBean.builder().dealId("4").stringAmount("2000").fromCurrencyCode("USD")
				.toCurrencyCode("EUR").stringTimestamp("2019-03-04T23:09:06,1005").build();
		DealBean dealFive = DealBean.builder().dealId("5").stringAmount("9000").fromCurrencyCode("USD")
				.toCurrencyCode("EUR").stringTimestamp("2019-03-04T23:09:06,1005").valid(true).build();

		List<DealBean> dealBeans = Arrays.asList(dealOne, dealTwo, dealThree, dealFour, dealFive);

		Resource resource = new ClassPathResource("test.csv");
		FileImportInfo fileImportInfo = FileImportInfo.builder().filename("test.csv").build();

		try {
			Path path = Paths.get("./uploads/test.csv");
			doReturn(dealBeans).when(csvParser).convertCSVToList(path.toString());
			doReturn(dealBeans).when(validator).validate(dealBeans);
			doReturn(fileImportInfo).when(fileImportRepository).save(fileImportInfo);
			

			ReportSummary summary = fileService.processCSV(path, "test,csv", resource.getInputStream());
			Assertions.assertEquals(2, summary.getNoOfInvalidDeals());
			Assertions.assertEquals(3, summary.getNoOfDeals());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
