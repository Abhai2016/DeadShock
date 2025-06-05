package com.abhai.deadshock.Characters;

import com.abhai.deadshock.Levels.Block;
import com.abhai.deadshock.Levels.Level;
import com.abhai.deadshock.Game;
import com.abhai.deadshock.Sounds;
import com.abhai.deadshock.Supply;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.nio.file.Path;
import java.nio.file.Paths;

public class Elizabeth extends Pane {
    private Path imagePath = Paths.get("resources", "images", "characters", "elizabeth.png");
    Image imgElizabeth = new Image(imagePath.toUri().toString());
    private Path imageMedicinePath = Paths.get("resources", "images", "characters", "elizabeth_medicine.png");
    private Image imgElizabethMedicine = new Image(imageMedicinePath.toUri().toString());
    ImageView imgView = new ImageView(imgElizabeth);

    Supply ammo;

    private short medicineInterval = 0;
    private short supplyInterval = 0;
    private short emptySupplyInterval = 0;
    private short moveInterval = 0;
    private byte rand = 0;
    short y = 0;
    byte countMedicine = 0;



    private boolean setY = true;
    private boolean startLevel = true;
    private boolean playVoiceWhereYouFrom = true;
    boolean canMove = true;
    boolean giveSupply = false;



    public Elizabeth() {
        imgView.setFitWidth(46);
        imgView.setFitHeight(74);
        setTranslateX(50);
        setTranslateY(400);
        y = (short) getTranslateY();

        getChildren().add(imgView);
        Game.gameRoot.getChildren().add(this);
    }




    private void moveX(int x) {
        for (int i = 0; i < Math.abs(x); i++) {
            if (x > 0) {
                setTranslateX(getTranslateX() + 1);
                setScaleX(1);
            } else {
                setTranslateX(getTranslateX() - 1);
                setScaleX(-1);
            }

            for (Block block : Game.blocks)
                if (getBoundsInParent().intersects(block.getBoundsInParent())) {
                    setTranslateX(getTranslateX() - getScaleX());
                    moveY(-5);
                    y = (short) getTranslateY();
                    return;
                }
        }
    }


    private void moveY(int y) {
        for (int i = 0; i < Math.abs(y); i++) {
            if (y > 0)
                setTranslateY(getTranslateY() + 1);
            else
                setTranslateY(getTranslateY() - 1);

            for (Block block : Game.blocks)
                if (getBoundsInParent().intersects(block.getBoundsInParent()))
                    if (y > 0) {
                        setTranslateY(getTranslateY() - 1);
                        return;
                    } else {
                        setTranslateY(getTranslateY() + 1);
                        return;
                    }

            if (getTranslateY() == 0)
                setTranslateY(getTranslateY() + 1);

            if (giveSupply && Game.levelNumber != 2)
                for (Block block : Level.enemyBlocks)
                    if (getBoundsInParent().intersects(block.getBoundsInParent())) {
                        setTranslateY(getTranslateY() - 1);
                        return;
                    }

            if (Game.levelNumber == 3)
                if (getBoundsInParent().intersects(Game.level.getImgView().getBoundsInParent()))
                    setTranslateY(getTranslateY() - 1);
        }
    }


