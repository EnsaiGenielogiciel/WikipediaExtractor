package extractorController;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.gson.Gson;

import wikipediaScraping.WikipediaHTMLExtractor;

@Controller

public class WikipediaController { 
	


	@RequestMapping(value = "/{link}", method = RequestMethod.GET)
	@ResponseBody
	public String getTableByLink(
		  @PathVariable("link") String link) throws IOException {
			WikipediaHTMLExtractor scrap = new WikipediaHTMLExtractor("https://en.wikipedia.org/wiki/", link);
			scrap.htmlParser();
			int total_table = scrap.scraper(true);
			List<ArrayList<String>> T= scrap.getTableData();
			 
			Gson json =new Gson();
			String jsonString =json.toJson(T);
			System.out.println(jsonString);
			
		    return "the table of link : " + link + "  is " +  jsonString;
		
		}
	}

	    

	  
	    
	
	
	
	
	
	

