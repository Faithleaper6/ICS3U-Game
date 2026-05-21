import java.util.ArrayList;
import java.util.HashMap;

public class Room {
    private String id;
    private String name;
    private String description;
    private HashMap<String, String> exits; // direction -> room id
    private ArrayList<RoomItem> items;
    private String type; // "trench", "open", "enemy", "bunker", "fortified", "safe"
    private double enemyChance; // chance of enemy encounter when entering
    private boolean searched; // has this room been searched already?

    public Room(String id, String name, String description, String type, double enemyChance) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.type = type;
        this.enemyChance = enemyChance;
        this.exits = new HashMap<>();
        this.items = new ArrayList<>();
        this.searched = false;
    }

    public void addExit(String direction, String roomId) {
        exits.put(direction, roomId);
    }

    public void addItem(RoomItem item) {
        items.add(item);
    }

    public void removeItem(String itemId) {
        items.removeIf(item -> item.getId().equals(itemId));
    }

    public RoomItem findItem(String itemId) {
        for (RoomItem item : items) {
            if (item.getId().equalsIgnoreCase(itemId) || item.getName().equalsIgnoreCase(itemId)) {
                return item;
            }
        }
        return null;
    }

    public void printRoom() {
        System.out.println("\n========================================");
        System.out.println("  " + name.toUpperCase());
        System.out.println("========================================");
        System.out.println(description);

        if (!items.isEmpty() && !searched) {
            System.out.println("\nYou notice:");
            for (RoomItem item : items) {
                System.out.println("  - " + item.getName() + ": " + item.getDescription());
            }
        } else if (searched) {
            System.out.println("\n[Already searched this location]");
        }

        System.out.print("\nExits:");
        for (String dir : exits.keySet()) {
            System.out.print(" [" + dir + "]");
        }
        System.out.println();
    }

    /**
     * Returns the cover modifier for this room type.
     * Fortified/bunker rooms give better cover.
     */
    public double getCoverMod() {
        switch (type) {
            case "fortified":
                return 0.40; // great cover
            case "bunker":
                return 0.50;
            case "trench":
                return 0.60;
            case "enemy":
                return 0.70;
            case "open":
                return 1.0; // no cover!
            case "safe":
                return 0.30;
            default:
                return 0.70;
        }
    }

    /**
     * Returns an accuracy bonus for this room type.
     * Sniper posts and MG nests give better accuracy.
     */
    public double getAccuracyBonus() {
        switch (type) {
            case "fortified":
                return 0.10; // good vantage
            case "open":
                return -0.10; // hard to aim while exposed
            default:
                return 0.0;
        }
    }

    // Getters
    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public HashMap<String, String> getExits() {
        return exits;
    }

    public ArrayList<RoomItem> getItems() {
        return items;
    }

    public String getType() {
        return type;
    }

    public double getEnemyChance() {
        return enemyChance;
    }

    public boolean isSearched() {
        return searched;
    }

    public void setSearched(boolean s) {
        searched = s;
    }

    public String getExit(String direction) {
        return exits.get(direction.toLowerCase());
    }
}
