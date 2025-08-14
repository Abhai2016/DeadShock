package com.abhai.deadshock.energetics;

import com.abhai.deadshock.Game;
import com.abhai.deadshock.characters.enemies.Enemy;
import com.abhai.deadshock.characters.enemies.EnemyType;
import com.abhai.deadshock.levels.Block;
import com.abhai.deadshock.utils.SpriteAnimation;
import javafx.geometry.Rectangle2D;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.util.Duration;

import java.nio.file.Paths;

class DevilKissShot extends Pane {
    private static final int SPEED = 5;

    private boolean toDelete;
    private boolean direction;
    private final SpriteAnimation animation;

    DevilKissShot() {
        toDelete = false;
        ImageView imageView = new ImageView(new Image(
                Paths.get("resources", "images", "energetics", "devilKissShot.png").toUri().toString()));
        imageView.setViewport(new Rectangle2D(0, 0, 64, 30));
        getChildren().add(imageView);
        animation = new SpriteAnimation(imageView, Duration.seconds(0.5), 8, 8, 0, 0, 64, 30);
    }

    private void intersectsWithWorld() {
        for (Block block : Game.level.getBlocks())
            if (getBoundsInParent().intersects(block.getBoundsInParent())) {
                toDelete = true;
                return;
            }

        if (getTranslateX() > Game.booker.getTranslateX() + 680
                || getTranslateX() < Game.booker.getTranslateX() - 600)
            toDelete = true;
    }

    public void init(double x, double y) {
        toDelete = false;
        animation.play();

        if (Game.booker.getScaleX() > 0) {
            setScaleX(1);
            direction = true;
            setTranslateX(x + 16);
        } else {
            setScaleX(-1);
            direction = false;
            setTranslateX(x - 24);
        }

        setTranslateY(y);
        Game.gameRoot.getChildren().add(this);
    }

    private void intersectsWithEnemies() {
        for (Enemy enemy : Game.enemies)
            if (getBoundsInParent().intersects(enemy.getBoundsInParent()))
                if (enemy.getType() == EnemyType.BOSS) {
                    enemy.setHP(enemy.getHP() - Game.weapon.getRpgDamage());
                    toDelete = true;
                } else
                    enemy.setHP(0);
    }

    public boolean isToDelete() {
        return toDelete;
    }

    public void update() {
        if (direction)
            setTranslateX(getTranslateX() + SPEED);
        else
            setTranslateX(getTranslateX() - SPEED);

        intersectsWithWorld();
        intersectsWithEnemies();
    }
}
