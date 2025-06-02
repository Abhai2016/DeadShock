package com.abhai.deadshock.Levels;


import com.abhai.deadshock.Supply;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import com.abhai.deadshock.Game;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.*;
import java.util.ArrayList;

public class Level {
    public static final int BLOCK_SIZE = 48;

    enum BlockType {
        NEW_LAND, LAND, BOX, METAL, STONE, LITTLE_BRICK, LITTLE_LAND, LITTLE_BOX, LITTLE_METAL, LITTLE_STONE
    }

    public static ArrayList<Block> enemyBlocks = new ArrayList<>();

    static Image imageNewLandBlock = new Image(new File("images/blocks/newLandBlock.png").toURI().toString());
    static Image imageBlock = new Image(new File("images/blocks/blocks.jpg").toURI().toString());
    private ImageView background;
    private ImageView imgView;

    private String[] level;


    public Level() {
        switch ((int)Game.levelNumber) {
            case 0:
                background = new ImageView(new Image(new File("images/backgrounds/bioshock.jpg").toURI().toString()));
                background.setFitHeight(BLOCK_SIZE * 15);

                imgView = new ImageView(new Image(new File("images/statue.jpg").toURI().toString()));
                imgView.setFitWidth(223);
                imgView.setTranslateX(BLOCK_SIZE * 300 - imgView.getFitWidth());
                Game.gameRoot.getChildren().addAll(background, imgView);
                break;
            case 1:
                background = new ImageView(new Image(new File("images/backgrounds/bioshock_level2.jpg").toURI().toString()));
                background.setFitHeight(BLOCK_SIZE * 15);
                Game.gameRoot.getChildren().add(background);
                break;
            case 2:
                background = new ImageView(new Image(new File("images/backgrounds/bioshock_level3.jpg").toURI().toString()));
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
                break;
            case 3:
                background = new ImageView(new Image(new File("images/backgrounds/bossLevel.jpg").toURI().toString()));
                Game.gameRoot.getChildren().add(background);
                imgView = new ImageView(new Image(new File("images/blocks/bottomBlocks.png").toURI().toString()));
                imgView.setTranslateY(Level.BLOCK_SIZE * 14);
                Game.gameRoot.getChildren().add(imgView);
                break;
        }
    }


    public void changeLevel(long level) {
        if (level == 1) {
            background.setImage(new Image(new File("images/backgrounds/bioshock_level2.jpg").toURI().toString()));
            Game.gameRoot.getChildren().remove(imgView);
        } else if (level == 2) {
            background.setImage(new Image(new File("images/backgrounds/bioshock_level3.jpg").toURI().toString()));
            Game.supplies.add(new Supply(0, 7920, 576));
            Game.supplies.add(new Supply(0, 8016, 576));
            Game.supplies.add(new Supply(1, 8592, 576));
            Game.supplies.add(new Supply(1, 8496, 576));
            Game.supplies.add(new Supply(0, 12144, 624));
            Game.supplies.add(new Supply(1, 12048, 624));
            for (Supply supply : Game.supplies)
                Game.gameRoot.getChildren().add(supply);
        } else {
            background.setImage(new Image(new File("images/backgrounds/bossLevel.jpg").toURI().toString()));
            imgView = new ImageView(new Image(new File("images/blocks/bottomBlocks.png").toURI().toString()));
            imgView.setTranslateY(Level.BLOCK_SIZE * 14);
            Game.gameRoot.getChildren().add(imgView);
        }
    }


    private void getLevel_data() {
        try {
            JSONParser jsonParser = new JSONParser();
            Object obj = jsonParser.parse(new FileReader("data/levels.dat"));
            JSONObject jsonObject = new JSONObject((JSONObject)obj);
            JSONArray jsonArrayForBlocks = (JSONArray) jsonObject.get("level1");
            JSONArray jsonArrayForEnemyBlocks;
            JSONObject enemyBlock;

            switch ((int)Game.levelNumber) {
                case 1:
                    jsonArrayForBlocks = (JSONArray) jsonObject.get("level2");
                    jsonArrayForEnemyBlocks = (JSONArray) jsonObject.get("enemyBlocksForLevel2");

                    for (int i = 0; i < jsonArrayForEnemyBlocks.size(); i++) {
                        enemyBlock = (JSONObject) jsonArrayForEnemyBlocks.get(i);
                        enemyBlocks.add(new Block( (String)enemyBlock.get("name"), (long)enemyBlock.get("x"), (long)enemyBlock.get("y")));
                    }
                    break;
                case 2:
                    jsonArrayForBlocks = (JSONArray) jsonObject.get("level3");
                    jsonArrayForEnemyBlocks = (JSONArray) jsonObject.get("enemyBlocksForLevel3");

                    for (int i = 0; i < jsonArrayForEnemyBlocks.size(); i++) {
                        enemyBlock = (JSONObject) jsonArrayForEnemyBlocks.get(i);
                        enemyBlocks.add(new Block( (String)enemyBlock.get("name"), (long)enemyBlock.get("x"), (long)enemyBlock.get("y")));
                    }
                    break;
                case 3:
                    jsonArrayForBlocks = (JSONArray) jsonObject.get("bossLevel");
                    break;
            }


            level = new String[jsonArrayForBlocks.size()];
            for (int i = 0; i < level.length; i++)
                level[i] = jsonArrayForBlocks.get(i).toString();
        } catch (Exception e) {
            System.exit(0);
        }
    }


