import java.util.Map;
import java.util.Scanner;

public class Game {
    private Map<String, Gear> rooms;
    private Player player;
    private CommandParser commandParser;
    private int numEnemies;
    private int numAllies;
    private int choice;

    public Game(int numEnemies, int numAllies, int choice, Scanner scanner) {
        this.numEnemies = numEnemies;
        this.numAllies = numAllies;
        this.choice = choice;

        RoomLoader loader = new RoomLoader();
        rooms = loader.loadRooms();

        player = new Player("Player", 100, 100, 100);
        player.setCurrentRoomId("room1");

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
