public class Weapon {
    private String name;
    private String type;
    private double hitChance;
    private double headshotChance;
    private int damage;
    private int headshotDamage;
    private int magSize;
    private int currentAmmo;
    private int reserveMags;
    private boolean fullAuto;

    public Weapon(String name, String type, double hitChance, double headshotChance, int damage, int headshotDamage,
            int magSize, int startMags, boolean fullAuto) {
        this.name = name;
        this.type = type;
        this.hitChance = hitChance;
        this.headshotChance = headshotChance;
        this.damage = damage;
        this.headshotDamage = headshotDamage;
        this.magSize = magSize;
        this.currentAmmo = magSize;
        this.reserveMags = startMags;
        this.fullAuto = fullAuto;
    }

    public Weapon(String itemId, String itemName, String itemDescription) {
        this(itemName, "item", 0.0, 0.0, 0, 0, 0, 0, false);
    }

    public static Weapon createPistol() {
        return new Weapon("Colt M1911", "pistol", 0.65, 0.08, 22, 55, 7, 4, false);
    }

    public static Weapon createRifle() {
        return new Weapon("M1 Garand", "rifle", 0.72, 0.10, 32, 75, 8, 3, false);
    }

    public static Weapon createSMG() {
        return new Weapon("Thompson SMG", "rifle", 0.58, 0.05, 18, 45, 20, 3, true);
    }

    public static Weapon createSniper() {
        return new Weapon("Springfield Sniper", "rifle", 0.82, 0.22, 45, 110, 5, 2, false);
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public double getHitChance() {
        return hitChance;
    }

    public double getHeadshotChance() {
        return headshotChance;
    }

    public int getDamage() {
        return damage;
    }

    public int getHeadshotDamage() {
        return headshotDamage;
    }

    public int getMagSize() {
        return magSize;
    }

    public int getCurrentAmmo() {
        return currentAmmo;
    }

    public int getReserveMags() {
        return reserveMags;
    }

    public boolean isFullAuto() {
        return fullAuto;
    }

    public boolean hasAmmo() {
        return currentAmmo > 0;
    }

    public boolean fireRound() {
        if (currentAmmo <= 0) {
            System.out.println(name + " is empty. Reload first!");
            return false;
        }

        currentAmmo--;
        return true;
    }

    public int fireBurst(int requestedRounds) {
        int roundsFired = Math.min(requestedRounds, currentAmmo);
        currentAmmo -= roundsFired;
        return roundsFired;
    }

    public void addMags(int mags) {
        reserveMags += mags;
    }

    public void reload() {
        if (currentAmmo == magSize) {
            System.out.println(name + " is already fully loaded.");
            return;
        }

        if (reserveMags <= 0) {
            System.out.println("No spare magazines for " + name + ".");
            return;
        }

        reserveMags--;
        currentAmmo = magSize;
        System.out.println("Reloaded " + name + ". Mags left: " + reserveMags);
    }
}
