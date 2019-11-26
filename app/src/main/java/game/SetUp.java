package game;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentLinkedQueue;

import nodes.Node;
import nodes.Parser;
import nodes.Script;
import screens.ChooseScript;


/**
 * Creates 3 AI robots to fight against
 * and sets up the players script to fight
 *
 * @author MattSBellis
 */
public class SetUp {
    private ConcurrentLinkedQueue<Robot> robots;

    public SetUp() {
        robots = new ConcurrentLinkedQueue<Robot>();
    }

    public ConcurrentLinkedQueue<Robot> getRobots() {
        return robots;
    }

    public void create() {
        double leftX = Constants.OFFSETX, rightX = Constants.WIDTH - Constants.OFFSETX - Constants.ROBOT_SIZE;
        double topY = Constants.OFFSETY, botY = Constants.HEIGHT - Constants.OFFSETY - Constants.ROBOT_SIZE;

        Robot.resetIds();

        // player
        ArrayList<String> list;
        list = ChooseScript.playerScript;
        Parser p = new Parser(list);
        Node headNode = p.createTree();
        Robot r = ChooseScript.playerRobot;
        r.setX(leftX);
        r.setY(topY);
        r.setId(1);
        //Robot r = new Robot(leftX, topY);
        Script s = new Script(headNode, r);
        s.setTokensInScript(list.size());
        r.setScript(s);
        robots.add(r);

        // robot 1
        list = ChooseScript.enemy1Script;
        p = new Parser(list);
        headNode = p.createTree();
        //r = new Robot(rightX, topY);
        r = ChooseScript.enemy1Robot;
        r.setX(rightX);
        r.setY(topY);
        r.setId(2);
        s = new Script(headNode, r);
        s.setTokensInScript(list.size());
        r.setScript(s);
        robots.add(r);

        // robot 2
        list = ChooseScript.enemy2Script;
        p = new Parser(list);
        headNode = p.createTree();
        //r = new Robot(leftX, botY);
        r = ChooseScript.enemy2Robot;
        r.setX(leftX);
        r.setY(botY);
        r.setId(3);
        s = new Script(headNode, r);
        s.setTokensInScript(list.size());
        r.setScript(s);
        robots.add(r);

        // robot 3
        list = ChooseScript.enemy3Script;
        p = new Parser(list);
        headNode = p.createTree();
        //r = new Robot(rightX, botY);
        r = ChooseScript.enemy3Robot;
        r.setX(rightX);
        r.setY(botY);
        r.setId(4);
        s = new Script(headNode, r);
        s.setTokensInScript(list.size());
        r.setScript(s);
        robots.add(r);
    }

    public static void fixStartPositions(ConcurrentLinkedQueue<Robot> robots) {
        double leftX = Constants.OFFSETX, rightX = Constants.WIDTH - Constants.OFFSETX - Constants.ROBOT_SIZE;
        double topY = Constants.OFFSETY, botY = Constants.HEIGHT - Constants.OFFSETY - Constants.ROBOT_SIZE;
        // set X and Y to be on 10 break
        if (leftX % Constants.ROBOT_SPEED != 0) {
            leftX += Constants.ROBOT_SPEED - (leftX % Constants.ROBOT_SPEED);
        }
        if (rightX % Constants.ROBOT_SPEED != 0) {
            rightX += Constants.ROBOT_SPEED - (rightX % Constants.ROBOT_SPEED);
        }
        if (topY % Constants.ROBOT_SPEED != 0) {
            topY += Constants.ROBOT_SPEED - (topY % Constants.ROBOT_SPEED);
        }
        if (botY % Constants.ROBOT_SPEED != 0) {
            botY += Constants.ROBOT_SPEED - (botY % Constants.ROBOT_SPEED);
        }

        // do "random" starting position based on script size or #tokens ect
        // find out each length
        ArrayList<Script> scriptList = new ArrayList<Script>();

        for (Robot r : robots) {
            scriptList.add(r.getScript());
        }


        Script[] scripts = new Script[scriptList.size()];
        scriptList.toArray(scripts);

        // use crappy bubble sort to sort
        Script tmp;
        for (int i = 0; i < scripts.length; i++) {
            for (int j = i; j < scripts.length; j++) {
                if (scripts[i].getTokensInScript() < scripts[j].getTokensInScript()) {
                    tmp = scripts[j];
                    scripts[j] = scripts[i];
                    scripts[i] = tmp;
                }
            }
        }

        //debug here ensure shitty bubble sort worked

        Robot r;
        // now assign based on pos in scripts
        for (int i = 0; i < scripts.length; i++) {
            r = scripts[i].getOwner();
            switch (i) {
                case 0:
                    r.setX(leftX);
                    r.setY(topY);
                    break;
                case 1:
                    r.setX(rightX);
                    r.setY(topY);
                    break;
                case 2:
                    r.setX(leftX);
                    r.setY(botY);
                    break;
                case 3:
                    r.setX(rightX);
                    r.setY(botY);
                    break;
                default:
                    break;
            }
        }
    }
}
