package com.abhai.deadshock;

import com.abhai.deadshock.Characters.*;
import com.abhai.deadshock.Characters.Character;
import com.abhai.deadshock.Energetics.Energetic;
import com.abhai.deadshock.Levels.Block;
import com.abhai.deadshock.Levels.Level;
import com.abhai.deadshock.Weapon.Bullet;
import com.abhai.deadshock.Weapon.EnemyBullet;
import com.abhai.deadshock.Weapon.Weapon;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;


public class Game extends Application {
    public static ArrayList<Supply> supplies = new ArrayList<>();
    public static ArrayList<Block> blocks = new ArrayList<>();
    public static ArrayList<Bullet> bullets = new ArrayList<>();
    public static ArrayList<EnemyBullet> enemyBullets = new ArrayList<>();
    public static ArrayList<EnemyBase> enemies = new ArrayList<>();
    static HashMap<KeyCode, Boolean> keys = new HashMap<>();

    public static Stage stage;
    public static Scene scene;

    public static Pane gameRoot = new Pane();
    public static Pane appRoot = new Pane();

    public static Menu menu;
    public static Character booker;
    public static HUD hud;
    public static Weapon weapon;
    public static Elizabeth elizabeth;
    public static Boss boss;
    static VendingMachine vendingMachine;

    static Tutorial tutorial;
    public static CutScenes cutScene;
    public static Energetic energetic;

    public static long levelNumber;
    public static String difficultyLevelText = "normal";

    public static Level level;

    public static AnimationTimer timer = new AnimationTimer() {
        @Override
        public void handle(long now) {
            update();
        }
    };





    static void initContent() {
        appRoot.getChildren().add(gameRoot);

        try {
            JSONParser jsonParser = new JSONParser();
            JSONObject data = (JSONObject) jsonParser.parse(new FileReader("file:/../data/saves.dat"));

            JSONObject levelData = (JSONObject) data.get("levelData");
            difficultyLevelText = (String) levelData.get("difficultyLevel");
            levelNumber = (long) levelData.get("levelNumber");

            level = new Level();
            level.createLevels(levelNumber);
            if (levelNumber != 3)
                vendingMachine = new VendingMachine();
            hud = new HUD();

            JSONObject character = (JSONObject) data.get("character");
            booker = new Character();
            booker.setMoney((long)character.get("money"));
            booker.setSalt((long)character.get("salt"));

            switch ((int)levelNumber) {
                case 1:
                    weapon = new Weapon((boolean) character.get("canChoosePistol"));
                    weapon.setWeaponClip((long) character.get("pistolClip"));
                    weapon.setBullets((long) character.get("pistolBullets"));
                    energetic = new Energetic((boolean)character.get("canChooseDevilKiss"));
                    break;
                case 2:
                    weapon = new Weapon((boolean) character.get("canChoosePistol"), (boolean) character.get("canChooseMachineGun"));
                    if (weapon.isCanChooseMachineGun()) {
                        weapon.setWeaponClip((long) character.get("machineGunClip"));
                        weapon.setBullets((long) character.get("machineGunBullets"));
                        if (weapon.isCanChoosePistol()) {
                            Weapon.WeaponData.pistolClip = (long) character.get("pistolClip");
                            Weapon.WeaponData.pistolBullets = (long) character.get("pistolBullets");
                        }
                    } else if (weapon.isCanChoosePistol()) {
                        weapon.setWeaponClip((long) character.get("pistolClip"));
                        weapon.setBullets((long) character.get("pistolBullets"));
                    }
                    energetic = new Energetic((boolean) character.get("canChooseDevilKiss"), (boolean) character.get("canChooseElectricity"));
                    boss = new Boss(Level.BLOCK_SIZE * 299, Level.BLOCK_SIZE * 13);
                    break;
                case 3:
                    weapon = new Weapon((boolean) character.get("canChoosePistol"),
                            (boolean) character.get("canChooseMachineGun"), (boolean) character.get("canChooseRPG"));
                    if (weapon.isCanChooseRPG()) {
                        weapon.setWeaponClip((long) character.get("rpgClip"));
                        weapon.setBullets((long) character.get("rpgBullets"));
                        if (weapon.isCanChooseMachineGun()) {
                            Weapon.WeaponData.machineGunClip = (long) character.get("machineGunClip");
                            Weapon.WeaponData.machineGunBullets = (long) character.get("machineGunBullets");
                        }
                        if (weapon.isCanChoosePistol()) {
                            Weapon.WeaponData.pistolClip = (long) character.get("pistolGunClip");
                            Weapon.WeaponData.pistolBullets = (long) character.get("pistolBullets");
                        }
                    } else if (weapon.isCanChooseMachineGun()) {
                        weapon.setWeaponClip((long)character.get("machineGunClip"));
                        weapon.setBullets((long)character.get("machineGunBullets"));
                        if (weapon.isCanChoosePistol()) {
                            Weapon.WeaponData.pistolClip = (long) character.get("pistolClip");
                            Weapon.WeaponData.pistolBullets = (long) character.get("pistolBullets");
                        }
                    } else if (weapon.isCanChoosePistol()) {
                        weapon.setWeaponClip((long)character.get("pistolClip"));
                        weapon.setBullets((long)character.get("pistolBullets"));
                    }
                    energetic = new Energetic((boolean) character.get("canChooseDevilKiss"), (boolean) character.get("canChooseElectricity"),
                            (boolean) character.get("canChooseHypnotist"));
                    boss = new Boss(Level.BLOCK_SIZE * 10, Level.BLOCK_SIZE * 14);
                    break;
            }
            elizabeth = new Elizabeth();
        } catch (Exception e) {
            clearDataForNewGame("New Game");
            levelNumber = 0;
            level = new Level();
            level.createLevels(levelNumber);
            vendingMachine = new VendingMachine();
            booker = new Character();
            weapon = new Weapon();
            hud = new HUD();
            energetic = new Energetic();
        }
        tutorial = new Tutorial();

        try {
            JSONParser jsonParser = new JSONParser();
            JSONObject optionsData = (JSONObject) jsonParser.parse(new FileReader("file:/../data/options.dat"));
            menu = new Menu();
            menu.musicSlider.setValue((double)optionsData.get("musicVolume"));
            menu.fxSlider.setValue((double)optionsData.get("FXVolume"));
            menu.voiceSlider.setValue((double)optionsData.get("voiceVolume"));

            String track = (String) optionsData.get("track");
            menu.checkMusicForContinueGame(track);
        } catch (Exception e) {
            menu = new Menu();
        }

        createEnemies();

        booker.translateXProperty().addListener( ((observable, oldValue, newValue) -> {
            int offset = newValue.intValue();
            if (offset > 600 && offset < gameRoot.getWidth() - 680) {
                gameRoot.setLayoutX( - (offset - 600) );
                level.getBackground().setLayoutX((offset - 600) / 1.5);
            }
        }));

        if (levelNumber != 3)
            vendingMachine.createButtons();
    }


