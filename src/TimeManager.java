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

    public void advanceTime(Player player, ArrayList<Enemy> enemies) {
        currentTimeIndex = (currentTimeIndex + 1) % times.length;
        if (currentTimeIndex == 0) {
            dayCount++;
            System.out.println("\n--- Day " + dayCount + " ---");
        }
    }

}
