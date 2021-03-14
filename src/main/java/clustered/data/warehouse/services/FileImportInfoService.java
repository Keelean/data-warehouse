package clustered.data.warehouse.services;

public interface FileImportInfoService {

	boolean isFileExist(String filename);

	String getFile(String filename);
}
