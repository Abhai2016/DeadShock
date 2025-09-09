package com.abhai.deadshock.energetics;

import com.abhai.deadshock.Game;
import com.abhai.deadshock.characters.enemies.Enemy;
import com.abhai.deadshock.types.DifficultyType;
import com.abhai.deadshock.utils.GameMedia;
import com.abhai.deadshock.utils.Texts;
import com.abhai.deadshock.world.levels.Level;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class Hypnosis {
    private final Text intervalText;
    private final Rectangle intervalRect;

    private double interval;
    private boolean hypnotized;
    protected double maxInterval;

    public Hypnosis() {
        interval = 0;
        hypnotized = false;
        setDifficultyLevel();

        intervalText = new Text(Texts.HYPNOSIS_DURATION);
        intervalText.setFont(Font.font("Aria", 28));
        intervalText.setFill(Color.WHITE);
        intervalText.setVisible(false);
        Game.getAppRoot().getChildren().add(intervalText);

        intervalRect = new Rectangle(300, 3, Color.WHITE);
        intervalRect.setVisible(false);
        Game.getAppRoot().getChildren().add(intervalRect);
    }

    public void delete() {
        hypnotized = false;
        unhypnotizeTheTarget();
        intervalRect.setWidth(300);
        intervalRect.setVisible(false);
        intervalText.setVisible(false);
    }

    public void hypnotize() {
        interval = 0;
        hypnotized = true;
        hypnotizeTheTarget();

        intervalRect.setWidth(300);
        if (Game.getGameWorld().getLevel().getCurrentLevelNumber() != Level.BOSS_LEVEL) {
            intervalRect.setTranslateX(Game.SCENE_WIDTH / 2 - intervalRect.getWidth() / 2);
            intervalRect.setTranslateY(40);
        } else {
            intervalRect.setTranslateX(Game.SCENE_WIDTH / 2 - intervalRect.getWidth() / 2 + 20);
            intervalRect.setTranslateY(80);
        }
        intervalRect.setVisible(true);


        if (Game.getGameWorld().getLevel().getCurrentLevelNumber() != Level.BOSS_LEVEL) {
            intervalText.setTranslateX(Game.SCENE_WIDTH / 2 - intervalRect.getWidth() / 2 + 25);
            intervalText.setTranslateY(30);
        } else {
            intervalText.setTranslateX(Game.SCENE_WIDTH / 2 - intervalRect.getWidth() / 2 + 25);
            intervalText.setTranslateY(70);
        }
        intervalText.setVisible(true);
    }

    protected void setDifficultyLevel() {
        switch (Game.getGameWorld().getDifficultyType()) {
            case DifficultyType.MARIK -> maxInterval = 550;
            case DifficultyType.EASY -> maxInterval = 450;
            case DifficultyType.MEDIUM -> maxInterval = 350;
            case DifficultyType.HARD -> maxInterval = 250;
            case DifficultyType.HARDCORE -> maxInterval = 150;
        }
    }

    protected void hypnotizeTheTarget() {
        for (Enemy enemy : Game.getGameWorld().getEnemies())
            enemy.setHypnotized(true);
        GameMedia.AUDIO_CLIP.play(Game.getGameWorld().getMenu().getFxSlider().getValue() / 100);
    }

    protected void unhypnotizeTheTarget() {
        for (Enemy enemy : Game.getGameWorld().getEnemies())
            enemy.setHypnotized(false);
    }

    public void update() {
        if (hypnotized) {
            interval++;
            intervalRect.setWidth((maxInterval - interval) / maxInterval * 300);

            if (interval > maxInterval) {
                interval = 0;
                hypnotized = false;
                unhypnotizeTheTarget();

                intervalRect.setWidth(300);
                intervalRect.setVisible(false);
                intervalText.setVisible(false);
            }
        }
    }
}
