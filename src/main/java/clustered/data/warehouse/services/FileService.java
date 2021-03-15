package clustered.data.warehouse.services;

import java.io.InputStream;
import java.nio.file.Path;

import clustered.data.warehouse.model.helpers.ReportSummary;

public interface FileService {
	
	ReportSummary processCSV(Path importPath, String filename, InputStream inpuStream);

}
