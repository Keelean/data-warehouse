package clustered.data.warehouse.services.impl;

import org.springframework.stereotype.Service;

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

}
