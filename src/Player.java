import java.util.ArrayList;
import java.util.List;

public class Player {
    private String name;
    private int health, maxHealth;
    private int hunger, maxHunger;
    private int energy, maxEnergy;

    private Gear helmet;
    private Gear bodyArmor;

    private Weapon pistol;   // always carried
    private Weapon rifle;    // one rifle slot (Garand, SMG, or Sniper)
    private boolean usingRifle; // true = rifle selected, false = pistol

    private int foodCount, waterCount, medkitCount;
    private int grenades;
    private int kills;
    private int level;

    // Difficulty-based bonuses per level
    private double lootBonusPerLevel;     // 0.02-0.05
    private double accuracyBonusPerLevel; 

   
