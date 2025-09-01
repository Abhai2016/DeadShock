package com.abhai.deadshock;

import com.abhai.deadshock.characters.Booker;
import com.abhai.deadshock.characters.Elizabeth;
import com.abhai.deadshock.characters.enemies.*;
import com.abhai.deadshock.energetics.Energetic;
import com.abhai.deadshock.hud.HUD;
import com.abhai.deadshock.hud.Tutorial;
import com.abhai.deadshock.world.VendingMachine;
import com.abhai.deadshock.menus.DifficultyLevel;
import com.abhai.deadshock.world.levels.Level;
import com.abhai.deadshock.menus.Menu;
import com.abhai.deadshock.world.supplies.Supply;
import com.abhai.deadshock.utils.Controller;
import com.abhai.deadshock.utils.Options;
import com.abhai.deadshock.utils.Saves;
import com.abhai.deadshock.utils.pools.ObjectPool;
import com.abhai.deadshock.utils.pools.ObjectPoolManager;
import com.abhai.deadshock.weapons.Weapon;
import com.abhai.deadshock.weapons.WeaponType;
import com.abhai.deadshock.weapons.bullets.EnemyBullet;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.animation.AnimationTimer;
import javafx.animation.FadeTransition;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.abhai.deadshock.world.levels.Block.BLOCK_SIZE;


public class Game extends Application {
    private static final Media THIRD_CUTSCENE = new Media(Paths.get("resources", "videos", "end.mp4").toUri().toString());
    private static final Media SECOND_CUTSCENE = new Media(Paths.get("resources", "videos", "comstock.mp4").toUri().toString());
    private static final Media FIRST_CUTSCENE = new Media(Paths.get("resources", "videos", "elizabeth.mp4").toUri().toString());

    private static MediaPlayer video;
    private static MediaView videoView;

    private static Path savesPath = Paths.get("resources", "data", "saves.dat");
    private static Path optionsPath = Paths.get("resources", "data", "options.dat");

    public static ArrayList<Supply> supplies = new ArrayList<>();
    public static ObjectPool<Supply> supplyPool = new ObjectPool<>(Supply::new, 10, 20);
    public static ArrayList<EnemyBullet> enemyBullets = new ArrayList<>();
    public static ObjectPool<EnemyBullet> enemyBulletsPool = new ObjectPool<>(EnemyBullet::new, 50, 150);
    public static ArrayList<Enemy> enemies = new ArrayList<>();
    private final static ObjectPoolManager<Enemy> enemyPools = new ObjectPoolManager<>();
    public static HashMap<KeyCode, Boolean> keys = new HashMap<>();
    public static ObjectMapper mapper = new ObjectMapper();

    public static Stage stage;
    public static Scene scene;

    public static Pane gameRoot = new Pane();
    public static Pane appRoot = new Pane();

    public static Menu menu;
    public static Booker booker;
    public static HUD hud;
    public static Elizabeth elizabeth;
    public static VendingMachine vendingMachine;

    public static int levelNumber;
    public static DifficultyLevel difficultyLevel = DifficultyLevel.MEDIUM;

    public static Level level;
    public static boolean active;

    //TODO connect frames to time
    public static AnimationTimer timer = new AnimationTimer() {
        @Override
        public void handle(long now) {
            update();
        }
    };

