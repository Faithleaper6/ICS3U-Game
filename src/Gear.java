import java.util.List;
import java.util.Map;

public class Gear {
    private String protection;
    private String name;
    private String description;
    private Map<String, String> exits; // direction → roomId
    private List<Weapon> items;

    public Gear(int protection, String name, String description) {
        this.protection = String.valueOf(protection);
        this.name = name;
        this.description = description;
        this.exits = exits;
        this.items = items;
    }

    public String getProtection() {
        return protection;
    }

    public String getName() {
        return name;
    }

    public String getShortDescription() {
        return description;
    }

    public String getLongDescription() {
        StringBuilder sb = new StringBuilder();
        sb.append(name).append("\n");
        sb.append(description).append("\n");

        if (!items.isEmpty()) {
            sb.append("You see: ");
            for (Weapon item : items) {
                sb.append(item.getName()).append(", ");
            }
            // Remove trailing comma and space
            sb.setLength(sb.length() - 2);
            sb.append(".\n");
        }

        if (!exits.isEmpty()) {
            sb.append("Exits: ");
            for (String direction : exits.keySet()) {
                sb.append(direction).append(", ");
            }
            sb.setLength(sb.length() - 2);
            sb.append(".\n");
        }

        return sb.toString();
    }

    public static Gear createHelmet() {
        Gear helmet = new Gear(50, "helmet",
                "A standard issue M1 steel helmet, providing basic protection against shrapnel and debris. It has seen better days but can still save your life if you get hit in the head.");
        return helmet;

    }

    public Gear createHelmet() {
        return Gear.createHelmet();
    }
}
