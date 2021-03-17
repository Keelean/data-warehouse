package clustered.data.warehouse.controllers;

import java.util.Objects;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import clustered.data.warehouse.model.helpers.SearchFile;
import clustered.data.warehouse.services.DealImportInfoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Controller
@RequiredArgsConstructor
@Slf4j
public class SearchController {
	
	private final DealImportInfoService dealImportInfoService;

	@GetMapping("/search")
	public String displaySearchPage(Model model) {
		SearchFile search = new SearchFile();
		model.addAttribute("search", search);
		return "search-page";
	}
	
	@PostMapping("/search")
	public String searchResult(@ModelAttribute("search") SearchFile search, RedirectAttributes attributes) {
		if(Objects.isNull(search.getSearch()) || search.getSearch().isEmpty()) {
			attributes.addFlashAttribute("message", "Please enter a file name to search.");
			return "redirect:/search";
		}
		
		String filename = search.getSearch().trim().replaceAll("\\s+", "_");
		
		if(!filename.endsWith(".csv")) {
			filename = filename + ".csv";
		}
		
		String result = dealImportInfoService.getFile(filename);
		attributes.addFlashAttribute("message", result);
		return "redirect:/search";
	}
}
