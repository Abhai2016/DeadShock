package com.abhai.deadshock.characters;

import com.abhai.deadshock.Game;
import com.abhai.deadshock.types.SupplySubType;
import com.abhai.deadshock.types.SupplyType;
import com.abhai.deadshock.utils.GameMedia;
import com.abhai.deadshock.world.levels.Block;
import com.abhai.deadshock.world.levels.Level;
import javafx.geometry.Rectangle2D;

import java.util.Random;

public class Elizabeth extends Character {
    private static final int WIDTH = 43;
    private static final Random RANDOM = new Random();
    private static final int MAX_SUPPLY_INTERVAL = 900;

    private int moveInterval;
    private int supplyInterval;

    private boolean canMove;
    private boolean startLevel;
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
        supplyInterval = 0;
        setTranslateX(START_X);
        setTranslateY(START_Y);
        imageView.setViewport(new Rectangle2D(0, 0, WIDTH, HEIGHT));
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
        supplyInterval++;

        if (supplyInterval > MAX_SUPPLY_INTERVAL) {
            SupplySubType subType;
            switch (RANDOM.nextInt(5)) {
                case 0 -> subType = SupplySubType.SALT;
                case 1 -> subType = SupplySubType.MEDICINE;
                case 2 -> subType = SupplySubType.RPG_BULLETS;
                case 3 -> subType = SupplySubType.PISTOL_BULLETS;
                default -> subType = SupplySubType.MACHINE_GUN_BULLETS;
            }

            playSupplyVoice();
            supplyInterval = 0;
            Game.getGameWorld().createSupply(SupplyType.ELIZABETH, subType, getTranslateX(), getTranslateY());
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

    public void resetForNewGame() {
        startLevel = true;
        playVoiceWhereYouFrom = true;
        Game.getGameWorld().getGameRoot().getChildren().remove(this);
    }

    public void playSupplyVoice() {
        switch (RANDOM.nextInt(3)) {
            case 0 -> GameMedia.BOOKER_CATCH.play(Game.getGameWorld().getMenu().getVoiceSlider().getValue() / 100);
            case 1 -> GameMedia.BOOKER_CATCH_2.play(Game.getGameWorld().getMenu().getVoiceSlider().getValue() / 100);
            case 2 -> GameMedia.BOOKER_CATCH_3.play(Game.getGameWorld().getMenu().getVoiceSlider().getValue() / 100);
        }
        supplyInterval = 0;
    }

    public void changeToSecondLevel() {
        startLevel = true;
        playVoiceWhereYouFrom = true;
        Game.getGameWorld().getGameRoot().getChildren().add(this);
    }

    @Override
    protected String getImageName() {
        return "elizabeth.png";
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

        if (Game.getGameWorld().getLevel().getCurrentLevelNumber() == Level.BOSS_LEVEL)
            supply();

        if (startLevel || playVoiceWhereYouFrom)
            playVoice();
    }
}