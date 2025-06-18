package com.abhai.deadshock.weapons;

import com.abhai.deadshock.characters.enemies.Enemy;
import com.abhai.deadshock.levels.Block;
import com.abhai.deadshock.Game;
import com.abhai.deadshock.Sounds;
import com.abhai.deadshock.levels.BlockType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.nio.file.Path;
import java.nio.file.Paths;

public class EnemyBullet extends Bullet {
    private byte damage;

    EnemyBullet(String enemyName, double scaleX, double x, double y) {
        Path imagePath = Paths.get("resources", "images", "weapons", "bullet.png");
        bullet = new ImageView(new Image(imagePath.toUri().toString()));

        if (scaleX < 0) {
            direction = false;
            setScaleX(-1);
        }

        switch (Game.difficultyLevelText) {
            case "marik":
                damage = 2;
                break;
            case "easy":
                damage = 3;
                break;
            case "normal":
                damage = 5;
                break;
            case "high":
                damage = 7;
                break;
            case "hardcore":
                damage = 10;
                break;
        }

        switch(enemyName) {
            case "comstock":
                setTranslateY(y + 9);
                if (direction)
                    setTranslateX(x + 67);
                else
                    setTranslateX(x - 5);
                break;
            case "red_eye":
                setTranslateY(y + 9);
                if (direction)
                    setTranslateX(x + 67);
                else
                    setTranslateX(x - 5);
                break;
        }
        getChildren().add(bullet);
        Game.gameRoot.getChildren().add(this);
    }



    @Override
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

        if (getBoundsInParent().intersects(Game.booker.getBoundsInParent())) {
            Game.gameRoot.getChildren().remove(this);
            delete = true;
            Game.booker.setHP(Game.booker.getHP() - damage);
            byte rand = (byte)(Math.random() * 3);
            switch (rand) {
                case 0:
                    Sounds.bookerHit.play(Game.menu.voiceSlider.getValue() / 100);
                    break;
                case 1:
                    Sounds.bookerHit2.play(Game.menu.voiceSlider.getValue() / 100);
                    break;
                case 2:
                    Sounds.bookerHit3.play(Game.menu.voiceSlider.getValue() / 100);
                    break;
            }
        }

        for (Block block : Game.blocks)
            if (getBoundsInParent().intersects(block.getBoundsInParent()) && !block.getType().equals(BlockType.INVISIBLE)) {
                Game.gameRoot.getChildren().remove(this);
                delete = true;
                return;
            }

        for (Enemy enemy : Game.enemies)
            if (getBoundsInParent().intersects(enemy.getBoundsInParent())) {
                if (Game.difficultyLevelText.equals("marik") || Game.difficultyLevelText.equals("easy")) {
                    enemy.setHP(enemy.getHP() - Game.weapon.getDamage());
                    Game.gameRoot.getChildren().remove(this);
                    delete = true;
                    return;
                }
            }
    }
}