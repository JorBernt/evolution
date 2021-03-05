package sample;

import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.Random;

public class CreatureCreator {
    private final ArrayList<Creature> creatureList;
    private final int creatureNumber;
    private final int childNum = 2;
    private final boolean survivor;
    private final double survivorX;
    private final double survivorY;
    private final int mutationRate;
    private final int startSpeed;
    private final int startSize;
    private final int startSense;
    private boolean bigger = false;
    private boolean faster = false;
    private double sizeChange, speedChange, senseChange;
    private boolean mutate;
    private boolean mutateSpeed;
    private boolean mutateSize;
    private boolean mutateSense;
    private boolean mutateChangeDir;
    private boolean mutateChangeDirNow;

/*
    private double size, speed, energy, xPos, yPos;
    private Color color;
    private int changeDirNow;
    private int dirTime = 1;
    private  int moveTime = 1;
*/


    public CreatureCreator(double size, double speed, double energy, double senseRadius, Color color,
                           int dirTime, int moveTime, int changeDirNow, int creatureNumber, int mutationRate,
                           int startSpeed, int startSize, int startSense,
                           boolean survivor, double survivorX, double survivorY) {
/*        this.size = size;
        this.speed = speed;
        this.energy = energy;
        this.color = color;
        this.xPos = xPos;
        this.yPos = yPos;
        this.moveTime = moveTime;
        this.dirTime = dirTime;
        this.changeDirNow = changeDirNow;*/
        this.creatureNumber = creatureNumber;
        this.survivor = survivor;
        this.survivorX = survivorX;
        this.survivorY = survivorY;
        this.mutationRate = mutationRate;
        this.startSize = startSize;
        this.startSpeed = startSpeed;
        this.startSense = startSense;


        mutateSize = false;
        mutateSpeed = false;
        mutateSense = false;
        mutateChangeDir = false;
        mutateChangeDirNow = false;

        Random random = new Random();
        int mutation = random.nextInt(5);

        if (mutation == 0) {
            mutateSize = true;
        }
        if (mutation == 1) {
            mutateSpeed = true;
        }
        if (mutation == 2) {
            mutateChangeDir = true;
        }
        if (mutation == 3) {
            mutateChangeDirNow = true;
        }
        if (mutation == 4) {
            mutateSense = true;
        }


        creatureList = new ArrayList<Creature>();
        createList(size, speed, energy, senseRadius, color, dirTime, moveTime, changeDirNow, childNum);

    }

    private void createList(double size, double speed, double energy, double senseRadius, Color c,
                            int dirTime, int moveTime, int changeDirNow, int childNum) {


        for (int i = 0; i < creatureNumber; i++) {
            double x = getXpos();
            double y = getYpos();

            if (!survivor) {
                Random random = new Random();
                if (random.nextInt(2) == 0) {
                    if (random.nextInt(2) == 0) {
                        x = 0;
                    } else {
                        x = 600;
                    }
                } else {
                    if (random.nextInt(2) == 0) {
                        y = 0;
                    } else {
                        y = 500;
                    }
                }
            }
            Creature creature = new Creature(getSize(size), getSpeed(speed), getEnergy(energy), getSense(senseRadius),
                    getColor(c), x, y, getDirTime(dirTime), getMoveTime(moveTime), getChangeDirNow(changeDirNow));
            creatureList.add(creature);
        }
    }

    private boolean getMutate() {
        Random random = new Random();
        return random.nextInt(100) <= mutationRate;
    }

    private double getSize(double size) {
        Random ranNum = new Random();
        if (size > 0) {
            if (getMutate() && mutateSize) {


                sizeChange = ranNum.nextInt(1) + ranNum.nextDouble();
                if (ranNum.nextInt(2) == 0) {
                    bigger = true;
                    return size + sizeChange;

                } else {
                    bigger = false;
                    return size - sizeChange;

                }
            } else {
                return size;
            }
        }

        // return ranNum.nextInt(10)+8;
        return startSize;
    }

    private double getSpeed(double speed) {
        Random ranNum = new Random();
        if (speed > 0) {
            if (getMutate() && mutateSpeed) {

                speedChange = ranNum.nextInt(1) + ranNum.nextDouble();

                if (ranNum.nextInt(2) == 0) {
                    faster = true;
                    return speed + speedChange;
                } else {
                    faster = false;
                    return speed - speedChange;
                }
            } else {
                return speed;
            }
        }

        //  return ranNum.nextInt(2) + ranNum.nextDouble();
        return startSpeed;
    }

    private double getSense(double senseRadius) {
        Random random = new Random();
        if (senseRadius > 0) {
            if (getMutate() && mutateSense) {
                senseChange = random.nextInt(2) + random.nextDouble();
                if (random.nextInt(2) == 0) {

                    return senseRadius + senseChange;

                } else {
                    return senseRadius - senseChange;
                }
            } else {
                return senseRadius;
            }
        }
        return startSense + random.nextInt(1);
    }

    private double getEnergy(double energy) {
      /*  Random ranNum = new Random();
        return ranNum.nextInt(2);*/
        return 2;
    }

    private Color getColor(Color c) {
        Color color = c;
        int cChange = 10;
        Random random = new Random();
        if (c != null) {
            int red = (int) (color.getRed() * 255);
            int green = (int) (color.getGreen() * 255);
            int blue = (int) (color.getBlue() * 255);

            if (!faster) {
                speedChange *= -1;
            }
            if (!bigger) {
                sizeChange *= -1;
            }

            try {
                color = Color.rgb(red + ((int) (speedChange * 20)), green, blue + ((int) (sizeChange * 20)));
            } catch (Exception e) {
                color = Color.rgb(red, green, blue);
            }
            return color;


        }

        return Color.rgb(50, 150, 50);
    }

    private double getXpos() {
        if (survivor) {
            return survivorX;
        }
        Random ranNum = new Random();
        return ranNum.nextInt(600);
    }

    private double getYpos() {
        if (survivor) {
            return survivorY;
        }
        Random ranNum = new Random();
        return ranNum.nextInt(500);
    }

    public ArrayList<Creature> getCreatureList() {
        return creatureList;
    }

    private int getDirTime(int dirTime) {
        Random ranNum = new Random();

        if (dirTime > 0) {
            if (getMutate() && mutateChangeDir) {
                if (ranNum.nextInt(2) == 0) {
                    return dirTime + ranNum.nextInt(10);
                } else {
                    return dirTime - ranNum.nextInt(10);
                }
            } else {
                return dirTime;
            }
        }

        return ranNum.nextInt(100);
    }

    private int getMoveTime(int moveTime) {
        Random random = new Random();
        return 3;
    }

    private int getChangeDirNow(int changeDirNow) {
        Random ranNum = new Random();
       /*  if(changeDirNow > 0) {
        if(getMutate() && mutateChangeDirNow) {

                if(ranNum.nextInt(2) == 0) {
                    return changeDirNow + ranNum.nextInt(100);
                }
                else {
                    if(changeDirNow > 100) {
                        return changeDirNow - ranNum.nextInt(100);
                    }
                    else {
                        return changeDirNow;
                    }
                }
            }
        else {
            return changeDirNow;
        }
        }

        */
        //return 1000;
        return ranNum.nextInt(20000) + 1;

    }

    private int getCreatureNumber() {
        return creatureNumber;
    }


}
