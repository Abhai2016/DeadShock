package com.abhai.deadshock.Characters;

import com.abhai.deadshock.Levels.Block;
import com.abhai.deadshock.Levels.Level;
import com.abhai.deadshock.Game;
import com.abhai.deadshock.Weapon.RedEyeWeapon;
import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

import java.nio.file.Path;
import java.nio.file.Paths;

public class EnemyRedEye extends EnemyComstock {

    public EnemyRedEye(long x, long y) {
        name = "red_eye";
        delete = false;
        pickUpSupply = false;
        canSeeBooker = false;
        hypnosis = false;

        voiceInterval = 0;
        HP = 125;

        rectHP = new Rectangle(62, 2, Color.RED);
        rectHP.setTranslateX(getTranslateX());
        rectHP.setVisible(false);

        velocity = new Point2D(0, 0);
        enemyWeapon = new RedEyeWeapon();
        Path imagePath = Paths.get("resources", "images", "characters", "red_eye.png");
        imgView = new ImageView(new Image(imagePath.toUri().toString()));
        imgView.setViewport(new Rectangle2D(0, 0, 62, 65));
        animation = new SpriteAnimation(imgView, Duration.seconds(0.5), 10, 10, 0, 0, 62, 65);

        setTranslateX(x);
        setTranslateY(y);

        getChildren().add(imgView);
        Game.gameRoot.getChildren().addAll(this, rectHP);
    }


    @Override
    public void update() {
        super.update();

        if (HP < 125)
            rectHP.setVisible(true);
        rectHP.setWidth(HP / 125 * 62);

        if (!hypnosis) {
            for (Block block : Level.enemyBlocks)
                if (block.getBoundsInParent().intersects(getBoundsInParent())) {
                    setTranslateX(getTranslateX() - getScaleX() * ENEMY_SPEED);
                    jump();
                    return;
                }
        }
    }
}
