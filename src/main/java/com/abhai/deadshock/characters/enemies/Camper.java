package com.abhai.deadshock.characters.enemies;

import com.abhai.deadshock.Game;
import com.abhai.deadshock.levels.Block;
import com.abhai.deadshock.utils.Sounds;
import javafx.geometry.Point2D;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.nio.file.Paths;

public class Camper extends Enemy {
    private int moveInterval = 0;

    public Camper(long x, long y) {
        setWidth(33);
        setHeight(65);

        imageView = new ImageView(new Image(
                Paths.get("resources", "images", "characters", "camper.png").toUri().toString()));
        velocity = new Point2D(0, 10);

        type = EnemyType.CAMPER;
        HP = 500;

        setTranslateX(x);
        setTranslateY(y);
        getChildren().add(imageView);
    }

    private void moveY(double y) {
        for (int i = 0; i < Math.abs(y); i++) {
            if (y > 0)
                setTranslateY(getTranslateY() + 1);
            else
                setTranslateY(getTranslateY() - 1);

            for (Block block : Game.blocks)
                if (getBoundsInParent().intersects(block.getBoundsInParent())) {
                    setTranslateY(getTranslateY() - 1);
                    return;
                }
        }
    }

    private void playCamperVoice() {
        Sounds.audioClipCamper.play(Game.menu.fxSlider.getValue() / 100);
        switch ((int) (Math.random() * 3)) {
            case 0 -> Sounds.bookerHit.play(Game.menu.fxSlider.getValue() / 100);
            case 1 -> Sounds.bookerHit2.play(Game.menu.fxSlider.getValue() / 100);
            case 2 -> Sounds.bookerHit3.play(Game.menu.fxSlider.getValue() / 100);
        }
        Game.booker.setHP(Game.booker.getHP() - 10);
        voiceInterval = 0;
    }

    private void camperBehave() {
        if (Sounds.audioClipCamper.isPlaying()) {
            moveInterval++;
            if (moveInterval < 10)
                setTranslateX(getTranslateX() + 1);
            else if (moveInterval < 20)
                setTranslateX(getTranslateX() - 1);
            else
                moveInterval = 0;
        }

        if (Game.booker.getTranslateX() > getTranslateX() - 750 && Game.booker.getTranslateX() < getTranslateX()) {
            voiceInterval++;
            setScaleX(1);
        } else if (Game.booker.getTranslateX() < getTranslateX() + 650 && Game.booker.getTranslateX() > getTranslateX()) {
            voiceInterval++;
            setScaleX(-1);
        } else if (Game.booker.getTranslateX() == getTranslateX()) {
            voiceInterval++;
        } else
            voiceInterval = 0;

        if (voiceInterval > 180)
            playCamperVoice();
    }

    private void playDeath() {
        deathVoice();
        Game.booker.setMoney(Game.booker.getMoney() + 5);
        Game.elizabeth.countMedicine++;
        toDelete = true;
    }

    @Override
    public void update() {
        moveY(velocity.getY());

        if (HP <= 0)
            playDeath();

        if (!hypnotized && HP > 0)
            camperBehave();
    }
}
