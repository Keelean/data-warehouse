package clustered.data.warehouse.services;

import static org.mockito.Mockito.doReturn;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import clustered.data.warehouse.entities.DealEntity;
import clustered.data.warehouse.entities.FileImportInfo;
import clustered.data.warehouse.entities.InvalidDeal;
import clustered.data.warehouse.entities.ValidDeal;
import clustered.data.warehouse.repositories.FileImportInfoRepository;

@SpringBootTest
public class FileImportInfoServiceTest {

	@Autowired
	private FileImportInfoService fileImportInfoService;

	@MockBean
	private FileImportInfoRepository fileImportInfoRepository;

	@Test
	@DisplayName("Test File Exist")
	void testFileExists() {

		FileImportInfo fileInfo = FileImportInfo.builder().filename("test002.csv").build();
		doReturn(Optional.of(fileInfo)).when(fileImportInfoRepository).findByFilename("test002.csv");

		boolean fileExist = fileImportInfoService.isFileExist("test002.csv");
		Assertions.assertTrue(fileExist);
	}

	@Test
	@DisplayName("Test File Does Not Exist")
	void testFileDoesExists() {
		FileImportInfo fileInfo = FileImportInfo.builder().filename("test002.csv").build();
		doReturn(Optional.of(fileInfo)).when(fileImportInfoRepository).findByFilename("test002.csv");

		boolean fileExist = fileImportInfoService.isFileExist("test003.csv");
		Assertions.assertFalse(fileExist);
	}

	@Test
	@DisplayName("Test Get File")
	void testGetFile() {
		FileImportInfo fileInfo = FileImportInfo.builder().filename("test002.csv").build();
		ValidDeal validDeal = new ValidDeal();
		
		validDeal.setAmount("1000.00");
		validDeal.setFileInfo(fileInfo);
		validDeal.setDealId("222");
		validDeal.setDealTimestamp("");
		validDeal.setFromCurrencyCode("EUR");
		validDeal.setToCurrencyCode("USD");
		Set<ValidDeal> setsOfvalidDeals = new HashSet<>();
		setsOfvalidDeals.add(validDeal);
		fileInfo.setValidDeals(setsOfvalidDeals);
		
		InvalidDeal invalidDeal = new InvalidDeal();
		validDeal.setAmount("2000.00");
		validDeal.setFileInfo(fileInfo);
		validDeal.setDealId("222");
		validDeal.setDealTimestamp("2019-03-04T23:09:06");
		validDeal.setFromCurrencyCode("USD");
		validDeal.setToCurrencyCode("EUR");
		Set<InvalidDeal> setsOfInvalidDeals = new HashSet<>();
		setsOfInvalidDeals.add(invalidDeal);
		fileInfo.setInvalidDeals(setsOfInvalidDeals);
		
		doReturn(Optional.of(fileInfo)).when(fileImportInfoRepository).findByFilename("test002.csv");
		
		String result = fileImportInfoService.getFile("test002.csv");
		Assertions.assertTrue(result.contains("Valid Deals: 1"));
		Assertions.assertTrue(result.contains("Filename: test002.csv"));
		Assertions.assertTrue(result.contains("Invalid Deals: 1"));
	}
	
	@Test
	@DisplayName("Test Get File Not Exist")
	void testGetFileNotExist() {
		
		String result = fileImportInfoService.getFile("test002.csv");
		Assertions.assertTrue(result.contains("test002.csv does not exist!"));
	}
}
