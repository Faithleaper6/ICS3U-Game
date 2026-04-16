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

    public Player(String name, int health, int hunger, int energy) {
        this.name = name;
        this.maxHealth = health;
        this.health = health;
        this.maxHunger = hunger;
        this.hunger = hunger;
        this.maxEnergy = energy;
        this.energy = energy;

        this.helmet = Gear.createHelmet();
        this.bodyArmor = Gear.createBodyArmor();

        this.pistol = Weapon.createPistol();
        this.rifle = null;
        this.usingRifle = false;

        this.foodCount = 3;
        this.waterCount = 3;
        this.medkitCount = 1;
        this.grenades = 3;
        this.kills = 0;
        this.level = 1;

    }
}
