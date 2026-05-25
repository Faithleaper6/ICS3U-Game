import java.util.ArrayList;
import java.util.Map;
import java.util.Scanner;

public class Game {
    private ArrayList<Enemy> enemies;
    private ArrayList<Boolean> allies;
    private Map<String, Room> rooms;
    private Player player;
    private TimeManager timeManager;
    private int numEnemies;
    private int numAllies;
    private int choice;
    private String playerName;
    private int activeEnemies = 0;

    public Game(int numEnemies, int numAllies, int choice, String name, int activeEnemies) {
        this.numEnemies = numEnemies;
        this.numAllies = numAllies;
        this.choice = choice;
        this.playerName = name;
        this.activeEnemies = activeEnemies;
        this.rooms = RoomLoader.loadRooms("rooms.json");

        this.player = new Player(playerName, 100, 100, 100);
        this.enemies = new ArrayList<>();
        this.allies = new ArrayList<>();
        this.timeManager = new TimeManager(choice);

        createForces();
    }

    public void run() {
        try (Scanner scanner = new Scanner(System.in)) {
            System.out.println("Welcome to the BattleField!");
            System.out.println("You are " + playerName + ", a soldier deployed behind enemy lines.");
            System.out.println("Your mission: eliminate all enemy forces and survive.");
            System.out.println("Type 'help' for a list of commands.\n");

            Room startingRoom = rooms.get(player.getCurrentRoomId());
            if (startingRoom != null) {
                startingRoom.printRoom();
            }

            while (player.isAlive() && countAliveEnemies() > 0) {
                System.out.print("> ");
                String input = scanner.nextLine();
                if (trenchDanger()) {
                    if (!player.isAlive()) {
                        continue;
                    }
                }
                CommandParser.parse(input, player, rooms, timeManager, this);
            }

            if (!player.isAlive()) {
                System.out.println("\nYou collapse in the mud. Mission failed.");
            } else {
                System.out.println("\nThe last enemy falls. Mission accomplished!");
            }
        }
    }

    private void createForces() {
        for (int i = 0; i < numEnemies; i++) {
            enemies.add(createRandomEnemy());
        }

        for (int i = 0; i < numAllies; i++) {
            allies.add(true);
        }
    }

    private Enemy createRandomEnemy() {
        double roll = Math.random();
        if (roll < 0.45) {
            return Enemy.createRifleman();
        }
        if (roll < 0.70) {
            return Enemy.createOfficer();
        }
        if (roll < 0.90) {
            return Enemy.createMachineGunner();
        }
        return Enemy.createSniper();
    }

    public void movePlayer(String direction) {
        if (direction.equals("n")) {
            direction = "north";
        }
        if (direction.equals("s")) {
            direction = "south";
        }
        if (direction.equals("e")) {
            direction = "east";
        }
        if (direction.equals("w")) {
            direction = "west";
        }

        Room currentRoom = rooms.get(player.getCurrentRoomId());
        if (currentRoom == null) {
            System.out.println("You are lost. The map could not find your current room.");
            return;
        }

        if (!currentRoom.getExits().containsKey(direction)) {
            System.out.println("You can't go that way.");
            return;
        }

        String nextRoomId = currentRoom.getExits().get(direction);
        player.setCurrentRoomId(nextRoomId);
        Room nextRoom = rooms.get(nextRoomId);
        nextRoom.printRoom();

        player.drainEnergy(2);
        player.drainHunger(1);

        if (Math.random() < nextRoom.getEnemyChance() && countAliveEnemies() > 0) {
            System.out.println("\n!! ENEMY CONTACT! You've been spotted!");
            incomingFire(nextRoom);
        }
    }

    public String getCoverDesc(Room room) {
        double cover = room.getCoverMod();
        if (cover <= 0.35) {
            return "excellent";
        }
        if (cover <= 0.55) {
            return "good";
        }
        if (cover <= 0.75) {
            return "fair";
        }
        return "poor";
    }

    public void printBattlefield() {
        System.out.println("\n--- BATTLEFIELD ---");
        System.out.println("Time: Day " + timeManager.getDayCount() + ", " + timeManager.getCurrentTime());
        System.out.println("Enemies alive: " + countAliveEnemies() + "/" + numEnemies);
        System.out.println("Allies alive: " + countAliveAllies() + "/" + numAllies);
    }

