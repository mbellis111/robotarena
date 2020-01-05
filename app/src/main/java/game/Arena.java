package game;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

import nodes.BoolNode;
import nodes.FunctionNode;
import nodes.IfNode;
import nodes.Node;
import nodes.Node.Functions;
import nodes.Node.Operators;
import nodes.Node.Variable;
import nodes.NothingNode;
import nodes.Script;
import nodes.WhileNode;


public class Arena {
    private ConcurrentLinkedQueue<Robot> robots;
    private ConcurrentLinkedQueue<Bullet> bullets;
    private static ArrayList<StatTracker> stats;

    public Arena(ConcurrentLinkedQueue<Robot> robots) {
        this.robots = robots;
        bullets = new ConcurrentLinkedQueue<Bullet>();
        stats = new ArrayList<StatTracker>();
        for (Robot r : robots) {
            stats.add(new StatTracker(r));
        }
    }

    public ConcurrentLinkedQueue<Robot> getRobots() {
        return robots;
    }

    public ConcurrentLinkedQueue<Bullet> getBullets() {
        return bullets;
    }

    public static ArrayList<StatTracker> getStats() {
        return stats;
    }

    public void advanceScripts() {
        for (Robot r : robots) {
            stepScript(r.getScript());
        }
    }

    private void stepScript(Script s) {
        Node currNode = s.getCurrentNode();

        if (currNode == null) {
            currNode = s.getHeadNode();
        }

        if (currNode == s.getHeadNode() && !s.getIsFunctionCalled()) {
            s.setLoopedNoCall(s.getLoopedNoCall() + 1);
        }

        if (currNode == s.getHeadNode()) {
            s.setFunctionCalled(false);
        }

        if (s.getLoopedNoCall() >= Constants.MAX_LOOP_ITER) {
            // went through the script too many times and nothing happened, robot done!
            executeFunction(null, s.getOwner(), -1); // resets values, does nothing
            return;
        }

        if (currNode instanceof FunctionNode) {
            double num;
            Functions f = ((FunctionNode) currNode).getFunction();
            if (f == Functions.MOVE || f == Functions.SHOOT || f == Functions.MISSILE) {
                if (((FunctionNode) currNode).usesNumber()) {
                    num = ((FunctionNode) currNode).getNumber();
                } else {
                    Variable v = ((FunctionNode) currNode).getVariable();
                    num = variableToNumber(v, s.getOwner());
                }
                executeFunction(f, s.getOwner(), num);
            } else {
                executeFunction(f, s.getOwner(), -1);
            }
            s.setCurrentNode(currNode.getNextNode());
            s.setFunctionCalled(true);
        } else if (currNode instanceof IfNode) {
            BoolNode boolNode = ((IfNode) currNode).getBoolNode();
            if (checkBoolStatement(boolNode.getVariable(), boolNode.getOperator(), boolNode.getNumber(), boolNode.getOtherVariable(), boolNode.hasNumber(), s.getOwner())) {
                s.setCurrentNode(currNode.getNextNode());
            } else {
                s.setCurrentNode(((IfNode) currNode).getElseNode());
            }
            stepScript(s);
        } else if (currNode instanceof WhileNode) {
            BoolNode boolNode = ((WhileNode) currNode).getBoolNode();
            if (checkBoolStatement(boolNode.getVariable(), boolNode.getOperator(), boolNode.getNumber(), boolNode.getOtherVariable(), boolNode.hasNumber(), s.getOwner())) {
                s.setCurrentNode(currNode.getNextNode());
            } else {
                s.setCurrentNode(((WhileNode) currNode).getBreakNode());
            }
            stepScript(s);
        } else if (currNode instanceof NothingNode) {
            s.setCurrentNode(currNode.getNextNode());
            stepScript(s);
        }
    }

    private boolean checkBoolStatement(Variable v, Operators op, double number, Variable ov, boolean hasNumber, Robot r) {
        double var = variableToNumber(v, r);
        double checkNumber = -1;
        if (hasNumber) {
            checkNumber = number;
        } else {
            checkNumber = variableToNumber(ov, r);
        }
        switch (op) {
            case EQUALS:
                return var == checkNumber;
            case LESS_THAN:
                return var < checkNumber;
            case GREATER_THAN:
                return var > checkNumber;
            case GREATER_EQUAL_THAN:
                return var >= checkNumber;
            case LESS_EQUAL_THAN:
                return var <= checkNumber;
            default:
                return false;
        }
    }

