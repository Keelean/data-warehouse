package clustered.data.warehouse.services;

import java.io.InputStream;

import clustered.data.warehouse.model.helpers.ReportSummary;

public interface DealImportService {
	
	ReportSummary processCSV(String path, String filename, InputStream file);

}
