package clustered.data.warehouse.controllers;

import java.io.File;
import java.io.IOException;
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
import clustered.data.warehouse.services.FileImportInfoService;
import clustered.data.warehouse.services.FileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Controller
@RequiredArgsConstructor
@Slf4j
public class FileImportController {

	private final ServletContext context;
	private final FileService fileService;
	private final FileImportInfoService fileImportInfoService;
	
	 private final String UPLOAD_DIR = "./uploads/";

	@GetMapping("/")
	public String displayFileImportPage() {
		return "index";
	}

	@PostMapping("/upload")
	public String processImportedFile(@RequestParam("file") MultipartFile file, RedirectAttributes attributes)
			throws IOException {
		
		if (file.isEmpty()) {
			attributes.addFlashAttribute("message", "Please select a csv file to upload.");
			return "redirect:/";
		}

		String filename = file.getOriginalFilename().trim().replaceAll("\\s+", "_");
		filename = StringUtils.cleanPath(filename);
		
		if (!filename.endsWith(".csv")) {
			attributes.addFlashAttribute("message", "Please select a csv file to upload.");
			return "redirect:/";
		}
		
		log.info("******FILENAME: [{}]", filename);

		if (fileImportInfoService.isFileExist(filename)) {
			attributes.addFlashAttribute("message", String.format("Please select another file with a different name. This %s already exist.",filename));
			return "redirect:/";
		}
		
		Path path = Paths.get(UPLOAD_DIR + filename);
		createDirectory(path.toString());
		log.info("******FILE PATH: [{}]", path.toString());
        Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);

		//FileCopyUtils.copy(file.getBytes(), new File(uploadPath + file.getOriginalFilename()));
		ReportSummary summary = fileService.processCSV(path.toString(), filename);
		attributes.addFlashAttribute("message", summary);
		return  "redirect:/";
	}
	
	protected static void createDirectory(String filePath) {
		File targetDir = new File(filePath);
		if (!targetDir.exists()) {
			targetDir.mkdirs();
		}
	}
}
