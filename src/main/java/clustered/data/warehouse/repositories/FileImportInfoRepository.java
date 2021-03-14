package clustered.data.warehouse.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import clustered.data.warehouse.entities.FileImportInfo;

@Repository
public interface FileImportInfoRepository extends JpaRepository<FileImportInfo, Long> {
	
	Optional<FileImportInfo> findByFilename(String filename);
}
