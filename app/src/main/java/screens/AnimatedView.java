package screens;


import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.os.Handler;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatImageView;
import androidx.core.content.ContextCompat;

import com.mbellis.RobotArena.R;

import java.util.concurrent.ConcurrentLinkedQueue;

import game.Arena;
import game.Bullet;
import game.Constants;
import game.Missile;
import game.Robot;
import game.SetUp;

public class AnimatedView extends AppCompatImageView {

    private Handler h;
    private Arena arena;
    private BitmapDrawable robotPic, robotPic2, robotPic3, bulletPic, shieldPic, radarPic,
            playerRobotPic, playerBulletPic, bullet2Pic, bullet3Pic, bullet4Pic, missilePic;
    private ConcurrentLinkedQueue<Robot> robots;
    private ConcurrentLinkedQueue<Bullet> bullets;
    private Intent endIntent;
    private int gameOver, healthChanged;
    private long startTime, endTime;
    private boolean fixStart;
    private double[] robotHp;

    public AnimatedView(Context context, AttributeSet attrs) {
        super(context, attrs);
        h = new Handler();
        endIntent = new Intent(super.getContext(), GameOver.class);
        gameOver = -1;
        healthChanged = 0;
        robotHp = new double[4];
        arena = null;
        robots = null;
        if (!isInEditMode()) {
            missilePic = (BitmapDrawable) ContextCompat.getDrawable(context, R.drawable.missile);
            bulletPic = (BitmapDrawable) ContextCompat.getDrawable(context, R.drawable.bullet);
            bullet2Pic = (BitmapDrawable) ContextCompat.getDrawable(context, R.drawable.bullet_2);
            bullet3Pic = (BitmapDrawable) ContextCompat.getDrawable(context, R.drawable.bullet_3);
            bullet4Pic = (BitmapDrawable) ContextCompat.getDrawable(context, R.drawable.bullet);
            robotPic = (BitmapDrawable) ContextCompat.getDrawable(context, R.drawable.robot);
            robotPic2 = (BitmapDrawable) ContextCompat.getDrawable(context, R.drawable.enemy_robot_2);
            robotPic3 = (BitmapDrawable) ContextCompat.getDrawable(context, R.drawable.enemy_robot_3);
            shieldPic = (BitmapDrawable) ContextCompat.getDrawable(context, R.drawable.shield);
            radarPic = (BitmapDrawable) ContextCompat.getDrawable(context, R.drawable.radar);
            playerRobotPic = (BitmapDrawable) ContextCompat.getDrawable(context, R.drawable.robot_player);
            playerBulletPic = (BitmapDrawable) ContextCompat.getDrawable(context, R.drawable.bullet_player);
            SetUp setUp = new SetUp();
            setUp.create();
            robots = setUp.getRobots();
            arena = new Arena(robots);
            int i = 0;
            for (Robot r : robots) {
                robotHp[i++] = r.getHealth();
            }
            fixStart = false;
        }
    }

    private Runnable r = new Runnable() {
        public void run() {
            invalidate();
            if (gameOver != -1) {
                return;
            }
        }
    };

