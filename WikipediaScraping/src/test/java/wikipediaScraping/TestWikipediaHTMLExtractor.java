package wikipediaScraping;

import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;

//import wikipediaScraping.WikipediaHTMLExtractor;



@SpringBootTest
public class TestWikipediaHTMLExtractor {

	@Test
	public void testIsNumeric() {
		
		WikipediaHTMLExtractor testa = new WikipediaHTMLExtractor("lf","ldf");
		
		assertEquals(true, testa.isNumeric("4"));
	}
	
	@Test
	public void testHtmlParser_null() throws IOException {
		
		WikipediaHTMLExtractor wikiExtractor = new WikipediaHTMLExtractor("https://en.wikipedia.org/wiki/","Comparison_of_World_War_I_tank");
		wikiExtractor.htmlParser();
		assertNull(wikiExtractor.getTables());
	}
	
	@Test
	public void testHtmlParser_notnull() throws IOException {
		
		WikipediaHTMLExtractor wikiExtractor = new WikipediaHTMLExtractor("https://en.wikipedia.org/wiki/","Comparison_of_DOS_operating_systems");
		wikiExtractor.htmlParser();
		assertNotNull(wikiExtractor.getTables());
	}
	
	
	@Test
	public void testScraper_number_table() throws IOException{
		//Test number of table in wikipedia page
		WikipediaHTMLExtractor scrap = new WikipediaHTMLExtractor("https://en.wikipedia.org/wiki/","Comparison_of_programming_languages_(syntax)");
		scrap.htmlParser();
		int number_of_table = scrap.scraper(true);	
		assertEquals(4, number_of_table);
	}
	
	@Test
	public void testCSVRowsColumnsNumber() throws IOException {
		// Test number of rows and columns of one table
		Path resourceDirectory = Paths.get("src","main","resources","csvdata");
		String absolutePath = resourceDirectory.toFile().getAbsolutePath();
		String filepath = absolutePath + "\\Comparison_of_programming_languages_(syntax)_table_2.csv";
			
		Reader in  = new FileReader(filepath);
		Iterable<CSVRecord> records = CSVFormat.DEFAULT.parse(in);
	
		int row = 0;
		int column = 0;
		for (CSVRecord record : records) {
			row++;
			if(row>0){
				column = record.size();
			}
		}
		
		assertTrue(row==18 && column==2);
	}	
	
	
	@Test
	public void testColumnNameOfTable() throws IOException{
		
		// Test number of rows and columns of one table
		Path resourceDirectory = Paths.get("src","main","resources","csvdata");
		String absolutePath = resourceDirectory.toFile().getAbsolutePath();
		String filepath = absolutePath + "\\Comparison_of_programming_languages_(syntax)_table_2.csv";
			
		Reader in  = new FileReader(filepath);
		Iterable<CSVRecord> records = CSVFormat.DEFAULT.parse(in);
		
		ArrayList<String> column_name = new ArrayList<String>();
		for(CSVRecord record : records) {
			column_name.add(record.get(0));
			column_name.add(record.get(1));
			break;
		}
		
		assertTrue(column_name.get(0).equals("Symbol") && column_name.get(1).equals("Languages"));
	}
	
	@Test
	public void testScraper_build_table_one() throws IOException{
		
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
			scrap.htmlParser();
			int total_table = scrap.scraper(true);
		}
		
	}
	
	@Test
	public void testScraper_build_table_two() throws IOException{
		
		List<String> lien = new ArrayList<String>();
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
		lien.add("Comparison_of_operating_system_kernels");
		lien.add("Comparison_of_Fukushima_and_Chernobyl_nuclear_accidents");
		
		
		for(int i=0 ; i<lien.size(); i++) {
			WikipediaHTMLExtractor scrap = new WikipediaHTMLExtractor("https://en.wikipedia.org/wiki/",lien.get(i));
			scrap.htmlParser();
			int total_table = scrap.scraper(true);
		}
		
	}

}


//https://mkyong.com/maven/how-to-run-unit-test-with-maven///https://mkyong.com/maven/how-to-run-unit-test-with-maven/