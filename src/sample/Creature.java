package sample;

import javafx.animation.AnimationTimer;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

import java.util.Random;

import static java.lang.Math.cos;
import static java.lang.Math.sin;

public class Creature {
    private final double size;
    private final double speed;
    private final double xPos;
    private final double yPos;
    private final Color color;
    private final int changeDirNow;
    private final int timer = 100;
    private final double[] myPos = new double[2];
    private final double[] enemyPos = new double[2];
    private final double[] xPosList;
    private final double[] yPosList;
    private final double senseRadius;
    private final boolean fleeing;
    private double energy;
    private Circle body;
    private double direction;
    private boolean changeDir;
    private int foodCount = 0;
    private int dirTime = 1;
    private final int dT = dirTime;
    private double currentDir;
    private double newDir;
    private double enemyAngle;
    private double foodAngle;
    private int t;
    private int moveTime = 1;
    private final int mT = moveTime;
    private boolean movingHome = false;
    private boolean eaten = false;
    private boolean first;
    private boolean moving;
    private boolean isHome;
    private boolean currentlyFleeing;
    private boolean currentlyMovingToFood;


    public Creature(double size, double speed, double energy, double senseRadius, Color color, double xPos, double yPos,
                    int dirTime, int moveTime, int changeDirNow) {
        this.size = size;
        this.speed = speed;
        this.energy = energy;
        this.color = color;
        this.xPos = xPos;
        this.yPos = yPos;
        this.moveTime = moveTime;
        this.dirTime = dirTime;
        this.changeDirNow = changeDirNow;
        this.senseRadius = senseRadius;
        first = true;
        moving = false;
        isHome = false;
        fleeing = false;
        currentlyFleeing = false;

        xPosList = new double[1000];
        yPosList = new double[1000];
        t = 0;

        for (int i = 0; i < xPosList.length; i++) {
            Random random = new Random();
            xPosList[i] = random.nextInt(600);
            yPosList[i] = random.nextInt(500);
        }

        /*System.out.println("Size: " + size);
        System.out.println("Speed: " + speed);
        System.out.println("moveTime: " + moveTime);
        System.out.println("dirTime: " + dirTime);
        System.out.println("changeDirNow: " + changeDirNow);
        System.out.println("Food: " + foodCount);
*/
        drawBody();
    }

    private void drawBody() {
        body = new Circle();
        body.setRadius(size);
        body.setFill(color);
        body.setCenterX(xPos);
        body.setCenterY(yPos);

    }

    public Circle getBody() {
        return body;
    }

    public double getSize() {
        return size;
    }

    public double getSpeed() {
        return speed;
    }

    public double getEnergy() {
        return energy;
    }

    public Color getColor() {
        return color;
    }

    public int getDirTime() {
        return dirTime;
    }

    public int getMoveTime() {
        return moveTime;
    }

    public int getChangeDirNow() {
        return changeDirNow;
    }

    public double getSense() {
        return senseRadius;
    }

    public double getxPos() {
        return body.getCenterX();
    }

    public double getyPos() {
        return body.getCenterY();
    }

    public void flee(boolean fleeing, double enemyAngle) {
        if (fleeing) {
            this.enemyAngle = enemyAngle;
            currentlyFleeing = true;
        } else {
            currentlyFleeing = false;
        }
    }

    public void resetFlee(int time) {
        new AnimationTimer() {
            int t = time;

            @Override
            public void handle(long now) {
                t--;
                if (t <= 0) {
                    currentlyFleeing = false;
                    stop();
                }
            }
        }.start();


    }

    public void moveToFood(boolean movingToFood, double x, double y, double x1, double y1) {
        if (movingToFood) {
            enemyPos[0] = x;
            enemyPos[1] = y;
            myPos[0] = x1;
            myPos[1] = y1;

            currentlyMovingToFood = true;
        } else {
            currentlyMovingToFood = false;
        }
    }


