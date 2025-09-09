package com.abhai.deadshock.characters.enemies;

import com.abhai.deadshock.Game;
import com.abhai.deadshock.types.EnemyType;
import com.abhai.deadshock.types.SupplyType;
import com.abhai.deadshock.utils.GameMedia;
import com.abhai.deadshock.world.levels.Block;

public class Camper extends Enemy {
    private static final int DAMAGE = 5;
    private int moveInterval;

    public Camper() {
        HP = 500;
        moveInterval = 0;
        type = EnemyType.CAMPER;
    }

    @Override
    public void init(int x, int y) {
        super.init(x, y);

        HP = 500;
        moveInterval = 0;
    }

    @Override
    protected String getImageName() {
        return "camper.png";
    }

    @Override
    protected SupplyType getSupplyType() {
        return SupplyType.RPG_BULLETS;
    }

    private void die() {
        toDelete = true;
        playDeathVoice();
        if (Math.random() < 0.5)
            Game.getGameWorld().createSupply(getSupplyType(), getTranslateX(), getTranslateY());
        Game.getGameWorld().getBooker().addMoneyForKillingEnemy();
    }

    private void moveY() {
        for (int i = 0; i < velocity.getY(); i++) {
            setTranslateY(getTranslateY() + 1);
            for (Block block : Game.getGameWorld().getLevel().getBlocks())
                if (getBoundsInParent().intersects(block.getBoundsInParent())) {
                    setTranslateY(getTranslateY() - 1);
                    return;
                }
        }
    }

    private void behave() {
        if (getBoundsInParent().intersects(Game.getGameWorld().getBooker().getBoundsInParent())
                && getTranslateY() == Game.getGameWorld().getBooker().getTranslateY())
            closeCombat();

        if (GameMedia.AUDIO_CLIP_CAMPER.isPlaying()) {
            moveInterval++;
            if (moveInterval < 10)
                setTranslateX(getTranslateX() + 1);
            else if (moveInterval < 20)
                setTranslateX(getTranslateX() - 1);
            else
                moveInterval = 0;
        }

        if (Game.getGameWorld().getBooker().getTranslateX() > getTranslateX() - 750
                && Game.getGameWorld().getBooker().getTranslateX() < getTranslateX()) {
            voiceInterval++;
            setScaleX(-1);
        } else if (Game.getGameWorld().getBooker().getTranslateX() < getTranslateX() + 650
                && Game.getGameWorld().getBooker().getTranslateX() > getTranslateX()) {
            voiceInterval++;
            setScaleX(1);
        } else if (Game.getGameWorld().getBooker().getTranslateX() == getTranslateX()) {
            voiceInterval++;
        } else
            voiceInterval = 0;

        if (voiceInterval > 200)
            playVoice();
    }

    private void playVoice() {
        GameMedia.AUDIO_CLIP_CAMPER.play(Game.getGameWorld().getMenu().getFxSlider().getValue() / 100);
        switch ((int) (Math.random() * 3)) {
            case 0 -> GameMedia.BOOKER_HIT.play(Game.getGameWorld().getMenu().getFxSlider().getValue() / 100);
            case 1 -> GameMedia.BOOKER_HIT_2.play(Game.getGameWorld().getMenu().getFxSlider().getValue() / 100);
            case 2 -> GameMedia.BOOKER_HIT_3.play(Game.getGameWorld().getMenu().getFxSlider().getValue() / 100);
        }
        Game.getGameWorld().getBooker().setHp(Game.getGameWorld().getBooker().getHp() - DAMAGE);
        voiceInterval = 0;
    }

    @Override
    public void update() {
        if (HP < 1) {
            die();
            return;
        }

        moveY();
        if (!hypnotized)
            behave();
    }
}
