public class Weapon {
    private String name;
    private String type; // "pistol" or "rifle"
    private double hitChance;
    private double headshotChance;
    private int damage;
    private int headshotDamage;
    private int magSize; // rounds per magazine
    private int currentAmmo; // rounds in current mag
    private int reserveMags; // extra magazines carried
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
        this.currentAmmo = magSize; // starts with a full mag loaded
        this.reserveMags = startMags;
        this.fullAuto = fullAuto;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }
}
