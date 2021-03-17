package clustered.data.warehouse.controllers;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import javax.servlet.ServletContext;

import org.springframework.stereotype.Controller;
import org.springframework.util.FileCopyUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import clustered.data.warehouse.model.helpers.ReportSummary;
import clustered.data.warehouse.services.DealImportInfoService;
import clustered.data.warehouse.services.DealImportService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Controller
@RequiredArgsConstructor
@Slf4j
public class DealImportController {

	private final DealImportService dealService;
	private final DealImportInfoService dealImportInfoService;
	private final String UPLOAD_DIR = "./uploads/";

	@GetMapping("/")
	public String displayFileImportPage() {
		return "index";
	}

	@PostMapping("/upload")
	public String processImportedFile(@RequestParam("file") MultipartFile multipartFile, RedirectAttributes attributes) throws IOException
			{
		
		if (multipartFile.isEmpty()) {
			attributes.addFlashAttribute("message", "Please select a csv file to upload.");
			return "redirect:/";
		}
	
		String filename = multipartFile.getOriginalFilename().trim().replaceAll("\\s+", "_");
		filename = StringUtils.cleanPath(filename);
		
		log.info("***FILENAME:" +filename);
		
		if (!filename.endsWith(".csv")) {
			attributes.addFlashAttribute("message", "Please select a csv file to upload.");
			return "redirect:/";
		}
		
		if (dealImportInfoService.isFileExist(filename)) {
			attributes.addFlashAttribute("message", String.format("Please select another file with a different name. This %s already exist.",filename));
			return "redirect:/";
		}
		
		String path = UPLOAD_DIR + filename;
		
		log.info("***PATH:" +path.toString());								

		ReportSummary summary = dealService.processCSV(path, filename,  multipartFile.getInputStream());
		log.info("***SUMMARY:" +summary);
		attributes.addFlashAttribute("message", summary);
		return  "redirect:/";
	}
	
	
}
