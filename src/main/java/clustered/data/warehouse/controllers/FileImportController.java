package clustered.data.warehouse.controllers;

import java.io.File;
import java.io.IOException;

import javax.servlet.ServletContext;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.FileCopyUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.multipart.MultipartFile;

import clustered.data.warehouse.services.FileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Controller
@RequiredArgsConstructor
@Slf4j
public class FileImportController {
	
	private final ServletContext context;
	private final FileService fileService;

	public void displayFileImportPage() {
		
	}
	
	public void processImportedFile(@Validated MultipartFile file, BindingResult result, ModelMap model) throws IOException {
		if(file.isEmpty()) {
			return;
		}
		
		String filename = file.getOriginalFilename();
		if(!filename.endsWith(".csv")) {
			return;
		}
		
        String uploadPath = context.getRealPath("") + File.separator + "temp" + File.separator;
        //Now do something with file...
        String filePath = uploadPath+file.getOriginalFilename();
        FileCopyUtils.copy(file.getBytes(), new File(uploadPath+file.getOriginalFilename()));
	}
}
