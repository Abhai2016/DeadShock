package com.abhai.deadshock.world;

import com.abhai.deadshock.Game;
import com.abhai.deadshock.characters.Animatable;
import com.abhai.deadshock.characters.Booker;
import com.abhai.deadshock.characters.Elizabeth;
import com.abhai.deadshock.characters.enemies.*;
import com.abhai.deadshock.dtos.EnemiesDTO;
import com.abhai.deadshock.dtos.EnemyDTO;
import com.abhai.deadshock.dtos.MenuOptionsDTO;
import com.abhai.deadshock.dtos.SavesDTO;
import com.abhai.deadshock.energetics.Energetic;
import com.abhai.deadshock.hud.HUD;
import com.abhai.deadshock.hud.Tutorial;
import com.abhai.deadshock.menus.Menu;
import com.abhai.deadshock.types.DifficultyType;
import com.abhai.deadshock.types.EnemyType;
import com.abhai.deadshock.types.SupplyType;
import com.abhai.deadshock.types.WeaponType;
import com.abhai.deadshock.utils.Controller;
import com.abhai.deadshock.utils.GameMedia;
import com.abhai.deadshock.utils.pools.ObjectPool;
import com.abhai.deadshock.utils.pools.ObjectPoolManager;
import com.abhai.deadshock.weapons.Weapon;
import com.abhai.deadshock.weapons.bullets.EnemyBullet;
import com.abhai.deadshock.world.levels.Level;
import javafx.animation.FadeTransition;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.util.Duration;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

import static com.abhai.deadshock.world.levels.Block.BLOCK_SIZE;

public class GameWorld {
    private MediaView videoView;
    private ArrayList<Enemy> enemies;
    private ArrayList<Supply> supplies;
    private ObjectPool<Supply> supplyPool;
    private ArrayList<EnemyBullet> enemyBullets;
    private ObjectPoolManager<Enemy> enemyPools;
    private ObjectPool<EnemyBullet> enemyBulletsPool;

    private HUD hud;
    private Menu menu;
    private Level level;
    private Booker booker;
    private MediaPlayer video;
    private Elizabeth elizabeth;
    private EnemiesDTO enemiesDTO;
    private VendingMachine vendingMachine;

    private boolean active;
    private DifficultyType difficultyType;

    public GameWorld() {

    }

    public HUD getHud() {
        return hud;
    }

    public Menu getMenu() {
        return menu;
    }

    public Level getLevel() {
        return level;
    }

    public Booker getBooker() {
        return booker;
    }

    public boolean isActive() {
        return active;
    }

