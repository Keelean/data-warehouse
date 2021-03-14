package clustered.data.warehouse.services.impl;

import java.util.Optional;

import org.springframework.stereotype.Service;

import clustered.data.warehouse.entities.FileImportInfo;
import clustered.data.warehouse.repositories.FileImportInfoRepository;
import clustered.data.warehouse.services.FileImportInfoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class FileImportInfoServiceImpl implements FileImportInfoService {

	private final FileImportInfoRepository fileImportRepository;

	@Override
	public boolean isFileExist(String filename) {
		return fileImportRepository.findByFilename(filename).isPresent();
	}

	public String getFile(String filename) {

		Optional<FileImportInfo> optionalFileInfo = fileImportRepository.findByFilename(filename);

		if (optionalFileInfo.isPresent()) {
			FileImportInfo fileInfo = optionalFileInfo.get();
			fileInfo.getInvalidDeals().size();
			fileInfo.getValidDeals().size();
			return String.format("Filename: %s, Valid Deals: %d, Invalid Deals: %d", fileInfo.getFilename(),
					fileInfo.getValidDeals().size(), fileInfo.getInvalidDeals().size());
		}

		return String.format("%s does not exist!", filename);
	}

}
