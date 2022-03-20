import pandas as pd
from bs4 import BeautifulSoup
import requests
import numpy as np


class WikipediaHTMLExtractor:
    def __init__(self, root_, link_):
        super(WikipediaHTMLExtractor).__init__()
        self.link = root_ + link_
        self.list_data_frame = list()

    # Request a webpage and send back a soup
    def html_parser(self):

        requete = requests.get(self.link)

        if requete.status_code == 200:
            html_parser = BeautifulSoup(requete.text, "html.parser")

            tables = html_parser.find_all("table", {"class": "wikitable"})

            return tables if len(tables) != 0 else None

        else:
            return None

    # Delete all <sup></sup> in cell
    def decompose_sup(self, parent):
        sup_tag = parent.find_all("sup")
        if len(sup_tag) > 0:
            for i in range(len(sup_tag)):
                parent.sup.decompose()
            return parent
        else:
            return parent

    # Create csv using pandas dataframe
    def data_to_csv(self, root_path):

        if self.list_data_frame:

            name_file = self.link.split("/")[-1]

            for i, data in enumerate(self.list_data_frame):

                data.to_csv(
                    root_path + "/" + name_file + "_file_" + str(i) + ".csv",
                    encoding="utf-8",
                    index=False,
                )

        else:
            print("Any data to save")

    # Create row using th and td in a tr
    def create_row(self, soup_tr):

        rows = list()
        for row_num, table_row in enumerate(soup_tr):
            rows.append(list())

            for cell in table_row.find_all(["th", "td"]):

                cell = self.decompose_sup(cell)

                rows[row_num].append(cell.text.rstrip("\n"))

        return rows

    # Uniformisation of number of cells per rows
    def uniformisation(self, rows):
        # for some tables, we have rows containing cells whose colspan attribute value is
        # such that cell position + colspan is greater than the number of columns. We have also
        # rows which contains less <td></td> than the number of columns. So at the end
        # we truncate the length of rows or fill it with whitespace it according to the number
        # of columns (length or rows[0])
        nb_col = len(rows[0])
        for i, elt in enumerate(rows):
            if len(elt) != nb_col:
                if len(elt) < nb_col:
                    rows[i] = rows[i] + [""] * (nb_col - len(elt))
                else:
                    rows[i] = rows[i][:nb_col]
        return rows

    # Manage colspan and update rows
    def fill_cell_span(self, rows, soup_tr, fill_span):

        for row_num, table_row in enumerate(soup_tr):

            index_row_cell = -1
            index_col_cell = -1
            for cell_num, cell in enumerate(table_row.find_all(["th", "td"])):

                index_row_cell += 1
                cell = self.decompose_sup(cell)
                if "colspan" in cell.attrs and cell.attrs["colspan"].isdigit():
                    colspan = int(cell.attrs["colspan"])
                    for i in range(1, colspan):
                        index_row_cell += 1
                        rows[row_num].insert(
                            index_row_cell, cell.text.rstrip("\n") if fill_span else ""
                        )

                if "rowspan" in cell.attrs and cell.attrs["rowspan"].isdigit():
                    rowspan = int(cell.attrs["rowspan"])

                    for i in range(1, rowspan):
                        ## rowspan < len(soup_tr)  if row_num+i <=len(soup_tr)
                        if row_num + i < len(soup_tr):
                            rows[row_num + i].insert(
                                cell_num, cell.text.rstrip("\n") if fill_span else ""
                            )

        rows = self.uniformisation(rows)
        return rows

    # Put all together to build and save data
    def scraper(self, fill_span=True):

        tables = self.html_parser()

        if tables is not None:

            count_table = 0  # count the number of table in the dataframe

            for tab in tables:

                soup_tr = tab.find_all("tr")

                if len(soup_tr) != 0:

                    count_table += 1  # increment only if the soup_tr isn't empty

                    # create row
                    rows = self.create_row(soup_tr)

                    # manage span of the row
                    rows = self.fill_cell_span(rows, soup_tr, fill_span)

                    # get column name
                    col = rows.pop(0)

                    # create dataframe and update list_data_frame
                    data = pd.DataFrame(rows, columns=col)

                    self.list_data_frame.append(data)

            print("INFO : Scraping completed successfully")
            return count_table
        else:
            print("WARNING : The web page not exist probably")
            return 0
