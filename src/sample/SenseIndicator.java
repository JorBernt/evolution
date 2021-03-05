package sample;

import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;


public class SenseIndicator {

    private final double size;
    private final double xPos;
    private final double yPos;
    private final Color color;
    private final int id;
    private Circle body;

    public SenseIndicator(double size, Color color, double xPos, double yPos, int id) {
        this.size = size;
        this.color = color;
        this.xPos = xPos;
        this.yPos = yPos;
        this.id = id;


        drawBody();
    }

    private void drawBody() {
        body = new Circle();
        body.setRadius(size);
        body.setFill(Color.rgb(200, 0, 0, 0.2));
        body.setCenterX(xPos);
        body.setCenterY(yPos);
        body.setTranslateZ(1);
    }

    public Circle getBody() {
        return body;
    }


    public void move(double x, double y) {
        body.setCenterX(x);
        body.setCenterY(y);
    }

    public int getID() {
        return id;
    }


}
