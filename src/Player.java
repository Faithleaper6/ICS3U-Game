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

    private String currentroomid;

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
        this.lootBonusPerLevel = 0.03;
        this.accuracyBonusPerLevel = 0.03;
        this.currentroomid = "trench5";
    }

    public String getName() {
        return name;
    }

    public double getLootBonus() {
        return lootBonusPerLevel * (level - 1);
    }

    public double getAccuracyBonus() {
        return accuracyBonusPerLevel * (level - 1);
    }

    public void addFood(int amount) {
        foodCount += amount;
        if (foodCount > 15) {
            foodCount = 15;
        }
    }

    public void addMedkit(int amount) {
        medkitCount += amount;
        if (medkitCount > 5) {
            medkitCount = 5;
        }
    }

    public void addWater(int amount) {
        waterCount += amount;
        if (waterCount > 15) {
            waterCount = 15;
        }
    }

    public void addGrenades(int amount) {
        grenades += amount;
        if (grenades > 10) {
            grenades = 10;
        }
    }

    public void pickUpRifle(Weapon rifle) {
        this.rifle = rifle;
        this.usingRifle = true;
        System.out.println("You equip the " + rifle.getName() + " as your primary weapon.");
    }

    public void replaceHelmet() {
        helmet = Gear.createHelmet();
        System.out.println("Equipped a new helmet!");
    }

    public void replaceArmor() {
        bodyArmor = Gear.createBodyArmor();
        System.out.println("Equipped new body armor!");
    }

    public Gear getHelmet() {
        return helmet;
    }

    public Gear getBodyArmor() {
        return bodyArmor;
    }

    public Weapon getPistol() {
        return pistol;
    }

    public Weapon getRifle() {
        return rifle;
    }

    public Weapon getCurrentWeapon() {
        if (usingRifle && rifle != null) {
            return rifle;
        }
        return pistol;
    }

    public void switchWeapon() {
        if (rifle == null) {
            System.out.println("You only have your pistol right now.");
            return;
        }

        usingRifle = !usingRifle;
        System.out.println("Switched to " + getCurrentWeapon().getName() + ".");
    }

    public String getCurrentRoomId() {
        return this.currentroomid;
    }

    public void setCurrentRoomId(String roomId) {
        this.currentroomid = roomId;
    }

    public void drainEnergy(int energyDrain) {
        energy -= energyDrain;
        if (energy < 0) {
            energy = 0;
        }
    }

    public void drainHunger(int hungerDrain) {
        hunger -= hungerDrain;
        if (hunger < 0) {
            hunger = 0;
        }
    }

    public void takeDamage(int damage, boolean headshot) {
        int actualDamage = damage;
        if (headshot && !helmet.isBroken()) {
            actualDamage = helmet.absorbDamage(damage);
        } else if (!headshot && !bodyArmor.isBroken()) {
            actualDamage = bodyArmor.absorbDamage(damage);
        }

        health -= actualDamage;
        if (health < 0) {
            health = 0;
        }
        System.out.println("  >> You took " + actualDamage + " damage! Health: " + health + "/" + maxHealth);
    }

    public void kill() {
        health = 0;
        System.out.println("  >> You were killed instantly! Health: " + health + "/" + maxHealth);
    }

    public void heal(int amount) {
        health += amount;
        if (health > maxHealth) {
            health = maxHealth;
        }
    }

    public void eat() {
        if (foodCount <= 0) {
            System.out.println("You have no food left.");
            return;
        }

        foodCount--;
        hunger += 35;
        if (hunger > maxHunger) {
            hunger = maxHunger;
        }
        System.out.println("You eat a ration. Hunger: " + hunger + "/" + maxHunger + " | Food left: " + foodCount);
    }

    public void drink() {
        if (waterCount <= 0) {
            System.out.println("You have no water left.");
            return;
        }

        waterCount--;
        energy += 30;
        if (energy > maxEnergy) {
            energy = maxEnergy;
        }
        System.out.println("You drink water. Energy: " + energy + "/" + maxEnergy + " | Water left: " + waterCount);
    }

    public void useMedkit() {
        if (medkitCount <= 0) {
            System.out.println("You have no medkits left.");
            return;
        }

        if (health == maxHealth) {
            System.out.println("You are already at full health.");
            return;
        }

        medkitCount--;
        heal(40);
        System.out.println("You use a medkit. Health: " + health + "/" + maxHealth + " | Medkits left: " + medkitCount);
    }

    public boolean useGrenade() {
        if (grenades <= 0) {
            System.out.println("You have no grenades left.");
            return false;
        }

        grenades--;
        return true;
    }

    public int getGrenades() {
        return grenades;
    }

    public void addKill() {
        kills++;
        int newLevel = 1 + kills / 3;
        if (newLevel > level) {
            level = newLevel;
            System.out.println("  >> Level up! You are now level " + level + ".");
        }
    }

    public int getKills() {
        return kills;
    }

    public int getLevel() {
        return level;
    }

    public boolean isAlive() {
        return health > 0;
    }

    public int getHealth() {
        return health;
    }

    public int getEnergy() {
        return energy;
    }

    public int getHunger() {
        return hunger;
    }

    public void printStatus() {
        System.out.println("\n--- STATUS ---");
        System.out.println(name + " | HP: " + health + "/" + maxHealth +
                " | Hunger: " + hunger + "/" + maxHunger +
                " | Energy: " + energy + "/" + maxEnergy);
        System.out.println("Food: " + foodCount + " | Water: " + waterCount +
                " | Medkits: " + medkitCount + " | Grenades: " + grenades);
        System.out.println("Weapon: " + getCurrentWeapon().getName() +
                " [" + getCurrentWeapon().getCurrentAmmo() + "/" + getCurrentWeapon().getMagSize() +
                ", mags: " + getCurrentWeapon().getReserveMags() + "]");
        if (rifle != null) {
            System.out.println("Side weapon: " + pistol.getName() +
                    " [" + pistol.getCurrentAmmo() + "/" + pistol.getMagSize() +
                    ", mags: " + pistol.getReserveMags() + "]");
        }
        System.out.println("Helmet: " + helmet.getHitsRemaining() + "/" + helmet.getMaxHits() +
                " | Armor: " + bodyArmor.getHitsRemaining() + "/" + bodyArmor.getMaxHits());
        System.out.println("Level: " + level + " | Kills: " + kills);
    }
}
