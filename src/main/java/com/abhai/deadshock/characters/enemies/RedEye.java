package com.abhai.deadshock.characters.enemies;

import com.abhai.deadshock.Game;
import com.abhai.deadshock.levels.Block;
import com.abhai.deadshock.utils.SpriteAnimation;
import com.abhai.deadshock.weapons.RedEyeWeapon;
import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Duration;

import java.nio.file.Paths;

public class RedEye extends Comstock {

    public RedEye(long x, long y) {
        type = EnemyType.RED_EYE;
        HP = 125;

        velocity = new Point2D(0, 0);
        enemyWeapon = new RedEyeWeapon();

        imageView = new ImageView(new Image(
                Paths.get("resources", "images", "characters", "redEye.png").toUri().toString()));
        imageView.setViewport(new Rectangle2D(0, 0, 62, 65));
        animation = new SpriteAnimation(imageView,
                Duration.seconds(0.5), 10, 10, 0, 0, 62, 65);

        setTranslateX(x);
        setTranslateY(y);

        getChildren().add(imageView);
        Game.gameRoot.getChildren().add(this);
    }

    @Override
    protected boolean intersects(Block block) {
        return getBoundsInParent().intersects(block.getBoundsInParent());
    }
}
