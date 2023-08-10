//Make sure your IDE has org.json library installed

import org.json.JSONArray;
import org.json.JSONObject;
import java.io.*;
import java.nio.file.FileSystems;


public class JsonStringParser {
    public static void main(String[] args) {
        try {

            // Specify the input and output file paths

            //YOU MUST SAVE YOUR INPUT FILE TO YOUR DESKTOP

            String fileName = "entropy_json.txt"; // UPDATE NAME OF INPUT FILE

            String username = System.getProperty("user.name");
            String fileSeparator = FileSystems.getDefault().getSeparator();
            String filePath = "C:" + fileSeparator + "Users" + fileSeparator + username + fileSeparator + "Desktop" + fileSeparator;
            String inputFile = filePath + fileName;

            String fileName2 = "EntropyOutput.txt"; // UPDATE NAME OF OUTPUT FILE (create an empty txt file and save to your desktop)

            //YOU MUST SAVE YOUR OUTPUT FILE TO YOUR DESKTOP

            String outputFile = filePath + fileName2;


            System.out.println("Starting program...");
            // Create a BufferedReader to read the input text file
            BufferedReader br = new BufferedReader(new FileReader(inputFile));

            // Create a FileWriter to write to the output text file
            FileWriter fw = new FileWriter(outputFile);

            // Turn each line within input file into JSON object

            String line;
            while ((line = br.readLine()) != null) {
                // Parse the JSON string
                JSONObject jsonObject = new JSONObject(line);

                // Initialize variables

                String keywords = "not found";
                double clickEntropy = 0.0;
                String pageType = "null";

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

//testing commit with git