    public ArrayList<Enemy> getEnemies() {
        return enemies;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public VendingMachine getVendingMachine() {
        return vendingMachine;
    }

    public DifficultyType getDifficultyType() {
        return difficultyType;
    }

    public ArrayList<EnemyBullet> getEnemyBullets() {
        return enemyBullets;
    }

    public ObjectPool<EnemyBullet> getEnemyBulletsPool() {
        return enemyBulletsPool;
    }

    public void setDifficultyType(DifficultyType difficultyType) {
        this.difficultyType = difficultyType;
    }

    public void deathPause() {
        booker.stopAnimation();
        active = false;
        menu.getMusic().pause();
        booker.clear();

        for (EnemyBullet enemyBullet : enemyBullets)
            enemyBulletsPool.put(enemyBullet);
        Game.getGameRoot().getChildren().removeAll(enemyBullets);
        enemyBullets.clear();

        for (Enemy enemy : enemies) {
            if (enemy instanceof Animatable animatable)
                animatable.stopAnimation();
            if (enemy instanceof Comstock comstock)
                comstock.setCanSeeBooker(false);
        }
    }

    public void resetLevel() {
        clearData(false);
        createEnemies();
    }

    private void initNewGame() {
        difficultyType = DifficultyType.MEDIUM;
        level = new Level(Level.FIRST_LEVEL);
        vendingMachine = new VendingMachine();
        hud = new HUD();
        booker = new Booker();
        booker.setWeapon(new Weapon.Builder());
        booker.setEnergetic(new Energetic.Builder());
        elizabeth = new Elizabeth();
    }

    public void newGameReset() {
        clearData(false);

        if (level.getCurrentLevelNumber() > Level.FIRST_LEVEL) {
            Path savesPath = Paths.get("resources", "data", "saves.dat");
            if (savesPath.toFile().exists())
                savesPath.toFile().delete();
        }

        level.setCurrentLevelNumber(Level.FIRST_LEVEL);
        level.changeLevel();
        createEnemies();
        booker.reset();
        Tutorial.reset();
        elizabeth.reset();
        vendingMachine.initializePosition();
    }

    public void playCutscene() {
        booker.setTranslateX(100);
        booker.setTranslateY(500);

        active = false;
        Game.timer.stop();
        menu.getMusic().pause();
        clearData(false);
        Game.getAppRoot().getChildren().add(videoView);

        FadeTransition ft = new FadeTransition(Duration.seconds(1), videoView);
        ft.setFromValue(0);
        ft.setToValue(1);
        ft.play();

        switch (level.getCurrentLevelNumber()) {
            case Level.FIRST_LEVEL -> {
                video = new MediaPlayer(GameMedia.FIRST_CUTSCENE);
                video.setOnEndOfMedia(this::initLevelAfterCutscene);
            }
            case Level.SECOND_LEVEL -> {
                video = new MediaPlayer(GameMedia.SECOND_CUTSCENE);
                video.setOnEndOfMedia(this::initLevelAfterCutscene);
            }
            case Level.BOSS_LEVEL -> {
                video = new MediaPlayer(GameMedia.THIRD_CUTSCENE);
                video.setOnEndOfMedia(() -> System.exit(0));
            }
        }

        videoView.setMediaPlayer(video);
        videoView.getMediaPlayer().setVolume(menu.getVoiceSlider().getValue() / 100);
        video.play();
    }

    public void setBossLevel() {
        booker.setTranslateX(100);
        booker.setTranslateY(500);
        clearData(true);
        elizabeth.setTranslateX(100);

        level.setCurrentLevelNumber(level.getCurrentLevelNumber() + 1);
        Tutorial.delete();
        level.changeLevel();
        booker.changeLevel();
        Game.getAppRoot().getChildren().remove(vendingMachine);

        Game.getSaveManager().saveProgress();
        Game.getSaveManager().saveMenuOptions();
    }

    public void deathUnpause() {
        active = true;
        menu.getMusic().play();

        Game.getGameRoot().setLayoutX(0);
        if (level.getCurrentLevelNumber() > Level.FIRST_LEVEL)
            elizabeth.resetAfterBookersDeath();
        level.setBackgroundLayoutX(0);
    }

    private void createEnemies() {
        ArrayList<EnemyDTO> enemiesByLevel = new ArrayList<>();

        switch (level.getCurrentLevelNumber()) {
            case Level.FIRST_LEVEL -> enemiesByLevel = enemiesDTO.getFirstLevel();
            case Level.SECOND_LEVEL -> enemiesByLevel = enemiesDTO.getSecondLevel();
            case Level.THIRD_LEVEL -> enemiesByLevel = enemiesDTO.getThirdLevel();
            case Level.BOSS_LEVEL -> enemiesByLevel = enemiesDTO.getBossLevel();
        }

        for (EnemyDTO enemy : enemiesByLevel)
            switch (enemy.getType()) {
                case "comstock" -> initEnemy(Comstock.class, enemy.getX(), enemy.getY());
                case "red_eye" -> initEnemy(RedEye.class, enemy.getX(), enemy.getY());
                case "camper" -> initEnemy(Camper.class, enemy.getX(), enemy.getY());
                case "boss" -> initEnemy(Boss.class, enemy.getX(), enemy.getY());
            }
    }

    public void setDifficultyLevel() {
        for (EnemyBullet enemyBullet : enemyBulletsPool.getAll())
            enemyBullet.setDifficultyLevel();
        hud.setDifficultyLevel();
        booker.setDifficultyLevel();
    }

    private void updateCacheableData() {
        for (Enemy enemy : enemies) {
            enemy.update();

            if (enemy instanceof Boss boss && level.getCurrentLevelNumber() == Level.THIRD_LEVEL && booker.getTranslateX() > BLOCK_SIZE * 285)
                boss.checkOnLevelChange();

            if (enemy.isToDelete()) {
                enemyPools.put(enemy);
                enemies.remove(enemy);
                Game.getGameRoot().getChildren().remove(enemy);
                break;
            }
        }

        for (EnemyBullet enemyBullet : enemyBullets) {
            enemyBullet.update();
            if (enemyBullet.isDelete()) {
                enemyBullets.remove(enemyBullet);
                enemyBulletsPool.put(enemyBullet);
                Game.getGameRoot().getChildren().remove(enemyBullet);
                break;
            }
        }

        for (Supply supply : supplies) {
            supply.update();
            if (supply.isDelete()) {
                supplyPool.put(supply);
                supplies.remove(supply);
                Game.getGameRoot().getChildren().remove(supply);
                break;
            }
        }
    }

    public SavesDTO generateSavesDTO() {
        SavesDTO savesDTO = new SavesDTO();
        savesDTO.setDifficultyType(difficultyType);
        savesDTO.setLevelNumber(level.getCurrentLevelNumber());
        savesDTO.setMoney(booker.getMoney());
        savesDTO.setSalt(booker.getSalt());

        switch (booker.getWeapon().getType()) {
            case WeaponType.PISTOL -> {
                savesDTO.setPistolBullets(booker.getWeapon().getCurrentBullets());
                savesDTO.setMachineGunBullets(booker.getWeapon().getMachineGunBullets());
                savesDTO.setRpgBullets(booker.getWeapon().getRpgBullets());
            }
            case WeaponType.MACHINE_GUN -> {
                savesDTO.setPistolBullets(booker.getWeapon().getPistolBullets());
                savesDTO.setMachineGunBullets(booker.getWeapon().getCurrentBullets());
                savesDTO.setRpgBullets(booker.getWeapon().getRpgBullets());
            }
            case WeaponType.RPG -> {
                savesDTO.setPistolBullets(booker.getWeapon().getPistolBullets());
                savesDTO.setMachineGunBullets(booker.getWeapon().getMachineGunBullets());
                savesDTO.setRpgBullets(booker.getWeapon().getCurrentBullets());
            }
        }

        savesDTO.setCanChoosePistol(booker.getWeapon().isCanChoosePistol());
        savesDTO.setCanChooseMachineGun(booker.getWeapon().isCanChooseMachineGun());
        savesDTO.setCanChooseRPG(booker.getWeapon().isCanChooseRpg());
        savesDTO.setCanChooseDevilKiss(booker.getEnergetic().canChooseDevilKiss());
        savesDTO.setCanChooseElectricity(booker.getEnergetic().canChooseElectricity());
        savesDTO.setCanChooseHypnosis(booker.getEnergetic().canChooseHypnosis());
        return savesDTO;
    }

    private void initLevelAfterCutscene() {
        video.stop();
        booker.setCanPlayVoice(true);
        Game.getAppRoot().getChildren().remove(videoView);

        level.setCurrentLevelNumber(level.getCurrentLevelNumber() + 1);
        createEnemies();
        level.changeLevel();
        booker.changeLevel();
        Tutorial.delete();

        active = true;
        Tutorial.init();
        elizabeth.init();
        Game.timer.start();
        menu.getMusic().play();
        vendingMachine.initializePosition();

        Game.getSaveManager().saveProgress();
        Game.getSaveManager().saveMenuOptions();
    }

    private void clearData(boolean forBossLevel) {
        Boss boss = null;
        for (Enemy enemy : enemies) {
            if (enemy.getType() != EnemyType.BOSS)
                enemyPools.put(enemy);
            else
                boss = (Boss) enemy;
        }
        Game.getGameRoot().getChildren().removeAll(enemies);
        enemies.clear();

        if (boss != null) {
            if (forBossLevel)
                enemies.add(boss);
            else
                boss.deleteHpUi();
        }

        for (Supply supply : supplies)
            supplyPool.put(supply);
        Game.getGameRoot().getChildren().removeAll(supplies);
        supplies.clear();

        for (EnemyBullet enemyBullet : enemyBullets)
            enemyBulletsPool.put(enemyBullet);
        Game.getGameRoot().getChildren().removeAll(enemyBullets);
        enemyBullets.clear();

        booker.clear();
        Game.getGameRoot().setLayoutX(0);
        Controller.keys.clear();
        level.setBackgroundLayoutX(0);
    }

    private void initSavedGame(SavesDTO savesDTO) {
        difficultyType = savesDTO.getDifficultyType();
        level = new Level(savesDTO.getLevelNumber());
        vendingMachine = new VendingMachine();
        hud = new HUD();

        Weapon.Builder weaponBuilder = new Weapon.Builder();
        weaponBuilder.setCanChoosePistol(savesDTO.isCanChoosePistol())
                .setCanChooseMachineGun(savesDTO.isCanChooseMachineGun())
                .setCanChooseRpg(savesDTO.isCanChooseRPG())
                .setPistolBullets(savesDTO.getPistolBullets())
                .setMachineGunBullets(savesDTO.getMachineGunBullets())
                .setRpgBullets(savesDTO.getRpgBullets());

        Energetic.Builder energeticBuilder = new Energetic.Builder();
        energeticBuilder.canChooseDevilKiss(savesDTO.isCanChooseDevilKiss())
                .canChooseElectricity(savesDTO.isCanChooseElectricity())
                .canChooseHypnosis(savesDTO.isCanChooseHypnosis());

        booker = new Booker();
        booker.setMoney(savesDTO.getMoney());
        booker.setSalt(savesDTO.getSalt());
        booker.setWeapon(weaponBuilder);
        booker.setEnergetic(energeticBuilder);

        elizabeth = new Elizabeth();
    }

    public MenuOptionsDTO generateMenuOptionsDTO() {
        MenuOptionsDTO menuOptionsDTO = new MenuOptionsDTO();
        menuOptionsDTO.setFxVolume(menu.getFxSlider().getValue());
        menuOptionsDTO.setMusicVolume(menu.getMusicSlider().getValue());
        menuOptionsDTO.setVoiceVolume(menu.getVoiceSlider().getValue());
        menuOptionsDTO.setTrack(menu.getMusic().getMedia().getSource());
        return menuOptionsDTO;
    }

    public void init(SavesDTO savesDTO, EnemiesDTO enemiesDTO) {
        video = new MediaPlayer(GameMedia.FIRST_CUTSCENE);
        videoView = new MediaView(video);
        this.enemiesDTO = enemiesDTO;

        active = false;
        enemies = new ArrayList<>();
        supplies = new ArrayList<>();
        enemyBullets = new ArrayList<>();
        enemyPools = new ObjectPoolManager<>();
        supplyPool = new ObjectPool<>(Supply::new, 10, 20);
        enemyBulletsPool = new ObjectPool<>(EnemyBullet::new, 50, 150);

        if (savesDTO == null)
            initNewGame();
        else
            initSavedGame(savesDTO);

        enemyPools.register(Boss.class, Boss::new, 1, 2);
        enemyPools.register(Camper.class, Camper::new, 5, 10);
        enemyPools.register(RedEye.class, RedEye::new, 10, 15);
        enemyPools.register(Comstock.class, Comstock::new, 15, 20);
        booker.translateXProperty().addListener(((observable, oldValue, newValue) -> {
            int offset = newValue.intValue();
            if (offset > 600 && offset < Game.getGameRoot().getWidth() - 680) {
                Game.getGameRoot().setLayoutX(-(offset - 600));
                level.setBackgroundLayoutX((offset - 600) / 1.5);
            }
        }));

        Tutorial.init();
        createEnemies();
        menu = new Menu();
        if (level.getCurrentLevelNumber() != Level.BOSS_LEVEL)
            vendingMachine.initializeButtons();
    }

    public void createSupply(SupplyType type, double x, double y) {
        Supply supply = supplyPool.get();
        supply.init(type, x, y);
        supplies.add(supply);
        Game.getGameRoot().getChildren().add(supply);
    }

    public void initializeMenuOptions(MenuOptionsDTO menuOptionsDTO) {
        menu.getFxSlider().setValue(menuOptionsDTO.getFxVolume());
        menu.checkMusicForContinueGame(menuOptionsDTO.getTrack());
        menu.getMusicSlider().setValue(menuOptionsDTO.getMusicVolume());
        menu.getVoiceSlider().setValue(menuOptionsDTO.getVoiceVolume());
    }

    private void initEnemy(Class<? extends Enemy> enemyClassType, int x, int y) {
        Enemy enemy = enemyPools.get(enemyClassType);
        enemy.init(x, y);
        enemies.add(enemy);
    }

    public void update() {
        Controller.update();

        if (active) {
            hud.update();
            booker.update();
            updateCacheableData();

            if (level.getCurrentLevelNumber() > Level.FIRST_LEVEL)
                elizabeth.update();

            if (booker.getTranslateX() > BLOCK_SIZE * 295)
                playCutscene();
        }
    }
}