    static void initContentForNewGame() {
        levelNumber = 0;
        level = new Level();
        level.createLevels(levelNumber);
        vendingMachine = new VendingMachine();
        booker = new Character();
        weapon = new Weapon();
        hud = new HUD();
        energetic = new Energetic();
        tutorial = new Tutorial();

        createEnemies();
        appRoot.getChildren().add(menu.menuBox);

        booker.translateXProperty().addListener( ((observable, oldValue, newValue) -> {
            int offset = newValue.intValue();
            if (offset > 600 && offset < gameRoot.getWidth() - 680) {
                gameRoot.setLayoutX( - (offset - 600) );
                level.getBackground().setLayoutX((offset - 600) / 1.5);
            }
        }));

        vendingMachine.createButtons();
    }


    public static void clearData() {
        Game.gameRoot.setLayoutX(0);
        if (level != null)
            level.getBackground().setLayoutX(0);
        for (EnemyBase enemy : Game.enemies) {
            Game.gameRoot.getChildren().remove(enemy);
            Game.gameRoot.getChildren().remove(enemy.getRectHP());
        }
        Game.enemies.clear();

        for (EnemyBullet enemyBullet : Game.enemyBullets)
            Game.gameRoot.getChildren().remove(enemyBullet);
        Game.enemyBullets.clear();

        for(Bullet bullet : Game.bullets)
            Game.gameRoot.getChildren().remove(bullet);
        Game.bullets.clear();

        Game.keys.clear();
    }


    static void clearDataForNewGame(String string) {
        Game.gameRoot.setLayoutX(0);
        if (level != null)
            Game.level.getBackground().setLayoutX(0);
        for (EnemyBase enemy : Game.enemies) {
            Game.gameRoot.getChildren().remove(enemy);
            Game.gameRoot.getChildren().remove(enemy.getRectHP());
        }
        Game.enemies.clear();

        for (EnemyBullet enemyBullet : Game.enemyBullets)
            Game.gameRoot.getChildren().remove(enemyBullet);
        Game.enemyBullets.clear();

        for(Bullet bullet : Game.bullets)
            Game.gameRoot.getChildren().remove(bullet);
        Game.bullets.clear();

        if (levelNumber != 0) {
            for (Block block : blocks)
                gameRoot.getChildren().remove(block);
            blocks.clear();

            gameRoot.getChildren().remove(elizabeth);
            elizabeth = null;

            if (string.equals("New Game")) {
                try (DataOutputStream dataOutputStream = new DataOutputStream(new FileOutputStream("file:/../data/saves.dat"))) {
                    dataOutputStream.writeChar(' ');
                } catch (IOException e) {
                    System.exit(0);
                }
            }

            gameRoot.getChildren().remove(vendingMachine);
            vendingMachine = null;

            gameRoot.getChildren().remove(level.getImgView());
        }

        Game.keys.clear();

        if (booker != null) {
            gameRoot.getChildren().remove(booker);
            booker = null;
        }

        if (hud != null) {
            appRoot.getChildren().remove(hud);
            hud = null;
        }

        if (weapon != null) {
            gameRoot.getChildren().remove(weapon);
            weapon = null;
        }

        if (tutorial != null) {
            tutorial.deleteMenuText();
            tutorial.deleteText();
            tutorial = null;
        }

        if (energetic != null) {
            gameRoot.getChildren().remove(energetic);
            energetic = null;
        }

        if (menu != null)
            appRoot.getChildren().remove(menu.menuBox);

        if (boss != null)
            boss = null;

        if (Game.levelNumber == 2) {
            for (Supply supply : supplies)
                Game.gameRoot.getChildren().remove(supply);
            supplies.clear();
        }
    }


