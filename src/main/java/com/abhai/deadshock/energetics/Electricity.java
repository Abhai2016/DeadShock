package com.abhai.deadshock.energetics;

import com.abhai.deadshock.characters.enemies.Enemy;
import com.abhai.deadshock.utils.SpriteAnimation;
import com.abhai.deadshock.Game;
import com.abhai.deadshock.utils.Sounds;
import com.abhai.deadshock.levels.Level;
import javafx.geometry.Rectangle2D;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

import java.nio.file.Path;
import java.nio.file.Paths;

class Electricity extends Pane {
    private ImageView imgView;
    private SpriteAnimation animation;
    private Path soundPath = Paths.get("resources", "sounds", "fx", "energetics", "electricity.mp3");
    private MediaPlayer mediaPlayerElectricity = new MediaPlayer(new Media(soundPath.toUri().toString()));
    private boolean delete = false;

    Electricity(double x, double y) {
        Path imagePath = Paths.get("resources", "images", "energetics", "electricityShot.png");
        imgView = new ImageView(new Image(imagePath.toUri().toString()));
        imgView.setViewport( new Rectangle2D(0, 0, 172, 63) );
        animation = new SpriteAnimation(imgView, Duration.seconds(0.5), 10, 1, 0, 0, 172, 63);
        animation.play();

        if (Game.booker.getScaleX() > 0)
            setTranslateX(x + Game.booker.getWidth());
        else {
            setScaleX(-1);
            setTranslateX(Game.booker.getTranslateX() - 170);
        }

        setTranslateY(y - 10);

        mediaPlayerElectricity.setVolume(Game.menu.fxSlider.getValue() / 100);
        mediaPlayerElectricity.play();
        mediaPlayerElectricity.setOnEndOfMedia( () -> {
            delete = true;
            Game.gameRoot.getChildren().remove(this);
        });

        getChildren().add(imgView);
        Game.gameRoot.getChildren().add(this);
    }

    boolean isDelete() {
        return delete;
    }

    private void move() {
        if (Game.booker.getScaleX() > 0) {
            setTranslateX(Game.booker.getTranslateX() + Game.booker.getWidth());
            setScaleX(1);
        }
        else {
            setScaleX(-1);
            setTranslateX(Game.booker.getTranslateX() - 170);
        }

        setTranslateY(Game.booker.getTranslateY() - 10);
    }

    public boolean update() {
        for (Enemy enemy : Game.enemies)
            if (getBoundsInParent().intersects(enemy.getBoundsInParent())) {
                enemy.setHP(0);
                Sounds.electricityDeath.play(Game.menu.fxSlider.getValue() / 100);
                return true;
            }

        if (Game.levelNumber == Level.BOSS_LEVEL)
            if (getBoundsInParent().intersects(Game.boss.getBoundsInParent()))
                Game.boss.setHP(Game.boss.getHP() - Game.weapon.getDamage() / 2);
        move();
        return false;
    }
}
