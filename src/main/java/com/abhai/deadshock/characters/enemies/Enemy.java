package com.abhai.deadshock.characters.enemies;

import com.abhai.deadshock.Game;
import com.abhai.deadshock.types.EnemyType;
import com.abhai.deadshock.types.SupplyType;
import com.abhai.deadshock.utils.GameMedia;
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

    protected int HP;
    protected boolean toDelete;
    protected int voiceInterval;
    protected boolean hypnotized;

    Enemy() {
        toDelete = false;
        hypnotized = false;
        voiceInterval = 0;

        velocity = new Point2D(0, GRAVITY);
        imageView = new ImageView(new Image(
                Paths.get("resources", "images", "characters", getImageName()).toUri().toString()));
        imageView.setViewport(new Rectangle2D(0, 0, WIDTH, HEIGHT));

        getChildren().add(imageView);
    }

    public void update() {
    }

    public void playHitVoice() {
        switch ((int) (Math.random() * 3)) {
            case 0 -> GameMedia.AUDIO_CLIP_HIT.play(Game.getGameWorld().getMenu().getVoiceSlider().getValue() / 100);
            case 1 -> GameMedia.AUDIO_CLIP_HIT_2.play(Game.getGameWorld().getMenu().getVoiceSlider().getValue() / 100);
            case 2 -> GameMedia.AUDIO_CLIP_HIT_3.play(Game.getGameWorld().getMenu().getVoiceSlider().getValue() / 100);
        }
    }

    public int getHP() {
        return HP;
    }

    protected void closeCombat() {
        setTranslateX(getTranslateX() - getScaleX());
        Game.getGameWorld().getBooker().closeCombat(getScaleX());
    }

    public void init(int x, int y) {
        toDelete = false;
        hypnotized = false;
        voiceInterval = 0;
        velocity = new Point2D(0, GRAVITY);

        setTranslateX(x);
        setTranslateY(y);
        Game.getGameRoot().getChildren().add(this);
    }

    protected void playDeathVoice() {
        switch ((int) (Math.random() * 2)) {
            case 0 -> GameMedia.DEATH.play(Game.getGameWorld().getMenu().getFxSlider().getValue() / 100);
            case 1 -> GameMedia.DEATH_2.play(Game.getGameWorld().getMenu().getFxSlider().getValue() / 100);
        }
    }

    public EnemyType getType() {
        return type;
    }

    public void setHP(int value) {
        HP = value;
    }

    protected String getImageName() {
        return "";
    }

    public boolean isToDelete() {
        return toDelete;
    }

    public boolean isHypnotized() {
        return hypnotized;
    }

    public void setHypnotized(boolean value) {
        hypnotized = value;
    }

    protected SupplyType getSupplyType() {
        return SupplyType.MEDICINE;
    }
}
