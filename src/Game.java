import java.util.Map;
import java.util.Scanner;

public class Game {
    private Map<String, Gear> rooms;
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

        player = new Player(playerName, 100, 100, 100);

        commandParser = new CommandParser();
    }

    public void run() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Welcome to the Text Adventure Game!");
        Gear currentRoom = rooms.get(player.getCurrentRoomId());
        System.out.println(currentRoom.getLongDescription());

        while (true) {

            System.out.print("> ");
            String input = scanner.nextLine();
            commandParser.parse(input, player, rooms);
        }
    }
}
