package wikipediaScraping;
import java.io.IOException;

import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.boot.autoconfigure.domain.EntityScan;





@EntityScan
public class WikipediaHTMLExtractor  {

	
	Logger logger = Logger.getLogger(MainExtractor.class);
	
	private String root_url="https://en.wikipedia.org/wiki/" ; 
	private String link;
	private Elements tables;
	
	
	
	
	
	public WikipediaHTMLExtractor(String root_url, String page_name) {
		
		this.root_url = root_url;
		this.link = page_name;
		this.tables = null;
		
	}
	

	
	
	// Getters and setters	
	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}

	public Elements getTables() {
		return tables;
	}

	public void setTables(Elements tables) {
		this.tables = tables;
	}
	
	
   
 
    //Method to requests the webpage, parse it and select <table> which attributes class contains 
    //"wikitable" element

    public void htmlParser() throws IOException{
    	
    	try {
    		String url = this.root_url + this.link;
    		
    		// request the web page and get the html source code
    		Document response = Jsoup.connect(url).get();
    		
    		// select table which attribute 'class' contain "wikitable"
    		Elements tables = response.select("table[class *= wikitable]"); 
    		
    		// update the attributes tables of the class
    		if(tables.size()>0) {
        		this.tables = tables;
        		logger.info("Successful parsing of the html source code.");
    		}else {
    			logger.warn("Sorry, but there is any table inside the webpage !!!");
    		}

    	}catch(Exception ex){
    		logger.error("The web page is probably unavailable !!!");
    	}
    }
    
    

	

	
}
