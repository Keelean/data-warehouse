package clustered.data.warehouse.controllers;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import clustered.data.warehouse.services.FileImportInfoService;
import clustered.data.warehouse.services.FileService;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = FileImportController.class)
public class FileImportControllerTest {

	  @Autowired
	  private MockMvc mockMvc;
	  
	  @MockBean
	  private FileService fileService;
	  
	  @MockBean	
	  private FileImportInfoService fileImportInfoService;
	  
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
	  
	  @Disabled
	  @Test
	  @DisplayName("Test A CSV File Uploaded")
	  public void givenACSVSelected_whenMockMVC_thenReturnUploadPageWithMessage() throws Exception {
		 Resource resource = new ClassPathResource("test.csv");
		  
		  
		  assertNotNull(resource);
		  
		    MockMultipartFile file 
		      = new MockMultipartFile(
		        "file", 
		        resource.getFilename(), 
		        MediaType.MULTIPART_FORM_DATA_VALUE, 
		        resource.getInputStream()
		      );
			this.mockMvc.perform(multipart("/upload").file(file))
					.andExpect(status().is3xxRedirection()).andExpect(view().name("redirect:/"))
				    .andExpect(MockMvcResultMatchers.flash().attribute("message", notNullValue()));
		}
}
