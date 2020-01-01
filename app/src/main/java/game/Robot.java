package game;

import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;

import java.io.Serializable;

import nodes.Script;

public class Robot implements Serializable {

    private static final long serialVersionUID = 1L;
    private double x, y, ammo, health, shield, speed, damage, robotsDetected, missiles;
    private int robot_id, buildPoints;
    private boolean isShielding, isDetecting;
    private Robot nearestEnemy, lowestHPEnemy, highestHPEnemy;
    private BitmapDrawable picture = null;
    private Script script;
    private static int id = 0;
    private String robotName;

    public Robot() {
        this(100, 100);
    }

    public Robot(double x, double y) {
        this.x = x;
        this.y = y;
        health = Constants.ROBOT_START_HP;
        ammo = Constants.ROBOT_START_AMMO;
        shield = Constants.ROBOT_START_SHIELDS;
        speed = Constants.ROBOT_SPEED;
        isShielding = false;
        isDetecting = false;
        damage = Constants.ROBOT_START_DAMAGE;
        missiles = Constants.ROBOT_START_MISSILES;
        buildPoints = Constants.ROBOT_BUILD_POINTS;
        robot_id = ++id;
        robotName = "Unnamed " + robot_id;
    }

    public Robot(double x, double y, Script script) {
        this(x, y);
        this.script = script;
    }

    public Robot(float x, float y, Script script, BitmapDrawable picture) {
        this(x, y, script);
        this.setPicture(picture);
    }

    public void draw(Canvas c) {
        c.drawBitmap(picture.getBitmap(), (float) x, (float) y, null);
    }

    // getters and setters

    public String getFullDisplayName() {
        String fullName = getRobotName();
        if (script != null) {
            fullName += " (" + script.getScriptName() + ")";
        }
        return fullName;
    }

    public String getRobotName() {
        return robotName;
    }

    public void setRobotName(String robotName) {
        this.robotName = robotName;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double getAmmo() {
        return ammo;
    }

    public void setAmmo(double ammo) {
        this.ammo = ammo;
    }

    public double getHealth() {
        return health;
    }

    public void setHealth(double health) {
        this.health = health;
    }

    public double getShield() {
        return shield;
    }

    public void setShield(double shield) {
        this.shield = shield;
    }

    public boolean isShielding() {
        return isShielding;
    }

    public void setShielding(boolean isShielding) {
        this.isShielding = isShielding;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }

    public double getSpeed() {
        return speed;
    }

    public int getId() {
        return robot_id;
    }

    public void setId(int id) {
        robot_id = id;
    }

    public Robot getNearestEnemy() {
        return nearestEnemy;
    }

    public void setNearestEnemy(Robot nearestEnemy) {
        this.nearestEnemy = nearestEnemy;
    }

    public Robot getLowestHPEnemy() {
        return lowestHPEnemy;
    }

    public void setLowestHPEnemy(Robot lowestHPEnemy) {
        this.lowestHPEnemy = lowestHPEnemy;
    }

    public Robot getHighestHPEnemy() {
        return highestHPEnemy;
    }

    public void setHighestHPEnemy(Robot highestHPEnemy) {
        this.highestHPEnemy = highestHPEnemy;
    }

    public double getDamage() {
        return damage;
    }

    public void setDamage(double damage) {
        this.damage = damage;
    }

    public BitmapDrawable getPicture() {
        return picture;
    }

    public void setPicture(BitmapDrawable picture) {
        this.picture = picture;
    }

    public Script getScript() {
        return script;
    }

    public void setScript(Script script) {
        this.script = script;
    }

    public static void resetIds() {
        id = 0;
    }

    public boolean isDetecting() {
        return isDetecting;
    }

    public void setDetecting(boolean isDetecting) {
        this.isDetecting = isDetecting;
    }

    public double getRobotsDetected() {
        return robotsDetected;
    }

    public void setRobotsDetected(double robotsDetected) {
        this.robotsDetected = robotsDetected;
    }

    public double getMissiles() {
        return missiles;
    }

    public void setMissiles(double missiles) {
        this.missiles = missiles;
    }

    public int getBuildPoints() {
        return buildPoints;
    }

    public void setBuildPoints(int buildPoints) {
        this.buildPoints = buildPoints;
    }

}
