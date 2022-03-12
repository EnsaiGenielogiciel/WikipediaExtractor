# WikipediaExtractor

L'application est un extracteur de table. Concrètement elle permet de se connecter à une page wikipedia, puis 
procède au scraping de toutes les tables qui s'y trouve. Une fois le scraping effectué, elle sauvegarde dans
le dossier src/main/ressources/csvdata les différents tableaux scrapés sous un format csv. 

Dans la classe principale WikipediaHTMLExtractor, on implémente via un pattern design builder les méthodes pour l'extraction et l'enregistrement des fichiers. La classe TestWikipediaHTMLExtractor implémente quant à elle les tests unitaires des méthodes. Par ailleurs, parmi ces tests, on dispose d'un test testScraper_build_table_sample qui lance l'extracteur sur un échantillon d'urls du fichier wikiurls.txt  présent dans le dossier scraping\WikipediaScraping\src\test\resources. La liste ci-dessous renseigne sur les liens ou nous avons testé l'extracteur

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

Le test testScraper_build_table_all quant à lui lance l'extracteur sur l'ensemble de tout les urls du fichier. 


