package com.abhai.deadshock;

import com.abhai.deadshock.dtos.MenuOptionsDTO;
import com.abhai.deadshock.utils.Controller;
import com.abhai.deadshock.utils.SaveManager;
import com.abhai.deadshock.world.GameWorld;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.nio.file.Paths;

public class Game extends Application {
    public static final int SCENE_WIDTH = 1280;
    public static final int SCENE_HEIGHT = 720;
    public static final AnimationTimer timer = new AnimationTimer() {
        @Override
        public void handle(long now) {
            gameWorld.update();
        }
    };

    private static Pane appRoot;
    private static Pane gameRoot;
    private static GameWorld gameWorld;
    private static SaveManager saveManager;

    @Override
    public void start(Stage primaryStage) {
        appRoot = new Pane();
        gameRoot = new Pane();
        gameWorld = new GameWorld();
        saveManager = new SaveManager();
        appRoot.getChildren().add(gameRoot);
        gameWorld.init(saveManager.loadProgress(), saveManager.getEnemies());

        MenuOptionsDTO menuOptionsDTO = saveManager.loadMenuOptions();
        if (menuOptionsDTO != null)
            gameWorld.initializeMenuOptions(menuOptionsDTO);

        Scene scene = new Scene(appRoot, SCENE_WIDTH, SCENE_HEIGHT);
        scene.addEventFilter(KeyEvent.KEY_PRESSED, event -> Controller.keys.put(event.getCode(), true));
        scene.addEventFilter(KeyEvent.KEY_RELEASED, event -> Controller.keys.put(event.getCode(), false));

        primaryStage.getIcons().add(new Image(Paths.get("resources", "images", "icons", "icon.jpg").toUri().toString()));
        primaryStage.setTitle("DeadShock");
        primaryStage.setResizable(false);
        primaryStage.setScene(scene);
        primaryStage.show();
        timer.start();
    }

    public static void main(String[] args) {
        launch(args);
    }

    public static Pane getAppRoot() {
        return appRoot;
    }

    public static Pane getGameRoot() {
        return gameRoot;
    }

    public static GameWorld getGameWorld() {
        return gameWorld;
    }

    public static SaveManager getSaveManager() {
        return saveManager;
    }
}