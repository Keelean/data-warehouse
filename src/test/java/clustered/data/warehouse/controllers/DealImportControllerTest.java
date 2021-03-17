package clustered.data.warehouse.controllers;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import clustered.data.warehouse.model.helpers.ReportSummary;
import clustered.data.warehouse.services.DealImportInfoService;
import clustered.data.warehouse.services.DealImportService;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = DealImportController.class)
public class DealImportControllerTest {

	  @Autowired
	  private MockMvc mockMvc;
	  
	  @MockBean
	  private DealImportService fileService;
	  
	  @MockBean	
	  private DealImportInfoService fileImportInfoService;
	  
	  @Test
	  @DisplayName("Test Index Page")
	  public void givenHomePageURI_whenMockMVC_thenReturnsIndexThymeleafViewName() throws Exception {
	      this.mockMvc.perform(get("/")).andDo(print())
	      	.andExpect(status().isOk())
	        .andExpect(view().name("index"))
	        .andExpect(content().string(containsString("Upload Deals")));
	  }
	  
	  @Test
	  @DisplayName("Test Selected A Non CSV File")
	  public void givenNonCSVFileWasSelected_whenMockMVC_thenReturnUploadPageWithErrorMessage() throws Exception {
		    MockMultipartFile file 
		      = new MockMultipartFile(
		        "file", 
		        "hello.txt", 
		        MediaType.MULTIPART_FORM_DATA_VALUE, 
		        "Hello, World!".getBytes()
		      );
			this.mockMvc.perform(multipart("/upload").file(file))
					.andExpect(status().is3xxRedirection()).andExpect(view().name("redirect:/"))
				    .andExpect(MockMvcResultMatchers.flash().attribute("message", "Please select a csv file to upload."));
		}
	  
	  
	  @Test
	  @DisplayName("Test No File Selected")
	  public void givenNoFileWasSelected_whenMockMVC_thenReturnUploadPageWithErrorMessage() throws Exception {
		    MockMultipartFile file 
		      = new MockMultipartFile(
		        "file", 
		        null, 
		        MediaType.MULTIPART_FORM_DATA_VALUE, 
		        "Hello, World!".getBytes()
		      );
			this.mockMvc.perform(multipart("/upload").file(file))
					.andExpect(status().is3xxRedirection()).andExpect(view().name("redirect:/"))
				    .andExpect(MockMvcResultMatchers.flash().attribute("message", "Please select a csv file to upload."));
		}
	  
	  
	  @Test
	  @DisplayName("Test A CSV File Uploaded")
	  public void givenACSVSelected_whenMockMVC_thenReturnUploadPageWithMessage() throws Exception {
		 Resource resource = new ClassPathResource("test.csv");
		 
		 String data = "809,EUR,USD,2019-03-04T23:09:06,1100\n"
		 		+ "810,EUR,USD,2019-03-04Z23:09:06,2000\n"
		 		+ "811,EUR,USD,2019-03-04T23:09:06,9000\n"
		 		+ "812,EUR,USD,2019-03-04T23:09:06,100.90\n"
		 		+ "813,EUR,USD,2019-03-04T23:09:06,1000.25";
		  
		  assertNotNull(resource);
		  
		    MockMultipartFile file 
		      = new MockMultipartFile(
		        "file", 
		        "test.csv", 
		        MediaType.MULTIPART_FORM_DATA_VALUE, 
		        data.getBytes()
		      );
		    
		    MvcResult mvcResult = this.mockMvc.perform(multipart("/upload").file(file))
					.andExpect(status().is3xxRedirection()).andExpect(view().name("redirect:/"))
					.andReturn();
			ArgumentCaptor<String> filePathCaptor = ArgumentCaptor.forClass(String.class);
			ArgumentCaptor<InputStream> inputStreamCaptor = ArgumentCaptor.forClass(InputStream.class);
			ArgumentCaptor<String> filename = ArgumentCaptor.forClass(String.class);
			Assertions.assertNotNull(mvcResult.getResponse());
			verify(fileService, times(1)).processCSV(filePathCaptor.capture(), filename.capture(), inputStreamCaptor.capture());
				    
		}
}
