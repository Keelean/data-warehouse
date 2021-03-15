package clustered.data.warehouse.controllers;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import clustered.data.warehouse.model.helpers.SearchFile;
import clustered.data.warehouse.services.FileImportInfoService;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = SearchController.class)
public class SearchControllerTest {
	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private FileImportInfoService fileImportInfoService;

	@Test
	@DisplayName("Search Page")
	public void givenSearchPageURI_whenMockMVC_thenReturnsSearchPageViewName() throws Exception {
		this.mockMvc.perform(get("/search")).andDo(print()).andExpect(status().isOk())
				.andExpect(view().name("search-page"))
				.andExpect(content().string(containsString("Enter filename to search")));
	}

	@Test
	@DisplayName("No Search Parameter")
	public void givenNoSearch_whenMockMVC_thenReturnsSearchPageViewName() throws Exception {
		this.mockMvc.perform(post("/search").requestAttr("search", SearchFile.builder().search("").build()))
				.andExpect(status().is3xxRedirection()).andExpect(view().name("redirect:/search"))
			    .andExpect(MockMvcResultMatchers.flash().attribute("message", "Please enter a file name to search."));
	}
	
	@Test
	@DisplayName("Search Parameter")
	public void givenSearcnParam_whenMockMVC_thenReturnsSearchPageViewName() throws Exception {
		this.mockMvc.perform(post("/search").requestAttr("search", SearchFile.builder().search("filename").build()))
				.andExpect(status().is3xxRedirection()).andExpect(view().name("redirect:/search"))
			    .andExpect(MockMvcResultMatchers.flash().attribute("message", notNullValue()));
	}
}
