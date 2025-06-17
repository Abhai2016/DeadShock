package com.abhai.deadshock.levels;

import com.abhai.deadshock.Game;
import javafx.geometry.Rectangle2D;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

import java.nio.file.Paths;

public class Block extends Pane {
    public static final int BLOCK_SIZE = 48;
    private BlockType type;

    public Block(BlockType type, int x, int y) {
        this.type = type;
        ImageView block = new ImageView(new Image(Paths.get("resources", "images", "blocks", "blocks.jpg")
                .toUri().toString()));
        block.setFitWidth(BLOCK_SIZE);
        block.setFitHeight(BLOCK_SIZE);

        setTranslateX(x);
        setTranslateY(y);

        switch (type) {
            case BlockType.LITTLE_BRICK -> {
                block.setFitHeight(BLOCK_SIZE / 2);
                block.setViewport( new Rectangle2D(360, 344, 82, 41) );
            }
            case BlockType.LAND -> block.setViewport( new Rectangle2D(140, 78, 82, 82) );
            case BlockType.BOX -> block.setViewport( new Rectangle2D(250, 208, 82, 82) );
            case BlockType.METAL -> block.setViewport( new Rectangle2D(360, 208, 82, 82) );
            case BlockType.STONE -> block.setViewport( new Rectangle2D(250, 78, 82, 82) );
            case BlockType.NEW_LAND -> block = new ImageView(new Image(
                    Paths.get("resources", "images", "blocks", "newLandBlock.png").toUri().toString()));
            case BlockType.LITTLE_LAND -> {
                block.setFitHeight(BLOCK_SIZE / 2);
                block.setViewport( new Rectangle2D(140, 344, 82, 41) );
            }
            case BlockType.LITTLE_BOX -> {
                block.setFitHeight(BLOCK_SIZE / 2);
                block.setViewport( new Rectangle2D(250, 414, 82, 41) );
            }
            case BlockType.LITTLE_METAL -> {
                block.setFitHeight(BLOCK_SIZE / 2);
                block.setViewport( new Rectangle2D(360, 414, 82, 41) );
            }
            case BlockType.LITTLE_STONE -> {
                block.setFitHeight(BLOCK_SIZE / 2);
                block.setViewport( new Rectangle2D(250, 344, 82, 41) );
            }
        }

        if (!type.equals(BlockType.INVISIBLE)) {
            getChildren().add(block);
        }

        Game.blocks.add(this);
        Game.gameRoot.getChildren().add(this);
    }


    public BlockType getType() {
        return type;
    }

    public void setBlockType(BlockType type) {
        this.type = type;
    }
}
