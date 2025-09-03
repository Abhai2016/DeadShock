package com.abhai.deadshock.characters;

import com.abhai.deadshock.menus.DifficultyLevel;
import com.abhai.deadshock.Game;
import com.abhai.deadshock.characters.enemies.Boss;
import com.abhai.deadshock.characters.enemies.Comstock;
import com.abhai.deadshock.characters.enemies.Enemy;
import com.abhai.deadshock.characters.enemies.EnemyType;
import com.abhai.deadshock.energetics.Energetic;
import com.abhai.deadshock.world.levels.Block;
import com.abhai.deadshock.world.levels.BlockType;
import com.abhai.deadshock.world.levels.Level;
import com.abhai.deadshock.utils.Sounds;
import com.abhai.deadshock.utils.SpriteAnimation;
import com.abhai.deadshock.utils.Texts;
import com.abhai.deadshock.weapons.Weapon;
import com.abhai.deadshock.weapons.WeaponType;
import com.abhai.deadshock.weapons.bullets.EnemyBullet;
import javafx.event.EventHandler;
import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.util.Duration;

import java.nio.file.Paths;

import static javafx.scene.input.KeyEvent.KEY_PRESSED;

public class Booker extends Character implements Animatable {
    public static final int WIDTH = 60;

    private static final int GRAVITY = 8;
    private static final int JUMP_SPEED = -23;
    private static final int SPRITES_COUNT = 6;
    private static final int RPG_OFFSET_Y = 215;
    private static final int NO_GUN_OFFSET_Y = 70;
    private static final int PISTOL_OFFSET_Y = 282;
    private static final int IDLE_RPG_OFFSET_X = 246;
    private static final double ANIMATION_SPEED = 0.5;
    private static final int MACHINE_GUN_OFFSET_Y = 144;
    private static final int IDLE_PISTOL_OFFSET_X = 125;
    private static final int IDLE_MACHINE_OFFSET_X = 185;
    private static final int IDLE_WITH_NO_GUN_OFFSET_X = 42;

    private boolean dead;
    private boolean start;
    private boolean canJump;
    private boolean hypnotized;
    private boolean canPlayVoice;
    private boolean booleanVelocityX;
    private boolean booleanVelocityY;

    private int HP;
    private int salt;
    private int money;
    private int livesCount;
    private int closeCombatDamage;
    private int priceForGeneration;
    private int moneyForKillingEnemy;
    private int bulletsForKillingEnemy;
    private int medicineForKillingEnemy;

    private Weapon weapon;
    private Point2D velocity;
    private Energetic energetic;
    private SpriteAnimation withRPG;
    private SpriteAnimation animation;
    private SpriteAnimation withoutGun;
    private SpriteAnimation withPistol;
    private SpriteAnimation withMachineGun;

    private Text moneyText;
    private Text gameOverText;
    private Text continueText;

    public Booker() {
        dead = false;
        start = true;
        canJump = true;
        hypnotized = false;
        canPlayVoice = true;
        booleanVelocityX = true;
        booleanVelocityY = true;
        velocity = new Point2D(0, 0);
        imageView.setViewport(new Rectangle2D(0, 0, WIDTH, HEIGHT));

        HP = 100;
        money = 0;
        salt = 100;
        livesCount = 0;
        closeCombatDamage = 0;
        priceForGeneration = 0;
        moneyForKillingEnemy = 0;
        bulletsForKillingEnemy = 0;
        medicineForKillingEnemy = 0;

        initDeathText();
        initAnimations();
        Game.gameRoot.getChildren().add(this);
    }

    @Override
    public void stopAnimation() {
        animation.stop();
    }

    @Override
    protected String getImageName() {
        return "booker.png";
    }

    public int getHP() {
        return HP;
    }

    public void setHP(int value) {
        HP = value;
        if (HP > 100)
            HP = 100;
    }

    public void setSalt(int value) {
        salt = value;
        if (salt > 100)
            salt = 100;
    }

    public int getSalt() {
        return salt;
    }

    public void setMoney(int value) {
        money = value;
        Game.hud.updateMoneyTextPosition();
    }

    public int getMoney() {
        return money;
    }

    public void addMoneyForKillingEnemy() {
        money += moneyForKillingEnemy;
        Game.hud.updateMoneyTextPosition();
    }

    public Weapon getWeapon() {
        return weapon;
    }

