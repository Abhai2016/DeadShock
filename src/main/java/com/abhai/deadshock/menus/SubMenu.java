package com.abhai.deadshock.menus;

import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class SubMenu extends VBox {

    SubMenu() {}

    void addLabel(Label label) {
        label.setTextFill(Color.WHITE);
        label.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        getChildren().add(label);
    }

    void addCustomButtons(CustomButton... customButtons) {
        setSpacing(15);
        setTranslateX(50);
        setTranslateY(100);
        getChildren().addAll(customButtons);
    }
}
