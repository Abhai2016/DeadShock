package com.abhai.deadshock.weapons.bullets;

import com.abhai.deadshock.Game;
import com.abhai.deadshock.characters.enemies.Enemy;
import com.abhai.deadshock.levels.Block;
import com.abhai.deadshock.levels.BlockType;
import com.abhai.deadshock.weapons.WeaponType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

import java.nio.file.Path;
import java.nio.file.Paths;

public class Bullet extends Pane {
    public static final int BULLET_SPEED = 15;

    public ImageView bullet;

    public boolean direction = true;
    public boolean delete = false;

    Bullet() {

    }

    public Bullet(WeaponType type) {
        if (Game.booker.getScaleX() < 0) {
            direction = false;
            setScaleX(-1);
        }

        Path imagePath = Paths.get("resources", "images", "weapons", "bullet.png");
        bullet = new ImageView(new Image(imagePath.toUri().toString()));

        switch (type) {
            case WeaponType.PISTOL -> setTranslateY(Game.booker.getTranslateY() + 14);
            case WeaponType.MACHINE_GUN -> setTranslateY(Game.booker.getTranslateY() + 20);
        }

        if (direction)
            setTranslateX(Game.booker.getTranslateX() + Game.booker.getWidth());
        else
            setTranslateX(Game.booker.getTranslateX());

        getChildren().add(bullet);
        Game.gameRoot.getChildren().add(this);
    }


    public boolean isDelete() {
        return delete;
    }


    public void update() {
        if (direction)
            setTranslateX(getTranslateX() + BULLET_SPEED);
        else
            setTranslateX(getTranslateX() - BULLET_SPEED);

        if (getTranslateX() > -Game.gameRoot.getLayoutX() + Game.appRoot.getWidth()
                || getTranslateX() < -Game.gameRoot.getLayoutX()) {
            Game.gameRoot.getChildren().remove(this);
            delete = true;
        }

        for (Enemy enemy : Game.enemies)
            if (getBoundsInParent().intersects(enemy.getBoundsInParent())) {
                Game.gameRoot.getChildren().remove(this);
                enemy.setHP((enemy.getHP() - Game.weapon.getDamage()));
                enemy.playHitVoice();
                delete = true;
                return;
            }

        for (Block block : Game.level.getBlocks())
            if (getBoundsInParent().intersects(block.getBoundsInParent()) && !block.getType().equals(BlockType.INVISIBLE)) {
                Game.gameRoot.getChildren().remove(this);
                delete = true;
                return;
            }
    }
}