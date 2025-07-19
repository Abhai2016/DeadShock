package com.abhai.deadshock.characters;

import com.abhai.deadshock.Game;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

import java.nio.file.Paths;

public class Character extends Pane {
    protected static final int SPEED = 4;
    protected static final int WIDTH = 46;
    protected static final int HEIGHT = 69;
    protected static final int START_X = 50;
    protected static final int START_Y = 400;

    protected ImageView imageView;

    public Character() {
        setTranslateX(START_X);
        setTranslateY(START_Y);

        imageView = new ImageView(new Image(
                Paths.get("resources", "images", "characters", getImageName()).toUri().toString()));
        getChildren().add(imageView);
        Game.gameRoot.getChildren().add(this);
    }

    protected String getImageName() {
        return "";
    }
}
