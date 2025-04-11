package assignment2002.utils;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class BTOFileService {
    private static String propertyFile = "Information/ProjectList.txt"; //Constant


    public static void appendBTO(String formattedString){

        try(BufferedWriter writer = new BufferedWriter(new FileWriter(propertyFile,true))){
            writer.newLine();
            writer.write(formattedString);

        } catch (IOException e){
            System.out.println("Exception: " + e);
            e.printStackTrace();
        } 


    }

    public static void removeBTO(String projName){

        try {
            List<String> allLines = Files.readAllLines(Paths.get(propertyFile));

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
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(propertyFile))) {
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
