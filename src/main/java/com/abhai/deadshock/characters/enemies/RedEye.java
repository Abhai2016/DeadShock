package com.abhai.deadshock.characters.enemies;

import com.abhai.deadshock.Game;
import com.abhai.deadshock.levels.Block;
import com.abhai.deadshock.weapons.RedEyeWeapon;

public class RedEye extends Comstock {

    public RedEye(long x, long y) {
        HP = 125;
        type = EnemyType.RED_EYE;
        enemyWeapon = new RedEyeWeapon();

        setTranslateX(x);
        setTranslateY(y);

        getChildren().add(imageView);
        Game.gameRoot.getChildren().add(this);
    }

    @Override
    protected boolean intersectsWithBlock(Block block) {
        return getBoundsInParent().intersects(block.getBoundsInParent());
    }

    @Override
    protected String getImageName() {
        return "redEye.png";
    }
}