    private double variableToNumber(Variable v, Robot r) {
        switch (v) {
            case HEALTH:
                return r.getHealth();
            case AMMO:
                return r.getAmmo();
            case SHIELDS:
                return r.getShield();
            case X:
                return r.getX();
            case Y:
                return r.getY();
            case NEAREST_ENEMY:
                return getAngle(r, r.getNearestEnemy());
            case LOWEST_HP_ENEMY:
                return getAngle(r, r.getLowestHPEnemy());
            case HIGHEST_HP_ENEMY:
                return getAngle(r, r.getHighestHPEnemy());
            case UP:
                return Constants.UP;
            case DOWN:
                return Constants.DOWN;
            case LEFT:
                return Constants.LEFT;
            case RIGHT:
                return Constants.RIGHT;
            case UP_RIGHT:
                return Constants.UP_RIGHT;
            case UP_LEFT:
                return Constants.UP_LEFT;
            case DOWN_LEFT:
                return Constants.DOWN_LEFT;
            case DOWN_RIGHT:
                return Constants.DOWN_RIGHT;
            case ROBOTS_DETECTED:
                return r.getRobotsDetected();
            case MISSILES:
                return r.getMissiles();
            default:
                return -1;
        }
    }

    private double getAngle(Robot a, Robot b) {
        if (b == null) {
            return 0;
        }
        double xa = a.getX() + Constants.ROBOT_SIZE / 2, xb = b.getX() + Constants.ROBOT_SIZE / 2,
                ya = a.getY() + Constants.ROBOT_SIZE / 2, yb = b.getY() + Constants.ROBOT_SIZE / 2;
        double angle = Math.atan2(yb - ya, xb - xa);
        double deg = Math.toDegrees(angle);
        if (deg < 0) {
            deg += 360;
        }
        return deg + 90;
    }

    private void executeFunction(Functions f, Robot r, double variable) {
        r.setShielding(false);
        r.setDetecting(false);
        switch (f) {
            case MOVE:
                move(r, variable);
                return;
            case RELOAD:
                reload(r);
                return;
            case DETECT:
                detect(r);
                return;
            case SHIELD:
                shield(r);
                return;
            case SHOOT:
                shoot(r, variable);
                return;
            case MISSILE:
                missile(r, variable);
                return;
            default:
                return;
        }
    }

    public void moveBullets() {
        for (Bullet b : bullets) {
            // move them
            double x = b.getX(), y = b.getY();
            // move missiles faster?
            b.setX(x + (double) (Constants.BULLET_SPEED * Math.sin(Math.toRadians(b.getDirection()))));
            b.setY(y - (double) (Constants.BULLET_SPEED * Math.cos(Math.toRadians(b.getDirection()))));
            x = b.getX();
            y = b.getY();
            // check for delete
            if (x > Constants.WIDTH || x < 0 || y > Constants.HEIGHT || y < 0) {
                bullets.remove(b);
            }
            // check for hits
            for (Robot r : robots) {
                if (checkHit(r, b)) {
                    // stat tracking
                    stats.get(b.getShotFrom().getId() - 1).addShotsConnected(1);
                    if (!r.isShielding()) {
                        if (b instanceof Missile) {
                            // explode the missile
                            // go through all the robots, seeing which are inside the radius
                            for (Robot rh : robots) {
                                if (checkMissileExplosion(rh, (Missile) b)) {
                                    r.setHealth(r.getHealth() - b.getDamage());
                                    stats.get(r.getId() - 1).addDamageTaken((int) b.getDamage());
                                    stats.get(b.getShotFrom().getId() - 1).addDamageDealt((int) b.getDamage());
                                }
                            }
                        } else if (b instanceof Bullet) {
                            r.setHealth(r.getHealth() - b.getDamage());
                            stats.get(r.getId() - 1).addDamageTaken((int) b.getDamage());
                            stats.get(b.getShotFrom().getId() - 1).addDamageDealt((int) b.getDamage());
                        }
                        // check for death now
                        if (r.getHealth() <= 0) {
                            robots.remove(r);
                        }
                    } else {
                        // stat tracking
                        stats.get(r.getId() - 1).addShotsBlocked(1);
                    }
                    bullets.remove(b);
                }
            }
        }
    }

    public int checkWin() {
        if (robots.size() == 1) {
            return robots.peek().getId();
        } else if (robots.isEmpty()) {
            return 5;
        }
        return -1;
    }

    public int checkTimeoutWinner() {
        if (robots.size() == 0) {
            return 5;
        } else if (robots.size() == 1) {
            return robots.peek().getId();
        }
        // choose the robots with the highest health
        List<Robot> finals = new ArrayList<Robot>(robots);
        finals.sort(new Comparator<Robot>() {
            @Override
            public int compare(Robot r1, Robot r2) {
                return (int) r2.getHealth() - (int) r1.getHealth();
            }
        });
        // if the top two robots have the same HP, call a tie
        if (finals.get(0).equals(finals.get(1))) {
            return 5;
        }
        return finals.get(0).getId();
    }

