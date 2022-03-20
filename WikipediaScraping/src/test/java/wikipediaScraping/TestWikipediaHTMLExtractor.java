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

import wikipediaScraping.WikipediaHTMLExtractor;
import wikipediaScraping.ExtractorA;





public class TestWikipediaHTMLExtractor {
    
	

		
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
		ExtractorA a = new ExtractorA(scrap);
		int number_of_table= a.scraper(true);
			
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
	
		
	}




//https://mkyong.com/maven/how-to-run-unit-test-with-maven/