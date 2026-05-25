public class Bunker {
    public static void search(Player player) {
        System.out.println("\nYou cautiously enter a bunker and search around...");
        getLoot(player);
    }

    public static void getLoot(Player player) {
        double lootBonus = player.getLootBonus();
        double roll = Math.random();

        double nothingThreshold = Math.max(0.05, 0.15 - lootBonus);

        if (roll < nothingThreshold) {
            System.out.println("The bunker is empty. Nothing useful here.");
        } else if (roll < nothingThreshold + 0.12) {
            int amount = (int) (Math.random() * 2) + 1;
            player.addFood(amount);
            System.out.println("Found " + amount + " food ration(s)!");
        } else if (roll < nothingThreshold + 0.24) {
            int amount = (int) (Math.random() * 2) + 1;
            player.addWater(amount);
            System.out.println("Found " + amount + " canteen(s) of water!");
        } else if (roll < nothingThreshold + 0.35) {
            giveAmmo(player);
        } else if (roll < nothingThreshold + 0.43) {
            player.addMedkit(1);
            System.out.println("Found a medkit!");
        } else if (roll < nothingThreshold + 0.50) {
            int amount = 1 + (int) (Math.random() * 2);
            player.addGrenades(amount);
            System.out.println("Found " + amount + " grenade(s)!");
        } else if (roll < nothingThreshold + 0.60) {
            Weapon found = getRandomWeapon();
            System.out.println("Found a " + found.getName() + "!");
            player.pickUpRifle(found);
        } else if (roll < nothingThreshold + 0.68) {
            if (player.getHelmet().isBroken()) {
                player.replaceHelmet();
            } else {
                System.out.println("Found a helmet, but yours is still intact.");
            }
        } else if (roll < nothingThreshold + 0.74) {
            if (player.getBodyArmor().isBroken()) {
                player.replaceArmor();
            } else {
                System.out.println("Found body armor, but yours is still intact.");
            }
        } else {
            System.out.println("JACKPOT! This bunker is loaded!");
            player.addFood(2);
            player.addWater(2);
            player.addMedkit(1);
            player.addGrenades(2);
            giveAmmo(player);
            System.out.println("  +2 food, +2 water, +1 medkit, +2 grenades, +ammo");
        }
    }

    private static void giveAmmo(Player player) {
        int mags = 1 + (int) (Math.random() * 2);
        player.getPistol().addMags(mags);
        System.out.println("Found " + mags + " pistol mag(s) for " + player.getPistol().getName() + "!");

        if (player.getRifle() != null) {
            int rifleMags = 1 + (int) (Math.random() * 2);
            player.getRifle().addMags(rifleMags);
            System.out.println("Found " + rifleMags + " rifle mag(s) for " + player.getRifle().getName() + "!");
        }
    }

    private static Weapon getRandomWeapon() {
        double r = Math.random();
        if (r < 0.40) {
            return Weapon.createRifle();
        }
        if (r < 0.70) {
            return Weapon.createSMG();
        }
        return Weapon.createSniper();
    }
}
