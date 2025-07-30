package com.abhai.deadshock.characters.enemies;

import com.abhai.deadshock.Game;
import com.abhai.deadshock.utils.Sounds;
import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

import java.nio.file.Paths;

public class Enemy extends Pane {
    protected static final int WIDTH = 62;
    protected static final int HEIGHT = 65;
    protected static final int GRAVITY = 10;

    protected EnemyType type;
    protected Point2D velocity;
    protected ImageView imageView;

    protected boolean toDelete;
    protected boolean hypnotized;

    protected int HP;
    protected int voiceInterval;

    Enemy() {
        toDelete = false;
        hypnotized = false;
        voiceInterval = 0;

        velocity = new Point2D(0, GRAVITY);
        imageView = new ImageView(new Image(
                Paths.get("resources", "images", "characters", getImageName()).toUri().toString()));
        imageView.setViewport(new Rectangle2D(0, 0, WIDTH, HEIGHT));

        getChildren().add(imageView);
        Game.gameRoot.getChildren().add(this);
    }

    protected void closeCombat() {
        setTranslateX(getTranslateX() - getScaleX());
        if (Game.booker.isBooleanVelocityX()) {
            Game.booker.closeCombat(true);
            Sounds.closeCombat.play(Game.menu.fxSlider.getValue() / 100);
        }
    }

    protected void playDeathVoice() {
        switch ((int) (Math.random() * 2)) {
            case 0 -> Sounds.death.play(Game.menu.fxSlider.getValue() / 100);
            case 1 -> Sounds.death2.play(Game.menu.fxSlider.getValue() / 100);
        }
    }

    protected String getImageName() {
        return "";
    }

    public boolean isToDelete() {
        return toDelete;
    }

    public int getHP() {
        return HP;
    }

    public void setHP(int value) {
        HP = value;
    }

    public void setHypnotized(boolean value) {
        hypnotized = value;
    }

    public boolean isCamper() {
        return type.equals(EnemyType.CAMPER);
    }

    public void playHitVoice() {
        switch ((int) (Math.random() * 3)) {
            case 0 -> Sounds.audioClipHit.play(Game.menu.voiceSlider.getValue() / 100);
            case 1 -> Sounds.audioClipHit2.play(Game.menu.voiceSlider.getValue() / 100);
            case 2 -> Sounds.audioClipHit3.play(Game.menu.voiceSlider.getValue() / 100);
        }
    }

    public void update() {

    }
}
