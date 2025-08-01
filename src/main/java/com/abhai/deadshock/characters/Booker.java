package com.abhai.deadshock.characters;

import com.abhai.deadshock.Game;
import com.abhai.deadshock.Supply;
import com.abhai.deadshock.characters.enemies.Enemy;
import com.abhai.deadshock.levels.Block;
import com.abhai.deadshock.levels.BlockType;
import com.abhai.deadshock.levels.Level;
import com.abhai.deadshock.utils.Sounds;
import com.abhai.deadshock.utils.SpriteAnimation;
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
    private static final int STUNNED_INTERVAL = 200;
    private static final int IDLE_RPG_OFFSET_X = 246;
    private static final double ANIMATION_SPEED = 0.5;
    private static final int MACHINE_GUN_OFFSET_Y = 144;
    private static final int IDLE_PISTOL_OFFSET_X = 125;
    private static final int IDLE_MACHINE_OFFSET_X = 185;
    private static final int IDLE_WITH_NO_GUN_OFFSET_X = 42;

    private boolean dead;
    private boolean start;
    private boolean stunned;
    private boolean canJump;
    private boolean canPlayVoice;
    private boolean booleanVelocityX;
    private boolean booleanVelocityY;

    private int HP;
    private int salt;
    private int money;
    private int livesCount;
    private int stunnedInterval;
    private int closeCombatDamage;
    private int priceForGeneration;
    private int moneyForKillingEnemy;
    private int bulletsForKillingEnemy;
    private int medicineForKillingEnemy;

    private Point2D velocity;
    private SpriteAnimation withRPG;
    private SpriteAnimation animation;
    private SpriteAnimation withoutGun;
    private SpriteAnimation withPistol;
    private SpriteAnimation withMachineGun;

    private Text moneyText;
    private Text gameOverText;
    private Text continueText;

    private final MediaPlayer videoDeath;
    private final MediaView videoView;

    public Booker() {
        dead = false;
        start = true;
        canJump = true;
        stunned = false;
        canPlayVoice = true;
        booleanVelocityX = true;
        booleanVelocityY = true;
        velocity = new Point2D(0, 0);
        imageView.setViewport(new Rectangle2D(0, 0, WIDTH, HEIGHT));
        videoDeath = new MediaPlayer(new Media(
                Paths.get("resources", "videos", "death.mp4").toUri().toString()));
        videoView = new MediaView(videoDeath);

        HP = 100;
        money = 0;
        salt = 100;
        livesCount = 0;
        stunnedInterval = 0;
        closeCombatDamage = 0;
        priceForGeneration = 0;
        moneyForKillingEnemy = 0;
        bulletsForKillingEnemy = 0;
        medicineForKillingEnemy = 0;

        initializeDeathText();
        initializeAnimations();
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

    public int getSalt() {
        return salt;
    }

    public int getMoney() {
        return money;
    }

    public void setHP(int value) {
        HP = value;
    }

    public boolean isStunned() {
        return stunned;
    }

    public void setSalt(int value) {
        salt = value;
    }

    public void setMoney(int value) {
        money = value;
    }

    public double getStunnedInterval() {
        return stunnedInterval;
    }

    public void setCanPlayVoice(boolean value) {
        canPlayVoice = value;
    }

    public int getBulletsForKillingEnemy() {
        return bulletsForKillingEnemy;
    }


    public void stun() {
        stunned = true;
        velocity = velocity.add(0, JUMP_SPEED);
        Game.energetic.setHypnosisForBooker();
    }

    private void die() {
        dead = true;
        if (Game.difficultyLevelText.equals("hardcore"))
            gameOver();
        else {
            stopAnimation();
            Game.timer.stop();
            Game.menu.music.pause();

            for (Enemy enemy : Game.enemies)
                if (enemy instanceof Animatable animatable)
                    animatable.stopAnimation();

            if (money < priceForGeneration)
                gameOver();
            else {
                livesCount--;
                money -= priceForGeneration;

                if (Game.levelNumber > Level.FIRST_LEVEL)
                    playVideoDeath();
                else {
                    Game.appRoot.getChildren().add(gameOverText);

                    if (start)
                        Game.appRoot.getChildren().add(moneyText);

                    if (livesCount < 0) {
                        continueText.setText("Для того, что начать уровень заново нажмите ввод");
                        continueText.setTranslateX(Game.scene.getWidth() / 4);
                    }
                    Game.appRoot.getChildren().add(continueText);
                    addEventListenerOnDeathText();
                }
            }
        }
    }

    private void behave() {
        moveY(velocity.getY());
        moveX(velocity.getX());

        if (stunned) {
            stunnedInterval++;
            Game.energetic.updateHypnosisForBooker();

            if (stunnedInterval > STUNNED_INTERVAL) {
                stunnedInterval = 0;
                stunned = false;
                Game.energetic.deleteHypnosis();
                velocity = velocity.add(0, JUMP_SPEED);
            }
        } else {
            if (!canJump)
                stopAnimation();
            else
                animation.play();

            if (!Game.supplies.isEmpty() || (Game.levelNumber > Level.FIRST_LEVEL && Game.elizabeth.isGiveSupply()))
                takeSupply();

            if (Game.levelNumber < Level.THIRD_LEVEL)
                playVoice();
        }
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
                Game.menu.newGame();
            }
        });
    }

    private void playVoice() {
        if (!Game.enemies.isEmpty() && Game.levelNumber == Level.FIRST_LEVEL)
            if (getTranslateX() > Game.enemies.getFirst().getTranslateX() - Block.BLOCK_SIZE * 15 && canPlayVoice) {
                Sounds.shit.play(Game.menu.voiceSlider.getValue() / 100);
                canPlayVoice = false;
                return;
            }

        if (Game.enemies.isEmpty() && !canPlayVoice && Game.levelNumber == Level.FIRST_LEVEL) {
            Sounds.cretins.play(Game.menu.voiceSlider.getValue() / 100);
            canPlayVoice = true;
            return;
        }

        if (Game.enemies.isEmpty() && Game.levelNumber == Level.SECOND_LEVEL && canPlayVoice) {
            Sounds.letsGo.play(Game.menu.voiceSlider.getValue() / 100);
            canPlayVoice = false;
        }
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

            if (Game.levelNumber == Level.BOSS_LEVEL && Game.boss.getHP() < 1 &&
                    getBoundsInParent().intersects(Game.boss.getBoundsInParent()))
                setTranslateX(getTranslateX() - getScaleX());
        }
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

            if (intersectsWithEnemies())
                return;
        }
    }

    private void takeSupply() {
        if (Game.elizabeth.isGiveSupply())
            if (getBoundsInParent().intersects(Game.elizabeth.getBoundsInParent())) {
                takeMedicine();
                Game.elizabeth.giveMedicine();
            }

        for (Supply supply : Game.supplies)
            if (supply.getBoundsInParent().intersects(getBoundsInParent())) {
                if (supply.getSupply().equals("medicine") && Game.booker.getHP() < 100) {
                    takeMedicine();
                    Game.gameRoot.getChildren().remove(supply);
                    Game.supplies.remove(supply);
                } else if (supply.getSupply().equals("ammo")) {
                    Sounds.great.play(Game.menu.voiceSlider.getValue() / 100);
                    Game.weapon.setBullets(Game.weapon.getBullets() + bulletsForKillingEnemy);
                    Game.gameRoot.getChildren().remove(supply);
                    Game.supplies.remove(supply);
                }
                break;
            }
    }

    private void takeMedicine() {
        HP += medicineForKillingEnemy;

        if (HP > 100)
            HP = 100;

        switch ((int) (Math.random() * 2)) {
            case 0 -> Sounds.feelsBetter.play(Game.menu.voiceSlider.getValue() / 100);
            case 1 -> Sounds.feelingBetter.play(Game.menu.voiceSlider.getValue() / 100);
        }
    }

    private void playVideoDeath() {
        videoView.getMediaPlayer().setVolume(Game.menu.voiceSlider.getValue() / 100);
        Game.appRoot.getChildren().add(videoView);

        videoDeath.play();
        videoDeath.setOnEndOfMedia(() -> {
            Game.timer.start();
            Game.menu.music.play();
            Game.appRoot.getChildren().remove(videoView);

            Game.gameRoot.setLayoutX(0);
            Game.level.getBackground().setLayoutX(0);
            setTranslateX(100);
            setTranslateY(300);
            Game.elizabeth.reinitialize();

            Game.menu.addListener();
            HP = 100;
            velocity = new Point2D(0, 0);
            Game.stage.setWidth(1280);
            stunnedInterval = 0;
            stunned = false;
            Game.energetic.deleteHypnosis();

            if (livesCount < 0) {
                canPlayVoice = true;
                livesCount = 2;
                Game.clearData();
                Game.createEnemies();
                salt = 100;
            }
        });
    }

    public void setIdleAnimation() {
        switch (Game.weapon.getName()) {
            case "pistol" -> imageView.setViewport(new Rectangle2D(IDLE_PISTOL_OFFSET_X, 0, WIDTH, HEIGHT));
            case "machine_gun" -> imageView.setViewport(new Rectangle2D(IDLE_MACHINE_OFFSET_X, 0, WIDTH, HEIGHT));
            case "rpg" -> imageView.setViewport(new Rectangle2D(IDLE_RPG_OFFSET_X, 0, WIDTH, HEIGHT));
            default -> imageView.setViewport(new Rectangle2D(IDLE_WITH_NO_GUN_OFFSET_X, 0, WIDTH, HEIGHT));
        }
        stopAnimation();
    }

    public void setDifficultyLevel() {
        int money = 0;
        switch (Game.difficultyLevelText) {
            case "marik" -> {
                money = 1000000;
                livesCount = 4;
                priceForGeneration = 0;
                closeCombatDamage = 100;
                moneyForKillingEnemy = 0;
                bulletsForKillingEnemy = 30;
                medicineForKillingEnemy = 30;
            }
            case "easy" -> {
                money = 300;
                livesCount = 4;
                closeCombatDamage = 75;
                priceForGeneration = 15;
                moneyForKillingEnemy = 10;
                bulletsForKillingEnemy = 20;
                medicineForKillingEnemy = 20;
            }
            case "normal" -> {
                money = 150;
                livesCount = 2;
                closeCombatDamage = 50;
                priceForGeneration = 20;
                moneyForKillingEnemy = 5;
                bulletsForKillingEnemy = 15;
                medicineForKillingEnemy = 15;
            }
            case "high" -> {
                money = 100;
                livesCount = 1;
                closeCombatDamage = 40;
                priceForGeneration = 25;
                moneyForKillingEnemy = 3;
                bulletsForKillingEnemy = 10;
                medicineForKillingEnemy = 10;
            }
            case "hardcore" -> {
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
    }

    private void initializeDeathText() {
        gameOverText = new Text("Вы мертвы!");
        gameOverText.setFont(Font.font("Arial", FontWeight.BOLD, 28));
        gameOverText.setFill(Color.RED);
        gameOverText.setTranslateX(Game.scene.getWidth() / 2 - 75);
        gameOverText.setTranslateY(Game.scene.getHeight() / 2);

        continueText = new Text("Для продолжения нажмите ввод");
        continueText.setFont(Font.font("Arial", FontWeight.BOLD, 28));
        continueText.setFill(Color.WHITE);
        continueText.setTranslateX(Game.scene.getWidth() / 3);
        continueText.setTranslateY(Game.scene.getHeight() / 2 + 40);


        moneyText = new Text("  С каждой смертью вы теряете " + priceForGeneration +
                " монет, если\nваши монеты закончатся - игра будет окончена!");
        moneyText.setFont(Font.font("Arial", FontWeight.BOLD, 28));
        moneyText.setFill(Color.RED);
        moneyText.setTranslateX(Game.scene.getWidth() / 4);
        moneyText.setTranslateY(Game.scene.getHeight() / 1.5 - 30);
    }

    private void initializeAnimations() {
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

    public void jump(boolean enemyJump) {
        if (enemyJump) {
            if (booleanVelocityY) {
                velocity = velocity.add(0, JUMP_SPEED);
                booleanVelocityY = false;
            }
        } else if (canJump) {
            velocity = velocity.add(0, JUMP_SPEED);
            canJump = false;
        }
    }

    public void closeCombat(double scaleX) {
        HP -= closeCombatDamage / 10;

        if (booleanVelocityX) {
            Sounds.closeCombat.play(Game.menu.fxSlider.getValue() / 100);
            velocity = velocity.add(scaleX * -JUMP_SPEED, 0);
            booleanVelocityX = false;
        }
    }

    private boolean intersectsWithEnemies() {
        for (Enemy enemy : Game.enemies)
            if (getBoundsInParent().intersects(enemy.getBoundsInParent())) {
                Sounds.closeCombat.play(Game.menu.fxSlider.getValue() / 100);
                setTranslateY(getTranslateY() - 1);
                enemy.setHP(enemy.getHP() - closeCombatDamage);
                jump(true);
                return true;
            }

        if (Game.levelNumber == Level.BOSS_LEVEL) {
            if (getBoundsInParent().intersects(Game.boss.getBoundsInParent())) {
                setTranslateY(getTranslateY() - 1);
                if (Game.boss.getHP() > 1) {
                    Sounds.closeCombat.play(Game.menu.fxSlider.getValue() / 100);
                    HP -= closeCombatDamage / 10;
                    jump(true);
                } else
                    canJump = true;
                return true;
            }
        }
        return false;
    }

    private void addEventListenerOnDeathText() {
        EventHandler<KeyEvent> removeDeathText = new EventHandler<>() {
            @Override
            public void handle(KeyEvent event) {
                if (event.getCode() == KeyCode.ENTER) {
                    Game.timer.start();
                    Game.menu.music.play();
                    Game.appRoot.getChildren().removeAll(gameOverText, continueText);

                    if (start) {
                        Game.appRoot.getChildren().remove(moneyText);
                        start = false;
                    }

                    setTranslateX(100);
                    setTranslateY(200);

                    Game.gameRoot.setLayoutX(0);
                    Game.level.getBackground().setLayoutX(0);
                    Game.menu.addListener();
                    HP = 100;
                    velocity = new Point2D(0, 0);

                    if (livesCount < 0) {
                        canPlayVoice = true;
                        livesCount = 1;
                        Game.clearData();
                        Game.createEnemies();
                        salt = 100;
                    }

                    Game.scene.removeEventFilter(KEY_PRESSED, this);
                }
            }
        };
        Game.scene.addEventFilter(KEY_PRESSED, removeDeathText);
    }

    public void changeWeaponAnimation(String weaponName) {
        stopAnimation();
        switch (weaponName) {
            case "pistol" -> animation = withPistol;
            case "machine_gun" -> animation = withMachineGun;
            case "rpg" -> animation = withRPG;
            default -> animation = withoutGun;
        }
        animation.play();
    }

    public void addMoneyForKillingEnemy() {
        money += moneyForKillingEnemy;
    }

    private boolean intersectsWithBlocks(char typeOfCoordinate, double coordinate) {
        if (Game.levelNumber == Level.BOSS_LEVEL)
            if (getBoundsInParent().intersects(Game.level.getImgView().getBoundsInParent())) {
                setTranslateY(getTranslateY() - 1);
                if (!stunned)
                    canJump = true;
                return true;
            }

        if (!stunned) {
            for (Block block : Game.blocks)
                if (getBoundsInParent().intersects(block.getBoundsInParent()) && !block.getType().equals(BlockType.INVISIBLE)) {
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