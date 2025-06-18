package com.abhai.deadshock.weapons;

import com.abhai.deadshock.characters.enemies.Enemy;
import com.abhai.deadshock.levels.Block;
import com.abhai.deadshock.Game;
import com.abhai.deadshock.levels.BlockType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

import java.nio.file.Path;
import java.nio.file.Paths;

public class Bullet extends Pane {
    static final int BULLET_SPEED = 15;

    ImageView bullet;

    boolean direction = true;
    boolean delete = false;

    Bullet() {

    }

    Bullet(String weaponName) {
        if (Game.booker.getScaleX() < 0) {
            direction = false;
            setScaleX(-1);
        }

        Path imagePath = Paths.get("resources", "images", "weapons", "bullet.png");
        bullet = new ImageView(new Image(imagePath.toUri().toString()));

        switch (weaponName) {
            case "pistol":
                setTranslateY(Game.booker.getTranslateY() + 14);
                break;
            case "machine_gun":
                setTranslateY(Game.booker.getTranslateY() + 20);
                break;
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
                enemy.setPlayVoice(true);
                enemy.playHitVoice();
                delete = true;
                return;
            }

        if (Game.boss != null)
            if (getBoundsInParent().intersects(Game.boss.getBoundsInParent())) {
                Game.gameRoot.getChildren().remove(this);
                Game.boss.setHP((Game.boss.getHP() - Game.weapon.getDamage()));
                delete = true;
                return;
            }

        for (Block block : Game.blocks)
            if (getBoundsInParent().intersects(block.getBoundsInParent()) && !block.getType().equals(BlockType.INVISIBLE)) {
                Game.gameRoot.getChildren().remove(this);
                delete = true;
                return;
            }
    }
}