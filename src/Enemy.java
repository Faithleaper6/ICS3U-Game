public class Enemy {
    private String type;
    private int health;
    private int maxHealth;
    private int damage;
    private double accuracy;
    private double headshotChance;
    private boolean alive;

    public Enemy(String type, int health, int damage, double accuracy, double headshotChance) {
        this.type = type;
        this.health = health;
        this.maxHealth = health;
        this.damage = damage;
        this.accuracy = accuracy;
        this.headshotChance = headshotChance;
        this.alive = true;
    }

    public ShotResult attack() {
        double roll = Math.random();
        if (roll > accuracy) {
            return new ShotResult(false, false, 0, "An enemy " + type + " fired at you and MISSED!");
        }
        double hsRoll = Math.random();
        if (hsRoll < headshotChance) {
            int hsDmg = (int) (damage * 2.5);
            return new ShotResult(true, true, hsDmg,
                    "Enemy " + type + " HEADSHOT you for " + hsDmg + " damage!");
        }
        return new ShotResult(true, false, damage,
                "Enemy " + type + " hit you for " + damage + " damage!");
    }

    public void takeDamage(int dmg) {
        health -= dmg;
        if (health <= 0) {
            health = 0;
            alive = false;
        }
    }

    public void upgradeWeapon() {
        damage += 5;
        accuracy = Math.min(accuracy + 0.05, 0.90);
    }

    public String getType() {
        return type;
    }

    public int getHealth() {
        return health;
    }

    public int getMaxHealth() {
        return maxHealth;
    }

    public boolean isAlive() {
        return alive;
    }

    public String toString() {
        return type + " [HP: " + health + "/" + maxHealth + "]";
    }

    public static Enemy createRifleman() {
        return new Enemy("Rifleman", 60, 15, 0.45, 0.05);
    }

    public static Enemy createOfficer() {
        return new Enemy("Officer", 50, 20, 0.50, 0.08);
    }

    public static Enemy createMachineGunner() {
        return new Enemy("MG Gunner", 80, 12, 0.55, 0.03);
    }

    public static Enemy createSniper() {
        return new Enemy("Sniper", 40, 40, 0.70, 0.25);
    }
}
