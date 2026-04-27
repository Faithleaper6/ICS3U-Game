import java.util.Map;
import java.util.Scanner;
import java.util.HashMap;
import java.util.ArrayList;

public class Game {
    private Map<String, Room> rooms;
    private Player player;
    private CommandParser commandParser;
    private int numEnemies;
    private int numAllies;
    private int choice;
    private String playerName;

    public Game(int numEnemies, int numAllies, int choice, String name) {
        this.numEnemies = numEnemies;
        this.numAllies = numAllies;
        this.choice = choice;
        this.playerName = name;
        this.rooms = RoomLoader.loadRooms("rooms.json");

        player = new Player();

        commandParser = new CommandParser();
    }

    public void run() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Welcome to the BattleField!");
        System.out.println("You are " + playerName + ", a soldier deployed behind enemy lines.");
        System.out.println("Your mission: eliminate all enemy forces and survive.");
        System.out.println("Type 'help' for a list of commands.\n");

        while (true) {

            System.out.print("> ");
            String input = scanner.nextLine();
            commandParser.parse(input, player, rooms);
        }
    }

    private void printHelp() {
        System.out.println("\n--- COMMANDS ---");
        System.out.println("  look     - Check battlefield conditions before acting");
        System.out.println("  shoot    - Peek out and fire a single shot at an enemy");
        System.out.println("  magdump  - [Thompson SMG only] Full-auto mag dump!");
        System.out.println("  grenade  - Throw a grenade (hits 1-3 enemies, 40-70 dmg)");
        System.out.println("  bunker   - Sprint to a bunker for supplies");
        System.out.println("  eat      - Eat food (+35 hunger)");
        System.out.println("  drink    - Drink water (+30 energy)");
        System.out.println("  medkit   - Use a medkit (+40 HP)");
        System.out.println("  reload   - Reload current weapon from reserve mags");
        System.out.println("  switch   - Switch between pistol and rifle");
        System.out.println("  status   - View all stats, gear, weapons, items");
        System.out.println("  level    - Check your current level and bonuses");
        System.out.println("  advance  - Fast-forward to next time period");
        System.out.println();
        System.out.println("  !! WARNING: The battlefield is NEVER safe. Even");
        System.out.println("     checking help has a chance of trench danger.");
        System.out.println("----------------");
    }

    public void movePlayer(String direction) {
        // Expand shorthand
        if (direction.equals("n"))
            direction = "north";
        if (direction.equals("s"))
            direction = "south";
        if (direction.equals("e"))
            direction = "east";
        if (direction.equals("w"))
            direction = "west";

        Room currentRoom = rooms.get(player.getCurrentRoomId());

        if (currentRoom.getExits().containsKey(direction)) {
            String nextRoomId = currentRoom.getExits().get(direction);
            player.setCurrentRoomId(nextRoomId);
            Room nextRoom = rooms.get(nextRoomId);
            nextRoom.printRoom();
        } else {
            System.out.println("You can't go that way.");

        }
        player.setCurrentRoomId(nextRoomId);
        player.drainEnergy(2);
        player.drainHunger(1);
    }

}
