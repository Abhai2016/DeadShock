package com.abhai.deadshock.world.supplies;

import com.abhai.deadshock.Game;
import com.abhai.deadshock.types.BlockType;
import com.abhai.deadshock.types.SupplyType;
import com.abhai.deadshock.world.levels.Block;
import javafx.geometry.Point2D;

public class EnemySupply extends Supply {
    private static final int GRAVITY = 10;
    private final Point2D velocity;

    public EnemySupply() {
        type = SupplyType.ENEMY;
        velocity = new Point2D(0, GRAVITY);
    }

    @Override
    protected void move() {
        for (int i = 0; i < velocity.getY(); i++) {
            setTranslateY(getTranslateY() + 1);
            for (Block block : Game.getGameWorld().getLevel().getBlocks())
                if (getBoundsInParent().intersects(block.getBoundsInParent()) && block.getType() != BlockType.INVISIBLE) {
                    setTranslateY(getTranslateY() - 1);
                    return;
                }
        }
    }
}
