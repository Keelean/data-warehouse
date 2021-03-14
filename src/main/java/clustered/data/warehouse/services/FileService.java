package clustered.data.warehouse.services;

import clustered.data.warehouse.model.helpers.ReportSummary;

public interface FileService {
	
	ReportSummary processCSV(String importPath, String filename);

}
