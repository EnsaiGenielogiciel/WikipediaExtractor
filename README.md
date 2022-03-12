#### 1. Summary of project
The application is a table extractor. Concretely it allows to connect to a wikipedia page, then proceeds to the scraping of all the tables that are there. Once the scraping is done, it saves in the folder src/main/resources/csvdata the different scraped tables in a csv format. 

In the main class WikipediaHTMLExtractor, we implement via a pattern design builder the methods for extracting the tables and saving it. The TestWikipediaHTMLExtractor class implements the unit tests of the methods. Moreover, among these tests, there is a testScraper_build_table_sample which launches the extractor on a sample of urls from the file wikiurls.txt present in the folder scraping\WikipediaScraping\src\test\resources. The list below shows the links where we have tested the extractor : 

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

The testScraper_build_table_all test runs the extractor on all the urls of the file wikiurls.txt.

#### 2. LAUNCH THE PROJECT
To run the project, just clone it, then import the wikipediaScraping project in eclipse. 
A test *mvn test* will allow to run the unit tests, and to apply the extractor on the links listed above.


