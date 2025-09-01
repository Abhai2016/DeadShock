package com.abhai.deadshock.characters.enemies;

import com.abhai.deadshock.world.levels.Block;
import com.abhai.deadshock.weapons.EnemyWeapon;
import com.abhai.deadshock.weapons.WeaponType;

public class RedEye extends Comstock {

    public RedEye() {
        HP = 125;
        initWeapon();
        type = EnemyType.RED_EYE;
    }

    @Override
    public void reset() {
        super.reset();

        HP = 125;
    }

    @Override
    protected boolean intersectsWithBlock(Block block) {
        return getBoundsInParent().intersects(block.getBoundsInParent());
    }

    @Override
    protected String getImageName() {
        return "redEye.png";
    }

    @Override
    protected void initWeapon() {
        enemyWeapon = new EnemyWeapon(WeaponType.MACHINE_GUN);
    }
}