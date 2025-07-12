package com.abhai.deadshock.characters.enemies;

import com.abhai.deadshock.Game;
import com.abhai.deadshock.levels.Block;
import com.abhai.deadshock.weapons.RedEyeWeapon;
import javafx.geometry.Point2D;

public class RedEye extends Comstock {

    public RedEye(long x, long y) {
        type = EnemyType.RED_EYE;
        HP = 125;

        velocity = new Point2D(0, 10);
        enemyWeapon = new RedEyeWeapon();

        setTranslateX(x);
        setTranslateY(y);

        getChildren().add(imageView);
        Game.gameRoot.getChildren().add(this);
    }

    @Override
    protected boolean intersects(Block block) {
        return getBoundsInParent().intersects(block.getBoundsInParent());
    }

    @Override
    protected String getImageName() {
        return "redEye.png";
    }
}
