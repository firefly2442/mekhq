/*
 * ExportCSV.java
 * 
 * MegaMek - Copyright (C) 2018 - The MegaMek Team
 * 
 * This file is part of MekHQ.
 * 
 * MekHQ is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * MekHQ is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with MekHQ.  If not, see <http://www.gnu.org/licenses/>.
 */

package mekhq.gui.utilities;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import javax.swing.JTable;
import javax.swing.table.TableModel;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;

import mekhq.MekHQ;
import mekhq.Utilities;
import mekhq.campaign.Campaign;
import mekhq.campaign.CampaignOptions;
import mekhq.campaign.finances.Transaction;
import mekhq.gui.CampaignGUI;

public class ExportCSV implements IExport
{
    @Override
    public String exportTable(JTable table, File file) {
        String METHOD_NAME = "exportTable()";
        String report;
        try {
            TableModel model = table.getModel();
            BufferedWriter writer = Files.newBufferedWriter(Paths.get(file.getPath()));
            String[] columns = new String[model.getColumnCount()];
            for (int i = 0; i < model.getColumnCount(); i++) {
                columns[i] = model.getColumnName(i);
            }
            CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT.withHeader(columns));

            for (int i = 0; i < model.getRowCount(); i++) {
                Object[] towrite = new String[model.getColumnCount()];
                for (int j = 0; j < model.getColumnCount(); j++) {
                    // use regex to remove any HTML tags
                    towrite[j] = model.getValueAt(i,j).toString().replaceAll("\\<[^>]*>", "");
                }
                csvPrinter.printRecord(towrite);
            }

            csvPrinter.flush();
            csvPrinter.close();

            report = model.getRowCount() + " " + resourceMap.getString("RowsWritten.text");
        } catch(Exception ioe) {
            MekHQ.getLogger().error(Utilities.class, METHOD_NAME, "Error exporting JTable");
            report = resourceMap.getString("dlgProblemWritingFile.text");
        }
        return report;
    }

    @Override
    public String exportParts(File file, CampaignGUI gui) {
        //TODO
        return null;
    }

    @Override
    public String exportFinances(File file, String format, Campaign c) {
        final String METHOD_NAME = "exportFinances()";
        String report;

        try {
            if (c.getFinances().getAllTransactions().size() != 0) {
                BufferedWriter writer = Files.newBufferedWriter(Paths.get(file.getPath()));
                CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT.withHeader("Date", "Category", "Description", "Amount", "RunningTotal"));
                SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy");
                ArrayList<Transaction> transactions = c.getFinances().getAllTransactions();

                int running_total = 0;
                for (int i = 0; i < transactions.size(); i++) {
                    running_total += transactions.get(i).getAmount();
                    csvPrinter.printRecord(
                            df.format(transactions.get(i).getDate()),
                            transactions.get(i).getCategoryName(),
                            transactions.get(i).getDescription(),
                            transactions.get(i).getAmount(),
                            running_total
                            );
                }

                csvPrinter.flush();
                csvPrinter.close();

                report = transactions.size() + " " + resourceMap.getString("FinanceExport.text");
            } else {
                report = resourceMap.getString("dlgNoFinances.text");
            }
        } catch(IOException ioe) {
            MekHQ.getLogger().error(getClass(), METHOD_NAME, "Error exporting finances to " + format);
            report = resourceMap.getString("dlgProblemWritingFile.text");
        }

        return report;
    }

    @Override
    public String exportCampaignOptions(File file, Campaign campaign, CampaignOptions co) {
        //TODO
        return null;
    }

    @Override
    public String exportPersonFile(File file, CampaignGUI gui) {
        //TODO
        return null;
    }
}
