import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;

public class RoomLoader {

    public static HashMap<String, Room> loadRooms(String filepath) {
        HashMap<String, Room> rooms = new HashMap<>();

        try {
            String json = readFile(filepath);
            // Remove outer braces
            json = json.trim();
            if (json.startsWith("{"))
                json = json.substring(1);
            if (json.endsWith("}"))
                json = json.substring(0, json.length() - 1);

            int pos = 0;
            while (pos < json.length()) {
                int keyStart = json.indexOf("\"", pos);
                if (keyStart == -1)
                    break;
                int keyEnd = json.indexOf("\"", keyStart + 1);
                String roomId = json.substring(keyStart + 1, keyEnd);

                int objStart = json.indexOf("{", keyEnd);
                int objEnd = findMatchingBrace(json, objStart);
                String roomJson = json.substring(objStart, objEnd + 1);

                Room room = parseRoom(roomId, roomJson);
                if (room != null) {
                    rooms.put(roomId, room);
                }

                pos = objEnd + 1;
            }

        } catch (IOException e) {
            System.out.println("Error loading rooms.json: " + e.getMessage());
            System.out.println("Make sure rooms.json is in the same folder as your .class files.");
        }

        return rooms;
    }

    private static Room parseRoom(String id, String json) {
        String name = extractString(json, "name");
        String description = extractString(json, "description");
        String type = extractString(json, "type");
        double enemyChance = extractDouble(json, "enemyChance");

        Room room = new Room(id, name, description, type, enemyChance);

        int exitsStart = json.indexOf("\"exits\"");
        if (exitsStart != -1) {
            int braceStart = json.indexOf("{", exitsStart);
            int braceEnd = findMatchingBrace(json, braceStart);
            String exitsJson = json.substring(braceStart + 1, braceEnd);

            int ePos = 0;
            while (ePos < exitsJson.length()) {
                int dStart = exitsJson.indexOf("\"", ePos);
                if (dStart == -1)
                    break;
                int dEnd = exitsJson.indexOf("\"", dStart + 1);
                String direction = exitsJson.substring(dStart + 1, dEnd);

                int vStart = exitsJson.indexOf("\"", dEnd + 1);
                int vEnd = exitsJson.indexOf("\"", vStart + 1);
                String target = exitsJson.substring(vStart + 1, vEnd);

                room.addExit(direction, target);
                ePos = vEnd + 1;
            }
        }

        int itemsStart = json.indexOf("\"items\"");
        if (itemsStart != -1) {
            int brackStart = json.indexOf("[", itemsStart);
            int brackEnd = findMatchingBracket(json, brackStart);
            String itemsJson = json.substring(brackStart + 1, brackEnd);

            int iPos = 0;
            while (iPos < itemsJson.length()) {
                int iObjStart = itemsJson.indexOf("{", iPos);
                if (iObjStart == -1)
                    break;
                int iObjEnd = findMatchingBrace(itemsJson, iObjStart);
                String itemJson = itemsJson.substring(iObjStart, iObjEnd + 1);

                String itemId = extractString(itemJson, "id");
                String itemName = extractString(itemJson, "name");
                String itemDesc = extractString(itemJson, "description");

                if (itemId != null && itemName != null) {
                    room.addItem(new RoomItem(itemId, itemName, itemDesc));
                }

                iPos = iObjEnd + 1;
            }
        }

        return room;
    }

    private static String extractString(String json, String key) {
        String search = "\"" + key + "\"";
        int idx = json.indexOf(search);
        if (idx == -1)
            return null;
        // Find the colon, then the opening quote of the value
        int colonIdx = json.indexOf(":", idx + search.length());
        int valStart = json.indexOf("\"", colonIdx);
        int valEnd = json.indexOf("\"", valStart + 1);
        // Handle escaped quotes
        while (valEnd > 0 && json.charAt(valEnd - 1) == '\\') {
            valEnd = json.indexOf("\"", valEnd + 1);
        }
        return json.substring(valStart + 1, valEnd);
    }

    private static double extractDouble(String json, String key) {
        String search = "\"" + key + "\"";
        int idx = json.indexOf(search);
        if (idx == -1)
            return 0.0;
        int colonIdx = json.indexOf(":", idx + search.length());
        // Read the number after the colon
        int numStart = colonIdx + 1;
        while (numStart < json.length()
                && (json.charAt(numStart) == ' ' || json.charAt(numStart) == '\n' || json.charAt(numStart) == '\r')) {
            numStart++;
        }
        int numEnd = numStart;
        while (numEnd < json.length() && (Character.isDigit(json.charAt(numEnd)) || json.charAt(numEnd) == '.')) {
            numEnd++;
        }
        try {
            return Double.parseDouble(json.substring(numStart, numEnd));
        } catch (NumberFormatException e) {
            return 0.0;
        }
    }

    private static int findMatchingBrace(String s, int openPos) {
        int depth = 0;
        for (int i = openPos; i < s.length(); i++) {
            if (s.charAt(i) == '{')
                depth++;
            else if (s.charAt(i) == '}') {
                depth--;
                if (depth == 0)
                    return i;
            }
        }
        return s.length() - 1;
    }

    private static int findMatchingBracket(String s, int openPos) {
        int depth = 0;
        for (int i = openPos; i < s.length(); i++) {
            if (s.charAt(i) == '[')
                depth++;
            else if (s.charAt(i) == ']') {
                depth--;
                if (depth == 0)
                    return i;
            }
        }
        return s.length() - 1;
    }

    private static String readFile(String filepath) throws IOException {
        StringBuilder sb = new StringBuilder();
        BufferedReader reader = new BufferedReader(new FileReader(filepath));
        String line;
        while ((line = reader.readLine()) != null) {
            sb.append(line).append("\n");
        }
        reader.close();
        return sb.toString();
    }
}

/*
 * import java.io.FileReader;
 * import java.util.*;
 * import com.google.gson.*;
 * 
 * public class RoomLoader {
 * public static HashMap<String, Room> loadRooms(String filePath) {
 * Map<String, Gear> rooms = new HashMap<>();
 * try {
 * Gson gson = new Gson();
 * JsonObject jsonObject = gson.fromJson(new FileReader(filePath),
 * JsonObject.class);
 * 
 * for (Map.Entry<String, JsonElement> entry : jsonObject.entrySet()) {
 * String roomId = entry.getKey();
 * JsonObject roomData = entry.getValue().getAsJsonObject();
 * 
 * String name = roomData.get("name").getAsString();
 * String description = roomData.get("description").getAsString();
 * 
 * Map<String, String> exits = new HashMap<>();
 * JsonObject exitsJson = roomData.getAsJsonObject("exits");
 * for (Map.Entry<String, JsonElement> exit : exitsJson.entrySet()) {
 * exits.put(exit.getKey(), exit.getValue().getAsString());
 * }
 * 
 * List<Weapon> items = new ArrayList<>();
 * JsonArray itemsJson = roomData.getAsJsonArray("items");
 * for (JsonElement itemElement : itemsJson) {
 * JsonObject itemObj = itemElement.getAsJsonObject();
 * String itemId = itemObj.get("id").getAsString();
 * String itemName = itemObj.get("name").getAsString();
 * String itemDescription = itemObj.get("description").getAsString();
 * items.add(new Weapon(itemId, itemName, itemDescription));
 * }
 * 
 * Gear room = new Gear(roomId, name, description);
 * rooms.put(roomId, room);
 * }
 * } catch (Exception e) {
 * e.printStackTrace();
 * }
 * return rooms;
 * }
 * }
 * /*
 */
