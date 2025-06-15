package com.abhai.deadshock.energetics;

import com.abhai.deadshock.characters.enemies.Enemy;
import com.abhai.deadshock.Game;
import com.abhai.deadshock.levels.Level;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class Hypnosis {
    private Rectangle intervalRect = new Rectangle(300, 3, Color.WHITE);
    private Text intervalText = new Text("Длительность гипноза");

    private double interval = 0;
    private double maxInterval = 0;
    private boolean hypnosis = false;


    Hypnosis() {
        switch(Game.difficultyLevelText) {
            case "marik":
                maxInterval = 600;
                break;
            case "easy":
                maxInterval = 450;
                break;
            case "normal":
                maxInterval = 300;
                break;
            case "high":
                maxInterval = 150;
                break;
            case "hardcore":
                maxInterval = 150;
                break;
        }
        intervalText.setFont( Font.font("Aria", 28) );
        intervalText.setFill(Color.WHITE);
        intervalText.setVisible(false);
        Game.appRoot.getChildren().add(intervalText);

        intervalRect.setVisible(false);
        Game.appRoot.getChildren().add(intervalRect);
    }

    boolean isHypnosis() {
        return hypnosis;
    }

    void setHypnosis() {
        if (Game.levelNumber == Level.BOSS_LEVEL)
            Game.boss.setHypnosis(true);
        for(Enemy enemy : Game.enemies)
            enemy.setHypnosis(true);
        hypnosis = true;
        interval = 0;

        intervalRect.setWidth(300);
        if (Game.levelNumber != Level.BLOCK_SIZE) {
            intervalRect.setTranslateX(Game.appRoot.getWidth() / 2 - intervalRect.getWidth() / 2);
            intervalRect.setTranslateY(40);
        } else {
            intervalRect.setTranslateX(Game.appRoot.getWidth() / 2 - intervalRect.getWidth() / 2 + 20);
            intervalRect.setTranslateY(80);
        }
        intervalRect.setVisible(true);


        if (Game.levelNumber != Level.BOSS_LEVEL) {
            intervalText.setTranslateX(Game.appRoot.getWidth() / 2 - intervalRect.getWidth() / 2 + 25);
            intervalText.setTranslateY(30);
        }
        else {
            intervalText.setTranslateX(Game.appRoot.getWidth() / 2 - intervalRect.getWidth() / 2 + 25);
            intervalText.setTranslateY(70);
        }
        intervalText.setVisible(true);
    }

    void setHypnosisForBooker() {
        intervalRect.setWidth(300);
        intervalRect.setTranslateX(Game.appRoot.getWidth() / 2 - intervalRect.getWidth() / 2 + 20);
        intervalRect.setTranslateY(80);
        intervalRect.setVisible(true);

        intervalText.setTranslateX(Game.appRoot.getWidth() / 2 - intervalRect.getWidth() / 2 + 25);
        intervalText.setTranslateY(70);
        intervalText.setVisible(true);
    }

    void updateHypnosis() {
        intervalRect.setWidth(300 - Game.booker.getStunnedInterval() / 180 * 300);
    }

    void deleteDeleteHypnosis() {
        intervalRect.setWidth(300);
        intervalRect.setVisible(false);
        intervalText.setVisible(false);
    }

    public void update() {
        interval++;
        intervalRect.setWidth((maxInterval - interval) / maxInterval * 300);

        if (interval > maxInterval) {
            if (Game.levelNumber == Level.BOSS_LEVEL)
                Game.boss.setHypnosis(false);
            for (Enemy enemy : Game.enemies)
                enemy.setHypnosis(false);
            hypnosis = false;
            interval = 0;

            intervalRect.setWidth(300);
            intervalRect.setVisible(false);
            intervalText.setVisible(false);
        }
    }
}
