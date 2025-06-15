package com.abhai.deadshock.weapon;

import com.abhai.deadshock.Game;
import com.abhai.deadshock.Sounds;
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
                    Sounds.audioClipIEmpty.play(Game.menu.voiceSlider.getValue() / 100);
                    break;
                case 1:
                    Sounds.audioClipINeedClip.play(Game.menu.voiceSlider.getValue() / 100);
                    break;
                case 2:
                    Sounds.audioClipIReloading.play(Game.menu.voiceSlider.getValue() / 100);
                    break;
                case 3:
                    if (Game.levelNumber > Level.FIRST_LEVEL)
                        Sounds.audioClipINeedClip2.play(Game.menu.voiceSlider.getValue() / 100);
                    else
                        Sounds.audioClipINeedClip.play(Game.menu.voiceSlider.getValue() / 100);
                    break;
                case 5:
                    if (Game.levelNumber > Level.FIRST_LEVEL)
                        Sounds.audioClipAmmoRunOut.play(Game.menu.voiceSlider.getValue() / 100);
                    else
                        Sounds.audioClipIReloading.play(Game.menu.voiceSlider.getValue() / 100);
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
