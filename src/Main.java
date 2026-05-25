import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("============================================");
        System.out.println("       BEHIND ENEMY LINES - WW2 SURVIVAL   ");
        System.out.println("============================================");
        System.out.println();
        System.out.println("You are a soldier deployed behind enemy lines.");
        System.out.println("Your mission: eliminate all enemy forces and survive.");
        System.out.println();
        System.out.println("Select Difficulty:");
        System.out.println("  1. Easy   (10 vs 10 | 0.5% trench danger | more good events)");
        System.out.println("  2. Medium (25 vs 25 | 0.8% trench danger | mixed events)");
        System.out.println("  3. Hard   (50 vs 50 | 1.0% trench danger | more bad events)");
        System.out.print("\nChoice: ");

        int choice = 0;
        while (choice < 1 || choice > 3) {
            if (scanner.hasNextInt()) {
                choice = scanner.nextInt();
                if (choice < 1 || choice > 3) {
                    System.out.print("Enter 1, 2, or 3: ");
                }
            } else {
                scanner.next();
                System.out.print("Enter 1, 2, or 3: ");
            }
        }
        scanner.nextLine();

        System.out.println("Pick a name for your soldier:");
        System.out.print("Name: ");
        String name = scanner.nextLine().trim();
        if (name.isEmpty()) {
            name = "Player";
        }
        int numEnemies, numAllies;
        String diffName;
        if (choice == 1) {
            numEnemies = 10;
            numAllies = 9;
            diffName = "Easy";
        } else if (choice == 2) {
            numEnemies = 25;
            numAllies = 24;
            diffName = "Medium";
        } else {
            numEnemies = 50;
            numAllies = 49;
            diffName = "Hard";
        }

        System.out.println("\n>> Difficulty: " + diffName);
        System.out.println(">> Enemies: " + numEnemies + " | Your squad: " + (numAllies + 1) + " (you + " + numAllies
                + " allies)");
        System.out.println("\nGear up, soldier. Move out!\n");

        Game game = new Game(numEnemies, numAllies, choice, name);
        game.run();

        scanner.close();
    }
}
