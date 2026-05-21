public class Gear {
    private double damageReduction;
    private String name;
    private String description;
    private int maxHits;
    private int hitsRemaining;

    public Gear(double damageReduction, String name, String description, int maxHits) {
        this.damageReduction = damageReduction;
        this.name = name;
        this.description = description;
        this.maxHits = maxHits;
        this.hitsRemaining = maxHits;
    }

    public double getDamageReduction() {
        return damageReduction;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public int getMaxHits() {
        return maxHits;
    }

    public int getHitsRemaining() {
        return hitsRemaining;
    }

    public static Gear createHelmet() {
        return new Gear(0.50, "helmet",
                "A standard issue M1 steel helmet that protects against headshots.", 3);
    }

    public static Gear createBodyArmor() {
        return new Gear(0.35, "body armor",
                "A reinforced combat vest that reduces damage from body shots.", 5);
    }

    public boolean isBroken() {
        return hitsRemaining <= 0;
    }

    public int absorbDamage(int incomingDamage) {
        if (isBroken()) {
            return incomingDamage;
        }

        hitsRemaining--;
        int absorbed = (int) (incomingDamage * damageReduction);
        int actualDamage = incomingDamage - absorbed;

        if (isBroken()) {
            System.out.println("  >> Your " + name + " has been DESTROYED!");
        } else {
            System.out.println("  >> Your " + name + " absorbed " + absorbed +
                    " damage! (" + hitsRemaining + " hits left)");
        }

        return actualDamage;
    }
}
