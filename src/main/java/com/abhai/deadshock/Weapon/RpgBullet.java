package com.abhai.deadshock.Weapon;


import com.abhai.deadshock.Characters.EnemyBase;
import com.abhai.deadshock.Game;
import com.abhai.deadshock.Levels.Block;
import com.abhai.deadshock.Sounds;
import javafx.geometry.Point2D;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.nio.file.Path;
import java.nio.file.Paths;

class RpgBullet extends Bullet {
    private static byte RPG_BULLET_SPEED = 10;

    private Point2D point2D = new Point2D(0, 0);
    private boolean isExplosion = false;



    RpgBullet() {
        direction = true;
        if (Game.booker.getScaleX() < 0) {
            direction = false;
            setScaleX(-1);
        }

        Path imagePath = Paths.get("resources", "images", "weapons", "rpg_bullet.png");
        bullet = new ImageView(new Image(imagePath.toUri().toString()));

        setTranslateY(Game.booker.getTranslateY() + 10);
        if (direction)
            setTranslateX(Game.booker.getTranslateX() + Game.booker.getWidth());
        else
            setTranslateX(Game.booker.getTranslateX());

        getChildren().add(bullet);
        Game.gameRoot.getChildren().add(this);
    }


    private void createExplosion() {
        getChildren().remove(bullet);
        Game.weapon.explosion.setTranslateX(getTranslateX() - Game.weapon.explosion.getFitWidth() / 2);
        Game.weapon.explosion.setTranslateY(getTranslateY() - Game.weapon.explosion.getFitHeight() / 2);
        Game.gameRoot.getChildren().add(Game.weapon.explosion);
        Game.weapon.explosionAnimation.play();
        Sounds.rpgExplosion.play(Game.menu.fxSlider.getValue() / 100);
        isExplosion = true;

        if (Game.levelNumber != 3)
            for (EnemyBase enemyBase : Game.enemies)
                if (Game.weapon.explosion.getBoundsInParent().intersects(enemyBase.getBoundsInParent())) {
                    point2D = new Point2D(enemyBase.getTranslateX() + enemyBase.getWidth() / 2,
                            enemyBase.getTranslateY() + enemyBase.getHeight() / 2).subtract(Game.weapon.explosion.getTranslateX() +
                            Game.weapon.explosion.getFitWidth() / 2, Game.weapon.explosion.getTranslateY() + Game.weapon.explosion.getFitHeight() / 2);
                    if (point2D.getX() > point2D.getY() || point2D.getX() == point2D.getY())
                        enemyBase.setHP(enemyBase.getHP() - (Game.weapon.getRpgDamage() - point2D.getX() * 3) );
                    else
                        enemyBase.setHP(enemyBase.getHP() - (Game.weapon.getRpgDamage() - point2D.getY() * 3) );
                }

        if (Game.weapon.explosion.getBoundsInParent().intersects(Game.booker.getBoundsInParent())) {
            point2D = new Point2D(Game.booker.getTranslateX() + Game.booker.getWidth() / 2, Game.booker.getTranslateY() +
                Game.booker.getTranslateY() + Game.booker.getHeight() / 2).subtract(Game.weapon.explosion.getTranslateX() +
                Game.weapon.explosion.getFitWidth() / 2, Game.weapon.explosion.getTranslateY() + Game.weapon.explosion.getFitHeight() / 2);
            if (point2D.getX() > point2D.getY() || point2D.getX() == point2D.getY())
                Game.booker.setHP((int) (Game.booker.getHP() - (Game.weapon.getRpgDamage() - point2D.getX() * 3)) );
            else
                Game.booker.setHP((int) (Game.booker.getHP() - (Game.weapon.getRpgDamage() - point2D.getY() * 3)) );
        }

        Game.weapon.explosionAnimation.setOnFinished( event -> {
            Game.weapon.explosionAnimation.stop();
            Game.gameRoot.getChildren().remove(Game.weapon.explosion);
            delete = true;
        });
    }


    public void update() {
        if (!isExplosion) {
            if (direction)
                setTranslateX(getTranslateX() + RPG_BULLET_SPEED);
            else
                setTranslateX(getTranslateX() - RPG_BULLET_SPEED);

            if (getTranslateX() > -Game.gameRoot.getLayoutX() + Game.appRoot.getWidth()
                    || getTranslateX() < -Game.gameRoot.getLayoutX()) {
                Game.gameRoot.getChildren().remove(this);
                delete = true;
                return;
            }

            if (Game.levelNumber == 3) {
                if (getBoundsInParent().intersects(Game.boss.getBoundsInParent())) {
                    Game.boss.setHP(Game.boss.getHP() - Game.weapon.getRpgDamage());
                    createExplosion();
                    return;
                }
            } else
                for (EnemyBase enemy : Game.enemies)
                    if (getBoundsInParent().intersects(enemy.getBoundsInParent())) {
                        enemy.setHP(enemy.getHP() - Game.weapon.getRpgDamage());
                        enemy.setPlayVoice(true);
                        enemy.playHitVoice();
                        createExplosion();
                        return;
                    }

            for (Block block : Game.blocks)
                if (getBoundsInParent().intersects(block.getBoundsInParent())) {
                    createExplosion();
                    return;
                }
        }
    }
}
