package com.abhai.deadshock.weapons.bullets;


import com.abhai.deadshock.Game;
import com.abhai.deadshock.characters.enemies.Enemy;
import com.abhai.deadshock.levels.Block;
import com.abhai.deadshock.levels.BlockType;
import com.abhai.deadshock.levels.Level;
import com.abhai.deadshock.utils.Sounds;
import javafx.geometry.Point2D;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.nio.file.Path;
import java.nio.file.Paths;

public class RpgBullet extends Bullet {
    private static byte RPG_BULLET_SPEED = 10;

    private Point2D point2D = new Point2D(0, 0);
    private boolean isExplosion = false;

    public RpgBullet() {
        direction = true;
        if (Game.booker.getScaleX() < 0) {
            direction = false;
            setScaleX(-1);
        }

        Path imagePath = Paths.get("resources", "images", "weapons", "rpgBullet.png");
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
        Sounds.rpgExplosion.play(Game.menu.getFxSlider().getValue() / 100);
        isExplosion = true;

        if (Game.levelNumber != Level.BOSS_LEVEL)
            for (Enemy enemy : Game.enemies)
                if (Game.weapon.explosion.getBoundsInParent().intersects(enemy.getBoundsInParent())) {
                    point2D = new Point2D(enemy.getTranslateX() + enemy.getWidth() / 2,
                            enemy.getTranslateY() + enemy.getHeight() / 2).subtract(Game.weapon.explosion.getTranslateX() +
                            Game.weapon.explosion.getFitWidth() / 2, Game.weapon.explosion.getTranslateY() + Game.weapon.explosion.getFitHeight() / 2);
                    if (point2D.getX() > point2D.getY() || point2D.getX() == point2D.getY())
                        enemy.setHP(enemy.getHP() - (Game.weapon.getRpgDamage() - (int) point2D.getX() * 3));
                    else
                        enemy.setHP(enemy.getHP() - (Game.weapon.getRpgDamage() - (int) point2D.getY() * 3));
                }

        if (Game.weapon.explosion.getBoundsInParent().intersects(Game.booker.getBoundsInParent()))
            Game.booker.setHP(Game.booker.getHP() - Game.weapon.getRpgDamage());

        Game.weapon.explosionAnimation.setOnFinished(event -> {
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

            for (Enemy enemy : Game.enemies)
                if (getBoundsInParent().intersects(enemy.getBoundsInParent())) {
                    enemy.setHP(enemy.getHP() - Game.weapon.getRpgDamage());
                    enemy.playHitVoice();
                    createExplosion();
                    return;
                }

            for (Block block : Game.level.getBlocks())
                if (getBoundsInParent().intersects(block.getBoundsInParent()) && !block.getType().equals(BlockType.INVISIBLE)) {
                    createExplosion();
                    return;
                }
        }
    }
}