import java.util.ArrayList;
import java.util.HashMap;

public class CommandParser {

    public static void parse(String input, Player player, HashMap<String, Room> rooms,
            ArrayList<Enemy> enemies, ArrayList<Boolean> allies,
            TimeManager timeManager, Game game) {

        String[] words = input.trim().toLowerCase().split("\\s+");
        if (words.length == 0) {
            System.out.println("Please enter a command.");
            return;
        }

        String command = words[0];

        switch (command) {
            case "go":
                if (words.length < 2) {
                    System.out.println("Go where? (north, south, east, west)");
                } else {
                    game.movePlayer(words[1]);
                }
                break;

            case "north":
            case "south":
            case "east":
            case "west":
            case "n":
            case "s":
            case "e":
            case "w":
                game.movePlayer(command);
                break;

            case "look":
                Room currentRoom = rooms.get(player.getCurrentRoomId());
                currentRoom.printRoom();
                System.out.println("\nCover quality: " + game.getCoverDesc(currentRoom));
                game.printBattlefield();
                break;

            case "search":
            case "loot":
                game.searchCurrentRoom();
                break;

            case "shoot":
            case "fire":
            case "attack":
                game.shootEnemy();
                break;

            case "magdump":
            case "dump":
            case "fullauto":
                game.magDump();
                break;

            case "grenade":
            case "throw":
            case "nade":
                game.throwGrenade();
                break;

            case "eat":
                player.eat();
                game.warRages(false);
                break;

            case "drink":
                player.drink();
                game.warRages(false);
                break;

            case "medkit":
            case "heal":
                player.useMedkit();
                game.warRages(false);
                break;

            case "reload":
                player.getCurrentWeapon().reload();
                game.warRages(false);
                break;

            case "switch":
                player.switchWeapon();
                break;

            case "status":
            case "stats":
                player.printStatus();
                game.printBattlefield();
                break;

            case "level":
                System.out.println("\nLevel " + player.getLevel() + " | Kills: " + player.getKills());
                System.out.println("Accuracy bonus: +" + String.format("%.0f", player.getAccuracyBonus() * 100) + "%");
                System.out.println("Loot bonus: +" + String.format("%.0f", player.getLootBonus() * 100) + "%");
                break;

            case "advance":
            case "time":
            case "wait":
                timeManager.advanceTime(player, enemies, allies);
                game.restockBunkers();
                game.warRages(true);
                game.warRages(true);
                break;

            case "help":
                printHelp();
                break;

            default:
                System.out.println("Unknown command. Type 'help' for a list of commands.");
                break;
        }
    }

    private static void printHelp() {
        System.out.println("\n--- MOVEMENT ---");
        System.out.println("  north/south/east/west (or n/s/e/w) - Move to adjacent location");
        System.out.println("  go <direction>                      - Same as above");
        System.out.println("\n--- COMBAT ---");
        System.out.println("  look     - View current location, cover, and danger");
        System.out.println("  shoot    - Peek out and fire at an enemy");
        System.out.println("  magdump  - [Thompson SMG only] Full-auto spray!");
        System.out.println("  grenade  - Throw a grenade (40-70 dmg, 1-3 enemies)");
        System.out.println("\n--- SURVIVAL ---");
        System.out.println("  search   - Search current location for loot");
        System.out.println("  eat      - Eat food (+35 hunger)");
        System.out.println("  drink    - Drink water (+30 energy)");
        System.out.println("  medkit   - Use a medkit (+40 HP)");
        System.out.println("  reload   - Reload weapon from reserve mags");
        System.out.println("  switch   - Switch between pistol and rifle");
        System.out.println("\n--- INFO ---");
        System.out.println("  status   - View all stats and gear");
        System.out.println("  level    - Check level and bonuses");
        System.out.println("  advance  - Fast-forward to next time period");
        System.out.println("  help     - Show this list");
        System.out.println("\n  !! The battlefield is NEVER safe. Every action");
        System.out.println("     has a chance of trench danger.");
    }
}
