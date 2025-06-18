package com.abhai.deadshock.characters.enemies;

import com.abhai.deadshock.levels.Block;
import com.abhai.deadshock.Game;
import com.abhai.deadshock.Sounds;
import javafx.geometry.Point2D;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.nio.file.Path;
import java.nio.file.Paths;

public class Camper extends Enemy {
    private int moveInterval = 0;


    public Camper(long x, long y) {
        setWidth(33);
        setHeight(65);

        Path imagePath = Paths.get("resources", "images", "characters", "camper.png");
        imgView = new ImageView(new Image(imagePath.toUri().toString()));
        velocity = new Point2D(0, 10);

        name = "camper";
        delete = false;
        hypnosis = false;

        voiceInterval = 0;
        HP = 500;

        setTranslateX(x);
        setTranslateY(y);

        rectHP = new Rectangle(33, 2, Color.RED);
        rectHP.setTranslateX(getTranslateX());
        rectHP.setVisible(false);

        getChildren().add(imgView);
        Game.gameRoot.getChildren().addAll(this, rectHP);
    }

    private void moveY(int y) {
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

            rectHP.setTranslateY(getTranslateY() - 15);
        }
    }

    private void playCamperVoice() {
        Sounds.audioClipCamper.play(Game.menu.fxSlider.getValue() / 100);
        rand = (byte)(Math.random() * 3);
        switch (rand) {
            case 0:
                Sounds.bookerHit.play(Game.menu.fxSlider.getValue() / 100);
                break;
            case 1:
                Sounds.bookerHit2.play(Game.menu.fxSlider.getValue() / 100);
                break;
            case 2:
                Sounds.bookerHit3.play(Game.menu.fxSlider.getValue() / 100);
                break;
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
            if (voiceInterval > 180)
                playCamperVoice();
        } else if (Game.booker.getTranslateX() < getTranslateX() + 650 && Game.booker.getTranslateX() > getTranslateX()) {
            voiceInterval++;
            setScaleX(-1);
            if (voiceInterval > 180)
                playCamperVoice();
        } else if (Game.booker.getTranslateX() == getTranslateX()) {
            voiceInterval++;
            if (voiceInterval > 180)
                playCamperVoice();
        } else
            voiceInterval = 0;
    }

    private void playDeath() {
        deathVoice();

        Game.booker.setMoney(Game.booker.getMoney() + 5);
        Game.elizabeth.countMedicine++;
        delete = true;

        Game.gameRoot.getChildren().remove(this);
        Game.gameRoot.getChildren().remove(rectHP);
    }

    @Override
    public void update() {
        if (HP < 500)
            rectHP.setVisible(true);
        rectHP.setWidth(HP / 500 * 33);
        rectHP.setTranslateX(getTranslateX());
        moveY((int)velocity.getY());

        if (HP <= 0)
            playDeath();

        if (!hypnosis && HP > 0)
            camperBehave();
    }
}
