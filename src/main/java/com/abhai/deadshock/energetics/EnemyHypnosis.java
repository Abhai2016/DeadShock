package com.abhai.deadshock.energetics;

import com.abhai.deadshock.menus.DifficultyLevel;
import com.abhai.deadshock.Game;
import com.abhai.deadshock.utils.Sounds;

public class EnemyHypnosis extends Hypnosis {

    @Override
    protected void hypnotizeTheTarget() {
        Game.booker.hypnotize();
        Sounds.bossTromp.play(Game.menu.getFxSlider().getValue() / 100);
    }

    @Override
    protected void setDifficultyLevel() {
        switch (Game.difficultyLevel) {
            case DifficultyLevel.MARIK -> maxInterval = 150;
            case DifficultyLevel.EASY -> maxInterval = 250;
            case DifficultyLevel.MEDIUM -> maxInterval = 350;
            case DifficultyLevel.HARD -> maxInterval = 450;
            case DifficultyLevel.HARDCORE -> maxInterval = 550;
        }
    }

    @Override
    protected void unhypnotizeTheTarget() {
        Game.booker.unhypnotize();
    }
}
