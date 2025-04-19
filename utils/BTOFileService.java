package assignment2002.utils;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class BTOFileService implements FilePath {
    private static final Map<String, Integer> COLUMN_MAP = Map.ofEntries(
    Map.entry("projName", 0),
    Map.entry("neighbourhood", 1),
    Map.entry("2-Room", 2),
    Map.entry("twoRoomAmt", 3),
    Map.entry("twoRoomPrice", 4),
    Map.entry("3-Room", 5),
    Map.entry("threeRoomAmt", 6),
    Map.entry("threeRoomPrice", 7),
    Map.entry("openDate", 8),
    Map.entry("closeDate", 9),
    Map.entry("manager", 10),
    Map.entry("officerSlot", 11),
    Map.entry("officers", 12),
    Map.entry("pendingOfficers", 13),
    Map.entry("rejectedOfficers", 14),
    Map.entry("visible", 15)
    );

    public static void appendBTO(String formattedString){

        try(BufferedWriter writer = new BufferedWriter(new FileWriter(PROJECT_TXT_PATH,true))){
            writer.newLine();
            writer.write(formattedString);

        } catch (IOException e){
            System.out.println("Exception: " + e);
            e.printStackTrace();
        } 


    }

    public static void editBTOByColumn(String projName, String colName, String newValue) {
        Integer columnIndex = COLUMN_MAP.get(colName);
        if (columnIndex == null) {
            System.out.println("Invalid Column name: " + colName);
            return;
        }
    
        try {
            List<String> allLines = Files.readAllLines(Paths.get(PROJECT_TXT_PATH));
            String header = allLines.get(0);
            List<String> updatedLines = new ArrayList<>();
    
            for (int i = 1; i < allLines.size(); i++) {
                String line = allLines.get(i).trim();
                if (!line.isEmpty()) {
                    String[] parts = line.split("\t");
    
                    if (parts[0].equalsIgnoreCase(projName)) {
                        parts[columnIndex] = newValue;
                        updatedLines.add(String.join("\t", parts));
    
                        for (int j = i + 1; j < allLines.size(); j++) {
                            updatedLines.add(allLines.get(j));
                        }
                        break;
                    } else {
                        updatedLines.add(line);
                    }
                }
            }
    
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(PROJECT_TXT_PATH))) {
                writer.write(header);
                if (!updatedLines.isEmpty()) writer.newLine();
                for (int i = 0; i < updatedLines.size(); i++) {
                    writer.write(updatedLines.get(i));
                    if (i != updatedLines.size() - 1) writer.newLine();
                }
            }
    
            System.out.printf("Successfully updated %s for %s -> %s\n", colName, projName, newValue);
    
        } catch (IOException e) {
            System.out.println("Error editing field: " + e.getMessage());
        }
    }

    public static void removeBTO(String projName){

        try {
            List<String> allLines = Files.readAllLines(Paths.get(PROJECT_TXT_PATH));

            if (allLines.isEmpty()) {
                System.out.println("File is empty.");
                return;
            }

            String header = allLines.get(0);
            List<String> filtered = new ArrayList<>();

            // Keep only lines that DON'T match the project to delete
            for (int i = 1; i < allLines.size(); i++) {
                String line = allLines.get(i).trim();
                if (!line.isEmpty()) {
                    String[] parts = line.split("\t");
                    String projectName = parts[0].trim();
                    if (!projectName.equalsIgnoreCase(projName)) {
                        filtered.add(line);
                    }
                }
            }

            // Write back header and filtered lines without trailing newline
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(PROJECT_TXT_PATH))) {
                writer.write(header);
                if (!filtered.isEmpty()) writer.newLine();
                for (int i = 0; i < filtered.size(); i++) {
                    writer.write(filtered.get(i));
                    if (i != filtered.size() - 1) writer.newLine(); // no trailing newline
                }
            }

            System.out.println("Deleted project: " + projName);

        } catch (IOException e) {
            System.out.println("Error processing file: " + e.getMessage());
        }
    }

}
