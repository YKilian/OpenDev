package src.main.java.jsonManipulator;


import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import src.main.java.gui.gui;

public class Writer {
    public String filename;
    public String updateFile = "/res/updates.json";

    public Writer( String filename){
        this.filename = filename;

        File currentFile = new File(gui.class.getProtectionDomain().getCodeSource().getLocation().getPath());
        File parentDir = currentFile.getParentFile().getParentFile();
        String parentPath = parentDir.getAbsolutePath().replace(File.separator, "/");
        updateFile = parentPath + updateFile;
    }

    public void writeToJson(String title, String note, String[] oldVal){
        JSONParser jsonParser = new JSONParser();

        try {
            Object obj = jsonParser.parse(new FileReader(this.filename));
            JSONArray jsonArray = (JSONArray)obj;
            JSONObject newEntry;

            newEntry = createEntry(title, note, oldVal, jsonArray);

            jsonArray.add(newEntry);

            FileWriter file = new FileWriter(this.filename);
            file.write(jsonArray.toJSONString());
            file.flush();
            file.close();

        } catch (ParseException | IOException e) {
            e.printStackTrace();
        }
    }

    public void updateNotes(){
        JSONParser jsonParser = new JSONParser();

        try {
            Object obj = jsonParser.parse(new FileReader(this.filename));
            JSONArray jsonArray = (JSONArray)obj;

            Object updates = jsonParser.parse(new FileReader(this.updateFile));
            JSONArray updateArray = (JSONArray)updates;

            JSONObject newEntry;

            for (Object update : updateArray) {
                JSONObject updateObject = (JSONObject) update;
                String title = (String) updateObject.get("title");
                String note = (String) updateObject.get("note");
                String timestamp = (String) updateObject.get("timestamp");

                newEntry = createEntry(title, note, new String[]{title, note, timestamp}, jsonArray);

                jsonArray.add(newEntry);
            }

            FileWriter file = new FileWriter(this.filename);
            file.write(jsonArray.toJSONString());
            file.flush();
            file.close();

            FileWriter updateFile = new FileWriter(this.updateFile);
            updateFile.write("[]");
            updateFile.flush();
            updateFile.close();

        } catch (ParseException | IOException e) {
            e.printStackTrace();
        }
    }

    public void deleteFromJson(String[] oldVal) {
        JSONParser jsonParser = new JSONParser();

        try {
            Object obj = jsonParser.parse(new FileReader(this.filename));
            JSONArray jsonArray = (JSONArray)obj;
            JSONObject object = new JSONObject();

            object.put("title", stringConverter(oldVal[0]));
            object.put("note",stringConverter(oldVal[1]));
            object.put("timestamp", oldVal[2]);

            int occ = jsonArray.indexOf(object);
            if(occ != -1){
                jsonArray.remove(occ);
            }
            object.clear();

            FileWriter file = new FileWriter(this.filename);
            file.write(jsonArray.toJSONString());
            file.flush();
            file.close();

        } catch (ParseException | IOException e) {
            e.printStackTrace();
        }
    }

    private static JSONObject createEntry(String title, String note, String[] oldVal, JSONArray jsonArray){
        String timestamp = oldVal[2];
        JSONObject new_object = new JSONObject();
        JSONObject old_object = new JSONObject();
        

        if(timestamp == ""){
            timestamp = createTimeStamp();
        }else{
            old_object.put("title", stringConverter(oldVal[0]));
            old_object.put("note",stringConverter(oldVal[1]));
            old_object.put("timestamp", timestamp);

            int occ = jsonArray.indexOf(old_object);
            if(occ != -1){
                jsonArray.remove(occ);
            }
        }

        new_object.put("title", stringConverter(title));
        new_object.put("note",stringConverter(note));
        new_object.put("timestamp", createTimeStamp());

        old_object.clear();

        return new_object;
    }

    private static String stringConverter(String input){
        String converted = input
                        .replace("ä", "ae")
                        .replace("ö", "oe")
                        .replace("ü", "ue")
                        .replace("Ä", "Ae")
                        .replace("Ö", "Oe")
                        .replace("Ü", "Ue")
                        .replace("ß", "ss");

        String safeCharacters = "0123456789 abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ"
                            + "<>,;.:_#'\\+*~'`!\"$%&/()=?^@€|{\\[\\]}\\\\ \r\n\t-";
    
        String pattern = "[^" + safeCharacters + "]";
        converted = converted.replaceAll(pattern, "?%uc!");                  
        return converted;
    }

    private static String createTimeStamp(){
        LocalDateTime now = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy-HH:mm:ss");
            String timestamp= now.format(formatter);
            return timestamp;
    }   
}
