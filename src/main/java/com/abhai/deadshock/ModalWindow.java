package com.abhai.deadshock;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.nio.file.Path;
import java.nio.file.Paths;

class ModalWindow {

    static void createNewWindows(String title) {
        Stage stage = new Stage();
        stage.setWidth(200);
        stage.setHeight(100);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setResizable(false);

        Pane root = new Pane();
        Text text = new Text("Вы уверены?");
        text.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        text.setFill(Color.BLACK);
        text.setTranslateX(30);
        text.setTranslateY(20);

        Text text2 = new Text("Все сохранения буду потеряны!");
        text2.setFont(Font.font("Arial", FontWeight.BOLD, 12));
        text2.setFill(Color.BLACK);
        text2.setTranslateY(40);

        Button yes = new Button("Да");
        yes.setPrefWidth(50);


        yes.setTranslateY(50);

        Button no = new Button("Нет");
        no.setPrefWidth(50);
        no.setTranslateY(50);
        no.setTranslateX(stage.getWidth() - 30 - no.getPrefWidth());
        root.getChildren().addAll(text, text2, yes, no);

        yes.setOnMouseClicked( event -> {
            Game.menu.booleanNewGame = true;
            stage.close();
        });
        no.setOnMouseClicked( event -> {
            Game.menu.booleanNewGame = false;
            stage.close();
        });
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle(title);
        Path imagePath = Paths.get("resources", "images", "icons", "icon.jpg");
        stage.getIcons().add(new Image(imagePath.toUri().toString()));
        stage.showAndWait();
    }
}
