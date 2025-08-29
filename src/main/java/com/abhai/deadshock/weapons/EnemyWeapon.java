package com.abhai.deadshock.weapons;

import com.abhai.deadshock.Game;
import com.abhai.deadshock.utils.Sounds;

public class EnemyWeapon {
    private int reloadingInterval = 0;
    int shootInterval = 0;
    int clip = 0;

    private boolean nowReloading = false;

    EnemyWeapon() {
    }

    public void shoot(double scaleX, double x, double y) {}

    void reload(int fullClip) {
        if (!nowReloading) {
            switch ((int) (Math.random() * 5)) {
                case 0 -> Sounds.noAmmo.play(Game.menu.getVoiceSlider().getValue() / 100);
                case 1 -> Sounds.noAmmo2.play(Game.menu.getVoiceSlider().getValue() / 100);
                case 2 -> Sounds.needAmmo.play(Game.menu.getVoiceSlider().getValue() / 100);
                case 3 -> Sounds.needAmmo2.play(Game.menu.getVoiceSlider().getValue() / 100);
                case 4 -> Sounds.reloading.play(Game.menu.getVoiceSlider().getValue() / 100);
            }
            nowReloading = true;
        }

        reloadingInterval++;
        if (reloadingInterval > 180) {
            clip = fullClip;
            reloadingInterval = 0;
            nowReloading = false;
        }
    }
}
