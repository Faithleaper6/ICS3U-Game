import java.io.FileReader;
import java.util.*;
import com.google.gson.*;

public class RoomLoader {
    public static HashMap<String, Room> loadRooms(String filePath) {
        Map<String, Gear> rooms = new HashMap<>();
        try {
            Gson gson = new Gson();
            JsonObject jsonObject = gson.fromJson(new FileReader(filePath), JsonObject.class);

            for (Map.Entry<String, JsonElement> entry : jsonObject.entrySet()) {
                String roomId = entry.getKey();
                JsonObject roomData = entry.getValue().getAsJsonObject();

                String name = roomData.get("name").getAsString();
                String description = roomData.get("description").getAsString();

                Map<String, String> exits = new HashMap<>();
                JsonObject exitsJson = roomData.getAsJsonObject("exits");
                for (Map.Entry<String, JsonElement> exit : exitsJson.entrySet()) {
                    exits.put(exit.getKey(), exit.getValue().getAsString());
                }

                List<Weapon> items = new ArrayList<>();
                JsonArray itemsJson = roomData.getAsJsonArray("items");
                for (JsonElement itemElement : itemsJson) {
                    JsonObject itemObj = itemElement.getAsJsonObject();
                    String itemId = itemObj.get("id").getAsString();
                    String itemName = itemObj.get("name").getAsString();
                    String itemDescription = itemObj.get("description").getAsString();
                    items.add(new Weapon(itemId, itemName, itemDescription));
                }

                Gear room = new Gear(roomId, name, description);
                rooms.put(roomId, room);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return rooms;
    }
}
