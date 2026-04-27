import java.util.*;

public class TimeManager {
    private String[] times = { "Morning", "Noon", "Afternoon", "Night" };
    private int currentTimeIndex;
    private int dayCount;
    private int difficultyLevel;

    public TimeManager(int difficultyLevel) {
        this.currentTimeIndex = 0; // Start at Morning
        this.dayCount = 1;
        this.difficultyLevel = difficultyLevel;
    }

    public String getCurrentTime() {
        return times[currentTimeIndex];
    }

    public int getDayCount() {
        return dayCount;
    }

    public void advanceTime(Player player, ArrayList<Enemy> enemies, ArrayList<Boolean> allies) {
        currentTimeIndex = (currentTimeIndex + 1) % times.length;
        if (currentTimeIndex == 0) {
            dayCount++;
            System.out.println("\n--- Day " + dayCount + " ---");
        }
        System.out.println("\nTime advances to: " + getCurrentTime());

        int hungerDrain = 5 + difficultyLevel * 2 * (Math.random() < 0.5 ? 1 : 2);
        int energyDrain = 5 + difficultyLevel * 2 * (Math.random() < 0.5 ? 1 : 2);

        if (getCurrentTime().equals("Night")) {
            energyDrain += 5;
        }

        player.drainHunger(hungerDrain);
        player.drainEnergy(energyDrain);
        System.out.println("You feel hungrier (-" + hungerDrain + " hunger) and more tired (-" + energyDrain
                + " energy) as time passes.");

        if (player.getHunger() < 25) {
            System.out.println("Your hunger is critically low! You need to eat soon or you'll start taking damage!");
        }
        if (player.getEnergy() < 25) {
            System.out.println(
                    "Your energy is critically low! You need to drink water soon or you'll start taking damage!");
        }

        if (player.getHunger() <= 0) {
            System.out.println("You are starving! You take 10 damage from hunger.");
            player.takeDamage(10);
        }
        if (player.getEnergy() <= 0) {
            System.out.println("You are exhausted! You take 10 damage from exhaustion.");
            player.takeDamage(10);
        }

    }

}
