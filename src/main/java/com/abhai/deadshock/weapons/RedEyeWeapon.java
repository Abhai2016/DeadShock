package com.abhai.deadshock.weapons;

import com.abhai.deadshock.Game;
import com.abhai.deadshock.Sounds;

public class RedEyeWeapon extends EnemyWeapon {

    public RedEyeWeapon() {
        clip = 30;
    }

    @Override
    public void shoot(double scaleX, double x, double y) {
        if (shootInterval > 20 && clip > 0) {
            Sounds.machineGunShot.play(Game.menu.fxSlider.getValue() / 100);
            clip--;
            shootInterval = 0;
            Game.enemyBullets.add(new EnemyBullet("red_eye", scaleX, x, y));
        }
    }

    @Override
    public void update() {
        shootInterval++;
        if (clip == 0)
            reload(30);
    }
}