    public static void initContent() {
        timer.start();
        active = false;
        videoView = new MediaView();
        appRoot.getChildren().add(gameRoot);
        scene.setOnKeyPressed(event -> keys.put(event.getCode(), true));
        scene.setOnKeyReleased(event -> keys.put(event.getCode(), false));

        try {
            ObjectMapper mapper = new ObjectMapper();
            Path savesPath = Paths.get("resources", "data", "saves.dat");

            if (savesPath.toFile().exists()) {
                loadSaves(mapper, savesPath);
            } else {
                levelNumber = Level.FIRST_LEVEL;
                level = new Level();
                vendingMachine = new VendingMachine();
                booker = new Booker();
                elizabeth = new Elizabeth();
                hud = new HUD();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        enemyPools.register(Boss.class, Boss::new, 1, 2);
        enemyPools.register(Camper.class, Camper::new, 5, 10);
        enemyPools.register(RedEye.class, RedEye::new, 10, 15);
        enemyPools.register(Comstock.class, Comstock::new, 15, 20);

        createEnemies();
        loadOptions();
        Tutorial.init();

        booker.translateXProperty().addListener(((observable, oldValue, newValue) -> {
            int offset = newValue.intValue();
            if (offset > 600 && offset < gameRoot.getWidth() - 680) {
                gameRoot.setLayoutX(-(offset - 600));
                level.setBackgroundLayoutX((offset - 600) / 1.5);
            }
        }));

        if (levelNumber != Level.BOSS_LEVEL)
            vendingMachine.createButtons();
    }

    //TODO fix a bug with vendingMachine where buttons to buy or esc don't work after the death/levelReset/changeLevel, etc.
    //TODO do not forget to change difficulty's level description
    public static void initContentForNewGame() {
        levelNumber = Level.FIRST_LEVEL;
        level.changeLevel();
        vendingMachine = new VendingMachine();
        createEnemies();
        Tutorial.init();
        booker.reset();

        if (levelNumber != Level.BOSS_LEVEL)
            vendingMachine.createButtons();
    }

    static void loadSaves(ObjectMapper mapper, Path savesPath) throws IOException {
        Saves saves = mapper.readValue(savesPath.toFile(), Saves.class);
        difficultyLevel = saves.getDifficultyLevel();
        levelNumber = saves.getLevelNumber();

        level = new Level();
        if (levelNumber != Level.BOSS_LEVEL)
            vendingMachine = new VendingMachine();
        hud = new HUD();

        Weapon.Builder weaponBuilder = new Weapon.Builder();
        weaponBuilder.setCanChoosePistol(saves.isCanChoosePistol())
                .setCanChooseMachineGun(saves.isCanChooseMachineGun())
                .setCanChooseRpg(saves.isCanChooseRPG())
                .setPistolBullets(saves.getPistolBullets())
                .setMachineGunBullets(saves.getMachineGunBullets())
                .setRpgBullets(saves.getRpgBullets());

        Energetic.Builder energeticBuilder = new Energetic.Builder();
        energeticBuilder.canChooseDevilKiss(saves.isCanChooseDevilKiss())
                .canChooseElectricity(saves.isCanChooseElectricity())
                .canChooseHypnosis(saves.isCanChooseHypnosis());

        booker = new Booker();
        elizabeth = new Elizabeth();
        elizabeth.init();
        booker.setMoney(saves.getMoney());
        booker.setSalt(saves.getSalt());
        booker.setWeapon(weaponBuilder);
        booker.setEnergetic(energeticBuilder);
    }

    static void loadOptions() {
        try {
            ObjectMapper mapper = new ObjectMapper();
            Path optionsPath = Paths.get("resources", "data", "options.dat");

            if (menu == null)
                menu = new Menu();

            if (optionsPath.toFile().exists()) {
                Options options = mapper.readValue(optionsPath.toFile(), Options.class);
                menu.getMusicSlider().setValue(options.getMusicVolume());
                menu.getFxSlider().setValue(options.getFxVolume());
                menu.getVoiceSlider().setValue(options.getVoiceVolume());

                String track = options.getTrack();
                menu.checkMusicForContinueGame(track);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    static void saveSaves() {
        try (FileWriter fileWriter = new FileWriter(savesPath.toFile())) {
            if (savesPath.toFile().exists()) {
                savesPath.toFile().delete();
            }

            if (!savesPath.toFile().exists()) {
                savesPath.toFile().createNewFile();
            }

            Saves saves = new Saves();
            saves.setDifficultyLevel(difficultyLevel);
            saves.setLevelNumber(levelNumber);
            saves.setMoney(booker.getMoney());
            saves.setSalt(booker.getSalt());

            switch (booker.getWeapon().getType()) {
                case WeaponType.PISTOL -> {
                    saves.setPistolBullets(booker.getWeapon().getCurrentBullets());
                    saves.setMachineGunBullets(booker.getWeapon().getMachineGunBullets());
                    saves.setRpgBullets(booker.getWeapon().getRpgBullets());
                }
                case WeaponType.MACHINE_GUN -> {
                    saves.setPistolBullets(booker.getWeapon().getPistolBullets());
                    saves.setMachineGunBullets(booker.getWeapon().getCurrentBullets());
                    saves.setRpgBullets(booker.getWeapon().getRpgBullets());
                }
                case WeaponType.RPG -> {
                    saves.setPistolBullets(booker.getWeapon().getPistolBullets());
                    saves.setMachineGunBullets(booker.getWeapon().getMachineGunBullets());
                    saves.setRpgBullets(booker.getWeapon().getCurrentBullets());
                }
            }

            saves.setCanChoosePistol(booker.getWeapon().isCanChoosePistol());
            saves.setCanChooseMachineGun(booker.getWeapon().isCanChooseMachineGun());
            saves.setCanChooseRPG(booker.getWeapon().isCanChooseRpg());
            saves.setCanChooseDevilKiss(booker.getEnergetic().canChooseDevilKiss());
            saves.setCanChooseElectricity(booker.getEnergetic().canChooseElectricity());
            saves.setCanChooseHypnosis(booker.getEnergetic().canChooseHypnosis());

            fileWriter.write(mapper.writeValueAsString(saves));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    static void saveOptions() {
        try (FileWriter fileWriter = new FileWriter(optionsPath.toFile())) {
            if (optionsPath.toFile().exists()) {
                optionsPath.toFile().delete();
            }

            if (!optionsPath.toFile().exists()) {
                optionsPath.toFile().createNewFile();
            }

            Options options = new Options();
            options.setFxVolume(menu.getFxSlider().getValue());
            options.setMusicVolume(menu.getMusicSlider().getValue());
            options.setVoiceVolume(menu.getVoiceSlider().getValue());
            options.setTrack(menu.getMusic().getMedia().getSource());

            fileWriter.write(mapper.writeValueAsString(options));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void playCutscene() {
        booker.setTranslateX(100);
        booker.setTranslateY(500);

        timer.stop();
        active = false;
        menu.getMusic().pause();
        clearData(false);
        appRoot.getChildren().add(videoView);

        FadeTransition ft = new FadeTransition(Duration.seconds(1), videoView);
        ft.setFromValue(0);
        ft.setToValue(1);
        ft.play();

        switch (levelNumber) {
            case Level.FIRST_LEVEL -> {
                video = new MediaPlayer(FIRST_CUTSCENE);
                video.setOnEndOfMedia(Game::initLevelAfterCutscene);
            }
            case Level.SECOND_LEVEL -> {
                video = new MediaPlayer(SECOND_CUTSCENE);
                video.setOnEndOfMedia(Game::initLevelAfterCutscene);
            }
            case Level.BOSS_LEVEL -> {
                video = new MediaPlayer(THIRD_CUTSCENE);
                video.setOnEndOfMedia(() -> System.exit(0));
            }
        }

        videoView.setMediaPlayer(video);
        videoView.getMediaPlayer().setVolume(menu.getVoiceSlider().getValue() / 100);
        video.play();
    }

    public static void initLevelAfterCutscene() {
        video.stop();
        booker.setCanPlayVoice(true);
        appRoot.getChildren().remove(videoView);

        Tutorial.delete();
        levelNumber++;
        level.changeLevel();
        booker.changeLevel();
        createEnemies();

        timer.start();
        active = true;
        menu.getMusic().play();
        Tutorial.init();
        elizabeth.init();
        vendingMachine.changeLevel();

        saveSaves();
        saveOptions();
    }

    public static void resetLevel() {
        gameRoot.setLayoutX(0);
        level.setBackgroundLayoutX(0);

        for (Enemy enemy : enemies) {
            enemy.reset();
            enemyPools.put(enemy);
        }
        enemies.clear();
        createEnemies();

        for (EnemyBullet enemyBullet : enemyBullets)
            enemyBulletsPool.put(enemyBullet);
        gameRoot.getChildren().removeAll(enemyBullets);
        enemyBullets.clear();

        booker.clear();

        for (Supply supply : supplies)
            supplyPool.put(supply);
        supplies.clear();
        keys.clear();
    }

    public static void clearData(boolean forBossLevel) {
        gameRoot.setLayoutX(0);
        level.setBackgroundLayoutX(0);

        Boss boss = null;
        for (Enemy enemy : enemies) {
            if (enemy.getType() != EnemyType.BOSS) {
                enemy.reset();
                enemyPools.put(enemy);
            } else
                boss = (Boss) enemy;
        }
        enemies.clear();

        if (forBossLevel)
            enemies.add(boss);

        for (EnemyBullet enemyBullet : enemyBullets)
            enemyBulletsPool.put(enemyBullet);
        gameRoot.getChildren().removeAll(enemyBullets);
        enemyBullets.clear();

        booker.clear();
        for (Supply supply : supplies)
            supplyPool.put(supply);
        supplies.clear();
        keys.clear();
    }

    public static void clearDataForNewGame() {
        gameRoot.setLayoutX(0);
        level.setBackgroundLayoutX(0);

        for (Enemy enemy : enemies) {
            enemy.reset();
            enemyPools.put(enemy);
        }
        enemies.clear();
        for (Supply supply : supplies)
            supplyPool.put(supply);
        supplies.clear();

        for (EnemyBullet enemyBullet : enemyBullets)
            enemyBulletsPool.put(enemyBullet);
        gameRoot.getChildren().removeAll(enemyBullets);
        enemyBullets.clear();

        if (levelNumber > Level.FIRST_LEVEL) {
            Path savesPath = Paths.get("resources", "data", "saves.dat");
            if (savesPath.toFile().exists())
                savesPath.toFile().delete();

            gameRoot.getChildren().remove(vendingMachine);
            vendingMachine = null;
        }

        keys.clear();
        Tutorial.delete();

        elizabeth.reset();
    }

    public static void createEnemies() {
        try {
            Map<String, EnemyData[]> jsonEnemies = mapper.readValue(
                    Paths.get("resources", "data", "enemies.dat").toFile(), new TypeReference<>() {});
            EnemyData[] enemiesByLevel = new EnemyData[]{};

            switch (levelNumber) {
                case Level.FIRST_LEVEL -> enemiesByLevel = jsonEnemies.get("firstLevel");
                case Level.SECOND_LEVEL -> enemiesByLevel = jsonEnemies.get("secondLevel");
                case Level.THIRD_LEVEL -> enemiesByLevel = jsonEnemies.get("thirdLevel");
                case Level.BOSS_LEVEL -> enemiesByLevel = jsonEnemies.get("bossLevel");
            }

            for (EnemyData enemy : enemiesByLevel) {
                switch (enemy.getType()) {
                    case "comstock" -> initEnemy(Comstock.class, enemy.getX(), enemy.getY());
                    case "red_eye" -> initEnemy(RedEye.class, enemy.getX(), enemy.getY());
                    case "camper" -> initEnemy(Camper.class, enemy.getX(), enemy.getY());
                    case "boss" -> initEnemy(Boss.class, enemy.getX(), enemy.getY());
                }
            }
        } catch (Exception e) {
            System.out.println(e.getLocalizedMessage());
        }
    }

    private static void initEnemy(Class<? extends Enemy> enemyClassType, int x, int y) {
        Enemy enemy = enemyPools.get(enemyClassType);
        enemy.init(x, y);
        enemies.add(enemy);
    }

    public static void setBossLevel() {
        booker.setTranslateX(100);
        booker.setTranslateY(500);
        elizabeth.setTranslateX(100);
        clearData(true);

        Tutorial.delete();
        levelNumber++;
        level.changeLevel();

        saveSaves();
        saveOptions();
    }

    private static void update() {
        Controller.update();

        if (!active)
            return;

        for (Enemy enemy : enemies) {
            enemy.update();
            if (enemy.isToDelete()) {
                enemy.reset();
                enemyPools.put(enemy);
                enemies.remove(enemy);
                break;
            }
        }
        for (EnemyBullet enemyBullet : enemyBullets) {
            enemyBullet.update();
            if (enemyBullet.isDelete()) {
                enemyBullets.remove(enemyBullet);
                enemyBulletsPool.put(enemyBullet);
                break;
            }
        }
        for (Supply supply : supplies) {
            supply.update();
            if (supply.isDelete()) {
                supplyPool.put(supply);
                supplies.remove(supply);
                break;
            }
        }

        booker.update();
        if (levelNumber > Level.FIRST_LEVEL)
            elizabeth.update();

        hud.update();

        if (booker.getTranslateX() > BLOCK_SIZE * 295)
            playCutscene();
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        scene = new Scene(appRoot, 1280, 720);
        stage = primaryStage;
        initContent();

        stage.getIcons().add(new Image(Paths.get("resources", "images", "icons", "icon.jpg").toUri().toString()));
        stage.setTitle("DeadShock");
        stage.setResizable(false);
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}