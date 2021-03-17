package clustered.data.warehouse.services;

public interface DealImportInfoService {

	boolean isFileExist(String filename);

	String getFile(String filename);
}
