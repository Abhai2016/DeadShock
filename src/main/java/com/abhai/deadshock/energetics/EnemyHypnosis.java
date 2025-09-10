package com.abhai.deadshock.energetics;

import com.abhai.deadshock.Game;
import com.abhai.deadshock.types.DifficultyType;
import com.abhai.deadshock.utils.GameMedia;

public class EnemyHypnosis extends Hypnosis {

    @Override
    protected void hypnotizeTheTarget() {
        Game.getGameWorld().getBooker().hypnotize();
        GameMedia.BOSS_TROMP.play(Game.getGameWorld().getMenu().getFxSlider().getValue() / 100);
    }

    @Override
    protected void setDifficultyType() {
        switch (Game.getGameWorld().getDifficultyType()) {
            case DifficultyType.MARIK -> maxInterval = 150;
            case DifficultyType.EASY -> maxInterval = 250;
            case DifficultyType.MEDIUM -> maxInterval = 350;
            case DifficultyType.HARD -> maxInterval = 450;
            case DifficultyType.HARDCORE -> maxInterval = 550;
        }
    }

    @Override
    protected void unhypnotizeTheTarget() {
        Game.getGameWorld().getBooker().unhypnotize();
    }
}
