package wikipediaScraping;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Properties;

//import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication


public class MainExtractor {
	
	private static final String LOG_FILE = "log4j.properties";
	
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		
		//Logger logger = Logger.getLogger(MainExtractor.class);
		Properties properties = new Properties();
		properties.load(new FileInputStream(LOG_FILE));
		PropertyConfigurator.configure(properties);
		
		
		/*List<String> lien = new ArrayList<String>();
		lien.add("Comparison_of_DOS_operating_systems");
		lien.add("Comparison_of_programming_languages_(syntax)");
		lien.add("Comparison_of_domestic_robots");
		lien.add("Comparison_of_HP_graphing_calculators");  //with image in cell
		lien.add("Comparison_of_World_War_I_tank");     //unavailable page*/
		
		/*List<String> lien = new ArrayList<String>();
		lien.add("Comparison_of_DOS_operating_systems");
		lien.add("Comparison_of_programming_languages_(syntax)");
		lien.add("Comparison_of_domestic_robots");
		lien.add("Comparison_of_HP_graphing_calculators");  
		lien.add("Comparison_of_World_War_I_tank"); 
		lien.add("Comparison_of_HTML_editors");
		lien.add("Comparison_of_layout_engines_(Document_Object_Model)");
		lien.add("Comparison_of_mail_servers");
		lien.add("Comparison_of_Internet_Relay_Chat_clients");
		lien.add("Comparison_of_Sony_Vaio_laptops");
		lien.add("Comparison_of_alcopops");
		lien.add("Comparison_of_geographic_information_systems_software");
		lien.add("Comparison_of_instant_messaging_clients");
		lien.add("Comparison_of_raster_graphics_editors");
		lien.add("Comparison_of_Microsoft_Windows_versions");
		lien.add("Comparison_of_audio_player_software");
		lien.add("Comparison_of_portable_media_players");
		lien.add("Comparison_of_layout_engines_(HTML)");
		lien.add("Comparison_of_open-source_operating_systems");
		lien.add("Comparison_of_programming_languages_(basic_instructions)");
		lien.add("List_of_AMD_graphics_processing_units");
		lien.add("Comparison_of_Android_devices");
		lien.add("List_of_Intel_graphics_processing_units");
		lien.add("Comparison_of_Internet_Relay_Chat_services");
		lien.add("Comparison_of_Nvidia_chipsets");
		lien.add("Comparison_of_BitTorrent_clients");
		lien.add("List_of_Nvidia_graphics_processing_units");
		lien.add("Comparison_of_TLS_implementations");
		lien.add("Comparison_of_antivirus_software");
		lien.add("Comparison_of_email_clients");
		lien.add("Comparison_of_integrated_development_environments");
		lien.add("Comparison_of_layout_engines_(XHTML_1.1)");
		lien.add("Comparison_of_operating_system_kernels");*/
		
		
		
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
	
			WikipediaHTMLExtractor scrap = new WikipediaHTMLExtractor("https://en.wikipedia.org/wiki/", wiki_url);
			System.out.println(wiki_url);
			scrap.htmlParser();
			int total_table = scrap.scraper(true);
			System.out.println(scrap.getTableData());
			break;
		}

		SpringApplication.run(MainExtractor.class, args);
	}
}