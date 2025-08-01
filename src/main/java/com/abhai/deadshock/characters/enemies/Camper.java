package com.abhai.deadshock.characters.enemies;

import com.abhai.deadshock.Game;
import com.abhai.deadshock.levels.Block;
import com.abhai.deadshock.utils.Sounds;

public class Camper extends Enemy {
    private int moveInterval;
    private static final int DAMAGE = 5;

    public Camper(long x, long y) {
        HP = 500;
        moveInterval = 0;
        type = EnemyType.CAMPER;

        setTranslateX(x);
        setTranslateY(y);
    }

    private void die() {
        toDelete = true;
        playDeathVoice();
        Game.booker.addMoneyForKillingEnemy();
        Game.elizabeth.addMedicineForKillingEnemy();
    }

    private void moveY() {
        for (int i = 0; i < Math.abs(velocity.getY()); i++) {
            if (velocity.getY() > 0)
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

    private void behave() {
        if (getBoundsInParent().intersects(Game.booker.getBoundsInParent()) && getTranslateY() == Game.booker.getTranslateY())
            closeCombat();

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

        if (voiceInterval > 200)
            playVoice();
    }

    private void playVoice() {
        Sounds.audioClipCamper.play(Game.menu.fxSlider.getValue() / 100);
        switch ((int) (Math.random() * 3)) {
            case 0 -> Sounds.bookerHit.play(Game.menu.fxSlider.getValue() / 100);
            case 1 -> Sounds.bookerHit2.play(Game.menu.fxSlider.getValue() / 100);
            case 2 -> Sounds.bookerHit3.play(Game.menu.fxSlider.getValue() / 100);
        }
        Game.booker.setHP(Game.booker.getHP() - DAMAGE);
        voiceInterval = 0;
    }

    @Override
    protected String getImageName() {
        return "camper.png";
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
