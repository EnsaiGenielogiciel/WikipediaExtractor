package wikipediaScraping;



import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.List;
import org.apache.log4j.Logger;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.opencsv.CSVWriter;

public class ExtractorA {
    
	Logger logger = Logger.getLogger(MainExtractor.class);
	// Build and save the csv file in the directory /src/main/ressources
	
	private WikipediaHTMLExtractor page;
	private Dictionary<Integer, List<ArrayList<String>>> tableData;
	
	public ExtractorA(WikipediaHTMLExtractor page) {
		assert page.getTables().size() > 0;
		this.page=page;
		this.tableData = new Hashtable();
	}
	
	public Dictionary<Integer, List<ArrayList<String>>> getTableData() {
		return tableData;
	}

	public void setTableData(Dictionary<Integer, List<ArrayList<String>>> tableData) {
		this.tableData = tableData;
	}

	void createCSVFile(List<ArrayList<String>> row, String name_file) throws IOException {
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
            
			logger.info("The file has been successfully saved in the path : "+ absolutePath);
        }catch(IOException e) {
        	logger.error("Error in saving the file !!!");
        }
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
    public List<ArrayList<String>> fillCellColspan(List<ArrayList<String>> row_data, int row_num, Element cell, int index_cell_col, int colspan, boolean fillspan) {
    	/*
    	 - row_data : list of list where each sublist contain the data of a row in the table
    	 - row_num : the number of the row where cell to be modified is located
    	 - cell : code source of cell to check
    	 - index_cell_col : the index of the cell in a row
    	 - fillspan : if true, we complete all the fillspan cell with the text present in the main cell
    	 */
		
		for (int k=1; k<colspan; k++) {
			
			index_cell_col++;
			// add the "text" inside the list if size<cell_num else add at the end of the list
			if(row_data.get(row_num).size()>=index_cell_col){
				row_data.get(row_num).add(index_cell_col, fillspan ? cell.text().replaceAll("[\\n\\t ]", "") : "");
			}else {
				row_data.get(row_num).add(fillspan ? cell.text().replaceAll("[\\n\\t ]", "") : "");
			}
		}
		
		return row_data;
    }
	
	
	// Check if cell contains "rowspan" attribute and manage it after by creating in the list (corresponding to the row) new cell 
	public List<ArrayList<String>> fillCellRowspan(List<ArrayList<String>> row_data, Elements soup_tr, int row_num, Element cell, int index_cell_row, boolean fillspan){
	   	 /*
		  - row_data : list of list where each sublist contain the data of a row in the table
		  - row_num : the number of the row where cell to be modified is located
		  - cell : code source of cell to check
		  - index_cell_row : the number of cell to be modified
		  - fillspan : if true, we complete all the colspan cell with the text present in the main cell
		  - soup_tr : source code of the all the line in the table
	   	 */    	
		if(cell.hasAttr("rowspan") && isNumeric(cell.attr("rowspan"))) {
			
			int rowspan = Integer.parseInt(cell.attr("rowspan"));
			
			for (int k=1; k<rowspan; k++) {	
				
				if(row_num+k < soup_tr.size()) {
					// add the "text" inside the list if size<cell_num else add at the end of the list
					if (row_data.get(row_num+k).size()>=index_cell_row) {
						row_data.get(row_num+k).add(index_cell_row, fillspan ? cell.text().replaceAll("[\\n\\t ]", "") : "");
					}else {
						row_data.get(row_num+k).add(fillspan ? cell.text().replaceAll("[\\n\\t ]", "") : "");
					}
				}
			}
			
		}
		
		return row_data;
    }
	
	
    //for some tables, we have rows containing cells whose colspan attribute value is
	//such that cell position + colspan is greater than the number of columns. We have also
	//rows which contains less <td></td> than the number of columns. So at the end
	//we truncate the length of rows or fill it with whitespace according to the number
	//of columns (length of first rows[0])
    public List<ArrayList<String>> uniformisation(List<ArrayList<String>> row_data){
    	
    	//number of columns
    	int nb_col = row_data.get(0).size();
    	
    	//number of rows
    	int nb_row = row_data.size();
    	
    	List<ArrayList<String>> new_data = new ArrayList<ArrayList<String>>();
    	
    	for(int i=0; i<nb_row ; i ++) {
    		
    		int len = row_data.get(i).size();
    		    
    		if(len==nb_col) {
    			new_data.add(row_data.get(i));
    		}else if(len > nb_col) {
    			//we have to much cells in rows than the number of columns due probably to invalid
    			//value of colspan at the end of last cell. So we remove invalid cells
    			ArrayList<String> mylist = new ArrayList<String>(row_data.get(i).subList(0, nb_col));
    			new_data.add(mylist);
    		}else {
    			//we have less number of cells than the number of columns. so we complete with whitespace
    			int to_add = nb_col-len;
    			List<String> myList = row_data.get(i);
    			for(int k=0; k<to_add; k++) {
    				myList.add("");
    			}
    			new_data.add((ArrayList<String>) myList);
    		}    		
    	}
   		
    	return new_data;
    }
	
	
  //build the data with the soup by merge all the previous function (main function of the class)
  // it put together all the previous function to create and save csv file
	public int scraper(boolean fillspan) throws IOException {
		
		// if fillspan = True, we fill all the cell involve in rowspan or colspan
		int table_index = 0;
		
		if (this.page.getTables() != null) {
			
			for(Element tab: this.page.getTables() ) {
				
				table_index++;
				
				List<ArrayList<String>> row_data = new ArrayList<ArrayList<String>>();
				
				// Select all the <tr> of tbody
				Elements soup_tr = tab.select("tbody tr"); 
				
				if (soup_tr.size()!=0) {
					
					// Create a row for each <tr> of the soup
					row_data = createRow(soup_tr);
						
					// manage cell that contain colspan or rowspan attributes 
					for(int row_num=0; row_num<soup_tr.size(); row_num++) {
						
						int index_cell_col = -1;       
						
						int index_cell_row = -1;
						
						for(Element cell : soup_tr.get(row_num).select("th , td")) {
							
							cell.select("sup").remove();
							index_cell_col++;
							index_cell_row++;
   							
							if(cell.hasAttr("colspan") && isNumeric(cell.attr("colspan"))) {
								
								int colspan = Integer.parseInt(cell.attr("colspan"));
							
								// manage colspan and update row_data
								row_data = fillCellColspan(row_data, row_num, cell, index_cell_row, colspan, fillspan) ;
								
								index_cell_row = index_cell_row + colspan-1;
							}
							
							//manage rowspan				
							row_data=fillCellRowspan(row_data, soup_tr, row_num, cell, index_cell_col, fillspan) ;
														
						}
							
					}
				}
				
				// Uniformisation of number of cells per rows
				row_data = uniformisation(row_data);
				
				//save file
				createCSVFile(row_data, this.page.getLink()+"_table_"+String.valueOf(table_index));
				this.tableData.put(table_index, row_data) ; 
			}
		
		}else{
			logger.warn("The soup is empty : there isn't table on the webpage !!!");
		}
		
		return table_index;
	
	}
}