package com.abhai.deadshock.energetics;

import com.abhai.deadshock.Game;
import com.abhai.deadshock.characters.Booker;
import com.abhai.deadshock.characters.enemies.Enemy;
import com.abhai.deadshock.types.EnemyType;
import com.abhai.deadshock.utils.GameMedia;
import com.abhai.deadshock.utils.SpriteAnimation;
import javafx.geometry.Rectangle2D;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.util.Duration;

import java.nio.file.Paths;

class Electricity extends Pane {
    private static final int LIFE_INTERVAL = 250;

    private int interval;
    private boolean active;
    private final SpriteAnimation animation;

    Electricity() {
        interval = 0;
        active = false;
        ImageView imageView = new ImageView(new Image(Paths.get("resources", "images", "energetics", "electricityShot.png").toUri().toString()));
        imageView.setViewport(new Rectangle2D(0, 0, 172, 63));
        animation = new SpriteAnimation(imageView, Duration.seconds(0.5), 10, 1, 0, 0, 172, 63);
        getChildren().add(imageView);
    }

    public void use() {
        if (!active) {
            interval = 0;
            active = true;
            animation.play();

            if (Game.getGameWorld().getBooker().getScaleX() > 0)
                setTranslateX(Game.getGameWorld().getBooker().getTranslateX() + Booker.WIDTH);
            else {
                setScaleX(-1);
                setTranslateX(Game.getGameWorld().getBooker().getTranslateX() - 170);
            }

            Game.getGameWorld().getGameRoot().getChildren().add(this);
            setTranslateY(Game.getGameWorld().getBooker().getTranslateY() - 10);
            GameMedia.ELECTRICITY.play(Game.getGameWorld().getMenu().getFxSlider().getValue() / 100);
        }
    }

    private void move() {
        if (Game.getGameWorld().getBooker().getScaleX() > 0) {
            setScaleX(1);
            setTranslateX(Game.getGameWorld().getBooker().getTranslateX() + Booker.WIDTH);
        } else {
            setScaleX(-1);
            setTranslateX(Game.getGameWorld().getBooker().getTranslateX() - 170);
        }
        setTranslateY(Game.getGameWorld().getBooker().getTranslateY() - 10);
    }

    public void delete() {
        active = false;
        Game.getGameWorld().getGameRoot().getChildren().remove(this);
    }

    private void intersectsWithEnemies() {
        for (Enemy enemy : Game.getGameWorld().getEnemies())
            if (getBoundsInParent().intersects(enemy.getBoundsInParent()))
                if (enemy.getType() != EnemyType.BOSS) {
                    enemy.setHP(0);
                    GameMedia.ELECTRICITY_DEATH.play(Game.getGameWorld().getMenu().getFxSlider().getValue() / 100);
                    return;
                } else
                    enemy.setHP(enemy.getHP() - Game.getGameWorld().getBooker().getWeapon().getBulletDamage() / 2);
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
