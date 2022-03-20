package wikipediaScraping;
import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.boot.autoconfigure.domain.EntityScan;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import com.opencsv.CSVWriter;
import java.io.FileWriter;





@EntityScan
public final class WikipediaHTMLExtractor {

	
	Logger logger = Logger.getLogger(MainExtractor.class);
	
	private String root_url="https://en.wikipedia.org/wiki/" ; 
	private String link;
	private Elements tables;
	private List<ArrayList<String>> tableData; 
	
	

	
	
	public WikipediaHTMLExtractor(String root_url, String page_name) {
		
		this.root_url = root_url;
		this.link = page_name;
		this.tables = null;
		this.tableData = null;
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
	public List<ArrayList<String>> getTableData() {
		return tableData;
	}

	public void setTableData(List<ArrayList<String>> tableData) {
		this.tableData = tableData;
	}
	//

	// check if the cast of string allow to obtain and integer
    public boolean isNumeric(String str) { 
		  try {  
		    Integer.parseInt(str);  
		    return true;
		  } catch(NumberFormatException e){  
		    return false;  
		  }  
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
    
    
    // Build and save the csv file in the directory /src/main/ressources
    public void createCSVFile(List<ArrayList<String>> row, String name_file) throws IOException {
	   /*
	    - row : list of list where each sublist represent a row of the table
	    - name_file : name to save the csv file
	     */
    	
    	
    	// To write data in file with opencsv, We must previously transform ArrayList<String> to String[]
		List<String[]> list = new ArrayList<>();
		for (ArrayList<String> elt : row) {
			
			String[] array = new String[elt.size()];
			
			for(int i=0 ; i<elt.size(); i++) {
				array[i] = elt.get(i);
			}
			list.add(array);
		}
		
		// Get absolute path of the folder where the files will be save
		Path resourceDirectory = Paths.get("src","main","resources","csvdata");
		String absolutePath = resourceDirectory.toFile().getAbsolutePath()+"\\"+name_file+".csv";
		
		// Write each row in the file
		try (CSVWriter writer = new CSVWriter(new FileWriter(absolutePath))) {
            writer.writeAll(list);
            logger.info("The file has been successfully saved in the path : "+absolutePath);
        }catch(IOException e) {
        	logger.error("Error in saving the file !!!");
        }
    }
    
    
    // Create row list for each <tr> tag
    public List<ArrayList<String>> createRow(Elements soup_tr) {
    	
    	/*
    	 - soup_tr : html code of line of all the lines of one table
    	*/
    	
    	List<ArrayList<String>> row_data = new ArrayList<ArrayList<String>>();
    	
		// Create a row for each <tr> of the soup
		for(int row_num=0; row_num<soup_tr.size(); row_num++) {
			
			ArrayList<String> table_row = new ArrayList<String>();
			
			for(Element cell : soup_tr.get(row_num).select("th , td")) {
				
				//if a cell contain one or more <sup> </sup> tags, we remove it all
				cell.select("sup").remove();
			
				// fill the table row and remove the substring "\n" at the end of each text
				table_row.add(cell.text().replaceAll("[\\n\\t ]", ""));  
			}
			
			row_data.add(table_row);			
		}
		
		return row_data;
    }

 // Check if cell contains "colspan" attribute and manage it after by creating in the list (corresponding to the row) new cell 
    public List<ArrayList<String>> fillCellColspan(List<ArrayList<String>> row_data, int row_num, Element cell, int cell_num, boolean fillspan) {
    	/*
    	 - row_data : list of list where each sublist contain the data of a row in the table
    	 - row_num : the number of the row where cell to be modified is located
    	 - cell : code source of cell to check
    	 - cell_num : the number of cell to be modified
    	 - fillspan : if true, we complete all the fillspan cell with the text present in the main cell
    	 */
		if(cell.hasAttr("colspan") && isNumeric(cell.attr("colspan"))) {
			
			int colspan = Integer.parseInt(cell.attr("colspan"));
			
			for (int k=1; k<colspan; k++) {
				
				// add the "text" inside the list if size<cell_num else add at the end of the list
				if(row_data.get(row_num).size()>=cell_num){
					row_data.get(row_num).add(cell_num, fillspan ? cell.text().replaceAll("[\\n\\t ]", "") : "");
				}else {
					row_data.get(row_num).add(fillspan ? cell.text().replaceAll("[\\n\\t ]", "") : "");
				}
			}
		}
		
		return row_data;

    }
    // Check if cell contains "rowspan" attribute and manage it after by creating in the list (corresponding to the row) new cell 
    public List<ArrayList<String>> fillCellRowspan(List<ArrayList<String>> row_data, Elements soup_tr, int row_num, Element cell, int cell_num, boolean fillspan){
	   	 /*
		  - row_data : list of list where each sublist contain the data of a row in the table
		  - row_num : the number of the row where cell to be modified is located
		  - cell : code source of cell to check
		  - cell_num : the number of cell to be modified
		  - colspan : if true, we complete all the colspan cell with the text present in the main cell
		  - soup_tr : source code of the all the line in the table
	   	 */    	
		if(cell.hasAttr("rowspan") && isNumeric(cell.attr("rowspan"))) {
			
			int rowspan = Integer.parseInt(cell.attr("rowspan"));
			
			for (int k=1; k<rowspan; k++) {	
				
				if(row_num+k < soup_tr.size()) {
					// add the "text" inside the list if size<cell_num else add at the end of the list
					if (row_data.get(row_num+k).size()>=cell_num) {
						row_data.get(row_num+k).add(cell_num, fillspan ? cell.text().replaceAll("[\\n\\t ]", "") : "");
					}else {
						row_data.get(row_num+k).add(fillspan ? cell.text().replaceAll("[\\n\\t ]", "") : "");
					}
				}
			}
			
		}
		
		return row_data;
    }
  //build the data with the soup by merge all the previous function (main function of the class)
    // it put together all the previous function to create and save csv file
	public int scraper(boolean fillspan) throws IOException {
		
		// if fillspan = True, we fill all the cell involve in rowspan or colspan
		int table_index = 0;
		
		if (this.tables != null) {
			
			for(Element tab: this.tables) {
				
				table_index++;
				
				List<ArrayList<String>> row_data = new ArrayList<ArrayList<String>>();
				
				// Select all the <tr> of tbody
				Elements soup_tr = tab.select("tbody tr"); 
				
				if (soup_tr.size()!=0) {
					
					// Create a row for each <tr> of the soup
					row_data = createRow(soup_tr);
						
					// manage cell that contain colspan or rowspan attributes 
					for(int row_num=0; row_num<soup_tr.size(); row_num++) {
						
						int cell_num =-1;
						
						for(Element cell : soup_tr.get(row_num).select("th , td")) {
							
							cell.select("sup").remove();
							cell_num++;    
   							
							// manage colspan and update row_data
							row_data = fillCellColspan(row_data, row_num, cell, cell_num, fillspan) ;
							
							//manage rowspan				
							row_data=fillCellRowspan(row_data, soup_tr, row_num, cell, cell_num, fillspan) ;
							
						}
							
					}
				}
				//save file
				createCSVFile(row_data, this.link+"_table_"+String.valueOf(table_index));
				setTableData(row_data) ; 
			}
		
		}else{
			logger.warn("The soup is empty : there isn't table on the webpage !!!");
		}
		
		return table_index;
			


	

	}
	

	
}
