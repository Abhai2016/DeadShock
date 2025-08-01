package com.abhai.deadshock.characters;

import com.abhai.deadshock.Game;
import com.abhai.deadshock.levels.Block;
import com.abhai.deadshock.levels.Level;
import com.abhai.deadshock.utils.Sounds;
import javafx.geometry.Rectangle2D;

public class Elizabeth extends Character {
    private static final int WIDTH = 43;

    private int moveInterval;
    private int medicineCount;
    private int supplyInterval;
    private int medicineInterval;
    private int emptySupplyInterval;

    private boolean canMove;
    private boolean startLevel;
    private boolean giveSupply;
    private boolean playVoiceWhereYouFrom;

    public Elizabeth() {
        moveInterval = 0;
        medicineCount = 0;
        supplyInterval = 0;
        medicineInterval = 0;
        emptySupplyInterval = 0;

        canMove = true;
        startLevel = true;
        giveSupply = false;
        playVoiceWhereYouFrom = true;

        imageView.setViewport(new Rectangle2D(0, 0, WIDTH, HEIGHT));
    }

    @Override
    protected String getImageName() {
        return "elizabeth.png";
    }


    private void move() {
        if (canMove) {
            moveInterval++;
            if (getTranslateX() < Game.booker.getTranslateX() - 100)
                moveX(SPEED);
            else if (getTranslateX() > Game.booker.getTranslateX() + 100)
                moveX(-SPEED);

            if (moveInterval < 50)
                moveY(1);
            else if (moveInterval < 100)
                moveY(-1);
            else
                moveInterval = 0;
        } else
            moveY(SPEED);
    }

    private void supply() {
        if (!giveSupply && canMove)
            supplyInterval++;

        if (Game.levelNumber == Level.BOSS_LEVEL)
            medicineInterval++;

        if (medicineInterval > 600) {
            medicineInterval = 0;
            medicineCount++;
        }

        generateSupply();

        if (giveSupply && supplyInterval > 300)
            playSupplyVoice();

        noSupply();
    }

    private void noSupply() {
        if (!giveSupply && canMove && medicineCount == 0)
            emptySupplyInterval++;
        else
            emptySupplyInterval = 0;

        if (emptySupplyInterval > 900) {
            if (Game.booker.getHP() < 100 || Game.weapon.getBullets() == 0) {
                switch ((int) (Math.random() * 4)) {
                    case 0 -> Sounds.empty.play(Game.menu.voiceSlider.getValue() / 100);
                    case 1 -> Sounds.foundNothing.play(Game.menu.voiceSlider.getValue() / 100);
                    case 2 -> Sounds.haveNothing.play(Game.menu.voiceSlider.getValue() / 100);
                    case 3 -> Sounds.tryToFind.play(Game.menu.voiceSlider.getValue() / 100);
                }
            }
            emptySupplyInterval = 0;
        }
    }

    private void playVoice() {
        if (Game.levelNumber == Level.SECOND_LEVEL)
            if (startLevel && getTranslateX() > 100) {
                Sounds.freedom.play(Game.menu.voiceSlider.getValue() / 100);
                startLevel = false;
            } else if (getTranslateX() > Block.BLOCK_SIZE * 150 && playVoiceWhereYouFrom) {
                canMove = false;
                Sounds.whereAreYouFrom.setVolume(Game.menu.voiceSlider.getValue() / 100);
                Sounds.whereAreYouFrom.play();
                Sounds.whereAreYouFrom.setOnEndOfMedia(() -> canMove = true);
                playVoiceWhereYouFrom = false;
            }
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

            if (intersectsWithBlocks('X', x))
                return;
        }
    }

    private void moveY(int y) {
        for (int i = 0; i < Math.abs(y); i++) {
            if (y > 0)
                setTranslateY(getTranslateY() + 1);
            else
                setTranslateY(getTranslateY() - 1);

            if (intersectsWithBlocks('Y', y))
                return;

            if (getTranslateY() == 0)
                setTranslateY(getTranslateY() + 1);
        }
    }

    public void giveMedicine() {
        giveSupply = false;
        canMove = true;
        medicineCount--;
        imageView.setViewport(new Rectangle2D(0, 0, WIDTH, HEIGHT));
    }

    public void reinitialize() {
        setTranslateX(START_X);
        setTranslateY(START_Y);
        canMove = true;
        giveSupply = false;
        medicineCount = 0;
        imageView.setViewport(new Rectangle2D(0, 0, WIDTH, HEIGHT));
    }

    private void generateSupply() {
        if (supplyInterval > 300 && medicineCount > 0 && Game.booker.getHP() < 100) {
            imageView.setViewport(new Rectangle2D(WIDTH, 0, WIDTH, HEIGHT));
            canMove = false;
            giveSupply = true;
            playSupplyVoice();
        }
    }

    private void playSupplyVoice() {
        switch ((int) (Math.random() * 3)) {
            case 0 -> Sounds.bookerCatch.play(Game.menu.voiceSlider.getValue() / 100);
            case 1 -> Sounds.bookerCatch2.play(Game.menu.voiceSlider.getValue() / 100);
            case 2 -> Sounds.bookerCatch3.play(Game.menu.voiceSlider.getValue() / 100);
        }
        supplyInterval = 0;
    }

    public boolean isGiveSupply() {
        return giveSupply;
    }

    public void addMedicineForKillingEnemy() {
        medicineCount++;
    }

    private boolean intersectsWithBlocks(char typeOfCoordinate, double coordinate) {
        for (Block block : Game.blocks)
            if (getBoundsInParent().intersects(block.getBoundsInParent())) {
                if (typeOfCoordinate == 'X') {
                    setTranslateX(getTranslateX() - getScaleX());
                    moveY(-2);
                } else {
                    if (coordinate > 0)
                        setTranslateY(getTranslateY() - 1);
                    else
                        setTranslateY(getTranslateY() + 1);
                }
                return true;
            }

        if (Game.levelNumber == Level.BOSS_LEVEL)
            if (getBoundsInParent().intersects(Game.level.getImgView().getBoundsInParent())) {
                setTranslateY(getTranslateY() - 1);
                return true;
            }

        return false;
    }

    public void update() {
        move();
        supply();

        if (startLevel || playVoiceWhereYouFrom)
            playVoice();
    }
}