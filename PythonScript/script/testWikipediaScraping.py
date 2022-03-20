import unittest
from wikipediaScraping import *


class TestWikipediaHTMLExtractor(unittest.TestCase):

    ## Test html_parser and must return Not None
    def test_html_parser_not_none(self):
        wiki = WikipediaHTMLExtractor(
            root_="https://en.wikipedia.org/wiki/",
            link_="Comparison_of_DOS_operating_systems",
        )
        result = wiki.html_parser()
        self.assertIsNotNone(result)

    ## Test html_parser and must return None
    def test_html_parser_none(self):
        wiki = WikipediaHTMLExtractor(
            root_="https://en.wikipedia.org/wiki/",
            link_="Comparison_of_World_War_I_tank",
        )
        result = wiki.html_parser()
        self.assertIsNone(result)

    # Test the number of table on a webpage
    def test_scraper(self):
        wiki = WikipediaHTMLExtractor(
            root_="https://en.wikipedia.org/wiki/",
            link_="Comparison_of_programming_languages_(syntax)",
        )
        result = wiki.scraper()
        self.assertEqual(result, 4)

    # Test number of rows and columns of a table
    def test_number_column_row_table(self):
        wiki = WikipediaHTMLExtractor(
            root_="https://en.wikipedia.org/wiki/",
            link_="Comparison_of_programming_languages_(syntax)",
        )
        wiki.scraper(fill_span=True)
        df = wiki.list_data_frame[0]
        nb_row = df.shape[0]
        nb_column = df.shape[1]
        self.assertTrue(nb_column == 3 and nb_row == 61)

    # Test name of column of a table
    def test_column_name_table(self):
        wiki = WikipediaHTMLExtractor(
            root_="https://en.wikipedia.org/wiki/",
            link_="Comparison_of_programming_languages_(syntax)",
        )
        wiki.scraper(fill_span=True)
        col = wiki.list_data_frame[0].columns
        self.assertTrue(
            col[0] == "Language"
            and col[1] == "Statement separator-terminator"
            and col[2] == "Secondary separator-terminator"
        )

    # Launch extractor in several link and save csv file of table
    def test_extraction(self):
        lien = []
        lien.append("Comparison_of_DOS_operating_systems")
        lien.append("Comparison_of_World_War_I_tank")
        lien.append("Comparison_of_programming_languages_(syntax)")
        lien.append("Comparison_of_domestic_robots")
        lien.append("Comparison_of_HP_graphing_calculators")
        lien.append("Comparison_of_World_War_I_tank")
        lien.append("Comparison_of_HTML_editors")
        lien.append("Comparison_of_layout_engines_(Document_Object_Model)")
        lien.append("Comparison_of_mail_servers")
        lien.append("Comparison_of_Internet_Relay_Chat_clients")
        lien.append("Comparison_of_Sony_Vaio_laptops")
        lien.append("Comparison_of_alcopops")
        lien.append("Comparison_of_geographic_information_systems_software")
        lien.append("Comparison_of_instant_messaging_clients")
        lien.append("Comparison_of_raster_graphics_editors")
        lien.append("Comparison_of_Microsoft_Windows_versions")
        lien.append("Comparison_of_audio_player_software")
        lien.append("Comparison_of_portable_media_players")
        lien.append("Comparison_of_layout_engines_(HTML)")
        lien.append("Comparison_of_open-source_operating_systems")
        lien.append("Comparison_of_programming_languages_(basic_instructions)")
        lien.append("List_of_AMD_graphics_processing_units")
        lien.append("Comparison_of_Android_devices")
        lien.append("List_of_Intel_graphics_processing_units")
        lien.append("Comparison_of_Internet_Relay_Chat_services")
        lien.append("Comparison_of_Nvidia_chipsets")
        lien.append("Comparison_of_BitTorrent_clients")
        lien.append("List_of_Nvidia_graphics_processing_units")
        lien.append("Comparison_of_TLS_implementations")
        lien.append("Comparison_of_antivirus_software")
        lien.append("Comparison_of_email_clients")
        lien.append("Comparison_of_integrated_development_environments")
        lien.append("Comparison_of_layout_engines_(XHTML_1.1)")

        for i, link in enumerate(lien):
            wiki = WikipediaHTMLExtractor(
                root_="https://en.wikipedia.org/wiki/", link_=link
            )
            wiki.scraper(fill_span=True)
            wiki.data_to_csv(root_path="data")


if __name__ == "__main__":
    unittest.main()
