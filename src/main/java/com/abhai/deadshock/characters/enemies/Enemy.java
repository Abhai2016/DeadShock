package com.abhai.deadshock.characters.enemies;

import com.abhai.deadshock.characters.SpriteAnimation;
import com.abhai.deadshock.Game;
import com.abhai.deadshock.Sounds;
import javafx.geometry.Point2D;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;

public class Enemy extends Pane {

    public SpriteAnimation animation;

    ImageView imgView;
    Point2D velocity;
    Rectangle rectHP;
    public String name;

    private boolean playVoice;

    boolean canSeeBooker;
    boolean delete;
    public boolean pickUpSupply;
    boolean hypnosis;
    byte rand;

    short voiceInterval;
    public int HP;

    Enemy() {
    }

    public boolean isCanSeeBooker() {
        return canSeeBooker;
    }

    public void setCanSeeBooker(boolean canSeeBooker) {
        this.canSeeBooker = canSeeBooker;
    }

    public boolean isDelete() {
        return delete;
    }

    public int getHP() {
        return HP;
    }

    public void setPlayVoice(boolean value) {
        playVoice = value;
    }

    public void setHP(int value) {
        HP = value;
    }

    public Rectangle getRectHP() {
        return rectHP;
    }

    public void setHypnosis(boolean value) {
        hypnosis = value;
    }

    void deathVoice() {
        if (playVoice) {
            byte randVoice = (byte) (Math.random() * 2);
            switch (randVoice) {
                case 0:
                    Sounds.audioClipDie.play(Game.menu.fxSlider.getValue() / 100);
                    break;
                case 1:
                    Sounds.audioClipDie2.play(Game.menu.fxSlider.getValue() / 100);
                    break;
            }
            playVoice = false;
        }
    }

    public void playHitVoice() {
        if (playVoice) {
            rand = (byte) (Math.random() * 3);
            switch (rand) {
                case 0:
                    Sounds.audioClipHit.play(Game.menu.voiceSlider.getValue() / 100);
                    break;
                case 1:
                    Sounds.audioClipHit2.play(Game.menu.voiceSlider.getValue() / 100);
                    break;
                case 2:
                    Sounds.audioClipHit3.play(Game.menu.voiceSlider.getValue() / 100);
                    break;
            }
           playVoice = false;
        }
    }

    public void update() {

    }
}
