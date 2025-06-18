package com.abhai.deadshock;

import com.abhai.deadshock.characters.*;
import com.abhai.deadshock.characters.enemies.*;
import com.abhai.deadshock.characters.enemies.EnemyData;
import com.abhai.deadshock.energetics.Energetic;
import com.abhai.deadshock.levels.Block;
import com.abhai.deadshock.levels.Level;
import com.abhai.deadshock.weapons.Bullet;
import com.abhai.deadshock.weapons.EnemyBullet;
import com.abhai.deadshock.weapons.Weapon;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.abhai.deadshock.levels.Block.BLOCK_SIZE;


public class Game extends Application {
    private static Path elizabethSoundPath = Paths.get("resources", "sounds", "voices", "elizabeth", "booker.mp3");
    private static Path bookerSoundPath = Paths.get("resources", "sounds", "voices", "booker", "fuck.mp3");

    public static ArrayList<Supply> supplies = new ArrayList<>();
    public static ArrayList<Block> blocks = new ArrayList<>();
    public static ArrayList<Bullet> bullets = new ArrayList<>();
    public static ArrayList<EnemyBullet> enemyBullets = new ArrayList<>();
    public static ArrayList<Enemy> enemies = new ArrayList<>();
    static HashMap<KeyCode, Boolean> keys = new HashMap<>();

    public static Stage stage;
    public static Scene scene;

    public static Pane gameRoot = new Pane();
    public static Pane appRoot = new Pane();

    public static Menu menu;
    public static Booker booker;
    public static HUD hud;
    public static Weapon weapon;
    public static Elizabeth elizabeth;
    public static Boss boss;
    static VendingMachine vendingMachine;

    static Tutorial tutorial;
    public static CutScenes cutScene;
    public static Energetic energetic;

    public static int levelNumber;
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
            ObjectMapper mapper = new ObjectMapper();
            Path savesPath = Paths.get("resources", "data", "saves.dat");

            if (savesPath.toFile().exists()) {
                loadSaves(mapper, savesPath);
            } else {
                levelNumber = Level.FIRST_LEVEL;
                level = new Level();
                level.createLevels();
                vendingMachine = new VendingMachine();
                booker = new Booker();
                weapon = new Weapon();
                hud = new HUD();
                energetic = new Energetic();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        tutorial = new Tutorial();
        loadOptions();
        createEnemies();

        booker.translateXProperty().addListener( ((observable, oldValue, newValue) -> {
            int offset = newValue.intValue();
            if (offset > 600 && offset < gameRoot.getWidth() - 680) {
                gameRoot.setLayoutX( - (offset - 600) );
                level.getBackground().setLayoutX((offset - 600) / 1.5);
            }
        }));

        if (levelNumber != Level.BOSS_LEVEL)
            vendingMachine.createButtons();
    }

    static void loadSaves(ObjectMapper mapper, Path savesPath) throws IOException {
        Saves saves = mapper.readValue(savesPath.toFile(), Saves.class);
        difficultyLevelText = saves.getDifficultyLevel();
        levelNumber = saves.getLevelNumber();

        level = new Level();
        level.createLevels();
        if (levelNumber != Level.BOSS_LEVEL)
            vendingMachine = new VendingMachine();
        hud = new HUD();

        booker = new Booker();
        booker.setMoney(saves.getMoney());
        booker.setSalt(saves.getSalt());

        switch (levelNumber) {
            case Level.SECOND_LEVEL -> {
                weapon = new Weapon(saves.isCanChoosePistol());
                weapon.setWeaponClip(saves.getPistolClip());
                weapon.setBullets(saves.getPistolBullets());
                energetic = new Energetic(saves.isCanChooseDevilKiss());
            }
            case Level.THIRD_LEVEL -> {
                weapon = new Weapon(saves.isCanChoosePistol(), saves.isCanChooseMachineGun());
                if (weapon.isCanChooseMachineGun()) {
                    weapon.setWeaponClip(saves.getMachineGunClip());
                    weapon.setBullets(saves.getMachineGunBullets());
                    if (weapon.isCanChoosePistol()) {
                        Weapon.WeaponData.pistolClip = saves.getPistolClip();
                        Weapon.WeaponData.pistolBullets = saves.getPistolBullets();
                    }
                } else if (weapon.isCanChoosePistol()) {
                    weapon.setWeaponClip(saves.getPistolClip());
                    weapon.setBullets(saves.getPistolBullets());
                }
                energetic = new Energetic(saves.isCanChooseDevilKiss(), saves.isCanChooseElectricity());
                boss = new Boss(BLOCK_SIZE * 299, BLOCK_SIZE * 13);
            }
            case Level.BOSS_LEVEL -> {
                weapon = new Weapon(saves.isCanChoosePistol(), saves.isCanChooseMachineGun(), saves.isCanChooseRPG());
                if (weapon.isCanChooseRPG()) {
                    weapon.setWeaponClip(saves.getRpgClip());
                    weapon.setBullets(saves.getRpgBullets());
                    if (weapon.isCanChooseMachineGun()) {
                        Weapon.WeaponData.machineGunClip = saves.getMachineGunClip();
                        Weapon.WeaponData.machineGunBullets = saves.getMachineGunBullets();
                    }
                    if (weapon.isCanChoosePistol()) {
                        Weapon.WeaponData.pistolClip = saves.getPistolClip();
                        Weapon.WeaponData.pistolBullets = saves.getPistolBullets();
                    }
                } else if (weapon.isCanChooseMachineGun()) {
                    weapon.setWeaponClip(saves.getMachineGunClip());
                    weapon.setBullets(saves.getMachineGunBullets());
                    if (weapon.isCanChoosePistol()) {
                        Weapon.WeaponData.pistolClip = saves.getPistolClip();
                        Weapon.WeaponData.pistolBullets = saves.getPistolBullets();
                    }
                } else if (weapon.isCanChoosePistol()) {
                    weapon.setWeaponClip(saves.getPistolClip());
                    weapon.setBullets(saves.getPistolBullets());
                }
                energetic = new Energetic(saves.isCanChooseDevilKiss(), saves.isCanChooseElectricity(), saves.isCanChooseHypnosis());
                boss = new Boss(BLOCK_SIZE * 10, BLOCK_SIZE * 14);
            }
        }
        elizabeth = new Elizabeth();
    }