    public static void createEnemies() {
        try {
            JSONParser jsonParser = new JSONParser();
            Object object = jsonParser.parse(new FileReader("file:/../data/enemies.dat"));
            JSONObject jsonObject = new JSONObject( (JSONObject)object );
            JSONArray jsonArray = (JSONArray)jsonObject.get("level1");
            if (levelNumber == 1)
                jsonArray = (JSONArray)jsonObject.get("level2");
            else if (levelNumber == 2)
                jsonArray = (JSONArray)jsonObject.get("level3");

            JSONObject enemy;
            for (int i = 0; i < jsonArray.size(); i++) {
                enemy = (JSONObject)jsonArray.get(i);
                switch ( (String)enemy.get("name") ) {
                    case "comstock":
                        enemies.add(new EnemyComstock( (long)enemy.get("x"), (long)enemy.get("y") ));
                        break;
                    case "red_eye":
                        enemies.add(new EnemyRedEye( (long)enemy.get("x"), (long)enemy.get("y")));
                        break;
                    case "camper":
                        enemies.add(new EnemyCamper( (long)enemy.get("x"), (long)enemy.get("y")));
                        break;
                }
            }
        } catch (Exception e) {
            System.exit(0);
        }
    }


    static void nothing() {
    }


    private static void update() {
        for (EnemyBase enemy : enemies) {
            enemy.update();
            if (enemy.isDelete()) {
                enemies.remove(enemy);
                break;
            }
        }
        for (Bullet bullet : bullets) {
            bullet.update();
            if (bullet.isDelete()) {
                bullets.remove(bullet);
                break;
            }
        }
        for (EnemyBullet enemyBullet : enemyBullets) {
            enemyBullet.update();
            if (enemyBullet.isDelete()) {
                enemyBullets.remove(enemyBullet);
                break;
            }
        }

        if (boss != null)
            boss.update();
        Controller.update();
        booker.update();

        if (!energetic.getName().equals(""))
            energetic.update();
        if (levelNumber > 0)
            elizabeth.update();

        menu.update();
        hud.update();
        weapon.update();

        if (booker.getTranslateX() > Level.BLOCK_SIZE * 295 && levelNumber != 2)
            cutScene = new CutScenes();

        if (booker.getTranslateX() > Level.BLOCK_SIZE * 285 && levelNumber == 2) {
            if (boss.getTrompInterval() == 0) {
                Sounds.elizabethMediaPlayer = new MediaPlayer(new Media(
                        new File("file:/../sounds/voice/elizabeth/oh_booker.mp3").toURI().toString()));
                Sounds.elizabethMediaPlayer.setVolume(Game.menu.voiceSlider.getValue() / 100);
                Sounds.elizabethMediaPlayer.play();

                Sounds.elizabethMediaPlayer.setOnEndOfMedia(() -> {
                    Sounds.bookerVoice = new MediaPlayer(new Media(
                            new File("file:/../sounds/voice/booker/your_mother.mp3").toURI().toString()));
                    Sounds.bookerVoice.setVolume(Game.menu.voiceSlider.getValue() / 100);
                    Sounds.bookerVoice.play();
                });
            }
            boss.setTrompInterval(boss.getTrompInterval() + 1);
            if (boss.getTrompInterval() > 90)
                cutScene = new CutScenes();
        }
    }



    @Override
    public void start(Stage primaryStage) throws Exception {
        stage = primaryStage;
        scene = new Scene(appRoot, 1280, 720);

        initContent();
        stage.getIcons().add(new Image("file:/../images/icon.jpg"));
        stage.setTitle("DeadShock");
        stage.setResizable(false);
        stage.setWidth(scene.getWidth());
        stage.setHeight(scene.getHeight());
        stage.setScene(scene);
        stage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}