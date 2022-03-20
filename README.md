#### 1. Summary of project
The application is a table extractor. Concretely it allows to connect to a wikipedia page, then proceeds to the scraping of all the tables that are there. Once the scraping is done, it saves in the folder src/main/resources/csvdata the different scraped tables in a csv format. 

In the main class WikipediaHTMLExtractor, we implement methods for extracting the tables and saving it. The TestWikipediaHTMLExtractor class implements the unit tests of the methods. 

Moreover, among these tests, there is a testScraper_build_table_two which launches the extractor on a sample of urls from the file wikiurls.txt present in the folder scraping\WikipediaScraping\src\test\resources. 

#### 2.WikiLinks that work
The list below shows the links where we have tested the extractor : 

- Comparison_of_DOS_operating_systems
- Comparison_of_programming_languages_(syntax)
- Comparison_of_domestic_robots
- Comparison_of_HP_graphing_calculators
- Comparison_of_World_War_I_tank"
- Comparison_of_HTML_editors"
- Comparison_of_layout_engines_(Document_Object_Model)
- Comparison_of_mail_servers
- Comparison_of_Internet_Relay_Chat_clients
- Comparison_of_Sony_Vaio_laptops
- Comparison_of_alcopops
- Comparison_of_geographic_information_systems_software
- Comparison_of_instant_messaging_clients
- Comparison_of_raster_graphics_editors
- Comparison_of_Microsoft_Windows_versions
- Comparison_of_audio_player_software
- Comparison_of_portable_media_players
- Comparison_of_layout_engines_(HTML)
- Comparison_of_open-source_operating_systems
- Comparison_of_programming_languages_(basic_instructions)
- List_of_AMD_graphics_processing_units
- Comparison_of_Android_devices
- List_of_Intel_graphics_processing_units
- Comparison_of_Internet_Relay_Chat_services
- Comparison_of_Nvidia_chipsets
- Comparison_of_BitTorrent_clients
- List_of_Nvidia_graphics_processing_units
- Comparison_of_TLS_implementations
- Comparison_of_antivirus_software
- Comparison_of_email_clients
- Comparison_of_integrated_development_environments
- Comparison_of_layout_engines_(XHTML_1.1)
- Comparison_of_operating_system_kernels
- Comparison_of_Fukushima_and_Chernobyl_nuclear_accidents

The testScraper_build_table_one test runs the extractor on all the urls of the file wikiurls.txt.

#### 3. LAUNCH THE PROJECT
To run the project, just clone it, then import the wikipediaScraping project in eclipse. 
A test *mvn test* will allow to run the unit tests, and to apply the extractor on the links listed above.
A mvn spring-boot:run should enable to run the spring boot app. An url of type :
http://localhost:8080/wikilink
Each time you can choose the wikilink from the list of links that work and it should display information on all tables of the wikilink.

#### 4. Architecture 
We have a simple architecture, the class WikipediaHTMLExtractor enable us to parse the html code and see if we have tables or not. We see if the attribute 'class' of the table contain the word "wikitable".
Then extracts the tables if the WikipediaHTMLExtractor has tables of strictly positive sizes. It starts by the method scraper which merges multiple other methods in order to create rows, fill the Cell Rowspan, the CellColspan and standardize the number of columns.
Eventually, the csv is created and the data is in it. 
![diagramme](https://user-images.githubusercontent.com/50030050/159177550-9288c4fa-f0d7-4806-b86a-5388f6dc0ef9.png)


#### 5.  Design Pattern
Design Patterns allow to solve problems related to the architecture or to optimize the code.
Since in here we have only the extraction class, there are no different types of classes from which we might deduct a possible pattern design approach. So, Instead, we thought of the observer pattern design while logging the entire application. 
The results of methods and the updates are shown at run time. 


#### 6.  All  about our App
 Our application manages to use several array structures and in particular nested arrays.
Indeed, in some cases, we sometimes have a lot of cells with cellspan and rowspan attributes and we were able to take these cases into account:
- It is possible to launch the scraping of several pages sequentially;
- All the tables of a page are transformed into arraylist, then converted into csv;
- When a page contains no table, the application indicates that there is none;
- When the page no longer exists, the application indicates that the page is non-existent and does not perform any task.
- A page can contain tags of table that are not really tables. We verify that it is a table before performing the scraping, checking that the "class" attribute of the tag contains the string "wikitable";

However, it has some limitations. In fact, while  exploring the different pages we realized that in some cases, there were errors in the structure of the basic html code. Indeed, the last cell of a row could contain a "colspan" attribute that spans more cells than there are total columns. To handle this case, we remove blank cells at the end of each row so that the total number of cells equals the number of columns. Note that these empty cells exist because new cells have been created using the colspan attributes. In the case of the rowspan attribute, we have managed this case, by creating a cell each time if and only the number of rows in a table is not exceeded.

In addition, some <tr> </tr> tags contained fewer cells than the total number of columns (ex: https://en.wikipedia.org/wiki/Comparison_of_programming_languages_(syntax)). To handle this case, we complete the corresponding rows with empty cells.
For some tables (those for which the class attribute contained "jquery sortable"), we found that by retrieving the texts at the column level, if the name of a column contained more than one word, the space between the words disappeared.

NB : The folder pythonscript contains the class wikipediaSraping where we implement all the functions to scrap wikipedia webpages like we do in java. More specifically, the python code has exactly the same code structure as the java code and lead to the same results. The python code we constructed allows to scrap more than one webpage. We also implemented unit test for python class. 
