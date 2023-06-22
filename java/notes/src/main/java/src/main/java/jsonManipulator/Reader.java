package src.main.java.jsonManipulator;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Reader {
    private String filename;

    public Reader(String filename) {
        this.filename = filename;
    }

    public String[] getAllElements() {
        ObjectMapper mapper = new ObjectMapper();
        List<String> elements = new ArrayList<>();
        elements.add("");   //To avoid that the whitespace acts like the last element
        //Load personal Notes
        try {
            // Read JSON file
            File jsonFile = new File(filename);
            JsonNode jsonNode = mapper.readTree(jsonFile);

            for (JsonNode item : jsonNode) {
                String note = item.get("note").asText();
                String title = item.get("title").asText();
                String timestamp = item.get("timestamp").asText();

                String shortNote = getShortText(note, 25);
                String shortTitle = getShortText(title, 10);

                if (note.contains("<!DOCTYPE html>") && !note.startsWith("###Enable JNotes++###")) {
                    shortNote = "JNotes++ disabled";
                }

                String formattedElement = String.format("<html> %s -- %s <br> <br> %s </html> <font color = 'rgba(0,0,0,0)'> <--Meta--> <--Title--> %s <--Timestamp--> %s <--Note--> %s",
                        shortTitle, timestamp, shortNote, title, timestamp, note);

                elements.add(formattedElement);
            }
            elements.add("<html> <br> + Add new note <br> <br>");
            Collections.reverse(elements);
            

        } catch (IOException e) {
            e.printStackTrace();
        }

        return elements.toArray(new String[0]);
    }

    private String getShortText(String text, int maxLength) {
        if (text.length() <= maxLength) {
            return text;
        } else {
            return text.substring(0, maxLength) + "...";
        }
    }
}
