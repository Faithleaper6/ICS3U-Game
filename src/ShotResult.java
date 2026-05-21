public class ShotResult {
    private boolean hit;
    private boolean headshot;
    private int damage;
    private String message;

    public ShotResult(boolean hit, boolean headshot, int damage, String message) {
        this.hit = hit;
        this.headshot = headshot;
        this.damage = damage;
        this.message = message;
    }

    public boolean isHit() {
        return hit;
    }

    public boolean isHeadshot() {
        return headshot;
    }

    public int getDamage() {
        return damage;
    }

    public String getMessage() {
        return message;
    }
}
