package com.abhai.deadshock;

import com.abhai.deadshock.characters.Booker;
import com.abhai.deadshock.characters.Elizabeth;
import com.abhai.deadshock.characters.enemies.*;
import com.abhai.deadshock.energetics.Energetic;
import com.abhai.deadshock.hud.HUD;
import com.abhai.deadshock.hud.Tutorial;
import com.abhai.deadshock.levels.Level;
import com.abhai.deadshock.menus.Menu;
import com.abhai.deadshock.utils.Controller;
import com.abhai.deadshock.utils.Options;
import com.abhai.deadshock.utils.Saves;
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
    public static ArrayList<Enemy> enemies = new ArrayList<>();
    public static HashMap<KeyCode, Boolean> keys = new HashMap<>();

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
    public static String difficultyLevelText = "normal";

    public static Level level;

    //TODO connect frames to time
    public static AnimationTimer timer = new AnimationTimer() {
        @Override
        public void handle(long now) {
            update();
        }
    };

    public static void initContent() {
        appRoot.getChildren().add(gameRoot);

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
                weapon = new Weapon();
                hud = new HUD();
                energetic = new Energetic.Builder().build();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

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

    //TODO add cache for enemies
    public static void initContentForNewGame() {
        levelNumber = Level.FIRST_LEVEL;
        level.changeLevel();
        vendingMachine = new VendingMachine();
        weapon = new Weapon();

        booker.reset();
        energetic.reset();
        weapon.setDamage();
        createEnemies();
        Tutorial.init();
        if (levelNumber != Level.BOSS_LEVEL)
            vendingMachine.createButtons();
    }

    static void loadSaves(ObjectMapper mapper, Path savesPath) throws IOException {
        Saves saves = mapper.readValue(savesPath.toFile(), Saves.class);
        difficultyLevelText = saves.getDifficultyLevel();
        levelNumber = saves.getLevelNumber();

        level = new Level();
        if (levelNumber != Level.BOSS_LEVEL)
            vendingMachine = new VendingMachine();
        hud = new HUD();

        booker = new Booker();
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
                enemies.add(new Boss());
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
        elizabeth = new Elizabeth();
    }

    static void loadOptions() {
        try {
            ObjectMapper mapper = new ObjectMapper();
            Path optionsPath = Paths.get("resources", "data", "options.dat");

            if (menu == null)
                menu = new Menu();

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

    static void saveSaves(ObjectMapper mapper) {
        try (FileWriter fileWriter = new FileWriter(savesPath.toFile())) {
            if (savesPath.toFile().exists()) {
                savesPath.toFile().delete();
            }

            if (!savesPath.toFile().exists()) {
                savesPath.toFile().createNewFile();
            }

            Saves saves = new Saves();
            saves.setDifficultyLevel(difficultyLevelText);
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

    static void saveOptions(ObjectMapper mapper) {
        try (FileWriter fileWriter = new FileWriter(optionsPath.toFile())) {
            if (optionsPath.toFile().exists()) {
                optionsPath.toFile().delete();
            }

            if (!optionsPath.toFile().exists()) {
                optionsPath.toFile().createNewFile();
            }

            Options options = new Options();
            options.setFxVolume(menu.fxSlider.getValue());
            options.setMusicVolume(menu.musicSlider.getValue());
            options.setVoiceVolume(menu.voiceSlider.getValue());
            options.setTrack(menu.music.getMedia().getSource());

            fileWriter.write(mapper.writeValueAsString(options));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void resetLevel() {
        gameRoot.setLayoutX(0);
        if (levelNumber != Level.BOSS_LEVEL) {
            gameRoot.getChildren().removeAll(enemies);
            enemies.clear();
            createEnemies();
        } else if (enemies.getFirst() instanceof Boss boss)
            boss.reset();

        level.setBackgroundLayoutX(0);
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

        if (forBossLevel) {
            for (Enemy enemy : enemies)
                if (enemy.getType() != EnemyType.BOSS) {
                    gameRoot.getChildren().remove(enemy);
                    enemies.remove(enemy);
                }
        } else {
            gameRoot.getChildren().removeAll(enemies);
            enemies.clear();
        }

        for (EnemyBullet enemyBullet : enemyBullets)
            gameRoot.getChildren().remove(enemyBullet);
        enemyBullets.clear();

        for (Bullet bullet : bullets)
            gameRoot.getChildren().remove(bullet);
        bullets.clear();

        keys.clear();
    }

    public static void clearDataForNewGame() {
        gameRoot.setLayoutX(0);
        level.setBackgroundLayoutX(0);
        gameRoot.getChildren().removeAll(enemies);
        enemies.clear();

        for (EnemyBullet enemyBullet : enemyBullets)
            gameRoot.getChildren().remove(enemyBullet);
        enemyBullets.clear();

        for (Bullet bullet : bullets)
            gameRoot.getChildren().remove(bullet);
        bullets.clear();

        if (levelNumber > Level.FIRST_LEVEL) {
            gameRoot.getChildren().remove(elizabeth);
            elizabeth = null;

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

        if (menu != null)
            appRoot.getChildren().remove(menu.menuBox);
    }

    public static void createEnemies() {
        if (levelNumber == Level.BOSS_LEVEL)
            enemies.add(new Boss());
        else {
            try {
                ObjectMapper mapper = new ObjectMapper();
                Path enemiesPath = Paths.get("resources", "data", "enemies.dat");
                Map<String, EnemyData[]> jsonEnemies = mapper.readValue(enemiesPath.toFile(), new TypeReference<>() {
                });
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
                System.out.println(e.getLocalizedMessage());
            }
        }
    }

    public static void nothing() {
    }

    public static void setBossLevel() {
        booker.setTranslateX(100);
        booker.setTranslateY(500);
        if (elizabeth != null)
            elizabeth.setTranslateX(100);
        gameRoot.getChildren().remove(weapon);
        gameRoot.getChildren().remove(energetic);
        clearData(true);

        Tutorial.delete();
        levelNumber++;
        level.changeLevel();

        ObjectMapper mapper = new ObjectMapper();
        saveSaves(mapper);
        saveOptions(mapper);
    }

    private static void update() {
        for (Enemy enemy : enemies) {
            enemy.update();
            if (enemy.isToDelete()) {
                enemies.remove(enemy);
                gameRoot.getChildren().remove(enemy);
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

        Controller.update();
        booker.update();

        if (energetic.getCountEnergetics() > 0)
            energetic.update();
        if (levelNumber > Level.FIRST_LEVEL)
            elizabeth.update();

        menu.update();
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