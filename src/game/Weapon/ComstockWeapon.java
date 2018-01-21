package game.Weapon;

import game.Game;
import game.Sounds;

public class ComstockWeapon extends EnemyWeapon {

    public ComstockWeapon() {
        clip = 20;
    }



    @Override
    public void shoot(double scaleX, double x, double y) {
        if (shootInterval > 30 && clip > 0) {
            Sounds.pistolShoot.play(Game.menu.fxSlider.getValue() / 100);
            clip--;
            shootInterval = 0;
            Game.enemyBullets.add(new EnemyBullet("comstock", scaleX, x, y));
        }
    }


    @Override
    public void update() {
        shootInterval++;
        if (clip == 0)
            reload(20);
    }
}
