package com.abhai.deadshock.characters;

import com.abhai.deadshock.Game;
import com.abhai.deadshock.utils.GameMedia;
import com.abhai.deadshock.world.levels.Block;
import com.abhai.deadshock.world.levels.Level;
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
        reset();

        if (Game.getGameWorld().getLevel().getCurrentLevelNumber() == Level.SECOND_LEVEL)
            changeToSecondLevel();
        else if (Game.getGameWorld().getLevel().getCurrentLevelNumber() > Level.FIRST_LEVEL)
            Game.getGameWorld().getGameRoot().getChildren().add(this);
    }

    public void reset() {
        canMove = true;
        moveInterval = 0;
        medicineCount = 0;
        giveSupply = false;
        supplyInterval = 0;
        medicineInterval = 0;
        emptySupplyInterval = 0;
        imageView.setViewport(new Rectangle2D(0, 0, WIDTH, HEIGHT));

        setTranslateX(START_X);
        setTranslateY(START_Y);
    }

    private void move() {
        if (canMove) {
            moveInterval++;
            if (getTranslateX() < Game.getGameWorld().getBooker().getTranslateX() - 100)
                moveX(SPEED);
            else if (getTranslateX() > Game.getGameWorld().getBooker().getTranslateX() + 100)
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
        if (Game.getGameWorld().getLevel().getCurrentLevelNumber() == Level.BOSS_LEVEL)
            medicineInterval++;

        if (medicineInterval > 600) {
            medicineCount++;
            medicineInterval = 0;
        }

        if (giveSupply)
            if (getBoundsInParent().intersects(Game.getGameWorld().getBooker().getBoundsInParent())) {
                Game.getGameWorld().getBooker().addMedicineForKillingEnemy();
                giveMedicine();
            } else if (supplyInterval > 300)
                playSupplyVoice();

        generateSupply();
        noSupply();
    }

    private void noSupply() {
        if (!giveSupply && canMove && medicineCount == 0)
            emptySupplyInterval++;
        else
            emptySupplyInterval = 0;

        if (emptySupplyInterval > 900) {
            if (Game.getGameWorld().getBooker().getHp() < 100 || Game.getGameWorld().getBooker().getWeapon().getCurrentBullets() == 0) {
                switch ((int) (Math.random() * 4)) {
                    case 0 -> GameMedia.EMPTY.play(Game.getGameWorld().getMenu().getVoiceSlider().getValue() / 100);
                    case 1 ->
                            GameMedia.FOUND_NOTHING.play(Game.getGameWorld().getMenu().getVoiceSlider().getValue() / 100);
                    case 2 ->
                            GameMedia.HAVE_NOTHING.play(Game.getGameWorld().getMenu().getVoiceSlider().getValue() / 100);
                    case 3 ->
                            GameMedia.TRY_TO_FIND.play(Game.getGameWorld().getMenu().getVoiceSlider().getValue() / 100);
                }
            }
            emptySupplyInterval = 0;
        }
    }

    private void playVoice() {
        if (Game.getGameWorld().getLevel().getCurrentLevelNumber() == Level.SECOND_LEVEL)
            if (startLevel && getTranslateX() > 100) {
                startLevel = false;
                GameMedia.FREEDOM.play(Game.getGameWorld().getMenu().getVoiceSlider().getValue() / 100);
            } else if (getTranslateX() > Block.BLOCK_SIZE * 150 && playVoiceWhereYouFrom) {
                canMove = false;
                playVoiceWhereYouFrom = false;
                GameMedia.WHERE_ARE_YOU_FROM.setOnEndOfMedia(() -> canMove = true);
                GameMedia.WHERE_ARE_YOU_FROM.setVolume(Game.getGameWorld().getMenu().getVoiceSlider().getValue() / 100);
                GameMedia.WHERE_ARE_YOU_FROM.play();
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
        canMove = true;
        medicineCount--;
        giveSupply = false;
        imageView.setViewport(new Rectangle2D(0, 0, WIDTH, HEIGHT));
    }

    public void resetForNewGame() {
        startLevel = true;
        playVoiceWhereYouFrom = true;
        Game.getGameWorld().getGameRoot().getChildren().remove(this);
    }

    private void generateSupply() {
        if (supplyInterval > 300 && medicineCount > 0 && Game.getGameWorld().getBooker().getHp() < 100) {
            canMove = false;
            giveSupply = true;
            playSupplyVoice();
            imageView.setViewport(new Rectangle2D(WIDTH, 0, WIDTH, HEIGHT));
        }
    }

    private void playSupplyVoice() {
        switch ((int) (Math.random() * 3)) {
            case 0 -> GameMedia.BOOKER_CATCH.play(Game.getGameWorld().getMenu().getVoiceSlider().getValue() / 100);
            case 1 -> GameMedia.BOOKER_CATCH_2.play(Game.getGameWorld().getMenu().getVoiceSlider().getValue() / 100);
            case 2 -> GameMedia.BOOKER_CATCH_3.play(Game.getGameWorld().getMenu().getVoiceSlider().getValue() / 100);
        }
        supplyInterval = 0;
    }

    @Override
    protected String getImageName() {
        return "elizabeth.png";
    }

    public void changeToSecondLevel() {
        startLevel = true;
        playVoiceWhereYouFrom = true;
        Game.getGameWorld().getGameRoot().getChildren().add(this);
    }

    private boolean intersectsWithBlocks(char typeOfCoordinate, double coordinate) {
        for (Block block : Game.getGameWorld().getLevel().getBlocks())
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
        return false;
    }

    public void update() {
        move();
        supply();

        if (startLevel || playVoiceWhereYouFrom)
            playVoice();
    }
}