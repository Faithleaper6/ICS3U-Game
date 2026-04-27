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

    }

}
