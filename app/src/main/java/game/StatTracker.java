package game;

public class StatTracker {
    private Robot r;
    private int shotsFired, shotsBlocked, shotsConnected, damageTaken, damageDealt;
    private double distanceMoved;

    public StatTracker(Robot r) {
        this.r = r;
        shotsFired = 0;
        shotsBlocked = 0;
        distanceMoved = 0;
        shotsConnected = 0;
        damageTaken = 0;
        setDamageDealt(0);
    }

    public Robot getR() {
        return r;
    }

    public void setR(Robot r) {
        this.r = r;
    }

    public int getShotsFired() {
        return shotsFired;
    }

    public void setShotsFired(int shotsFired) {
        this.shotsFired = shotsFired;
    }

    public void addShotsFired(int shotsFired) {
        if (shotsFired > 0) {
            this.shotsFired += shotsFired;
        }
    }

    public int getShotsBlocked() {
        return shotsBlocked;
    }

    public void setShotsBlocked(int shotsBlocked) {
        this.shotsBlocked = shotsBlocked;
    }

    public void addShotsBlocked(int shotsBlocked) {
        if (shotsBlocked > 0) {
            this.shotsBlocked += shotsBlocked;
        }
    }

    public double getDistanceMoved() {
        return distanceMoved;
    }

    public void addShotsConnected(int shotsConnected) {
        if (shotsConnected > 0) {
            this.shotsConnected += shotsConnected;
        }
    }

    public void setDistanceMoved(double distanceMoved) {
        this.distanceMoved = distanceMoved;
    }

    public void addDistanceMoved(double distanceMoved) {
        this.distanceMoved += distanceMoved;
    }

    public double getAccuracy() {
        if (shotsFired > 0) {
            return (double) shotsConnected / (double) shotsFired;
        }
        return 0;
    }

    public int getDamageTaken() {
        return damageTaken;
    }

    public void setDamageTaken(int damageTaken) {
        this.damageTaken = damageTaken;
    }

    public void addDamageTaken(int damageTaken) {
        if (damageTaken > 0) {
            this.damageTaken += damageTaken;
        }
    }


    public int getDamageDealt() {
        return damageDealt;
    }

    public void setDamageDealt(int damageDealt) {
        this.damageDealt = damageDealt;
    }

    public void addDamageDealt(int damageDealt) {
        if (damageDealt > 0) {
            this.damageDealt += damageDealt;
        }
    }

    public String toString() {
        if (r.getId() != 1) {
            return "Robot [" + r.getId() + "]:\nShots Fired: " + shotsFired + ", Accuracy: "
                    + (Math.round(getAccuracy() * 100)) + "%\n" + "Shots Shielded: " + shotsBlocked
                    + ", Distance Moved: " + Math.round(distanceMoved) +
                    "\nDamage Taken: " + damageTaken + ", Damage Dealt: " + damageDealt;
        }
        return "Player:\nShots Fired: " + shotsFired + ", Accuracy: "
                + (Math.round(getAccuracy() * 100)) + "%\n" + "Shots Shielded: " + shotsBlocked
                + ", Distance Moved: " + Math.round(distanceMoved) +
                "\nDamage Taken: " + damageTaken + ", Damage Dealt: " + damageDealt;
    }

}