    protected void onDraw(Canvas c) {
        if (!isInEditMode() && arena != null && gameOver == -1) {

            // draw the arena border for clarity
            Paint borderPaint = new Paint();
            borderPaint.setColor(Color.rgb(0, 0, 0));
            borderPaint.setStrokeWidth(15);
            borderPaint.setStyle(Paint.Style.STROKE);
            c.drawRect(0, 0, (float) Constants.WIDTH, (float) Constants.HEIGHT, borderPaint);

            bullets = arena.getBullets();
            robots = arena.getRobots();
            int i = 0;
            boolean hasChanged = false;
            for (Robot r : robots) {
                if (r.getPicture() == null) {
                    if (r.getId() == 1) {
                        r.setPicture(playerRobotPic);
                    } else if (r.getId() == 2) {
                        r.setPicture(robotPic2);
                    } else if (r.getId() == 3) {
                        r.setPicture(robotPic3);
                    } else {
                        r.setPicture(robotPic);
                    }
                }
                r.draw(c);
                if (r.isShielding()) {
                    c.drawBitmap(shieldPic.getBitmap(), (float) r.getX(), (float) r.getY(), null);
                } else if (r.isDetecting()) {
                    c.drawBitmap(radarPic.getBitmap(), (float) r.getX(), (float) r.getY(), null);
                }

                if (r.getId() == 1) {
                    AnimatedActivity.setHpText("Player: " + r.getHealth(), r.getId());
                } else {
                    AnimatedActivity.setHpText("R[" + r.getId() + "]: " + r.getHealth(), r.getId());
                }

                if (r.getHealth() != robotHp[i]) {
                    hasChanged = true;
                }
                robotHp[i++] = r.getHealth();
            }

            for (Bullet b : bullets) {
                if (b instanceof Missile) {
                    b.setPicture(missilePic);
                    // rotate missile
                    c.save();
                    c.rotate((float) b.getDirection(), (float) b.getX(), (float) b.getY());
                    b.draw(c);
                    c.restore();
                } else if (b instanceof Bullet) {
                    if (b.getPicture() == null) {
                        switch (b.getShotFrom().getId()) {
                            case 1:
                                b.setPicture(playerBulletPic);
                                break;
                            case 2:
                                b.setPicture(bullet2Pic);
                                break;
                            case 3:
                                b.setPicture(bullet3Pic);
                                break;
                            case 4:
                                b.setPicture(bullet4Pic);
                                break;
                            default:
                                b.setPicture(bulletPic);
                                break;
                        }
                    }
                    b.draw(c);
                }
            }
            arena.advanceScripts();
            arena.moveBullets();

            // update all hp
            int[] accessed = new int[4];
            accessed[0] = 0;
            accessed[1] = 0;
            accessed[2] = 0;
            accessed[3] = 0;
            for (Robot r : robots) {
                if (r.getId() == 1) {
                    AnimatedActivity.setHpText("P: " + (int) r.getHealth(), r.getId());
                } else {
                    AnimatedActivity.setHpText("R[" + r.getId() + "]: " + (int) r.getHealth(), r.getId());
                }
                accessed[r.getId() - 1] = 1;
            }
            for (int j = 0; j < accessed.length; j++) {
                if (accessed[j] == 0) {
                    if (j == 0) {
                        AnimatedActivity.setHpText("P: Dead", 1);
                    } else {
                        AnimatedActivity.setHpText("R[" + (j + 1) + "]: Dead", j + 1);
                    }
                }
            }

            // check for a draw due to nothing happening for too long
            if (!hasChanged) {
                healthChanged++;
            } else {
                healthChanged = 0;
            }

            if (healthChanged == Constants.DRAW_TIMER * Constants.FRAME_RATE) {
                startTime = System.nanoTime();
            }

            endTime = System.nanoTime();
            if (startTime != 0 && ((endTime - startTime) / 1000000000) >= Constants.DRAW_TIMER) {
                // force a draw due to inaction
                gameOver = arena.checkTimeoutWinner();
            } else {
                gameOver = arena.checkWin();
            }

            if (gameOver != -1) {
                endIntent.putExtra("winner_data", gameOver + "");
                super.getContext().startActivity(endIntent);
                AnimatedActivity.removeMyView(super.getContext());
            }
        }
        h.postDelayed(r, Constants.FRAME_RATE);
    }

    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        // comment in our out to support screen size changing, don't know why it is!
        Constants.WIDTH = w;
        Constants.HEIGHT = h;
        if (!fixStart && !isInEditMode()) {
            SetUp.fixStartPositions(robots);
            fixStart = true;
        }
        super.onSizeChanged(w, h, oldw, oldh);
    }

}