    private double getDirection() {
        Random random = new Random();

        int r = random.nextInt(changeDirNow);

        if ((r < 100 || first) && !movingHome && !currentlyFleeing && !currentlyMovingToFood) {
            if (first) {

                if (body.getCenterX() <= 10) {
                    direction = 0;
                } else if (body.getCenterX() >= 590) {
                    direction = 180;
                } else if (body.getCenterY() <= 10) {
                    direction = 90;
                } else if (body.getCenterY() >= 490) {
                    direction = 270;
                }
                newDir = random.nextInt(360);
                currentDir = direction;
                first = false;
            } else {
                if (t < 1000) {
                    t++;
                }

                float angle = (float) Math.toDegrees(Math.atan2(yPosList[t] - getBody().getCenterY(),
                        xPosList[t] - getBody().getCenterX()));


                if (angle < 0) {
                    angle += 360;
                }

                newDir = angle;
                currentDir = direction;

            }

        } else if (movingHome && !moving && !currentlyFleeing) {


            double[] m = {body.getCenterX(), 600 - body.getCenterX(), body.getCenterY(), 500 - body.getCenterY()};
            double min = m[0];
            for (int i = 0; i < 4; i++) {
                if (min > m[i]) {
                    min = m[i];
                }
            }


            for (int i = 0; i < 4; i++) {
                if (m[i] == min) {
                    if (i == 1) {
                        moving = true;
                        return Math.toRadians(180);
                    } else if (i == 0) {
                        moving = true;
                        return Math.toRadians(0);
                    } else if (i == 3) {
                        moving = true;
                        return Math.toRadians(270);
                    } else if (i == 2) {
                        moving = true;
                        return Math.toRadians(90);
                    }
                }
            }


        }

        if (currentlyFleeing) {
            body.setFill(Color.YELLOW);
            direction = (Math.toRadians(enemyAngle) + Math.PI) % (2 * Math.PI) - Math.PI;
            currentDir = Math.toDegrees(direction);
            newDir = Math.toDegrees(direction);
            return direction;

        } else {
            body.setFill(color);
        }

        if (currentlyMovingToFood && !movingHome && !currentlyFleeing) {

            float angle = (float) Math.toDegrees(Math.atan2(enemyPos[1]
                    - myPos[1], enemyPos[0] - myPos[0]));

            if (angle < 0) {
                angle += 360;
            }
            foodAngle = angle;


            return Math.toRadians(foodAngle);
        }

        if (!fleeing) {
            int turnRate = 5;
            if (currentDir <= newDir) {
                currentDir += turnRate;
            } else {
                currentDir -= turnRate;
            }

            direction = currentDir;
        }


        return Math.toRadians(direction);

    }


    public void eatFood() {
        foodCount += 1;
    }

    public boolean getEaten() {
        return eaten;
    }

    public void eaten() {
        eaten = true;
    }

    public int getFoodCount() {
        return foodCount;
    }

    public void resetFood() {
        foodCount = 0;
    }

    public boolean isHome() {
        return isHome;
    }

    public void stopMovingToFood() {
        currentlyMovingToFood = false;
    }


    public void move() {


        if (foodCount > 2) {
            moveHome(false);

        }

        if (foodCount > 0) {
            if (body.getCenterX() < 10) {
                isHome = true;
            } else if (body.getCenterX() > 590) {
                isHome = true;
            } else if (body.getCenterY() < 10) {
                isHome = true;
            } else if (body.getCenterY() > 490) {
                isHome = true;
            }
        }


        energy -= (Math.pow(speed, 2) + Math.pow(size, 3) + Math.pow(senseRadius, 2)) / 100000;

        if (energy > 0 && !isHome) {

            double x = 0;
            double y = 0;

            double radAngle = getDirection();
            x += speed * cos(radAngle);
            y += speed * sin(radAngle);

            if (body.getCenterX() + x <= 600 && body.getCenterX() + x >= 0) {

                body.setCenterX(body.getCenterX() + x);


            }
            if (body.getCenterY() + y <= 500 && body.getCenterY() + y >= 0) {

                body.setCenterY(body.getCenterY() + y);


            }


        }


    }

    public void moveHome(boolean timeOver) {


        Random random = new Random();
        if (!movingHome) {
            if (energy * random.nextInt(100) < 100) {
                if (foodCount > 0) {
                    movingHome = true;
                }
            } else if (timeOver && foodCount > 0) {
                movingHome = true;
            }
        }


    }
}
