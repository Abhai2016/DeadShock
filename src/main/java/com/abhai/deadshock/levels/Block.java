package com.abhai.deadshock.levels;

import com.abhai.deadshock.Game;
import javafx.geometry.Rectangle2D;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

import java.nio.file.Paths;

public class Block extends Pane {
    public static final int BLOCK_SIZE = 48;
    private static final int LITTLE_BLOCK_HEIGHT = 24;

    private BlockType type;
    private final ImageView blockImage;

    public Block() {
        blockImage = new ImageView(new Image(Paths.get("resources", "images", "levels", "blocks.jpg").toUri().toString()));
    }

    public BlockType getType() {
        return type;
    }

    public void init(BlockType type, int x, int y) {
        this.type = type;
        setTranslateX(x);
        setTranslateY(y);

        if (type.equals(BlockType.LITTLE_METAL) || type.equals(BlockType.LITTLE_BOX) || type.equals(BlockType.LITTLE_BRICK)
                || type.equals(BlockType.LITTLE_LAND) || type.equals(BlockType.LITTLE_STONE))
            blockImage.setFitHeight(LITTLE_BLOCK_HEIGHT);
        else
            blockImage.setFitHeight(BLOCK_SIZE);

        switch (type) {
            case BlockType.LAND -> blockImage.setViewport(new Rectangle2D(66, 0, BLOCK_SIZE, BLOCK_SIZE));
            case BlockType.BOX -> blockImage.setViewport(new Rectangle2D(132, 77, BLOCK_SIZE, BLOCK_SIZE));
            case BlockType.STONE -> blockImage.setViewport(new Rectangle2D(132, 0, BLOCK_SIZE, BLOCK_SIZE));
            case BlockType.METAL -> blockImage.setViewport(new Rectangle2D(198, 76, BLOCK_SIZE, BLOCK_SIZE));
            case BlockType.BRICK ->  blockImage.setViewport(new Rectangle2D(198, 0, BLOCK_SIZE, BLOCK_SIZE));
            case BlockType.NEW_LAND -> blockImage.setViewport(new Rectangle2D(0, 235, BLOCK_SIZE, BLOCK_SIZE));
            case BlockType.LITTLE_BOX -> blockImage.setViewport(new Rectangle2D(132, 200, BLOCK_SIZE, LITTLE_BLOCK_HEIGHT));
            case BlockType.LITTLE_LAND -> blockImage.setViewport(new Rectangle2D(66, 158, BLOCK_SIZE, LITTLE_BLOCK_HEIGHT));
            case BlockType.LITTLE_BRICK -> blockImage.setViewport(new Rectangle2D(198, 158, BLOCK_SIZE, LITTLE_BLOCK_HEIGHT));
            case BlockType.LITTLE_METAL -> blockImage.setViewport(new Rectangle2D(198, 200, BLOCK_SIZE, LITTLE_BLOCK_HEIGHT));
            case BlockType.LITTLE_STONE -> blockImage.setViewport(new Rectangle2D(132, 158, BLOCK_SIZE, LITTLE_BLOCK_HEIGHT));
        }

        if (!type.equals(BlockType.INVISIBLE) && !getChildren().contains(blockImage))
            getChildren().add(blockImage);
        else if (type.equals(BlockType.INVISIBLE))
            getChildren().remove(blockImage);

        Game.gameRoot.getChildren().add(this);
    }
}