    public void searchCurrentRoom() {
        Room currentRoom = rooms.get(player.getCurrentRoomId());
        if (currentRoom == null) {
            System.out.println("There is nowhere useful to search.");
            return;
        }
        boolean inBunker = currentRoom.getType().equals("bunker");

        if (currentRoom.isSearched()) {
            System.out.println("You already searched this area.");
            if (!inBunker) {
                warRages(false);
            }
            return;
        }

        currentRoom.setSearched(true);
        player.drainEnergy(2);
        player.drainHunger(1);

        if (inBunker) {
            Bunker.search(player);
        } else if (!currentRoom.getItems().isEmpty()) {
            System.out.println("\nYou search the area and find:");
            for (RoomItem item : currentRoom.getItems()) {
                System.out.println("  - " + item.getName() + ": " + item.getDescription());
            }
        } else {
            System.out.println("\nYou search carefully, but find nothing useful.");
        }

        if (!inBunker) {
            warRages(false);
        }
    }

    public void shootEnemy() {
        Room currentRoom = rooms.get(player.getCurrentRoomId());

        // Can't shoot from bunker
        if (currentRoom.getType().equals("bunker")) {
            System.out.println("You can't shoot from inside the bunker! Go back to the trench first.");
            return;
        }

        // Must have active enemies
        if (activeEnemies <= 0) {
            System.out.println("No enemies in sight right now. Advance time or move to spot targets.");
            return;
        }

        Enemy target = getFirstAliveEnemy();
        if (target == null) {
            System.out.println("There are no enemies left to shoot.");
            return;
        }

        Weapon weapon = player.getCurrentWeapon();
        if (!weapon.fireRound()) {
            return;
        }

        double conditionPenalty = player.getConditionAccuracyPenalty();
        double chance = weapon.getHitChance() + player.getAccuracyBonus() - conditionPenalty;
        if (currentRoom != null) {
            chance += currentRoom.getAccuracyBonus();
        }
        chance = Math.min(0.95, Math.max(0.05, chance));

        System.out.println("You fire your " + weapon.getName() + " at a " + target.getType() + "...");
        if (conditionPenalty > 0) {
            System.out.println("Your hunger and exhaustion make it harder to aim.");
        }
        if (Math.random() > chance) {
            System.out.println("You missed!");
            warRages(false);
            return;
        }

        boolean headshot = Math.random() < weapon.getHeadshotChance();
        int damage = headshot ? weapon.getHeadshotDamage() : weapon.getDamage();
        target.takeDamage(damage);
        System.out.println((headshot ? "HEADSHOT! " : "Hit! ") + "Dealt " + damage + " damage.");

        if (!target.isAlive()) {
            System.out.println("Enemy " + target.getType() + " eliminated!");
            player.addKill();
            activeEnemies--;
            if (activeEnemies < 0) {
                activeEnemies = 0;
            }
        }

        warRages(false);
    }

    public void magDump() {
        Room currentRoom = rooms.get(player.getCurrentRoomId());
        if (currentRoom.getType().equals("bunker")) {
            System.out.println("You can't shoot from inside the bunker! Go back to the trench first.");
            return;
        }

        if (activeEnemies <= 0) {
            System.out.println("No enemies in sight right now. Advance time or move to spot targets.");
            return;
        }

        Weapon weapon = player.getCurrentWeapon();
        if (!weapon.isFullAuto()) {
            System.out.println("Only the Thompson SMG can magdump.");
            return;
        }

        if (!weapon.hasAmmo()) {
            System.out.println(weapon.getName() + " is empty. Reload first!");
            return;
        }

        int rounds = weapon.fireBurst(Math.min(8, weapon.getCurrentAmmo()));
        System.out.println("You dump " + rounds + " rounds from the " + weapon.getName() + "!");
        double conditionPenalty = player.getConditionAccuracyPenalty();
        if (conditionPenalty > 0) {
            System.out.println("Your hunger and exhaustion make it harder to control the weapon.");
        }

        for (int i = 0; i < rounds && countAliveEnemies() > 0; i++) {
            Enemy target = getRandomAliveEnemy();
            double chance = Math.min(0.90, weapon.getHitChance() + player.getAccuracyBonus() - conditionPenalty);
            chance = Math.max(0.05, chance);
            if (Math.random() < chance) {
                target.takeDamage(weapon.getDamage());
                System.out.println("  Round hits " + target.getType() + " for " + weapon.getDamage() + " damage.");
                if (!target.isAlive()) {
                    System.out.println("  Enemy " + target.getType() + " eliminated!");
                    player.addKill();
                    activeEnemies--;
                    if (activeEnemies < 0) {
                        activeEnemies = 0;
                    }
                }
            }
        }

        warRages(false);
    }

