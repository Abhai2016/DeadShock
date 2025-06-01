package com.abhai.deadshock.Levels;

import com.abhai.deadshock.Game;
import javafx.geometry.Rectangle2D;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

public class Block extends Pane {
    private String blockType;

    public Block(Level.BlockType blockType, int x, int y) {
        this.blockType = blockType.toString();
        ImageView block = new ImageView(Level.imageBlock);
        block.setFitWidth(Level.BLOCK_SIZE);
        block.setFitHeight(Level.BLOCK_SIZE);

        setTranslateX(x);
        setTranslateY(y);

        if (Game.levelNumber == 0)
            switch (this.blockType) {
                case "BOX":
                    block.setViewport( new Rectangle2D(250, 208, 82, 82) );
                    break;
                case "METAL":
                    block.setViewport( new Rectangle2D(360, 208, 82, 82) );
                    break;
                case "STONE":
                    block.setViewport( new Rectangle2D(250, 78, 82, 82) );
                    break;
                case "LITTLE_BOX":
                    block.setFitHeight(Level.BLOCK_SIZE / 2);
                    block.setViewport( new Rectangle2D(250, 414, 82, 41) );
                    break;
                case "LITTLE_BRICK":
                    block.setFitHeight(Level.BLOCK_SIZE / 2);
                    block.setViewport( new Rectangle2D(360, 344, 82, 41) );
                    break;
                case "LITTLE_STONE":
                    block.setFitHeight(Level.BLOCK_SIZE / 2);
                    block.setViewport( new Rectangle2D(250, 344, 82, 41) );
                    break;
            }
        else if (Game.levelNumber == 1)
            switch (this.blockType) {
                case "LAND":
                    block.setViewport( new Rectangle2D(140, 78, 82, 82) );
                    break;
                case "METAL":
                    block.setViewport( new Rectangle2D(360, 208, 82, 82) );
                    break;
                case "STONE":
                    block.setViewport( new Rectangle2D(250, 78, 82, 82) );
                    break;
                case "LITTLE_BOX":
                    block.setFitHeight(Level.BLOCK_SIZE / 2);
                    block.setViewport( new Rectangle2D(250, 414, 82, 41) );
                    break;
                case "LITTLE_BRICK":
                    block.setFitHeight(Level.BLOCK_SIZE / 2);
                    block.setViewport( new Rectangle2D(360, 344, 82, 41) );
                    break;
                case "LITTLE_LAND":
                    block.setFitHeight(Level.BLOCK_SIZE / 2);
                    block.setViewport( new Rectangle2D(140, 344, 82, 41) );
                    break;
                case "LITTLE_METAL":
                    block.setFitHeight(Level.BLOCK_SIZE / 2);
                    block.setViewport( new Rectangle2D(360, 414, 82, 41) );
                    break;
                case "LITTLE_STONE":
                    block.setFitHeight(Level.BLOCK_SIZE / 2);
                    block.setViewport( new Rectangle2D(250, 344, 82, 41) );
                    break;
            } else if (Game.levelNumber == 2) {
            block = new ImageView(Level.imageNewLandBlock);
        } else if (Game.levelNumber == 3) {
            block.setFitHeight(Level.BLOCK_SIZE / 2);
            block.setViewport( new Rectangle2D(360, 414, 82, 41) );
        }

        getChildren().add(block);
        Game.blocks.add(this);
        Game.gameRoot.getChildren().add(this);
    }


    public Block(String blockType, long x, long y) {
        if (blockType.equals("invisible")) {
            setTranslateX(x);
            setTranslateY(y);

            setWidth(Level.BLOCK_SIZE);
            setHeight(Level.BLOCK_SIZE);
        }
    }


    public String getBlockType() {
        return blockType;
    }
}
