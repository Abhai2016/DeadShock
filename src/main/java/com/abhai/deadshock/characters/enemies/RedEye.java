package com.abhai.deadshock.characters.enemies;

import com.abhai.deadshock.types.EnemyType;
import com.abhai.deadshock.types.SupplyType;
import com.abhai.deadshock.types.WeaponType;
import com.abhai.deadshock.weapons.EnemyWeapon;
import com.abhai.deadshock.world.levels.Block;

public class RedEye extends Comstock {

    public RedEye() {
        HP = 125;
        initWeapon();
        type = EnemyType.RED_EYE;
    }

    @Override
    public void init(int x, int y) {
        super.init(x, y);

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
    protected SupplyType getSupplyType() {
        return SupplyType.MACHINE_GUN_BULLETS;
    }

    @Override
    protected void initWeapon() {
        enemyWeapon = new EnemyWeapon(WeaponType.MACHINE_GUN);
    }
}