    public void throwGrenade() {
        Room room = rooms.get(player.getCurrentRoomId());

        // Throwing in bunker hits YOU
        if (room.getType().equals("bunker")) {
            if (!player.useGrenade()) {
                return;
            }
            System.out.println("\nYou throw a grenade... inside the bunker?!");
            System.out.println("!! The blast has nowhere to go in the enclosed space!");
            player.kill();
            return;
        }

        // Must have enemies in sight
        if (activeEnemies <= 0) {
            System.out.println("No enemies in sight to throw a grenade at!");
            return;
        }

        if (!player.useGrenade()) {
            return;
        }

        // 30% chance it misses the trench
        if (Math.random() < 0.30) {
            System.out.println("You throw a grenade but it sails over the enemy trench!");
            System.out.println("The grenade explodes harmlessly. Wasted!");
            warRages(false);
            return;
        }

        if (countAliveEnemies() == 0) {
            System.out.println("You throw a grenade, but there are no enemies left.");
            return;
        }

        int targets = Math.min(countAliveEnemies(), 1 + (int) (Math.random() * 3));
        System.out.println("You throw a grenade into the enemy line!");

        for (int i = 0; i < targets; i++) {
            Enemy target = getRandomAliveEnemy();
            int damage = 40 + (int) (Math.random() * 31);
            target.takeDamage(damage);
            System.out.println("  Blast hits " + target.getType() + " for " + damage + " damage.");
            if (!target.isAlive()) {
                System.out.println("  Enemy " + target.getType() + " eliminated!");
                player.addKill();
                activeEnemies--;
                if (activeEnemies < 0) {
                    activeEnemies = 0;
                }
            }
        }

        warRages(false);
    }

    public void restockBunkers() {
        for (Room room : rooms.values()) {
            if (room.getType().equals("bunker")) {
                room.setSearched(false);
            }
        }
        System.out.println("Bunkers may have fresh supplies after the battlefield shifts.");
    }

    public void warRages(boolean majorEvent) {
        if (!player.isAlive() || countAliveEnemies() == 0) {
            return;
        }

        Room currentRoom = rooms.get(player.getCurrentRoomId());
        boolean inBunker = currentRoom != null && currentRoom.getType().equals("bunker");

        double incomingChance = majorEvent ? 0.35 : 0.18;
        incomingChance += choice * 0.03;
        if (currentRoom != null) {
            incomingChance += currentRoom.getEnemyChance() * 0.25;
        }


        if (inBunker) {
            incomingChance *= 0.1;
        }

        if (Math.random() < incomingChance) {
            if (inBunker) {
                System.out.println("\nA bullet ricochets near the bunker entrance!");
            } else {
                System.out.println("\nThe battlefield erupts around you!");
            }
            incomingFire(currentRoom);
            return;
        }

        resolveDistantFighting(majorEvent);
    }

    public int countAliveEnemies() {
        int count = 0;
        for (Enemy enemy : enemies) {
            if (enemy.isAlive()) {
                count++;
            }
        }
        return count;
    }

    public void incomingFire(Room room) {
        if (countAliveEnemies() == 0) {
            return;
        }

        if (room == null) {
            room = rooms.get(player.getCurrentRoomId());
        }

        int attackers = Math.min(countAliveEnemies(), 1 + (int) (Math.random() * 3));
        double coverMod = room == null ? 0.75 : room.getCoverMod();

        for (int i = 0; i < attackers && player.isAlive(); i++) {
            Enemy enemy = getRandomAliveEnemy();
            ShotResult result = enemy.attack();
            System.out.println(result.getMessage());

            if (result.isHit()) {
                int coveredDamage = Math.max(1, (int) (result.getDamage() * coverMod));
                player.takeDamage(coveredDamage, result.isHeadshot());
            }
        }
    }

    private int countAliveAllies() {
        int count = 0;
        for (Boolean allyAlive : allies) {
            if (allyAlive) {
                count++;
            }
        }
        return count;
    }

    private Enemy getFirstAliveEnemy() {
        for (Enemy enemy : enemies) {
            if (enemy.isAlive()) {
                return enemy;
            }
        }
        return null;
    }