    static void loadOptions() {
        try {
            ObjectMapper mapper = new ObjectMapper();
            Path optionsPath = Paths.get("resources", "data", "options.dat");

            if (menu == null) {
                menu = new Menu();
            }

            if (optionsPath.toFile().exists()) {
                Options options = mapper.readValue(optionsPath.toFile(), Options.class);
                menu.musicSlider.setValue(options.getMusicVolume());
                menu.fxSlider.setValue(options.getFxVolume());
                menu.voiceSlider.setValue(options.getVoiceVolume());

                String track = options.getTrack();
                menu.checkMusicForContinueGame(track);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void clearData() {
        Game.gameRoot.setLayoutX(0);
        if (level != null)
            level.getBackground().setLayoutX(0);

        for (Enemy enemy : Game.enemies) {
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

    static void clearDataForNewGame() {
        Game.appRoot.getChildren().remove(Game.gameRoot);
        Game.gameRoot.setLayoutX(0);

        if (level != null)
            Game.level.getBackground().setLayoutX(0);

        for (Enemy enemy : Game.enemies) {
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

        if (levelNumber > Level.FIRST_LEVEL) {
            for (Block block : blocks)
                gameRoot.getChildren().remove(block);
            blocks.clear();

            gameRoot.getChildren().remove(elizabeth);
            elizabeth = null;

            Path savesPath = Paths.get("resources", "data", "saves.dat");
            if (savesPath.toFile().exists()) {
                savesPath.toFile().delete();
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

        if (Game.levelNumber == Level.THIRD_LEVEL) {
            for (Supply supply : supplies)
                Game.gameRoot.getChildren().remove(supply);
            supplies.clear();
        }
    }

    public static void createEnemies() {
        try {
            ObjectMapper mapper = new ObjectMapper();
            Path enemiesPath = Paths.get("resources", "data", "enemies.dat");
            Map<String, EnemyData[]> jsonEnemies = mapper.readValue(enemiesPath.toFile(), new TypeReference<>() {});
            EnemyData[] enemiesByLevel = new EnemyData[]{};

            switch (levelNumber) {
                case Level.FIRST_LEVEL -> enemiesByLevel = jsonEnemies.get("firstLevel");
                case Level.SECOND_LEVEL -> enemiesByLevel = jsonEnemies.get("secondLevel");
                case Level.THIRD_LEVEL -> enemiesByLevel = jsonEnemies.get("thirdLevel");
            }

            for (EnemyData enemy : enemiesByLevel) {
                switch (enemy.getType()) {
                    case "comstock" -> enemies.add(new Comstock(enemy.getX(), enemy.getY()));
                    case "red_eye" -> enemies.add(new RedEye(enemy.getX(), enemy.getY()));
                    case "camper" -> enemies.add(new Camper(enemy.getX(), enemy.getY()));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    static void nothing() {
    }

    private static void update() {
        for (Enemy enemy : enemies) {
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

        if (energetic.getType() != null)
            energetic.update();
        if (levelNumber > Level.FIRST_LEVEL)
            elizabeth.update();

        menu.update();
        hud.update();
        weapon.update();

        if (booker.getTranslateX() > BLOCK_SIZE * 295 && levelNumber != Level.THIRD_LEVEL)
            cutScene = new CutScenes();

        if (booker.getTranslateX() > BLOCK_SIZE * 285 && levelNumber == Level.THIRD_LEVEL) {
            if (boss.getTrompInterval() == 0) {
                Sounds.elizabethMediaPlayer = new MediaPlayer(new Media(elizabethSoundPath.toUri().toString()));
                Sounds.elizabethMediaPlayer.setVolume(Game.menu.voiceSlider.getValue() / 100);
                Sounds.elizabethMediaPlayer.play();

                Sounds.elizabethMediaPlayer.setOnEndOfMedia(() -> {
                    Sounds.feelsBetter = new MediaPlayer(new Media(bookerSoundPath.toUri().toString()));
                    Sounds.feelsBetter.setVolume(Game.menu.voiceSlider.getValue() / 100);
                    Sounds.feelsBetter.play();
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
        Path iconPath = Paths.get("resources", "images", "icons", "icon.jpg");
        stage.getIcons().add(new Image(iconPath.toUri().toString()));
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