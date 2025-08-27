package com.abhai.deadshock;

import com.abhai.deadshock.characters.Booker;
import com.abhai.deadshock.characters.Elizabeth;
import com.abhai.deadshock.characters.enemies.*;
import com.abhai.deadshock.energetics.Energetic;
import com.abhai.deadshock.hud.HUD;
import com.abhai.deadshock.hud.Tutorial;
import com.abhai.deadshock.levels.Level;
import com.abhai.deadshock.menus.Menu;
import com.abhai.deadshock.supplies.Supply;
import com.abhai.deadshock.utils.Controller;
import com.abhai.deadshock.utils.Options;
import com.abhai.deadshock.utils.Saves;
import com.abhai.deadshock.utils.pools.ObjectPoolManager;
import com.abhai.deadshock.weapons.Weapon;
import com.abhai.deadshock.weapons.bullets.Bullet;
import com.abhai.deadshock.weapons.bullets.EnemyBullet;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.abhai.deadshock.levels.Block.BLOCK_SIZE;


public class Game extends Application {
    private static Path savesPath = Paths.get("resources", "data", "saves.dat");
    private static Path optionsPath = Paths.get("resources", "data", "options.dat");

    public static ArrayList<Supply> supplies = new ArrayList<>();
    public static ArrayList<Bullet> bullets = new ArrayList<>();
    public static ArrayList<EnemyBullet> enemyBullets = new ArrayList<>();
    private final static ObjectPoolManager<Enemy> enemyPools = new ObjectPoolManager<>();
    public static ArrayList<Enemy> enemies = new ArrayList<>();
    public static HashMap<KeyCode, Boolean> keys = new HashMap<>();
    public static ObjectMapper mapper = new ObjectMapper();

    public static Stage stage;
    public static Scene scene;

    public static Pane gameRoot = new Pane();
    public static Pane appRoot = new Pane();

    public static Menu menu;
    public static Booker booker;
    public static HUD hud;
    public static Weapon weapon;
    public static Elizabeth elizabeth;
    public static VendingMachine vendingMachine;

    public static CutScenes cutScene;
    public static Energetic energetic;

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
                weapon = new Weapon();
                hud = new HUD();
                energetic = new Energetic.Builder().build();
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
    //TODO fix a bug with a weapon
    public static void initContentForNewGame() {
        levelNumber = Level.FIRST_LEVEL;
        level.changeLevel();
        vendingMachine = new VendingMachine();
        weapon = new Weapon();
        weapon.setDifficultyLevel();
        createEnemies();
        Tutorial.init();

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

        booker = new Booker();
        elizabeth = new Elizabeth();
        elizabeth.init();
        booker.setMoney(saves.getMoney());
        booker.setSalt(saves.getSalt());
        Energetic.Builder builder = new Energetic.Builder();
        builder.canChooseDevilKiss(saves.isCanChooseDevilKiss())
                .canChooseElectricity(saves.isCanChooseElectricity())
                .canChooseHypnosis(saves.isCanChooseHypnosis());
        energetic = builder.build();

        switch (levelNumber) {
            case Level.SECOND_LEVEL -> {
                weapon = new Weapon(saves.isCanChoosePistol());
                weapon.setWeaponClip(saves.getPistolClip());
                weapon.setBullets(saves.getPistolBullets());
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
            }
        }
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

            saves.setPistolClip(Weapon.WeaponData.pistolClip);
            saves.setPistolBullets(Weapon.WeaponData.pistolBullets);
            saves.setMachineGunClip(Weapon.WeaponData.machineGunClip);
            saves.setMachineGunBullets(Weapon.WeaponData.machineGunBullets);
            saves.setRpgClip(Weapon.WeaponData.rpgClip);
            saves.setRpgBullets(Weapon.WeaponData.rpgBullets);

            saves.setCanChoosePistol(weapon.isCanChoosePistol());
            saves.setCanChooseMachineGun(weapon.isCanChooseMachineGun());
            saves.setCanChooseRPG(weapon.isCanChooseRPG());
            saves.setCanChooseDevilKiss(energetic.canChooseDevilKiss());
            saves.setCanChooseElectricity(energetic.canChooseElectricity());
            saves.setCanChooseHypnosis(energetic.canChooseHypnosis());

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
            gameRoot.getChildren().remove(enemyBullet);
        enemyBullets.clear();

        for (Bullet bullet : bullets)
            gameRoot.getChildren().remove(bullet);
        bullets.clear();

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

        if (forBossLevel) {
            enemies.add(boss);
        } else {
            gameRoot.getChildren().remove(booker);
            gameRoot.getChildren().remove(elizabeth);
        }

        for (EnemyBullet enemyBullet : enemyBullets)
            gameRoot.getChildren().remove(enemyBullet);
        enemyBullets.clear();

        for (Bullet bullet : bullets)
            gameRoot.getChildren().remove(bullet);
        bullets.clear();

        gameRoot.getChildren().remove(weapon);
        gameRoot.getChildren().remove(energetic);

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

        for (EnemyBullet enemyBullet : enemyBullets)
            gameRoot.getChildren().remove(enemyBullet);
        enemyBullets.clear();

        for (Bullet bullet : bullets)
            gameRoot.getChildren().remove(bullet);
        bullets.clear();

        if (levelNumber > Level.FIRST_LEVEL) {
            Path savesPath = Paths.get("resources", "data", "saves.dat");
            if (savesPath.toFile().exists())
                savesPath.toFile().delete();

            gameRoot.getChildren().remove(vendingMachine);
            vendingMachine = null;
        }

        keys.clear();

        if (weapon != null) {
            gameRoot.getChildren().remove(weapon);
            weapon = null;
        }

        Tutorial.delete();

        booker.reset();
        energetic.reset();
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

        booker.update();

        if (energetic.getCountEnergetics() > 0)
            energetic.update();
        if (levelNumber > Level.FIRST_LEVEL)
            elizabeth.update();

        hud.update();
        weapon.update();

        if (booker.getTranslateX() > BLOCK_SIZE * 295)
            cutScene = new CutScenes();
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