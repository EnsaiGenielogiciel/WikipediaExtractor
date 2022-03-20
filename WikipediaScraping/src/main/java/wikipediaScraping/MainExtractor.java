package wikipediaScraping;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Properties;

import org.apache.log4j.Logger;
//import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
 
@SpringBootApplication


public class MainExtractor {
	
	private static final String LOG_FILE = "log4j.properties";
	
	public static void main(String[] args) throws IOException {
		SpringApplication.run(MainExtractor.class, args);
		// TODO Auto-generated method stub
		
		Logger logger = Logger.getLogger(MainExtractor.class);
		Properties properties = new Properties();
		properties.load(new FileInputStream(LOG_FILE));
		PropertyConfigurator.configure(properties);
		
		
		
		
		Path resourceDirectory = Paths.get("src","test","resources");
		String absolutePath = resourceDirectory.toFile().getAbsolutePath();
		String filepath = absolutePath + "\\wiki_urls.txt";

		BufferedReader bufReader = new BufferedReader(new FileReader(filepath));
		ArrayList<String> listOfUrls = new ArrayList<>(); 
		String line = bufReader.readLine();
		while (line != null) { 
			listOfUrls.add(line); 
			line = bufReader.readLine(); 
		} 
		bufReader.close();

		
		for(String wiki_url : listOfUrls) {
	
			WikipediaHTMLExtractor scrap = new WikipediaHTMLExtractor( "https://en.wikipedia.org/wiki/",wiki_url);
			System.out.println(wiki_url);
			scrap.htmlParser();
			ExtractorA ext= new ExtractorA(scrap);
			int total_table = ext.scraper(true);

			System.out.println(ext.getTableData());
		
		}

		
	}
}
