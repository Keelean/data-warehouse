package clustered.data.warehouse.services.impl;

import java.util.Optional;

import org.springframework.stereotype.Service;

import clustered.data.warehouse.entities.FileImportInfo;
import clustered.data.warehouse.repositories.DealImportInfoRepository;
import clustered.data.warehouse.services.DealImportInfoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class DealImportInfoServiceImpl implements DealImportInfoService {

	private final DealImportInfoRepository dealImportRepository;

	@Override
	public boolean isFileExist(String filename) {
		return dealImportRepository.findByFilename(filename).isPresent();
	}

	public String getFile(String filename) {

		Optional<FileImportInfo> optionalFileInfo = dealImportRepository.findByFilename(filename);

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
