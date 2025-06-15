package com.abhai.deadshock.levels;


import com.abhai.deadshock.characters.enemies.json.EnemyData;
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
    public static final int FIRST_LEVEL = 1;
    public static final int SECOND_LEVEL = 2;
    public static final int THIRD_LEVEL = 3;
    public static final int BOSS_LEVEL = 4;

    enum BlockType {
        NEW_LAND, LAND, BOX, METAL, STONE, LITTLE_BRICK, LITTLE_LAND, LITTLE_BOX, LITTLE_METAL, LITTLE_STONE
    }

    public static ArrayList<Block> enemyBlocks = new ArrayList<>();
    static Path newLandBlockImagePath = Paths.get("resources", "images", "blocks", "newLandBlock.png");
    static Image imageNewLandBlock = new Image(newLandBlockImagePath.toUri().toString());
    static Path imageBlockPath = Paths.get("resources", "images", "blocks", "block.jpg");
    static Image imageBlock = new Image(imageBlockPath.toUri().toString());

    Path secondLevelImagePath = Paths.get("resources", "images", "backgrounds", "bioshock_level2.jpg");
    Path thirdLevelImagePath = Paths.get("resources", "images", "backgrounds", "bioshock_level3.jpg");
    Path bossLevelImagePath = Paths.get("resources", "images", "backgrounds", "bossLevel.jpg");
    Path bottomBlockImagePath = Paths.get("resources", "images", "blocks", "bottomBlocks.png");

    private ImageView background;
    private ImageView imgView;

    private String[] level;


    public Level() {
        switch (Game.levelNumber) {
            case FIRST_LEVEL -> {
                Path bioshockBackgroundImagePath = Paths.get("resources", "images", "backgrounds", "bioshock.jpg");
                background = new ImageView(new Image(bioshockBackgroundImagePath.toUri().toString()));
                background.setFitHeight(BLOCK_SIZE * 15);
                Path statueImagePath = Paths.get("resources", "images", "statue.jpg");
                imgView = new ImageView(new Image(statueImagePath.toUri().toString()));
                imgView.setFitWidth(223);
                imgView.setTranslateX(BLOCK_SIZE * 300 - imgView.getFitWidth());
                Game.gameRoot.getChildren().addAll(background, imgView);
            }
            case SECOND_LEVEL -> {
                background = new ImageView(new Image(secondLevelImagePath.toUri().toString()));
                background.setFitHeight(BLOCK_SIZE * 15);
                Game.gameRoot.getChildren().add(background);
            }
            case THIRD_LEVEL -> {
                background = new ImageView(new Image(thirdLevelImagePath.toUri().toString()));
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
            case BOSS_LEVEL -> {
                background = new ImageView(new Image(bossLevelImagePath.toUri().toString()));
                Game.gameRoot.getChildren().add(background);
                imgView = new ImageView(new Image(bottomBlockImagePath.toUri().toString()));
                imgView.setTranslateY(Level.BLOCK_SIZE * 14);
                Game.gameRoot.getChildren().add(imgView);
            }
        }
    }

    public void changeLevel(int level) {
        if (level == Level.SECOND_LEVEL) {
            background.setImage(new Image(secondLevelImagePath.toUri().toString()));
            Game.gameRoot.getChildren().remove(imgView);
        } else if (level == Level.THIRD_LEVEL) {
            background.setImage(new Image(thirdLevelImagePath.toUri().toString()));
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
            case SECOND_LEVEL -> {
                blocks = levelData.getSecondLevel();
                for (EnemyData enemyData : levelData.getEnemyBlocksForTheSecondLevel()) {
                    enemyBlocks.add(new Block(enemyData.getType(), enemyData.getX(), enemyData.getY()));
                }
            }
            case THIRD_LEVEL -> {
                blocks = levelData.getThirdLevel();
                for (EnemyData enemyData : levelData.getEnemyBlocksForTheThirdLevel()) {
                    enemyBlocks.add(new Block(enemyData.getType(), enemyData.getX(), enemyData.getY()));
                }
            }
            case BOSS_LEVEL -> blocks = levelData.getBossLevel();
            default -> blocks = levelData.getFirstLevel();
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