    private void playVoice() {
        if (Game.levelNumber == 1)
            if (startLevel && getTranslateX() > 100) {
                Path freedomSoundPath = Paths.get("resources", "sounds", "voice", "elizabeth", "freedom.mp3");
                Sounds.elizabethMediaPlayer = new MediaPlayer(new Media(freedomSoundPath.toUri().toString()));
                Sounds.elizabethMediaPlayer.setVolume(Game.menu.voiceSlider.getValue() / 100);
                Sounds.elizabethMediaPlayer.play();
                Sounds.elizabethMediaPlayer.setOnEndOfMedia( () -> {
                    Path ammoSoundPath = Paths.get("resources", "sounds", "voice", "do_you_have_ammo.mp3");
                    Sounds.elizabethMediaPlayer = new MediaPlayer(new Media(ammoSoundPath.toUri().toString()));
                    Sounds.elizabethMediaPlayer.setVolume(Game.menu.voiceSlider.getValue() / 100);
                    Sounds.elizabethMediaPlayer.play();
                } );

                startLevel = false;
            } else if (getTranslateX() > Level.BLOCK_SIZE * 150 && playVoiceWhereYouFrom) {
                canMove = false;
                Path soundPath = Paths.get("resources", "sounds", "voice", "where_are_you_from.mp3");
                Sounds.elizabethMediaPlayer = new MediaPlayer(new Media(
                        soundPath.toUri().toString()));
                Sounds.elizabethMediaPlayer.setVolume(Game.menu.voiceSlider.getValue() / 100);
                Sounds.elizabethMediaPlayer.play();
                Sounds.elizabethMediaPlayer.setOnEndOfMedia( () -> {
                    canMove = true;
                    Sounds.elizabethMediaPlayer = null;
                });

                playVoiceWhereYouFrom = false;
            }
    }


    private void playSupplyVoice() {
        rand = (byte) (Math.random() * 3);
        switch (rand) {
            case 0:
                Sounds.audioClipBookerCatch.play(Game.menu.voiceSlider.getValue() / 100);
                break;
            case 1:
                Sounds.audioClipBookerCatch2.play(Game.menu.voiceSlider.getValue() / 100);
                break;
            case 2:
                Sounds.audioClipBookerCatch3.play(Game.menu.voiceSlider.getValue() / 100);
                break;
        }
        supplyInterval = 0;
    }


    private void supply() {
        if (supplyInterval > 240 && countMedicine > 0 && Game.booker.getHP() < 100) {
            imgView.setImage(imgElizabethMedicine);
            canMove = false;
            giveSupply = true;

            playSupplyVoice();
        } else if (giveSupply && supplyInterval > 240)
            playSupplyVoice();


        if (emptySupplyInterval > 900 && countMedicine == 0)
            if (Game.booker.getHP() < 100 || Game.weapon.getBullets() == 0) {
                rand = (byte) (Math.random() * 4);
                switch (rand) {
                    case 0:
                        Sounds.audioClipEmpty.play(Game.menu.voiceSlider.getValue() / 100);
                        break;
                    case 1:
                        Sounds.audioClipAnything.play(Game.menu.voiceSlider.getValue() / 100);
                        break;
                    case 2:
                        Sounds.audioClipAnymore.play(Game.menu.voiceSlider.getValue() / 100);
                        break;
                    case 3:
                        Sounds.audioClipAnother.play(Game.menu.voiceSlider.getValue() / 100);
                        break;
                }
                emptySupplyInterval = 0;
            }
    }


    private void behave() {
        if (!canMove) {
            if (setY) {
                y = (short) getTranslateY();
                setY = false;
            }
            moveY(5);
        } else if ((int) getTranslateY() > y)
            moveY(-5);
        else setY = true;

        if (canMove) {
            moveInterval++;
            if (getTranslateX() < Game.booker.getTranslateX() - 100)
                moveX(Game.booker.getCHARACTER_SPEED());
            else if (getTranslateX() > Game.booker.getTranslateX() + 100)
                moveX(-Game.booker.getCHARACTER_SPEED());

            if (moveInterval > 0) {
                moveY(1);
                if (moveInterval > 30)
                    moveInterval = -30;
            } else if (moveInterval < 0)
                moveY(-1);
        }

        if (Game.levelNumber == 3)
            medicineInterval++;

        if (medicineInterval > 600) {
            medicineInterval = 0;
            countMedicine++;
        }

        supply();
    }


    public void update() {
        supplyInterval++;

        if (!giveSupply && canMove)
            emptySupplyInterval++;
        else
            emptySupplyInterval = 0;

        behave();

        if (ammo != null) {
            ammo.update();
            if (ammo.isDelete())
                ammo = null;
        }

        playVoice();
    }
}
