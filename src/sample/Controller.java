package sample;

import javafx.animation.AnimationTimer;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

public class Controller {
    private final Stage thisStage = new Stage();
    private final int TIMER = 10;
    private int timer;
    private int t;
    private ArrayList<Creature> creatureList;
    private ArrayList<Creature> survivorList;
    private ArrayList<Food> foodList;
    private ArrayList<SenseIndicator> senseIndicatorsList;
    private Food food;
    private int creatureNumber = 10;
    private int foodNumber = 40;
    private int dayTime;
    private int DAYTIME = 200;
    private int generation = 0;
    private double speedSum = 0;
    private double sizeSum = 0;
    private double energySum = 0;
    private double senseSum = 0;
    private int mutationRate;
    private int startSpeed;
    private int startSize;
    private int startSense;
    private CreatureCreator creatureCreator;
    private Graph speedGraph;
    private Graph populationGraph;
    private Graph sizeGraph;
    private Graph senseGraph;
    private Graph allGraph;
    @FXML
    private AnchorPane mainPane, graphPane, graphPane2;
    @FXML
    private TextField txtNrStart, txtFood, txtDaytime, txtMutationRate, txtStartSize, txtStartSpeed, txtStartSense;
    @FXML
    private Label lblGen, lblSpeed, lblSize, lblEnergy, lblPopulation, lblSense, lblDayTime;


    public Controller() {

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("sample.fxml"));
            loader.setController(this);
            thisStage.setScene(new Scene(loader.load()));
            thisStage.setTitle("Evolution");
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    public void showStage() {
        thisStage.show();
    }

    @FXML
    public void startBtn(ActionEvent event) {

        startSim();
        createGraph();

    }

    private void startSim() {
        creatureNumber = Integer.parseInt(txtNrStart.getText());
        foodNumber = Integer.parseInt(txtFood.getText());
        DAYTIME = Integer.parseInt(txtDaytime.getText());
        mutationRate = Integer.parseInt(txtMutationRate.getText());
        startSize = Integer.parseInt(txtStartSize.getText());
        startSpeed = Integer.parseInt(txtStartSpeed.getText());
        startSense = Integer.parseInt(txtStartSense.getText());

        creatureCreator = new CreatureCreator
                (0, 0, 0, 0, null,
                        0, 0, 0, creatureNumber, mutationRate, startSpeed, startSize,
                        startSense, false, 0, 0);

        senseIndicatorsList = new ArrayList<SenseIndicator>();

        creatureList = creatureCreator.getCreatureList();
        addCreature(creatureList);
        foodList = new ArrayList<Food>();
        addFood();

        t = timer;
        startTimer();
        lblGen.setText("Generation: " + generation);
    }

    private void addCreature(ArrayList<Creature> creatures) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                for (Creature c : creatures) {
                    SenseIndicator senseIndicator =
                            new SenseIndicator(c.getSense() + c.getSize()
                                    , Color.RED, c.getxPos(), c.getyPos(), c.hashCode());
                    senseIndicatorsList.add(senseIndicator);
                }

                for (SenseIndicator si : senseIndicatorsList) {
                    mainPane.getChildren().addAll(si.getBody());
                }

