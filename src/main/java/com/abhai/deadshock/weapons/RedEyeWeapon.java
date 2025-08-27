package com.abhai.deadshock.weapons;

import com.abhai.deadshock.Game;
import com.abhai.deadshock.utils.Sounds;
import com.abhai.deadshock.weapons.bullets.EnemyBullet;

public class RedEyeWeapon extends EnemyWeapon {

    public RedEyeWeapon() {
        clip = 30;
    }

    @Override
    public void shoot(double scaleX, double x, double y) {
        shootInterval++;
        if (clip == 0)
            reload(30);

        if (shootInterval > 20 && clip > 0) {
            Sounds.machineGunShot.play(Game.menu.getFxSlider().getValue() / 100);
            clip--;
            shootInterval = 0;
            Game.enemyBullets.add(new EnemyBullet("red_eye", scaleX, x, y));
        }
    }
}
