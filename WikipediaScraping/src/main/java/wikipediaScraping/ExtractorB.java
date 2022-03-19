package wikipediaScraping;

import java.util.ArrayList;
import java.util.Dictionary;
import java.util.List;

import org.apache.log4j.Logger;
import org.jsoup.select.Elements;

public class ExtractorB {

	
	Logger logger = Logger.getLogger(MainExtractor.class);
	// Build and save the csv file in the directory /src/main/ressources
	
	private WikipediaHTMLExtractor page;

	
	public ExtractorB(WikipediaHTMLExtractor page) {
		
		assert page.getTables().size() == 0;
	
	}
	
	public void  affihcer() { 
	logger.error("No table here ") ;
}
}
