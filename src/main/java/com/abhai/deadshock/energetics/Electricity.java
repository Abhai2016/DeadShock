package com.abhai.deadshock.energetics;

import com.abhai.deadshock.Game;
import com.abhai.deadshock.characters.Booker;
import com.abhai.deadshock.characters.enemies.Enemy;
import com.abhai.deadshock.characters.enemies.EnemyType;
import com.abhai.deadshock.utils.Sounds;
import com.abhai.deadshock.utils.SpriteAnimation;
import javafx.geometry.Rectangle2D;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.util.Duration;

import java.nio.file.Paths;

class Electricity extends Pane {
    private static final int LIFE_INTERVAL = 550;

    private int interval;
    private boolean active;
    private final SpriteAnimation animation;

    Electricity() {
        interval = 0;
        active = false;
        ImageView imageView = new ImageView(new Image(
                Paths.get("resources", "images", "energetics", "electricityShot.png").toUri().toString()));
        imageView.setViewport(new Rectangle2D(0, 0, 172, 63));
        animation = new SpriteAnimation(imageView,
                Duration.seconds(0.5), 10, 1, 0, 0, 172, 63);
        getChildren().add(imageView);
    }

    public void use() {
        if (!active) {
            interval = 0;
            active = true;
            animation.play();

            if (Game.booker.getScaleX() > 0)
                setTranslateX(Game.booker.getTranslateX() + Booker.WIDTH);
            else {
                setScaleX(-1);
                setTranslateX(Game.booker.getTranslateX() - 170);
            }

            Game.gameRoot.getChildren().add(this);
            setTranslateY(Game.booker.getTranslateY() - 10);
            Sounds.electricity.play(Game.menu.getFxSlider().getValue() / 100);
        }
    }

    private void move() {
        if (Game.booker.getScaleX() > 0) {
            setTranslateX(Game.booker.getTranslateX() + Booker.WIDTH);
            setScaleX(1);
        } else {
            setScaleX(-1);
            setTranslateX(Game.booker.getTranslateX() - 170);
        }

        setTranslateY(Game.booker.getTranslateY() - 10);
    }

    public void delete() {
        active = false;
        Game.gameRoot.getChildren().remove(this);
    }

    private void intersectsWithEnemies() {
        for (Enemy enemy : Game.enemies)
            if (getBoundsInParent().intersects(enemy.getBoundsInParent())) {
                if (enemy.getType() == EnemyType.BOSS)
                    enemy.setHP(enemy.getHP() - Game.weapon.getDamage() / 2);
                else {
                    enemy.setHP(0);
                    Sounds.electricityDeath.play(Game.menu.getFxSlider().getValue() / 100);
                    return;
                }
            }
    }

    public void update() {
        if (active) {
            move();

            interval++;
            if (interval >= LIFE_INTERVAL)
                delete();

            intersectsWithEnemies();
        }
    }
}
