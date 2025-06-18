package com.abhai.deadshock.energetics;

import com.abhai.deadshock.characters.enemies.Enemy;
import com.abhai.deadshock.characters.SpriteAnimation;
import com.abhai.deadshock.levels.Block;
import com.abhai.deadshock.Game;
import com.abhai.deadshock.levels.Level;
import javafx.geometry.Rectangle2D;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.util.Duration;

import java.nio.file.Path;
import java.nio.file.Paths;

class DevilKissShot extends Pane {
    private ImageView imgView;
    private SpriteAnimation animation;
    private boolean direction;

    DevilKissShot(double x, double y) {
        Path imagePath = Paths.get("resources", "images", "energetics", "devilKissShot.png");
        imgView = new ImageView(new Image(imagePath.toUri().toString()));
        imgView.setViewport( new Rectangle2D(0, 0, 64, 30) );
        animation = new SpriteAnimation(imgView, Duration.seconds(0.5), 8, 8, 0, 0, 64, 30);
        animation.play();

        setTranslateX(x - 24);
        setTranslateY(y);

        if (Game.booker.getScaleX() > 0) {
            direction = true;
            setTranslateX(getTranslateX() + 40);
        } else
            setScaleX(-1);

        getChildren().add(imgView);
        Game.gameRoot.getChildren().add(this);
    }


    public boolean update() {
        if (direction)
            setTranslateX(getTranslateX() + 5);
        else
            setTranslateX(getTranslateX() - 5);

        for (Enemy enemy : Game.enemies)
            if (getBoundsInParent().intersects(enemy.getBoundsInParent()))
                enemy.setHP(0);

        for (Block block : Game.blocks)
            if (getBoundsInParent().intersects(block.getBoundsInParent()))
                return true;

        if (Game.levelNumber == Level.BOSS_LEVEL)
            if (getBoundsInParent().intersects(Game.boss.getBoundsInParent())) {
                Game.boss.setHP(Game.boss.getHP() - Game.weapon.getRpgDamage());
                return true;
            }

        if (getTranslateX() > Game.booker.getTranslateX() + 680
                || getTranslateX() < Game.booker.getTranslateX() - 600)
            return true;

        return false;
    }
}