    private Enemy getRandomAliveEnemy() {
        int alive = countAliveEnemies();
        if (alive == 0) {
            return null;
        }

        int targetIndex = (int) (Math.random() * alive);
        for (Enemy enemy : enemies) {
            if (enemy.isAlive()) {
                if (targetIndex == 0) {
                    return enemy;
                }
                targetIndex--;
            }
        }
        return getFirstAliveEnemy();
    }

    private void resolveDistantFighting(boolean majorEvent) {
        double chance = majorEvent ? 0.45 : 0.18;
        if (Math.random() > chance) {
            return;
        }

        if (countAliveEnemies() > 0 && Math.random() < 0.55) {
            Enemy enemy = getRandomAliveEnemy();
            enemy.takeDamage(enemy.getHealth());
            System.out.println("\nYour squad drops an enemy " + enemy.getType() + " elsewhere on the line.");
        } else {
            markRandomAllyDead();
        }
    }

    private void markRandomAllyDead() {
        if (countAliveAllies() == 0) {
            return;
        }

        int targetIndex = (int) (Math.random() * countAliveAllies());
        for (int i = 0; i < allies.size(); i++) {
            if (allies.get(i)) {
                if (targetIndex == 0) {
                    allies.set(i, false);
                    System.out.println("\nYou hear that one of your allies has fallen.");
                    return;
                }
                targetIndex--;
            }
        }
    }

    public void sleep() {
        Room room = rooms.get(player.getCurrentRoomId());
        if (!room.getType().equals("bunker")) {
            System.out.println("You can't sleep in the open! Find a bunker first.");
            return;
        }

        System.out.println("\nYou lean against the bunker wall and close your eyes...");
        System.out.println("Zzzzz...\n");

        int restored = 30 + (int) (Math.random() * 21);
        player.drainEnergy(-restored);
        System.out.println("Energy restored! +" + restored + " energy.");

        // Explosion chance scales with difficulty
        double explosionChance = choice == 1 ? 0.20 : choice == 2 ? 0.35 : 0.50;
        if (Math.random() < explosionChance) {
            System.out.println("\n!! Jolted awake by an explosion!");
            int dmg = 10 + (int) (Math.random() * 15);
            player.takeDamage(dmg, false);
        }

        // Enemy sneak chance scales
        double sneakChance = choice == 1 ? 0.10 : choice == 2 ? 0.20 : 0.35;
        if (Math.random() < sneakChance && countAliveEnemies() > 0) {
            System.out.println("!! An enemy found your bunker while you slept!");
            incomingFire(room);
        }

        player.drainHunger(10);
        System.out.println("You feel rested but hungry. (-10 hunger)");
    }

    public void spawnEnemies() {
        if (countAliveEnemies() == 0) {
            return;
        }
        int newEnemies = 2 + (int) (Math.random() * 4);
        newEnemies = Math.min(newEnemies, countAliveEnemies());
        activeEnemies += newEnemies;
        activeEnemies = Math.min(activeEnemies, countAliveEnemies());
        System.out.println(">> " + newEnemies + " enemy soldiers spotted! (" + activeEnemies + " in sight)");
    }

    private boolean trenchDanger() {
        Room room = rooms.get(player.getCurrentRoomId());
        boolean inBunker = room != null && room.getType().equals("bunker");

        // Base chance: Easy=0.5%, Medium=0.8%, Hard=1.0%
        double dangerChance;
        if (choice == 1) {
            dangerChance = 0.005;
        } else if (choice == 2) {
            dangerChance = 0.008;
        } else {
            dangerChance = 0.010;
        }

        // Much safer in bunker
        if (inBunker) {
            dangerChance *= 0.15;
        }

        double roll = Math.random();

        if (roll < dangerChance * 0.50) {
            int dmg = 20 + (int) (Math.random() * 25);
            System.out.println("\n!! A GRENADE lands near you!");
            player.takeDamage(dmg, false);
            return true;
        } else if (roll < dangerChance * 0.80) {
            int dmg = 15 + (int) (Math.random() * 20);
            System.out.println("\n!! MORTAR STRIKE near your position!");
            player.takeDamage(dmg, false);
            return true;
        } else if (roll < dangerChance) {
            if (Math.random() < 0.5) {
                System.out.println("\n!! A sniper round cracks past your head!");
            } else {
                int dmg = 8 + (int) (Math.random() * 12);
                System.out.println("\n!! A sniper round clips you!");
                player.takeDamage(dmg, false);
            }
            return true;
        }
        return false;
    }
}
