package com.abhai.deadshock.Levels;


import com.abhai.deadshock.Characters.Data;
import com.abhai.deadshock.Characters.Enemies;
import com.abhai.deadshock.Supply;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import com.abhai.deadshock.Game;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;

public class Level {
    public static final int BLOCK_SIZE = 48;

    enum BlockType {
        NEW_LAND, LAND, BOX, METAL, STONE, LITTLE_BRICK, LITTLE_LAND, LITTLE_BOX, LITTLE_METAL, LITTLE_STONE
    }

    public static ArrayList<Block> enemyBlocks = new ArrayList<>();
    static Path newLandBlockImagePath = Paths.get("resources", "images", "blocks", "newLandBlock.png");
    static Image imageNewLandBlock = new Image(newLandBlockImagePath.toUri().toString());
    static Path imageBlockPath = Paths.get("resources", "images", "blocks", "block.jpg");
    static Image imageBlock = new Image(imageBlockPath.toUri().toString());

    Path level2ImagePath = Paths.get("resources", "images", "backgrounds", "bioshock_level2.jpg");
    Path level3ImagePath = Paths.get("resources", "images", "backgrounds", "bioshock_level3.jpg");
    Path bossLevelImagePath = Paths.get("resources", "images", "backgrounds", "bossLevel.jpg");
    Path bottomBlockImagePath = Paths.get("resources", "images", "blocks", "bottomBlocks.png");

    private ImageView background;
    private ImageView imgView;

    private String[] level;


    public Level() {
        switch (Game.levelNumber) {
            case 0 -> {
                Path bioshockBackgroundImagePath = Paths.get("resources", "images", "backgrounds", "bioshock.jpg");
                background = new ImageView(new Image(bioshockBackgroundImagePath.toUri().toString()));
                background.setFitHeight(BLOCK_SIZE * 15);
                Path statueImagePath = Paths.get("resources", "images", "statue.jpg");
                imgView = new ImageView(new Image(statueImagePath.toUri().toString()));
                imgView.setFitWidth(223);
                imgView.setTranslateX(BLOCK_SIZE * 300 - imgView.getFitWidth());
                Game.gameRoot.getChildren().addAll(background, imgView);
            }
            case 1 -> {
                background = new ImageView(new Image(level2ImagePath.toUri().toString()));
                background.setFitHeight(BLOCK_SIZE * 15);
                Game.gameRoot.getChildren().add(background);
            }
            case 2 -> {
                background = new ImageView(new Image(level3ImagePath.toUri().toString()));
                background.setFitHeight(BLOCK_SIZE * 15);
                Game.gameRoot.getChildren().add(background);

                Game.supplies.add(new Supply(0, 7920, 576));
                Game.supplies.add(new Supply(0, 8016, 576));
                Game.supplies.add(new Supply(1, 8592, 576));
                Game.supplies.add(new Supply(1, 8496, 576));
                Game.supplies.add(new Supply(0, 12144, 624));
                Game.supplies.add(new Supply(1, 12048, 624));

                for (Supply supply : Game.supplies)
                    Game.gameRoot.getChildren().add(supply);
            }
            case 3 -> {
                background = new ImageView(new Image(bossLevelImagePath.toUri().toString()));
                Game.gameRoot.getChildren().add(background);
                imgView = new ImageView(new Image(bottomBlockImagePath.toUri().toString()));
                imgView.setTranslateY(Level.BLOCK_SIZE * 14);
                Game.gameRoot.getChildren().add(imgView);
            }
        }
    }


    public void changeLevel(int level) {
        if (level == 1) {
            background.setImage(new Image(level2ImagePath.toUri().toString()));
            Game.gameRoot.getChildren().remove(imgView);
        } else if (level == 2) {
            background.setImage(new Image(level3ImagePath.toUri().toString()));
            Game.supplies.add(new Supply(0, 7920, 576));
            Game.supplies.add(new Supply(0, 8016, 576));
            Game.supplies.add(new Supply(1, 8592, 576));
            Game.supplies.add(new Supply(1, 8496, 576));
            Game.supplies.add(new Supply(0, 12144, 624));
            Game.supplies.add(new Supply(1, 12048, 624));
            for (Supply supply : Game.supplies)
                Game.gameRoot.getChildren().add(supply);
        } else {
            background.setImage(new Image(bossLevelImagePath.toUri().toString()));
            imgView = new ImageView(new Image(bottomBlockImagePath.toUri().toString()));
            imgView.setTranslateY(Level.BLOCK_SIZE * 14);
            Game.gameRoot.getChildren().add(imgView);
        }
    }


    private String[] getLevel(int levelNumber) throws IOException {
        Path levelsPath = Paths.get("resources", "data", "levels.dat");
        LevelData levelData = new ObjectMapper().readValue(levelsPath.toFile(), LevelData.class);
        String[] blocks;

        switch (levelNumber) {
            case 1 -> {
                blocks = levelData.getLevel2();
                for (Data enemyData : levelData.getEnemyBlocksForLevel2()) {
                    enemyBlocks.add(new Block(enemyData.getName(), enemyData.getX(), enemyData.getY()));
                }
            }
            case 2 -> {
                blocks = levelData.getLevel3();
                for (Data enemyData : levelData.getEnemyBlocksForLevel3()) {
                    enemyBlocks.add(new Block(enemyData.getName(), enemyData.getX(), enemyData.getY()));
                }
            }
            case 3 -> blocks = levelData.getBossLevel();
            default -> blocks = levelData.getLevel1();
        }

        level = new String[blocks.length];
        for (int i = 0; i < level.length; i++) {
            level[i] = Arrays.stream(blocks).toList().get(i);
        }
        return level;
    }


    public void createLevels() {
        try {
            level = getLevel(Game.levelNumber);
        } catch (IOException e) {
            e.printStackTrace();
        }

        for (int i = 0; i < level.length; i++) {
            String line = level[i];
            for (int j = 0; j < line.length(); j++)
                switch (line.charAt(j)) {
                case '1' -> new Block(BlockType.LITTLE_BRICK, j * BLOCK_SIZE, i * BLOCK_SIZE);
                case '2' -> new Block(BlockType.LAND, j * BLOCK_SIZE, i * BLOCK_SIZE);
                case '3' -> new Block(BlockType.BOX, j * BLOCK_SIZE, i * BLOCK_SIZE);
                case '4' -> new Block(BlockType.METAL, j * BLOCK_SIZE, i * BLOCK_SIZE);
                case '5' -> new Block(BlockType.STONE, j * BLOCK_SIZE, i * BLOCK_SIZE);
                case '6' -> new Block(BlockType.NEW_LAND, j * BLOCK_SIZE, i * BLOCK_SIZE);
                case '7' -> new Block(BlockType.LITTLE_LAND, j * BLOCK_SIZE, i * BLOCK_SIZE);
                case '8' -> new Block(BlockType.LITTLE_BOX, j * BLOCK_SIZE, i * BLOCK_SIZE);
                case '9' -> new Block(BlockType.LITTLE_METAL, j * BLOCK_SIZE, i * BLOCK_SIZE);
                case '_' -> new Block(BlockType.LITTLE_STONE, j * BLOCK_SIZE, i * BLOCK_SIZE);
            }
        }
    }


    public ImageView getBackground() {
        return background;
    }


    public ImageView getImgView() {
        return imgView;
    }
}
