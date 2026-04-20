import java.util.Map;

public class CommandParser {
    public void parse(String input, Player player, Map<String, Gear> game) {
        String[] words = input.trim().toLowerCase().split("\\s+");
        if (words.length == 0) {
            System.out.println("Please enter a command.");
            return;
        }

        String command = words[0];

        switch (command) {
            case "go":
                if (words.length < 2) {
                    System.out.println("Go where?");
                } else {
                    String direction = words[1];
                    Gear currentRoom = rooms.get(player.getCurrentRoomId());
                    String nextRoomId = currentRoom.getExits().get(direction);
                    if (nextRoomId != null) {
                        player.setCurrentRoomId(nextRoomId);
                        System.out.println("You move " + direction + ".");
                        currentRoom = rooms.get(player.getCurrentRoomId());
                        System.out.println(currentRoom.getLongDescription());

                    } else {
                        System.out.println("You can't go that way.");
                    }
                }
                break;
            case "look":
                Gear currentRoom = rooms.get(player.getCurrentRoomId());
                System.out.println(currentRoom.getLongDescription());
                break;
            case "inventory":
                if (player.getInventory().isEmpty()) {
                    System.out.println("Your inventory is empty.");
                } else {
                    System.out.println("You are carrying:");
                    for (Weapon item : player.getInventory()) {
                        System.out.println("- " + item.getName());
                    }
                }
                break;
            case "take":
                if (words.length < 2) {
                    System.out.println("Take what?");
                } else {
                    String itemName = words[1];
                    Gear room = rooms.get(player.getCurrentRoomId());
                    Weapon itemToTake = null;
                    for (Weapon item : room.getItems()) {
                        if (item.getName().equalsIgnoreCase(itemName)) {
                            itemToTake = item;
                            break;
                        }
                    }
                    if (itemToTake != null) {
                        room.removeItem(itemToTake);
                        player.addItem(itemToTake);
                        System.out.println("You take the " + itemToTake.getName() + ".");
                    } else {
                        System.out.println("There is no " + itemName + " here.");
                    }
                }
                break;
            case "drop":
                if (words.length < 2) {
                    System.out.println("Drop what?");
                } else {
                    String itemName = words[1];
                    Weapon itemToDrop = null;
                    for (Weapon item : player.getInventory()) {
                        if (item.getName().equalsIgnoreCase(itemName)) {
                            itemToDrop = item;
                            break;
                        }
                    }
                    if (itemToDrop != null) {
                        player.removeItem(itemToDrop);
                        Gear room = rooms.get(player.getCurrentRoomId());
                        room.addItem(itemToDrop);
                        System.out.println("You drop the " + itemToDrop.getName() + ".");
                    } else {
                        System.out.println("You don't have a " + itemName + ".");
                    }
                }
            case "shoot":
                player.shootCurrentWeapon(); 
                break;

            case "magdump":
                if (player.getCurrentWeapon().getName().equalsIgnoreCase("Thompson SMG")) {
                    player.magDump();
                } else {
                    System.out.println("You need a Thompson SMG to do that!");
                }
                break;

            case "grenade":
                player.throwGrenade(); // Handle 1-3 enemies and 40-70 damage logic
                break;

            case "bunker":
                System.out.println("You sprint through the mud toward a bunker...");
                player.searchForSupplies(); 
                break;

            case "eat":
                player.consumeItem("Food", 35);

            case "drink":
                player.consumeItem("Water", 30); 
                break;

            case "medkit":
                player.useMedkit(40);
                break;

            case "reload":
                player.reloadWeapon();
                break;

            case "switch":
                player.switchWeapon();
                break;

            case "status":
                player.displayStatus();
                break;

            case "level":
                player.displayLevelStats();
                break;

            case "advance":
                game.advanceTime();
                break;

            case "help":
                printHelp();
                if (Math.random() < 0.05) { 
                    System.out.println("!! Incoming mortar! You took damage while reading!");
                    player.takeDamage(10);
                }
                break;
            

            default:
                System.out.println("I don't understand that command. Retype if available.");
                break;
        }
    }
}

