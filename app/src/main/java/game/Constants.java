package game;

public class Constants {
    public static double WIDTH = 720, HEIGHT = 1000;
    public static final double OFFSETX = 100;
    public static final double OFFSETY = 300;
    public static final double DETECT_DISTANCE = 1000;
    public static final double BULLET_SPEED = 15;
    public static final double BULLET_SIZE = 10;
    public static final double ROBOT_SIZE = 74;
    public static final double ROBOT_SPEED = 10;
    public static final double UP = 0, DOWN = 180, LEFT = 270, RIGHT = 90;
    public static final double UP_LEFT = 315, UP_RIGHT = 45, DOWN_LEFT = 225, DOWN_RIGHT = 135;
    public static final int PAUSE = 1000, FPS = 30;
    public static final int FRAME_RATE = PAUSE / FPS;
    public static final String SAVEDSCRIPTS = "userSavedScripts";
    public static final String SAVEDROBOTS = "userSavedRobots";
    public static final int ROBOT_START_HP = 100;
    public static final int ROBOT_START_MISSILES = 3;
    public static final int ROBOT_START_AMMO = 5;
    public static final int ROBOT_START_DAMAGE = 3;
    public static final int ROBOT_START_SHIELDS = 100;
    public static final int ROBOT_BUILD_POINTS = 10;
    public static final int MISSILE_DAMAGE = 25;
    public static final int MISSILE_EXPLODE_RADIUS = 100;
    public static final int MISSILE_SIZE = 25;
    public static final int MAX_LOOP_ITER = 10;
}
