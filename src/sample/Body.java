package sample;

import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class Body extends Group {
    Circle body;

    public Body(double x, double y, double size, Color color) {
        this.body = createElement(x, y, size, color);
        Circle lEye = createElement(body.getCenterX() + 1, 0, 3, Color.BLACK);
        Circle rEye = createElement(body.getCenterY() + 1, 0.4, 3, Color.BLACK);


        getChildren().addAll(body, lEye, rEye);
        body.setTranslateX(x);
        body.setTranslateY(y);

    }

    private Circle createElement(double x, double y, double size, Color color) {
        Circle ball = new Circle();
        ball.setTranslateX(0);
        ball.setTranslateY(0);
        ball.setRadius(size);
        ball.setFill(color);
        return ball;
    }

    public double getRadius() {
        return body.getRadius();
    }
}
