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
        RoomLoader roomLoader = new RoomLoader();
        rooms = roomLoader.loadRooms("rooms.json");
        player = new Player("entrance");
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
