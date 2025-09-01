package com.abhai.deadshock.world.levels;

import com.abhai.deadshock.Game;
import com.abhai.deadshock.utils.Texts;
import com.abhai.deadshock.utils.pools.ObjectPool;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Map;

import static com.abhai.deadshock.world.levels.Block.BLOCK_SIZE;

public class Level {
    public static final int FIRST_LEVEL = 1;
    public static final int SECOND_LEVEL = 2;
    public static final int THIRD_LEVEL = 3;
    public static final int BOSS_LEVEL = 4;

    private static final Path LEVELS_DATA_PATH = Paths.get("resources", "data", "levels.dat");
    private static final Path FIRST_LEVEL_IMAGE_PATH = Paths.get("resources", "images", "levels", "backgrounds", "firstLevel.jpg");
    private static final Path SECOND_LEVEL_IMAGE_PATH = Paths.get("resources", "images", "levels", "backgrounds", "secondLevel.jpg");
    private static final Path THIRD_LEVEL_IMAGE_PATH = Paths.get("resources", "images", "levels", "backgrounds", "thirdLevel.jpg");
    private static final Path BOSS_LEVEL_IMAGE_PATH = Paths.get("resources", "images", "levels", "backgrounds", "bossLevel.jpg");

    private ImageView statue;
    private final ImageView background;
    private final ArrayList<Block> blocks;
    private final ObjectPool<Block> blockPool;

    public Level() {
        blocks = new ArrayList<>();
        background = new ImageView();
        blockPool = new ObjectPool<>(Block::new, 350, 500);

        initializeBackground();

        Game.gameRoot.getChildren().add(background);
        if (Game.levelNumber == FIRST_LEVEL && !Game.gameRoot.getChildren().contains(statue))
            Game.gameRoot.getChildren().add(statue);

        createLevel();
    }

    public void changeLevel() {
        initializeBackground();
        if (Game.levelNumber == FIRST_LEVEL && !Game.gameRoot.getChildren().contains(statue))
            Game.gameRoot.getChildren().add(statue);
        createLevel();
    }

    private void createLevel() {
        String[] level = new String[0];

        try {
            Map<String, String[]> levelData = new ObjectMapper().readValue(LEVELS_DATA_PATH.toFile(), new TypeReference<>() {});
            switch (Game.levelNumber) {
                case FIRST_LEVEL -> level = levelData.get(Texts.FIRST_LEVEL);
                case SECOND_LEVEL -> level = levelData.get(Texts.SECOND_LEVEL);
                case THIRD_LEVEL -> level =  levelData.get(Texts.THIRD_LEVEL);
                case BOSS_LEVEL -> level = levelData.get(Texts.BOSS_LEVEL);
            }
        } catch (IOException e) {
            System.out.println(e.getLocalizedMessage());
        }

        if (!blocks.isEmpty())
            resetBlocks();

        for (int i = 0; i < level.length; i++) {
            String line = level[i];
            for (int j = 0; j < line.length(); j++)
                switch (line.charAt(j)) {
                    case '1' -> initBlock(BlockType.LITTLE_BRICK, j * BLOCK_SIZE, i * BLOCK_SIZE);
                    case '2' -> initBlock(BlockType.LAND, j * BLOCK_SIZE, i * BLOCK_SIZE);
                    case '3' -> initBlock(BlockType.BOX, j * BLOCK_SIZE, i * BLOCK_SIZE);
                    case '4' -> initBlock(BlockType.METAL, j * BLOCK_SIZE, i * BLOCK_SIZE);
                    case '5' -> initBlock(BlockType.STONE, j * BLOCK_SIZE, i * BLOCK_SIZE);
                    case '6' -> initBlock(BlockType.NEW_LAND, j * BLOCK_SIZE, i * BLOCK_SIZE);
                    case '7' -> initBlock(BlockType.LITTLE_LAND, j * BLOCK_SIZE, i * BLOCK_SIZE);
                    case '8' -> initBlock(BlockType.LITTLE_BOX, j * BLOCK_SIZE, i * BLOCK_SIZE);
                    case '9' -> initBlock(BlockType.LITTLE_METAL, j * BLOCK_SIZE, i * BLOCK_SIZE);
                    case '_' -> initBlock(BlockType.LITTLE_STONE, j * BLOCK_SIZE, i * BLOCK_SIZE);
                    case '-' -> initBlock(BlockType.INVISIBLE, j * BLOCK_SIZE, i * BLOCK_SIZE);
                    case '*' -> initBlock(BlockType.BRICK, j * BLOCK_SIZE, i * BLOCK_SIZE);
                }
        }
    }

    private void resetBlocks() {
        for (Block block : blocks) {
            blockPool.put(block);
            Game.gameRoot.getChildren().remove(block);
        }
        blocks.clear();
    }

    private void initializeBackground() {
        switch (Game.levelNumber) {
            case FIRST_LEVEL -> {
                background.setImage(new Image(FIRST_LEVEL_IMAGE_PATH.toUri().toString()));
                statue = new ImageView(new Image(Paths.get("resources", "images", "levels", "statue.jpg").toUri().toString()));
                statue.setTranslateX(BLOCK_SIZE * 300 - statue.getImage().getWidth());
            }
            case SECOND_LEVEL -> {
                background.setImage(new Image(SECOND_LEVEL_IMAGE_PATH.toUri().toString()));
                Game.gameRoot.getChildren().remove(statue);
            }
            case THIRD_LEVEL -> background.setImage(new Image(THIRD_LEVEL_IMAGE_PATH.toUri().toString()));
            case BOSS_LEVEL -> background.setImage(new Image(BOSS_LEVEL_IMAGE_PATH.toUri().toString()));
        }
    }

    public ArrayList<Block> getBlocks() {
        return blocks;
    }

    private void initBlock(BlockType type, int x, int y) {
        Block block = blockPool.get();
        block.init(type, x, y);
        blocks.add(block);
    }

    public void setBackgroundLayoutX(double x) {
        background.setLayoutX(x);
    }
}
