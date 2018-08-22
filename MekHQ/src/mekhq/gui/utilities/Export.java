/*
 * Export.java
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

import java.awt.Component;
import java.io.File;
import java.io.IOException;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.filechooser.FileFilter;

import mekhq.Utilities;
import mekhq.campaign.Campaign;
import mekhq.campaign.CampaignOptions;
import mekhq.campaign.universe.Planets;
import mekhq.gui.CampaignGUI;

public class Export {

    private static IExport exportCSV = new ExportCSV();
    private static IExport exportXML = new ExportXML();


    /**
     * Gets a filename from the user using the JFileChooser
     * @param format
     * @param dialogTitle
     * @param filename
     * @param uiComponent
     * @return JFileChooser
     */
    private static JFileChooser getExportFile(String format, String dialogTitle, String filename, Component uiComponent) {
        JFileChooser filePicker = new JFileChooser(".");
        filePicker.setDialogTitle(dialogTitle);
        filePicker.setFileFilter(new FileFilter() {
            @Override
            public boolean accept(File dir) {
                if (dir.isDirectory()) {
                    return true;
                }
                return dir.getName().endsWith(format);
            }

            @Override
            public String getDescription() {
                if(format.equals("csv")) {
                    return "CSV File";
                } else if (format.equals("xml")) {
                    return "XML File";
                } else if (format.equals("json")) {
                    return "JSON File";
                } else if (format.equals("html")) {
                    return "HTML File";
                } else {
                    return "File";
                }
            }
        });
        filePicker.setSelectedFile(new File(filename + "." + format));
        int returnVal = filePicker.showSaveDialog(uiComponent);

        if ((returnVal != JFileChooser.APPROVE_OPTION)
                || (filePicker.getSelectedFile() == null)) {
            // I want a file, y'know!
            return null;
        }
        return filePicker;
    }

    /**
     * Checks to make sure the file has the appropriate ending / extension.
     * @param file
     * @param format
     * @return File
     */
    private static File checkFileEnding(File file, String format) {
        String path = file.getPath();
        if (!path.endsWith("." + format)) {
            path += "." + format;
            file = new File(path);
        }
        return file;
    }

    /**
     * Checks if a file already exists, if so it makes a backup copy.
     * @param file
     * @param path
     */
    private static void checkToBackupFile(File file, String path) {
        // check for existing file and make a back-up if found
        String path2 = path + "_backup";
        File backupFile = new File(path2);
        if (file.exists()) {
            Utilities.copyfile(file, backupFile);
        }
    }

    /**
     * Exports JTable to a file (CSV, XML, etc.)
     * @param table
     * @param format
     * @param dialogTitle
     * @param filename
     * @param uiComponent
     */
    public static void exportTable(JTable table, String format, String dialogTitle, String filename, Component uiComponent) {
        JFileChooser fc = getExportFile(format, dialogTitle, filename, uiComponent);
        String report = "";

        File file = fc.getSelectedFile();
        if (file == null) {
            return; // something went wrong
        }

        file = checkFileEnding(file, format);
        checkToBackupFile(file, file.getPath());

        if (format.equals("csv")) {
            report = exportCSV.exportTable(table, file);
        } else if (format.equals("xml")) {
            // TODO
        } else if (format.equals("json")) {
            // TODO
        } else if (format.equals("html")) {
            // TODO
        }
        JOptionPane.showMessageDialog(uiComponent, report);
    }

    /**
     * Exports Finances to a file (CSV, XML, etc.)
     * @param format
     * @param dialogTitle
     * @param filename
     * @param uiComponent
     * @param campaign
     */
    public static void exportFinances(String format, String dialogTitle, String filename, Component uiComponent, Campaign campaign) {
        JFileChooser fc = getExportFile(format, dialogTitle, filename, uiComponent);

        File file = fc.getSelectedFile();
        if (file == null) {
            return; // something went wrong
        }

        file = checkFileEnding(file, format);
        checkToBackupFile(file, file.getPath());

        String report = "";
        if (format.equals("csv")) {
            report = exportCSV.exportFinances(file, format, campaign);
        } else if (format.equals("json")) {
            // TODO
        } else if (format.equals("html")) {
            // TODO
        } else if (format.equals("xml")) {
            // TODO
        }
        JOptionPane.showMessageDialog(uiComponent, report);
    }

    /**
     * Exports Planets to a file (CSV, XML, etc.)
     * @param format
     * @param dialogTitle
     * @param filename
     * @param uiComponent
     */
    public static void exportPlanets(String format, String dialogTitle, String filename, Component uiComponent) {
        JFileChooser fc = getExportFile(format, dialogTitle, filename, uiComponent);

        File file = fc.getSelectedFile();
        if (file == null) {
            return; // something went wrong
        }

        file = checkFileEnding(file, format);
        checkToBackupFile(file, file.getPath());

        String report = "";
        if (format.equals("xml")) {
            report = Planets.getInstance().exportPlanets(file.getPath(), format);
        } else if (format.equals("json")) {
            // TODO
        } else if (format.equals("html")) {
            // TODO
        } else if (format.equals("csv")) {
            // TODO
        }
        JOptionPane.showMessageDialog(uiComponent, report);
    }

    /**
     * Save campaign options to a file (CSV, XML, etc.)
     * @param format
     * @param dialogTitle
     * @param filename
     * @param uiComponent
     * @param campaign
     * @param campaignOptions
     * 
     */
    public static void saveOptionsFile(String format, String dialogTitle, String filename, Component uiComponent, Campaign campaign, CampaignOptions campaignOptions) throws IOException {
        JFileChooser fc = getExportFile(format, dialogTitle, filename, uiComponent);

        File file = fc.getSelectedFile();
        if (file == null) {
            return; // something went wrong
        }

        file = checkFileEnding(file, format);
        checkToBackupFile(file, file.getPath());

        String report = "";
        if (format.equals("xml")) {
            report = exportXML.exportCampaignOptions(file, campaign, campaignOptions);
        } else if (format.equals("json")) {
            // TODO
        } else if (format.equals("html")) {
            // TODO
        } else if (format.equals("csv")) {
            // TODO
        }
        JOptionPane.showMessageDialog(uiComponent, report);
    }

    /**
     * Save selected warehouse parts to a file (PARTS, CSV, etc.)
     * @param format
     * @param dialogTitle
     * @param filename
     * @param uiComponent
     * @param gui
     */
    public static void savePartsFile(String format, String dialogTitle, String filename, Component uiComponent, CampaignGUI gui) throws IOException {
        JFileChooser fc = getExportFile(format, dialogTitle, filename, uiComponent);

        File file = fc.getSelectedFile();
        if (file == null) {
            return; // something went wrong
        }

        file = checkFileEnding(file, format);
        checkToBackupFile(file, file.getPath());

        String report = "";
        if (format.equals("parts")) { //.parts is XML
            report = exportXML.exportParts(file, gui);
        } else if (format.equals("csv")) {
            // TODO
        } else if (format.equals("html")) {
            // TODO
        } else if (format.equals("json")) {
            // TODO
        }
        JOptionPane.showMessageDialog(uiComponent, report);
    }

    /**
     * Save one or more selected personnel to a file (PRSX, CSV, etc.)
     * @param format
     * @param dialogTitle
     * @param filename
     * @param uiComponent
     * @param gui
     */
    public static void savePersonFile(String format, String dialogTitle, String filename, Component uiComponent, CampaignGUI gui) throws IOException {
        JFileChooser fc = getExportFile(format, dialogTitle, filename, uiComponent);

        File file = fc.getSelectedFile();
        if (file == null) {
            return; // something went wrong
        }

        file = checkFileEnding(file, format);
        checkToBackupFile(file, file.getPath());

        String report = "";
        if (format.equals("prsx")) { //.prsx is XML
            report = exportXML.exportPersonFile(file, gui);
        } else if (format.equals("json")) {
            // TODO
        } else if (format.equals("csv")) {
            // TODO
        } else if (format.equals("html")) {
            // TODO
        }
        JOptionPane.showMessageDialog(uiComponent, report);
    }
}