    private boolean checkHit(Robot r, Bullet b) {
        if (r.getId() == b.getShotFrom().getId()) {
            return false;
        }
        double xd = (r.getX() + Constants.ROBOT_SIZE / 2) - b.getX();
        double yd = (r.getY() + Constants.ROBOT_SIZE / 2) - b.getY();
        double dist = Math.sqrt((xd * xd) + (yd * yd));
        if (b instanceof Bullet) {
            if (dist < Constants.BULLET_SIZE + Constants.ROBOT_SIZE / 2) {
                return true;
            }
        } else if (b instanceof Missile) {
            if (dist < Constants.MISSILE_SIZE + Constants.ROBOT_SIZE / 2) {
                return true;
            }
        }
        return false;
    }

    private boolean checkMissileExplosion(Robot r, Missile m) {
        if (r.getId() == m.getShotFrom().getId()) {
            return false;
        }
        double xd = (r.getX() + Constants.ROBOT_SIZE / 2) - m.getX();
        double yd = (r.getY() + Constants.ROBOT_SIZE / 2) - m.getY();
        double dist = Math.sqrt((xd * xd) + (yd * yd));
        if (dist < m.getRadius() + Constants.ROBOT_SIZE / 2) {
            return true;
        }
        return false;
    }

    private void missile(Robot r, double variable) {
        if (r.getMissiles() > 0) {
            Missile m = new Missile(r, variable);
            bullets.add(m);
            r.setMissiles(r.getMissiles() - 1);
            // add stat tracking??
        }
    }

    private void shoot(Robot r, double variable) {
        if (r.getAmmo() > 0) {
            Bullet b = new Bullet(r, variable);
            bullets.add(b);
            r.setAmmo(r.getAmmo() - 1);
            //stat tracking
            stats.get(r.getId() - 1).addShotsFired(1);
        }
    }

    private void shield(Robot r) {
        if (r.getShield() > 0) {
            r.setShielding(true);
            r.setShield(r.getShield() - 1);
        }
    }

    private void move(Robot r, double variable) {
        double x = r.getX(), y = r.getY();
        double sx = r.getX(), sy = r.getY(); // stat tracking
        r.setX(x + (double) (r.getSpeed() * Math.sin(Math.toRadians(variable))));
        r.setY(y - (double) (r.getSpeed() * Math.cos(Math.toRadians(variable))));
        x = r.getX();
        y = r.getY();
        // stat tracking
        double dist = Math.sqrt((sx - x) * (sx - x) + (sy - y) * (sy - y));
        stats.get(r.getId() - 1).addDistanceMoved(dist);
        if (x + Constants.ROBOT_SIZE >= Constants.WIDTH) {
            r.setX(2);
        }
        if (x <= 0) {
            r.setX(Constants.WIDTH - Constants.ROBOT_SIZE);
        }
        if (y + Constants.ROBOT_SIZE >= Constants.HEIGHT) {
            r.setY(2);
        }
        if (y <= 0) {
            r.setY(Constants.HEIGHT - Constants.ROBOT_SIZE);
        }
        // ~~~~~~~~~~~~~~~~~~~~collision detection here!~~~~~~~~~~~~~~~~~~~~~~~
        // if hit consider bouncing off and changing direction?
    }

    private void detect(Robot r) {
        Robot closestR = null, lowestR = null, highestR = null;
        double closest = Double.MAX_VALUE, lowest = Double.MAX_VALUE, highest = -10;
        double numberDetected = 0;
        for (Robot robot : robots) {
            if (robot.getId() != r.getId()) {
                double xd = robot.getX() - r.getX();
                double yd = robot.getY() - r.getY();
                double dist = Math.sqrt((xd * xd) + (yd * yd));
                if (dist < Constants.DETECT_DISTANCE) {
                    numberDetected++;
                    if (dist < closest) {
                        closest = dist;
                        closestR = robot;
                    }
                    if (robot.getHealth() < lowest) {
                        lowest = robot.getHealth();
                        lowestR = robot;
                    }
                    if (robot.getHealth() > highest) {
                        highest = robot.getHealth();
                        highestR = robot;
                    }
                }
            }
        }
        r.setRobotsDetected(numberDetected);
        r.setLowestHPEnemy(lowestR);
        r.setHighestHPEnemy(highestR);
        r.setNearestEnemy(closestR);
        r.setDetecting(true);
    }

    private void reload(Robot r) {
        r.setAmmo(r.getAmmo() + 1);
    }
}