                for (Creature c : creatures) {
                    mainPane.getChildren().add(c.getBody());
                    c.getBody().setCenterY(c.getBody().getCenterY());
                    c.getBody().setCenterX(c.getBody().getCenterX());
                }


            }
        });
    }

    private void addFood() {
        Random random = new Random();


        for (int i = 0; i < foodNumber; i++) {
            int x = random.nextInt(500) + 50;
            int y = random.nextInt(400) + 50;
            food = new Food(5, Color.LIMEGREEN, x, y);
            foodList.add(food);
        }


        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                for (Food f : foodList) {
                    if (f != null) {

                        mainPane.getChildren().add(f.getBody());

                    }
                }
            }
        });
    }

    private void startTimer() {
        t = TIMER;
        dayTime = DAYTIME;
        new AnimationTimer() {
            @Override
            public void handle(long now) {

                t--;
                dayTime--;
                lblDayTime.setText("Daytime: " + dayTime);

                if (t < 0) {

                    for (Creature c : creatureList) {
                        c.move();
                        startFleeing(c);
                        foodCollision(c);
                        eatCreature(c);

                        for (Food f : foodList) {
                            double x = Math.pow(c.getBody().getCenterX() - f.getBody().getCenterX(), 2);
                            double y = Math.pow(c.getBody().getCenterY() - f.getBody().getCenterY(), 2);
                            double distance = Math.sqrt(x + y);


                            if (c.getSense() + f.getBody().getRadius() >= distance) {

                                c.moveToFood(true, f.getBody().getCenterX(), f.getBody().getCenterY(),
                                        c.getBody().getCenterX(), c.getBody().getCenterY());
                                f.getBody().setFill(Color.PINK);
                            }
                        }


                        for (SenseIndicator si : senseIndicatorsList) {
                            if (si.getID() == c.hashCode()) {
                                si.getBody().setCenterX(c.getxPos());
                                si.getBody().setCenterY(c.getyPos());

                            }
                        }
                        if (dayTime < (DAYTIME / 100) * 50) {
                            c.moveHome(true);
                        }
                    }



                    dayOver();

                }

            }

        }.start();

    }

    private void eatCreature(Creature c) {

            for (Creature otherC : creatureList) {
                double x1 = Math.pow(c.getBody().getCenterX() - otherC.getBody().getCenterX(), 2);
                double y1 = Math.pow(c.getBody().getCenterY() - otherC.getBody().getCenterY(), 2);
                double distance1 = Math.sqrt(x1 + y1);

                if (c != otherC && dayTime <= DAYTIME - 20 && !c.isHome() && !otherC.isHome()) {
                    if (c.getBody().getRadius() + otherC.getBody().getRadius() >= distance1) {
                        if (c.getSize() > otherC.getSize() * 1.3 && c.getEnergy() > 0) {

                            removeCreature(otherC);
                            c.eatFood();
                            otherC.eaten();

                            for (SenseIndicator si : senseIndicatorsList) {
                                if (otherC.hashCode() == si.getID()) {
                                    removeSI(si);
                                }
                            }

                        }
                    }
                }
            }
        }


    private void startFleeing(Creature c) {

            for (Creature otherC : creatureList) {
                if (c != otherC) {
                    double x1 = Math.pow(c.getBody().getCenterX() - otherC.getBody().getCenterX(), 2);
                    double y1 = Math.pow(c.getBody().getCenterY() - otherC.getBody().getCenterY(), 2);
                    double distance1 = Math.sqrt(x1 + y1);


                    if (c.getSense() + otherC.getBody().getRadius() > distance1) {
                        if (c.getSize() * 1.2 < otherC.getSize() && !otherC.isHome()) {
                            c.flee(true, getAngle(c, otherC));
                            c.resetFlee(10);
                        }
                    }


                }
            }
        }



    private void foodCollision(Creature c) {

            if (c.getFoodCount() < 100) {
                for (Food f : foodList) {
                    if (c.getBody() != null && food.getBody() != null) {
                        double x = Math.pow(c.getBody().getCenterX() - f.getBody().getCenterX(), 2);
                        double y = Math.pow(c.getBody().getCenterY() - f.getBody().getCenterY(), 2);
                        double distance = Math.sqrt(x + y);
                        if (c.getBody().getRadius() + f.getBody().getRadius() >= distance) {

                            removeFood(f);
                            c.eatFood();


                        }

                    }
                }
            }
        }


    private void removeFood(Food f) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                mainPane.getChildren().remove(f.getBody());
                foodList.remove(f);
            }
        });
    }

    private void removeSI(SenseIndicator si) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                mainPane.getChildren().remove(si.getBody());
                senseIndicatorsList.remove(si);
            }
        });
    }

    private void removeCreature(Creature c) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                mainPane.getChildren().remove(c.getBody());
                creatureList.remove(c);
            }
        });
    }

    private void dayOver() {
        if (dayTime <= 0) {


            survivorList = new ArrayList<Creature>();


            for (Food f : foodList) {
                removeFood(f);
            }
            for (SenseIndicator si : senseIndicatorsList) {
                removeSI(si);
            }
            int survivors = 0;
            double survivorX;
            double survivorY;
            for (Creature c : creatureList) {
                sizeSum += c.getBody().getRadius();
                speedSum += c.getSpeed();
                energySum += c.getEnergy();
                senseSum += c.getSense();

                removeCreature(c);
                survivorX = c.getxPos();
                survivorY = c.getyPos();
                survivors++;
                CreatureCreator survivorCreator = new CreatureCreator
                        (c.getSize(), c.getSpeed(), c.getEnergy(), c.getSense(),
                                c.getColor(), c.getDirTime(),
                                c.getMoveTime(), c.getChangeDirNow(),
                                survivors, mutationRate, startSpeed, startSize, startSense,
                                true, survivorX, survivorY);


                if (c.getFoodCount() >= 1 && ((c.getBody().getCenterY() <= 10 ||
                        c.getBody().getCenterY() >= 490) ||
                        c.getBody().getCenterX() <= 10 || c.getBody().getCenterX() >= 590)) {
                    if (!c.getEaten()) {
                        survivorList.add(survivorCreator.getCreatureList().get(0));
                        if (c.getFoodCount() >= 2) {
                            CreatureCreator childCreator = new CreatureCreator
                                    (c.getSize(), c.getSpeed(), c.getEnergy(), c.getSense(),
                                            c.getColor(), c.getDirTime(),
                                            c.getMoveTime(), c.getChangeDirNow(),
                                            100, mutationRate, startSpeed, startSize, startSense,
                                            false, 0, 0);
                            for (int i = 0; i < 2; i++) {
                                survivorList.add(childCreator.getCreatureList().get(i));

                            }
                        }

                    }

                }

            }
            sizeSum /= creatureList.size();
            lblSize.setText("Avg size: " + String.format("%2f", sizeSum));

            speedSum /= creatureList.size();

            lblSpeed.setText("Avg speed: " + String.format("%2f", speedSum));

            energySum /= creatureList.size();
            lblEnergy.setText("Avg Energy: " + String.format("%2f", energySum));

            senseSum /= creatureList.size();
            lblSense.setText("Avg sense: " + String.format("%2f", senseSum));

            updateGraph(speedSum, sizeSum, senseSum);


            lblPopulation.setText("Population: " + survivorList.size());


            sizeSum = 0;
            energySum = 0;
            speedSum = 0;
            senseSum = 0;

            nextGen();
        }


    }


    private void nextGen() {

        creatureList = survivorList;
        if (creatureList.size() == 0) {
            creatureCreator = new CreatureCreator
                    (0, 0, 0, 0, null,
                            0, 0, 0, creatureNumber, mutationRate, startSpeed, startSize,
                            startSense, false, 0, 0);
            creatureList = creatureCreator.getCreatureList();
            senseGraph.resetGraph();
            speedGraph.resetGraph();
            sizeGraph.resetGraph();
            populationGraph.resetGraph();
            allGraph.resetGraph();

            generation = -1;
        }

        addFood();
        addCreature(creatureList);

        t = TIMER;
        dayTime = DAYTIME;
        generation++;
        lblGen.setText("Generation: " + generation);


    }

    private void createGraph() {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                speedGraph = new Graph("Generations", "Speed");
                sizeGraph = new Graph("Generations", "Size");
                populationGraph = new Graph("Generations:", "Population");
                senseGraph = new Graph("Generation: ", "sense");
                allGraph = new Graph("Generation", "Speed, Size, Sense");
                graphPane.getChildren().addAll(speedGraph.drawGraph(generation, startSpeed),
                        sizeGraph.drawGraph(generation, startSize),
                        populationGraph.drawGraph(generation, creatureNumber));
                graphPane2.getChildren().addAll(allGraph.drawAllGraph(
                        generation, startSpeed, startSize, startSense),
                        senseGraph.drawGraph(generation, startSense));
                senseGraph.getGraph().setTranslateX(450);
                sizeGraph.getGraph().setTranslateX(0);
                sizeGraph.getGraph().setTranslateY(300);
                populationGraph.getGraph().setTranslateY(600);
            }
        });
    }

    private void updateGraph(double speedSum, double sizeSum, double senseSum) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                speedGraph.drawGraph(generation, speedSum);
                sizeGraph.drawGraph(generation, sizeSum);
                senseGraph.drawGraph(generation, senseSum);
                populationGraph.drawGraph(generation, survivorList.size());
                allGraph.drawAllGraph(generation, speedSum, sizeSum, senseSum);
            }
        });
    }

    public float getAngle(Creature target, Creature victim) {
        float angle = (float) Math.toDegrees(Math.atan2(target.getBody().getCenterY()
                - victim.getBody().getCenterY(), target.getBody().getCenterX()
                - victim.getBody().getCenterX()));


        if (angle < 0) {
            angle += 360;
        }


        return angle;
    }

    @FXML
    private void updateBtn(ActionEvent event) {
        foodNumber = Integer.parseInt(txtFood.getText());
        DAYTIME = Integer.parseInt(txtDaytime.getText());
        mutationRate = Integer.parseInt(txtMutationRate.getText());
        startSpeed = Integer.parseInt(txtStartSpeed.getText());
        startSize = Integer.parseInt(txtStartSize.getText());
        startSense = Integer.parseInt(txtStartSense.getText());
    }

    @FXML
    private void testBtn(ActionEvent event) {
        ArrayList<TestCreature> tcl = new ArrayList<TestCreature>();
        ArrayList<TestCreatureControl> tccl = new ArrayList<TestCreatureControl>();
        ArrayList<SenseIndicator> sil = new ArrayList<SenseIndicator>();
        ArrayList<Food> fl = new ArrayList<Food>();


        TestCreature tc = new TestCreature(5, 2, 1000, 100, Color.RED,
                100, 100, 50, 1, 1000);
        TestCreatureControl tcc = new TestCreatureControl(5, Color.BLUE, 300, 250);

        SenseIndicator si = new SenseIndicator(tc.getSense(), Color.GREEN, tc.getxPos(), tc.getyPos(), tc.hashCode());

        Food f = new Food(5, Color.GREEN, 300, 300);
        Food f2 = new Food(5, Color.GREEN, 350, 300);

        tcl.add(tc);
        tccl.add(tcc);
        sil.add(si);
        fl.add(f);
        fl.add(f2);


        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                mainPane.getChildren().addAll(si.getBody(), tc.getBody(), tcc.getBody(), f.getBody(), f2.getBody());

            }
        });
        startTestTimer(tc, tcc, tcl, tccl, si, sil, fl);

    }


    private void startTestTimer(TestCreature testCreature, TestCreatureControl testCreatureControl,
                                ArrayList<TestCreature> tcl, ArrayList<TestCreatureControl> tccl,
                                SenseIndicator si, ArrayList<SenseIndicator> sil,
                                ArrayList<Food> fl) {
        t = TIMER;
        dayTime = DAYTIME;

        new AnimationTimer() {
            @Override
            public void handle(long now) {

                mainPane.setOnMouseMoved(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent event) {
                        moveTestController(event.getX(), event.getY(), testCreatureControl);
                    }
                });


                for (TestCreature tc : tcl) {
                    if (tc != null) {
                        tc.move();
                        si.move(tc.getBody().getCenterX(), tc.getBody().getCenterY());
                        for (TestCreatureControl tcc : tccl) {
                            if (tcc != null) {
                                double x = Math.pow(tc.getBody().getCenterX() - tcc.getBody().getCenterX(), 2);
                                double y = Math.pow(tc.getBody().getCenterY() - tcc.getBody().getCenterY(), 2);
                                double distance = Math.sqrt(x + y);


                                tc.flee(tc.getSense() + tc.getBody().getRadius() + tcc.getBody().getRadius() >= distance, getAngle(tc, tcc));
                            }
                        }

                        for (Food f : fl) {
                            tc.moveToFood(true, f.getBody().getCenterX(), f.getBody().getCenterY(),
                                    tc.getBody().getCenterX(), tc.getBody().getCenterY());
                            double x = Math.pow(tc.getBody().getCenterX() - f.getBody().getCenterX(), 2);
                            double y = Math.pow(tc.getBody().getCenterY() - f.getBody().getCenterY(), 2);
                            double distance = Math.sqrt(x + y);


                            tc.moveToFood(tc.getSense() + tc.getBody().getRadius() + f.getBody().getRadius() >= distance, f.getBody().getCenterX(), f.getBody().getCenterY(),
                                    tc.getBody().getCenterX(), tc.getBody().getCenterY());

                            if (tc.getBody().getRadius() + f.getBody().getRadius() >= distance) {
                                Platform.runLater(new Runnable() {
                                    @Override
                                    public void run() {
                                        mainPane.getChildren().remove(f.getBody());
                                        fl.remove(f);
                                        tc.stopMovingToFood();

                                    }
                                });
                            }

                        }
                    }
                }


            }

        }.start();


    }

    public float getAngle(TestCreature target, TestCreatureControl player) {
        float angle = (float) Math.toDegrees(Math.atan2(target.getBody().getCenterY()
                - player.getBody().getCenterY(), target.getBody().getCenterX()
                - player.getBody().getCenterX()));


        if (angle < 0) {
            angle += 360;
        }


        return angle;
    }

    public float getAngle(TestCreature target, Food f) {
        float angle = (float) Math.toDegrees(Math.atan2(target.getBody().getCenterY()
                - f.getBody().getCenterY(), target.getBody().getCenterX()
                - f.getBody().getCenterX()));


        if (angle < 0) {
            angle += 360;
        }


        return angle;
    }

    private void moveTestController(double x, double y, TestCreatureControl controller) {
        controller.move(x, y);
    }
}
