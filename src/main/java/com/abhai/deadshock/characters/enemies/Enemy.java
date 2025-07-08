package com.abhai.deadshock.characters.enemies;

import com.abhai.deadshock.Game;
import com.abhai.deadshock.utils.Sounds;
import javafx.geometry.Point2D;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

public class Enemy extends Pane {
    protected ImageView imageView;
    protected Point2D velocity;
    protected EnemyType type;

    protected boolean toDelete = false;
    protected boolean hypnotized = false;

    protected int HP = 100;
    protected int voiceInterval = 0;

    Enemy() {
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

    protected void deathVoice() {
        switch ((int) (Math.random() * 2)) {
            case 0:
                Sounds.death.play(Game.menu.fxSlider.getValue() / 100);
            case 1:
                Sounds.death2.play(Game.menu.fxSlider.getValue() / 100);
        }
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
