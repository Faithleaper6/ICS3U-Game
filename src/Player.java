import java.util.ArrayList;
import java.util.List;

public class Player {
    private String name;
    private int health, maxHealth;
    private int hunger, maxHunger;
    private int energy, maxEnergy;

    private Gear helmet;
    private Gear bodyArmor;

    private Weapon pistol;   
    private Weapon rifle;    
    private boolean usingRifle; 

    private int foodCount, waterCount, medkitCount;
    private int grenades;
    private int kills;
    private int level;

    
    private double lootBonusPerLevel;    
    private double accuracyBonusPerLevel; 

   
