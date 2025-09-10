package com.abhai.deadshock.weapons;

import com.abhai.deadshock.Game;
import com.abhai.deadshock.types.WeaponType;
import com.abhai.deadshock.utils.GameMedia;
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
                case 0 -> GameMedia.NO_AMMO.play(Game.getGameWorld().getMenu().getVoiceSlider().getValue() / 100);
                case 1 -> GameMedia.NO_AMMO_2.play(Game.getGameWorld().getMenu().getVoiceSlider().getValue() / 100);
                case 2 -> GameMedia.NEED_AMMO.play(Game.getGameWorld().getMenu().getVoiceSlider().getValue() / 100);
                case 3 -> GameMedia.NEED_AMMO_2.play(Game.getGameWorld().getMenu().getVoiceSlider().getValue() / 100);
                case 4 -> GameMedia.RELOADING.play(Game.getGameWorld().getMenu().getVoiceSlider().getValue() / 100);
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
            GameMedia.PISTOL_SHOT.play(Game.getGameWorld().getMenu().getFxSlider().getValue() / 100);
            currentClip--;
            currentShootInterval = 0;
            EnemyBullet enemyBullet = Game.getGameWorld().getEnemyBulletsPool().get();
            enemyBullet.init(scaleX, x, y);
            Game.getGameWorld().getEnemyBullets().add(enemyBullet);
        }
    }
}
