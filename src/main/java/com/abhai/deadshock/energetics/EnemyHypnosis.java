package com.abhai.deadshock.energetics;

import com.abhai.deadshock.Game;
import com.abhai.deadshock.utils.Sounds;

public class EnemyHypnosis extends Hypnosis {

    @Override
    protected void hypnotizeTheTarget() {
        Game.booker.hypnotize();
        Sounds.bossTromp.play(Game.menu.fxSlider.getValue() / 100);
    }

    @Override
    protected void setDifficultyLevel() {
        switch (Game.difficultyLevelText) {
            case "marik" -> maxInterval = 150;
            case "easy" -> maxInterval = 250;
            case "normal" -> maxInterval = 350;
            case "high" -> maxInterval = 450;
            case "hardcore" -> maxInterval = 550;
        }
    }

    @Override
    protected void unhypnotizeTheTarget() {
        Game.booker.unhypnotize();
    }
}
