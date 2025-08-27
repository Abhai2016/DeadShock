package com.abhai.deadshock.menus;

import javafx.animation.Animation;
import javafx.animation.FillTransition;
import javafx.geometry.Pos;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.util.Duration;

public class CustomButton extends StackPane {

    CustomButton(String name) {
        Rectangle rectangle = new Rectangle(200, 20, Color.WHITE);
        rectangle.setOpacity(0.5);

        Text text = new Text(name);
        text.setFill(Color.WHITE);
        text.setFont(Font.font("Arial", FontWeight.BOLD, 14));

        setAlignment(Pos.CENTER);
        getChildren().addAll(rectangle, text);
        addListeners(rectangle);
    }

    private void addListeners(Rectangle rectangle) {
        FillTransition fillTransition = new FillTransition(Duration.seconds(0.5), rectangle);
        setOnMouseEntered(event -> {
            fillTransition.setFromValue(Color.WHITE);
            fillTransition.setToValue(Color.DARKGOLDENROD);
            fillTransition.setCycleCount(Animation.INDEFINITE);
            fillTransition.setAutoReverse(true);
            fillTransition.play();
        });

        setOnMouseExited(event -> {
            fillTransition.stop();
            rectangle.setFill(Color.WHITE);
        });
    }
}
