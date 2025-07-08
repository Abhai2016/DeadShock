package com.abhai.deadshock.weapons;

import com.abhai.deadshock.Game;
import com.abhai.deadshock.utils.Sounds;
import com.abhai.deadshock.levels.Level;

public class EnemyWeapon {
    private short reloadingInterval = 0;
    short shootInterval = 0;
    byte clip = 0;

    private boolean nowReloading = false;

    EnemyWeapon() {
    }

    public void shoot(double scaleX, double x, double y) {}

    void reload(int fullClip) {
        if (!nowReloading) {
            byte rand = (byte)(Math.random() * 5);
            switch (rand) {
                case 0:
                    Sounds.noAmmo2.play(Game.menu.voiceSlider.getValue() / 100);
                    break;
                case 1:
                    Sounds.needAmmo.play(Game.menu.voiceSlider.getValue() / 100);
                    break;
                case 2:
                    Sounds.reloading.play(Game.menu.voiceSlider.getValue() / 100);
                    break;
                case 3:
                    if (Game.levelNumber > Level.FIRST_LEVEL)
                        Sounds.needAmmo2.play(Game.menu.voiceSlider.getValue() / 100);
                    else
                        Sounds.needAmmo.play(Game.menu.voiceSlider.getValue() / 100);
                    break;
                case 5:
                    if (Game.levelNumber > Level.FIRST_LEVEL)
                        Sounds.noAmmo.play(Game.menu.voiceSlider.getValue() / 100);
                    else
                        Sounds.reloading.play(Game.menu.voiceSlider.getValue() / 100);
                    break;
            }
            nowReloading = true;
        }
        reloadingInterval++;
        if (reloadingInterval > 180) {
                clip = (byte)fullClip;
            reloadingInterval = 0;
            nowReloading = false;
        }
    }

    public void update() {

    }
}