    public void addMedicineForKillingEnemy() {
        setHP(HP + medicineForKillingEnemy);

        switch ((int) (Math.random() * 2)) {
            case 0 -> Sounds.feelsBetter.play(Game.menu.getVoiceSlider().getValue() / 100);
            case 1 -> Sounds.feelingBetter.play(Game.menu.getVoiceSlider().getValue() / 100);
        }

    }

    public boolean isHypnotized() {
        return hypnotized;
    }

    public Energetic getEnergetic() {
        return energetic;
    }

    public void setCanPlayVoice(boolean value) {
        canPlayVoice = value;
    }

    public void setWeapon(Weapon.Builder builder) {
        weapon = builder.build();
    }

    public void minusSaltForUsingEnergetic(int saltPrice) {
        salt -= saltPrice;
    }

    public void setEnergetic(Energetic.Builder builder) {
        energetic = builder.build();
    }

    private void die() {
        dead = true;
        if (Game.difficultyLevel.equals(DifficultyLevel.HARDCORE))
            gameOver();
        else {
            stopAnimation();
            Game.active = false;
            Game.menu.getMusic().pause();
            energetic.clear();

            for (EnemyBullet enemyBullet : Game.enemyBullets)
                Game.enemyBulletsPool.put(enemyBullet);
            Game.gameRoot.getChildren().removeAll(Game.enemyBullets);
            Game.enemyBullets.clear();
            weapon.clearBullets();

            for (Enemy enemy : Game.enemies) {
                if (enemy instanceof Animatable animatable)
                    animatable.stopAnimation();
                if (enemy instanceof Comstock comstock)
                    comstock.setCanSeeBooker(false);
            }

            if (money < priceForGeneration)
                gameOver();
            else {
                livesCount--;
                money -= priceForGeneration;
                Game.hud.updateMoneyTextPosition();

                if (Game.levelNumber > Level.FIRST_LEVEL)
                    playVideoDeath();
                else {
                    Game.appRoot.getChildren().add(gameOverText);

                    if (start)
                        Game.appRoot.getChildren().add(moneyText);

                    if (livesCount < 0) {
                        continueText.setText(Texts.PUSH_ENTER_TO_START_LEVEL_AGAIN);
                        continueText.setTranslateX(Game.scene.getWidth() / 4);
                    } else {
                        continueText.setText(Texts.PUSH_ENTER_TO_CONTINUE);
                        continueText.setTranslateX(Game.scene.getWidth() / 3);
                    }
                    Game.appRoot.getChildren().add(continueText);
                    addEventListenerOnDeathText();
                }
            }
        }
    }

    public void clear() {
        energetic.clear();
        weapon.clearBullets();
    }

    public void reset() {
        dead = false;
        start = true;
        canJump = true;
        weapon.reset();
        energetic.reset();
        hypnotized = false;
        canPlayVoice = true;
        animation = withoutGun;
        booleanVelocityX = true;
        booleanVelocityY = true;
        velocity = new Point2D(0, 0);
        imageView.setViewport(new Rectangle2D(0, 0, WIDTH, HEIGHT));

        if (Sounds.whereAreYouFrom.getStatus() == MediaPlayer.Status.PLAYING)
            Sounds.whereAreYouFrom.stop();

        HP = 100;
        salt = 100;
        setTranslateX(100);
        setTranslateY(200);
    }

    private void behave() {
        weapon.update();
        energetic.update();
        moveY(velocity.getY());
        moveX(velocity.getX());

        if (!hypnotized) {
            if (!canJump)
                stopAnimation();
            else
                animation.play();

            if (Game.elizabeth.isGiveSupply() && getBoundsInParent().intersects(Game.elizabeth.getBoundsInParent())) {
                addMedicineForKillingEnemy();
                Game.elizabeth.giveMedicine();
            }

            if (Game.levelNumber < Level.THIRD_LEVEL)
                playVoice();
        }
    }

    public void takeAmmo() {
        if (weapon.getType() != WeaponType.RPG)
            weapon.setCurrentBullets(weapon.getCurrentBullets() + bulletsForKillingEnemy);
        else
            weapon.setCurrentBullets(weapon.getCurrentBullets() + bulletsForKillingEnemy / 5);
    }

    public void hypnotize() {
        hypnotized = true;
        velocity = velocity.add(0, JUMP_SPEED);
    }

