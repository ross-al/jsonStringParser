//Make sure your IDE has org.json library installed

import org.json.JSONArray;
import org.json.JSONObject;

import javax.swing.*;
import java.io.*;
import java.nio.file.FileSystems;
import java.util.Scanner;


public class JsonStringParser {
    public static void main(String[] args) {
        try {

            // Specify the input file path

            String inputFile = "null";

            JFileChooser chooser = new JFileChooser();
            int status = chooser.showOpenDialog(null);
            if (status == JFileChooser.APPROVE_OPTION) {
                File file = chooser.getSelectedFile();
                if (file == null) {
                    return;
                }

                inputFile = chooser.getSelectedFile().getAbsolutePath();
            }


            // Create new txt file for output and save to desktop

            String fileSeparator = FileSystems.getDefault().getSeparator();
            String userHomeFolder = System.getProperty("user.home") + fileSeparator + "Desktop";
            File textFile = new File(userHomeFolder, "EntropyOutput.txt");

            System.out.println("Starting program...");

            // Create a BufferedReader to read the input text file
            BufferedReader br = new BufferedReader(new FileReader(inputFile));

            // Create a FileWriter to write to the output text file
            FileWriter fw = new FileWriter(textFile);

            // Turn each line within input file into JSON object
            String line;
            while ((line = br.readLine()) != null) {
                // Parse the JSON string
                JSONObject jsonObject = new JSONObject(line);

                // Initialize variables
                String keywords = "not found";
                double clickEntropy = 0.0;
                String pageType = "null";

                // Create JSON array
                JSONArray queryInfoArray = jsonObject.getJSONArray("queryInfo");

                // Get keywords from first element json array
                keywords = queryInfoArray.getJSONObject(0)
                        .getJSONObject("meta")
                        .getString("keywords");

                // Check if each element in json array has a click-entropy score, and return it if so
                for(int i = 0; i < queryInfoArray.length(); i++){
                    JSONObject obj = queryInfoArray.getJSONObject(i);
                    if(obj.has("type") && obj.get("type").toString().equals("query-specificity")){
                        if(obj.getJSONObject("meta").has("click-entropy")){
                            clickEntropy = obj.getJSONObject("meta")
                                    .getDouble("click-entropy");
                        }
                    }
                }

                if(clickEntropy <= 3){
                    pageType = "Detail";
                } else {
                    pageType = "Search";
                }


                // Write the extracted values to the output text file
                fw.write("Keyword: " + keywords + ", Score: " + clickEntropy + ", Page Type: " + pageType + "\n");

            }

            // Close the input and output files
            br.close();
            fw.close();

            System.out.println("Extraction and writing completed successfully.");
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
