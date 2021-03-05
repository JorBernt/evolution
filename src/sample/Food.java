package sample;

import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;


public class Food {

    private final double size;
    private final double xPos;
    private final double yPos;
    private final Color color;
    private Circle body;

    public Food(double size, Color color, double xPos, double yPos) {
        this.size = 2;
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


}
