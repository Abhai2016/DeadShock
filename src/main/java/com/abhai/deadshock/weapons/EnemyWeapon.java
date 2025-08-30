package com.abhai.deadshock.weapons;

import com.abhai.deadshock.Game;
import com.abhai.deadshock.utils.Sounds;
import com.abhai.deadshock.weapons.bullets.EnemyBullet;

public class EnemyWeapon {
    private static final int RELOAD_INTERVAL = 180;

    private final int clip;
    private int currentClip;
    private boolean nowReloading;
    private final int shootInterval;
    private int currentShootInterval;
    private int currentReloadInterval;

    public EnemyWeapon(WeaponType type) {
        if (type == WeaponType.PISTOL) {
            clip = 20;
            shootInterval = 30;
        } else {
            clip = 30;
            shootInterval = 20;
        }
        currentClip = clip;
        nowReloading = false;
        currentShootInterval = 0;
        currentReloadInterval = 0;
    }

    private void reload() {
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

        currentReloadInterval++;
        if (currentReloadInterval > RELOAD_INTERVAL) {
            currentClip = clip;
            nowReloading = false;
            currentReloadInterval = 0;
        }
    }

    public void shoot(double scaleX, double x, double y) {
        if (currentClip == 0)
            reload();

        currentShootInterval++;
        if (currentShootInterval > shootInterval && currentClip > 0) {
            Sounds.pistolShot.play(Game.menu.getFxSlider().getValue() / 100);
            currentClip--;
            currentShootInterval = 0;
            EnemyBullet enemyBullet = Game.enemyBulletsPool.get();
            enemyBullet.init(scaleX, x, y);
            Game.enemyBullets.add(enemyBullet);
        }
    }
}