    private void gameOver() {
        gameOverText.setFont(Font.font("Arial", FontWeight.BOLD, 28));
        gameOverText.setFill(Color.RED);
        gameOverText.setTranslateX(Game.scene.getWidth() / 2 - 100);
        gameOverText.setTranslateY(Game.scene.getHeight() / 2);
        Game.appRoot.getChildren().add(gameOverText);

        Game.scene.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ESCAPE) {
                Game.appRoot.getChildren().remove(gameOverText);
                Game.menu.gameOver();
            }
        });
    }

    private void playVoice() {
        if (!Game.enemies.isEmpty() && Game.levelNumber == Level.FIRST_LEVEL)
            if (getTranslateX() > Game.enemies.getFirst().getTranslateX() - Block.BLOCK_SIZE * 15 && canPlayVoice) {
                Sounds.shit.play(Game.menu.getVoiceSlider().getValue() / 100);
                canPlayVoice = false;
                return;
            }

        if (Game.enemies.isEmpty() && !canPlayVoice && Game.levelNumber == Level.FIRST_LEVEL) {
            Sounds.cretins.play(Game.menu.getVoiceSlider().getValue() / 100);
            canPlayVoice = true;
            return;
        }

        if (Game.enemies.isEmpty() && Game.levelNumber == Level.SECOND_LEVEL && canPlayVoice) {
            Sounds.letsGo.play(Game.menu.getVoiceSlider().getValue() / 100);
            canPlayVoice = false;
        }
    }

    private void deathReset() {
        setTranslateX(100);
        setTranslateY(200);

        HP = 100;
        dead = false;
        Game.gameRoot.setLayoutX(0);
        if (Game.levelNumber > Level.FIRST_LEVEL)
            Game.elizabeth.resetAfterBookersDeath();
        Game.level.setBackgroundLayoutX(0);
        velocity = new Point2D(0, 0);

        if (livesCount < 0) {
            salt = 100;
            switch (Game.difficultyLevel) {
                case DifficultyLevel.MARIK, DifficultyLevel.EASY -> livesCount = 4;
                case DifficultyLevel.MEDIUM -> livesCount = 2;
                case DifficultyLevel.HARD -> livesCount = 1;
                case DifficultyLevel.HARDCORE -> livesCount = 0;
            }
            Game.resetLevel();
            canPlayVoice = true;
        }
    }

    public void unhypnotize() {
        hypnotized = false;
        velocity = velocity.add(0, JUMP_SPEED);
    }

    public void changeLevel() {
        weapon.changeLevel();
        energetic.changeLevel();
    }

    public void moveX(double x) {
        if (velocity.getX() < 0)
            velocity = velocity.add(ANIMATION_SPEED, 0);
        else if (velocity.getX() > 0)
            velocity = velocity.add(-ANIMATION_SPEED, 0);
        else
            booleanVelocityX = true;

        for (int i = 0; i < Math.abs(x); i++) {
            if (x > 0 && Game.booker.getTranslateX() < Game.gameRoot.getWidth() - WIDTH)
                setTranslateX(getTranslateX() + 1);
            else if (Game.booker.getTranslateX() > 1)
                setTranslateX(getTranslateX() - 1);

            if (intersectsWithBlocks('X', x))
                return;

            if (intersectsWithEnemies('X'))
                return;
        }
    }

    private void initDeathText() {
        gameOverText = new Text(Texts.YOU_ARE_DEAD);
        gameOverText.setFont(Font.font("Arial", FontWeight.BOLD, 28));
        gameOverText.setFill(Color.RED);
        gameOverText.setTranslateX(Game.scene.getWidth() / 2 - 75);
        gameOverText.setTranslateY(Game.scene.getHeight() / 2);

        continueText = new Text(Texts.PUSH_ENTER_TO_CONTINUE);
        continueText.setFont(Font.font("Arial", FontWeight.BOLD, 28));
        continueText.setFill(Color.WHITE);
        continueText.setTranslateX(Game.scene.getWidth() / 3);
        continueText.setTranslateY(Game.scene.getHeight() / 2 + 40);


        moneyText = new Text(Texts.WITH_EACH_DEATH_YOU_LOSE + priceForGeneration + Texts.NO_MONEY_NO_GAME);
        moneyText.setFont(Font.font("Arial", FontWeight.BOLD, 28));
        moneyText.setFill(Color.RED);
        moneyText.setTranslateX(Game.scene.getWidth() / 4);
        moneyText.setTranslateY(Game.scene.getHeight() / 1.5 - 30);
    }

    private void moveY(double y) {
        if (velocity.getY() < GRAVITY)
            velocity = velocity.add(0, ANIMATION_SPEED);
        else
            booleanVelocityY = true;

        for (int i = 0; i < Math.abs(y); i++) {
            if (y > 0) {
                setTranslateY(getTranslateY() + 1);
                canJump = false;
            } else
                setTranslateY(getTranslateY() - 1);

            if (intersectsWithBlocks('Y', y))
                return;

            if (intersectsWithEnemies('Y'))
                return;
        }
    }

    private void initAnimations() {
        withoutGun = new SpriteAnimation(imageView, Duration.seconds(ANIMATION_SPEED),
                SPRITES_COUNT, SPRITES_COUNT, 0, NO_GUN_OFFSET_Y, WIDTH, HEIGHT);
        withPistol = new SpriteAnimation(imageView, Duration.seconds(ANIMATION_SPEED),
                SPRITES_COUNT, SPRITES_COUNT, 0, PISTOL_OFFSET_Y, WIDTH, HEIGHT);
        withMachineGun = new SpriteAnimation(imageView, Duration.seconds(ANIMATION_SPEED),
                SPRITES_COUNT, SPRITES_COUNT, 0, MACHINE_GUN_OFFSET_Y, WIDTH, HEIGHT);
        withRPG = new SpriteAnimation(imageView, Duration.seconds(ANIMATION_SPEED),
                SPRITES_COUNT, SPRITES_COUNT, 0, RPG_OFFSET_Y, WIDTH, HEIGHT);
        animation = withoutGun;
    }

    private void playVideoDeath() {
        if (Sounds.whereAreYouFrom.getStatus() == MediaPlayer.Status.PLAYING)
            Sounds.whereAreYouFrom.pause();

        if (Game.levelNumber == Level.BOSS_LEVEL && Game.enemies.getFirst() instanceof Boss boss)
            boss.unStun();

        MediaPlayer videoDeath = new MediaPlayer(new Media(
                Paths.get("resources", "videos", "death.mp4").toUri().toString()));
        MediaView videoView = new MediaView(videoDeath);
        Game.appRoot.getChildren().add(videoView);
        videoView.getMediaPlayer().setVolume(Game.menu.getVoiceSlider().getValue() / 100);

        videoDeath.play();
        videoDeath.setOnEndOfMedia(() -> {
            Game.active = true;
            Game.menu.getMusic().play();
            Game.appRoot.getChildren().remove(videoView);

            if (Sounds.whereAreYouFrom.getStatus() == MediaPlayer.Status.PAUSED)
                Sounds.whereAreYouFrom.play();

            deathReset();
        });
    }

    public void setIdleAnimation() {
        switch (weapon.getType()) {
            case WeaponType.PISTOL -> imageView.setViewport(new Rectangle2D(IDLE_PISTOL_OFFSET_X, 0, WIDTH, HEIGHT));
            case WeaponType.MACHINE_GUN ->
                    imageView.setViewport(new Rectangle2D(IDLE_MACHINE_OFFSET_X, 0, WIDTH, HEIGHT));
            case WeaponType.RPG -> imageView.setViewport(new Rectangle2D(IDLE_RPG_OFFSET_X, 0, WIDTH, HEIGHT));
            default -> imageView.setViewport(new Rectangle2D(IDLE_WITH_NO_GUN_OFFSET_X, 0, WIDTH, HEIGHT));
        }
        stopAnimation();
    }

    public void setDifficultyLevel() {
        int money = 0;
        switch (Game.difficultyLevel) {
            case DifficultyLevel.MARIK -> {
                money = 1000000;
                livesCount = 4;
                priceForGeneration = 0;
                closeCombatDamage = 75;
                moneyForKillingEnemy = 0;
                bulletsForKillingEnemy = 30;
                medicineForKillingEnemy = 30;
            }
            case DifficultyLevel.EASY -> {
                money = 300;
                livesCount = 4;
                closeCombatDamage = 50;
                priceForGeneration = 15;
                moneyForKillingEnemy = 10;
                bulletsForKillingEnemy = 25;
                medicineForKillingEnemy = 25;
            }
            case DifficultyLevel.MEDIUM -> {
                money = 150;
                livesCount = 2;
                closeCombatDamage = 40;
                priceForGeneration = 20;
                moneyForKillingEnemy = 5;
                bulletsForKillingEnemy = 20;
                medicineForKillingEnemy = 20;
            }
            case DifficultyLevel.HARD -> {
                money = 100;
                livesCount = 1;
                closeCombatDamage = 30;
                priceForGeneration = 25;
                moneyForKillingEnemy = 3;
                bulletsForKillingEnemy = 10;
                medicineForKillingEnemy = 10;
            }
            case DifficultyLevel.HARDCORE -> {
                money = 100;
                livesCount = 0;
                closeCombatDamage = 25;
                priceForGeneration = 0;
                moneyForKillingEnemy = 2;
                bulletsForKillingEnemy = 10;
                medicineForKillingEnemy = 10;
            }
        }

        if (Game.levelNumber == Level.FIRST_LEVEL)
            this.money = money;

        energetic.setDifficultyLevel();
        Game.hud.updateMoneyTextPosition();
    }

    public void jump(boolean enemyJump) {
        if (enemyJump) {
            if (booleanVelocityY) {
                booleanVelocityY = false;
                velocity = velocity.add(0, JUMP_SPEED);
            }
        } else if (canJump) {
            canJump = false;
            velocity = velocity.add(0, JUMP_SPEED);
        }
    }

    public void closeCombat(double scaleX) {
        HP -= closeCombatDamage / 10;

        if (booleanVelocityX) {
            booleanVelocityX = false;
            velocity = velocity.add(scaleX * -JUMP_SPEED, 0);
            Sounds.closeCombat.play(Game.menu.getFxSlider().getValue() / 100);
        }
    }

    private void addEventListenerOnDeathText() {
        EventHandler<KeyEvent> removeDeathText = new EventHandler<>() {
            @Override
            public void handle(KeyEvent event) {
                if (event.getCode() == KeyCode.ENTER) {
                    Game.active = true;
                    Game.menu.getMusic().play();
                    Game.appRoot.getChildren().removeAll(gameOverText, continueText);

                    if (start) {
                        Game.appRoot.getChildren().remove(moneyText);
                        start = false;
                    }

                    deathReset();
                    Game.scene.removeEventFilter(KEY_PRESSED, this);
                }
            }
        };
        Game.scene.addEventFilter(KEY_PRESSED, removeDeathText);
    }

    public void changeWeaponAnimation(WeaponType weaponType) {
        stopAnimation();
        switch (weaponType) {
            case WeaponType.PISTOL -> animation = withPistol;
            case WeaponType.MACHINE_GUN -> animation = withMachineGun;
            case WeaponType.RPG -> animation = withRPG;
            default -> animation = withoutGun;
        }
        animation.play();
    }

    private boolean intersectsWithEnemies(char typeOfCoordinate) {
        for (Enemy enemy : Game.enemies)
            if (getBoundsInParent().intersects(enemy.getBoundsInParent())) {
                if (typeOfCoordinate == 'Y') {
                    if ((enemy.getType() == EnemyType.BOSS && enemy.getHP() < 1) || enemy.isHypnotized())
                        canJump = true;
                    else {
                        jump(true);
                        if (enemy.getType() == EnemyType.BOSS)
                            HP -= closeCombatDamage / 10;
                        enemy.setHP(enemy.getHP() - closeCombatDamage);
                        Sounds.closeCombat.play(Game.menu.getFxSlider().getValue() / 100);
                    }
                    setTranslateY(getTranslateY() - 1);
                } else
                    setTranslateX(getTranslateX() - getScaleX());
                return true;
            }
        return false;
    }

    private boolean intersectsWithBlocks(char typeOfCoordinate, double coordinate) {
        if (hypnotized) {
            for (Block block : Game.level.getBlocks())
                if (block.getType().equals(BlockType.METAL) && getBoundsInParent().intersects(block.getBoundsInParent())) {
                    setTranslateY(getTranslateY() - 1);
                    return true;
                }
        } else {
            for (Block block : Game.level.getBlocks())
                if (!block.getType().equals(BlockType.INVISIBLE) && getBoundsInParent().intersects(block.getBoundsInParent())) {
                    if (typeOfCoordinate == 'X')
                        if (coordinate > 0)
                            setTranslateX(getTranslateX() - 1);
                        else
                            setTranslateX(getTranslateX() + 1);
                    else if (coordinate > 0) {
                        setTranslateY(getTranslateY() - 1);
                        canJump = true;
                    } else {
                        setTranslateY(getTranslateY() + 1);
                        velocity = new Point2D(0, 8);
                    }
                    return true;
                }
        }
        return false;
    }

    public void update() {
        if ((HP < 1 || getTranslateY() > Game.scene.getHeight()) && !dead) {
            die();
        } else
            behave();
    }
}