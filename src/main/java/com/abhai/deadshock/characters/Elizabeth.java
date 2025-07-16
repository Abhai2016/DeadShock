package com.abhai.deadshock.characters;

import com.abhai.deadshock.levels.Block;
import com.abhai.deadshock.levels.Level;
import com.abhai.deadshock.Game;
import com.abhai.deadshock.utils.Sounds;
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
    private Path imageMedicinePath = Paths.get("resources", "images", "characters", "elizabethWithMedicine.png");
    private Image imgElizabethMedicine = new Image(imageMedicinePath.toUri().toString());
    ImageView imgView = new ImageView(imgElizabeth);

    private Supply ammo;

    private int medicineInterval = 0;

    private int supplyInterval = 0;
    private int emptySupplyInterval = 0;
    private int moveInterval = 0;
    private int rand = 0;
    public int y = 0;

    public int countMedicine = 0;
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

    public Supply getAmmo() {
        return ammo;
    }

    public void setAmmo(Supply ammo) {
        this.ammo = ammo;
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

            if (Game.levelNumber == Level.BOSS_LEVEL)
                if (getBoundsInParent().intersects(Game.level.getImgView().getBoundsInParent()))
                    setTranslateY(getTranslateY() - 1);
        }
    }

    private void playVoice() {
        if (Game.levelNumber == Level.SECOND_LEVEL)
            if (startLevel && getTranslateX() > 100) {
                Sounds.freedom.setVolume(Game.menu.voiceSlider.getValue() / 100);
                Sounds.freedom.play();
                startLevel = false;
            } else if (getTranslateX() > Block.BLOCK_SIZE * 150 && playVoiceWhereYouFrom) {
                canMove = false;
                Sounds.whereAreYouFrom.setVolume(Game.menu.voiceSlider.getValue() / 100);
                Sounds.whereAreYouFrom.play();
                Sounds.whereAreYouFrom.setOnEndOfMedia( () -> canMove = true);
                playVoiceWhereYouFrom = false;
            }
    }

    private void playSupplyVoice() {
        rand = (byte) (Math.random() * 3);
        switch (rand) {
            case 0:
                Sounds.bookerCatch.play(Game.menu.voiceSlider.getValue() / 100);
                break;
            case 1:
                Sounds.bookerCatch2.play(Game.menu.voiceSlider.getValue() / 100);
                break;
            case 2:
                Sounds.bookerCatch3.play(Game.menu.voiceSlider.getValue() / 100);
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
                        Sounds.empty.play(Game.menu.voiceSlider.getValue() / 100);
                        break;
                    case 1:
                        Sounds.foundNothing.play(Game.menu.voiceSlider.getValue() / 100);
                        break;
                    case 2:
                        Sounds.haveNothing.play(Game.menu.voiceSlider.getValue() / 100);
                        break;
                    case 3:
                        Sounds.tryToFind.play(Game.menu.voiceSlider.getValue() / 100);
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

        if (Game.levelNumber == Level.BOSS_LEVEL)
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