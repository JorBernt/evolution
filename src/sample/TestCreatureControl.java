package sample;

import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;


public class TestCreatureControl {

    private final double size;
    private final double xPos;
    private final double yPos;
    private final Color color;
    private Circle body;

    public TestCreatureControl(double size, Color color, double xPos, double yPos) {
        this.size = size;
        this.color = color;
        this.xPos = xPos;
        this.yPos = yPos;

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


    public void move(double x, double y) {
        body.setCenterX(x);
        body.setCenterY(y);
    }


}
