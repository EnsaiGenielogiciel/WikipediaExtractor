package wikipediaScraping;


import java.io.IOException;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.List;

import org.json.JSONArray;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;

import wikipediaScraping.WikipediaHTMLExtractor;





@RestController
public class ExtractorController {

	@RequestMapping(value = "/Link/{link}", method = RequestMethod.GET, produces ={"application/json"})
	@ResponseBody
	public String getTableByLink(
	  @PathVariable("link") String link) throws IOException {
		WikipediaHTMLExtractor scrap = new WikipediaHTMLExtractor("https://en.wikipedia.org/wiki/", link);
		scrap.htmlParser();
		ExtractorA ext= new ExtractorA(scrap);
		int total_table = ext.scraper(true);
		Dictionary<Integer, List<ArrayList<String>>> T= ext.getTableData();
		
	    
		Gson json =new Gson();
		String jsonString =json.toJson(T);
		System.out.println(jsonString);
		
	    return "the table of link : " + link + "  is " +  jsonString;
	    
	 
	}
}