    public void createLevels(long levelNumber) {
        getLevel_data();
        if (levelNumber == 0)
            for (int i = 5; i < level.length; i++) {
                String line = level[i];
                for (int j = 0; j < line.length(); j++)
                    switch (line.charAt(j)) {
                        case '0':
                            break;
                        case '1':
                            Block platfromFloor = new Block(BlockType.LITTLE_BRICK, j * BLOCK_SIZE, i * BLOCK_SIZE);
                            break;
                        case '3':
                            Block box = new Block(BlockType.BOX, j * BLOCK_SIZE, i * BLOCK_SIZE);
                            break;
                        case '4':
                            Block metal = new Block(BlockType.METAL, j * BLOCK_SIZE, i * BLOCK_SIZE);
                            break;
                        case '5':
                            Block stone = new Block(BlockType.STONE, j * BLOCK_SIZE, i * BLOCK_SIZE);
                            break;
                        case '8':
                            Block littleBox = new Block(BlockType.LITTLE_BOX, j * BLOCK_SIZE, i * BLOCK_SIZE);
                            break;
                        case '_':
                            Block littleStone = new Block(BlockType.LITTLE_STONE, j * BLOCK_SIZE, i * BLOCK_SIZE);
                            break;
                    }
            }
        else if (levelNumber == 1)
            for (int i = 5; i < level.length; i++) {
                String line = level[i];
                for (int j = 0; j < line.length(); j++)
                    switch (line.charAt(j)) {
                        case '0':
                            break;
                        case '1':
                            Block platfromFloor = new Block(BlockType.LITTLE_BRICK, j * BLOCK_SIZE, i * BLOCK_SIZE);
                            break;
                        case '2':
                            Block land = new Block(BlockType.LAND, j * BLOCK_SIZE, i * BLOCK_SIZE);
                            break;
                        case '4':
                            Block metal = new Block(BlockType.METAL, j * BLOCK_SIZE, i * BLOCK_SIZE);
                            break;
                        case '5':
                            Block stone = new Block(BlockType.STONE, j * BLOCK_SIZE, i * BLOCK_SIZE);
                            break;
                        case '7':
                            Block littleLand = new Block(BlockType.LITTLE_LAND, j * BLOCK_SIZE, i * BLOCK_SIZE);
                            break;
                        case '8':
                            Block littleBox = new Block(BlockType.LITTLE_BOX, j * BLOCK_SIZE, i * BLOCK_SIZE);
                            break;
                        case '9':
                            Block littleMetal = new Block(BlockType.LITTLE_METAL, j * BLOCK_SIZE, i * BLOCK_SIZE);
                            break;
                        case '_':
                            Block littleStone = new Block(BlockType.LITTLE_STONE, j * BLOCK_SIZE, i * BLOCK_SIZE);
                            break;
                    }
            } else if (Game.levelNumber == 2)
                for (int i = 3; i < level.length; i++) {
                    String line = level[i];
                    for (int j = 0; j < line.length(); j++)
                        switch (line.charAt(j)) {
                            case '0':
                                break;
                            case '1':
                                Block newLandBlock = new Block(BlockType.NEW_LAND, j * BLOCK_SIZE, i * BLOCK_SIZE);
                                break;
                        }
                } else if (Game.levelNumber == 3)
                    for (int i = 1; i < level.length; i++) {
                        String line = level[i];
                        for (int j = 0; j < line.length(); j++)
                            switch (line.charAt(j)) {
                                case '0':
                                    break;
                                case '1':
                                    Block block = new Block(BlockType.LITTLE_METAL, j * BLOCK_SIZE, i * BLOCK_SIZE);
                                    break;
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
