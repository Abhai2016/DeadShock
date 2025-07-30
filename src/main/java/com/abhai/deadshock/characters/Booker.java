package com.abhai.deadshock.characters;

import com.abhai.deadshock.Game;
import com.abhai.deadshock.Supply;
import com.abhai.deadshock.characters.enemies.Enemy;
import com.abhai.deadshock.characters.enemies.EnemyType;
import com.abhai.deadshock.levels.Block;
import com.abhai.deadshock.levels.BlockType;
import com.abhai.deadshock.levels.Level;
import com.abhai.deadshock.utils.Sounds;
import com.abhai.deadshock.utils.SpriteAnimation;
import javafx.event.EventHandler;
import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;
import javafx.scene.image.ImageView;
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

import java.nio.file.Path;
import java.nio.file.Paths;

import static javafx.scene.input.KeyEvent.KEY_PRESSED;

public class Booker extends Character implements Animatable {
    public static final int WIDTH = 60;

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
    private int bulletCount;
    private int enemyDogfight;
    private int medicineCount;
    private int stunnedInterval;
    private int characterDogFight;
    private int priceForGeneration;
    private int moneyForKillingEnemy;

    private Point2D velocity;
    private SpriteAnimation withRPG;
    private SpriteAnimation animation;
    private SpriteAnimation withoutGun;
    private SpriteAnimation withPistol;
    private SpriteAnimation withMachineGun;

    private Text moneyText;
    private Text gameOverText;
    private Text continueText;

    public Booker() {
        start = true;
        canJump = true;
        stunned = false;
        canPlayVoice = true;
        booleanVelocityX = true;
        booleanVelocityY = true;
        velocity = new Point2D(0, 0);
        imageView.setViewport(new Rectangle2D(0, 0, WIDTH, HEIGHT));

        HP = 100;
        money = 0;
        salt = 100;
        livesCount = 0;
        bulletCount = 0;
        enemyDogfight = 0;
        medicineCount = 0;
        stunnedInterval = 0;
        characterDogFight = 0;
        priceForGeneration = 0;
        moneyForKillingEnemy = 0;

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

    public void moveX(int x) {
        for (int i = 0; i < Math.abs(x); i++) {
            if (x > 0) {
                setTranslateX(getTranslateX() + 1);
            } else if (getTranslateX() > 1)
                setTranslateX(getTranslateX() - 1);

            for (Block block : Game.blocks) {
                if (getBoundsInParent().intersects(block.getBoundsInParent()) && !block.getType().equals(BlockType.INVISIBLE))
                    if (x > 0) {
                        setTranslateX(getTranslateX() - 1);
                        return;
                    } else {
                        setTranslateX(getTranslateX() + 1);
                        return;
                    }
            }

            for (Enemy enemy : Game.enemies)
                if (getBoundsInParent().intersects(enemy.getBoundsInParent())) {
                    setTranslateX(getTranslateX() - getScaleX());
                    HP -= enemyDogfight;
                    Sounds.closeCombat.play(Game.menu.fxSlider.getValue() / 100);
                    if (enemy.isCamper())
                        velocity = velocity.add(-getScaleX() * 15, 0);
                    return;
                }

            if (Game.boss != null)
                if (getBoundsInParent().intersects(Game.boss.getBoundsInParent()))
                    setTranslateX(getTranslateX() - getScaleX());
        }
    }

    private void moveY(int y) {
        for (int i = 0; i < Math.abs(y); i++) {
            if (y > 0) {
                setTranslateY(getTranslateY() + 1);
                canJump = false;
            } else
                setTranslateY(getTranslateY() - 1);

            if (!stunned)
                for (Block block : Game.blocks) {
                    if (getBoundsInParent().intersects(block.getBoundsInParent()) && !block.getType().equals(BlockType.INVISIBLE))
                        if (y > 0) {
                            setTranslateY(getTranslateY() - 1);
                            canJump = true;
                            return;
                        } else {
                            setTranslateY(getTranslateY() + 1);
                            velocity = new Point2D(0, 8);
                            return;
                        }
                }

            for (Enemy enemy : Game.enemies)
                if (getBoundsInParent().intersects(enemy.getBoundsInParent())) {
                    HP -= enemyDogfight;
                    Sounds.closeCombat.play(Game.menu.fxSlider.getValue() / 100);
                    if (y > 0) {
                        setTranslateY(getTranslateY() - 1);
                        if (enemy.isCamper())
                            enemy.setHP(enemy.getHP() - characterDogFight * 3);
                        else
                            enemy.setHP(enemy.getHP() - characterDogFight);
                        if (booleanVelocityY) {
                            velocity = velocity.add(0, -22);
                            booleanVelocityY = false;
                        }
                    } else
                        setTranslateY(getTranslateY() + 1);
                    return;
                }

            if (Game.levelNumber == Level.BOSS_LEVEL) {
                if (getBoundsInParent().intersects(Game.level.getImgView().getBoundsInParent())) {
                    setTranslateY(getTranslateY() - 1);
                    if (stunnedInterval > 180) {
                        stunnedInterval = 0;
                        stunned = false;
                        Game.energetic.deleteHypnosis();
                    }
                    if (!stunned)
                        canJump = true;
                }
                if (getBoundsInParent().intersects(Game.boss.getBoundsInParent())) {
                    setTranslateY(getTranslateY() - 1);
                    if (Game.boss.getHP() > 1) {
                        Sounds.closeCombat.play(Game.menu.fxSlider.getValue() / 100);
                        if (!Game.difficultyLevelText.equals("marik"))
                            HP -= enemyDogfight;
                        else
                            HP -= 5;
                        if (booleanVelocityY) {
                            velocity = velocity.add(0, -20);
                            booleanVelocityY = false;
                        }
                    } else
                        canJump = true;
                    return;
                }
            }
        }
    }

    public void jump() {
        if (canJump) {
            velocity = velocity.add(0, JUMP_SPEED);
            canJump = false;
        }
    }

    public void stun(EnemyType enemyType) {
        if (enemyType.equals(EnemyType.BOSS)) {
            stunned = true;
            Game.energetic.setHypnosisForBooker();
        } else
            booleanVelocityY = false;

        if (velocity.getY() > 0)
            velocity = velocity.add(0, JUMP_SPEED);
    }

    public int getHP() {
        return HP;
    }

    public void setHP(int value) {
        HP = value;
    }

    public int getSalt() {
        return salt;
    }

    public void setSalt(int value) {
        salt = value;
    }

    public boolean isBooleanVelocityX() {
        return booleanVelocityX;
    }

    public void setBooleanVelocityX(boolean value) {
        booleanVelocityX = value;
    }

    public boolean isBooleanVelocityY() {
        return booleanVelocityY;
    }

    public void setBooleanVelocityY(boolean value) {
        booleanVelocityY = value;
    }

    public int getMoney() {
        return money;
    }

    public void setMoney(int value) {
        money = value;
    }

    public ImageView getImageView() {
        return imageView;
    }

    public int getCharacterDogFight() {
        return characterDogFight;
    }

    public void closeCombat(boolean jump) {
        HP -= enemyDogfight;

        if (jump) {
            velocity = velocity.add(getScaleX() * JUMP_SPEED, 0);
            booleanVelocityX = false;
        }
    }

    public int getBulletCount() {
        return bulletCount;
    }

    public boolean isStunned() {
        return stunned;
    }

    public void setStunned(boolean value) {
        stunned = value;
    }

    public double getStunnedInterval() {
        return stunnedInterval;
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
                    Game.weapon.setBullets(Game.weapon.getBullets() + Game.booker.getBulletCount());
                    Game.gameRoot.getChildren().remove(supply);
                    Game.supplies.remove(supply);
                }
                break;
            }
    }

    private void takeMedicine() {
        HP += medicineCount;

        if (HP > 100)
            HP = 100;

        switch ((int) (Math.random() * 2)) {
            case 0 -> Sounds.feelsBetter.play(Game.menu.voiceSlider.getValue() / 100);
            case 1 -> Sounds.feelingBetter.play(Game.menu.voiceSlider.getValue() / 100);
        }
    }

    public void setDifficultyLevel() {
        switch (Game.difficultyLevelText) {
            case "marik":
                if (Game.levelNumber == Level.FIRST_LEVEL)
                    money = 1000000;
                medicineCount = 30;
                bulletCount = 30;
                enemyDogfight = 0;
                moneyForKillingEnemy = 0;
                characterDogFight = 100;
                priceForGeneration = 0;
                livesCount = 4;
                break;
            case "easy":
                if (Game.levelNumber == Level.FIRST_LEVEL)
                    money = 300;
                medicineCount = 20;
                bulletCount = 20;
                enemyDogfight = 5;
                moneyForKillingEnemy = 10;
                characterDogFight = 75;
                livesCount = 4;
                priceForGeneration = 15;
                break;
            case "normal":
                if (Game.levelNumber == Level.FIRST_LEVEL)
                    money = 150;
                medicineCount = 15;
                bulletCount = 15;
                enemyDogfight = 10;
                moneyForKillingEnemy = 5;
                characterDogFight = 50;
                livesCount = 2;
                priceForGeneration = 20;
                break;
            case "high":
                if (Game.levelNumber == Level.FIRST_LEVEL)
                    money = 100;
                medicineCount = 10;
                bulletCount = 10;
                enemyDogfight = 15;
                moneyForKillingEnemy = 3;
                characterDogFight = 50;
                livesCount = 1;
                priceForGeneration = 25;
                break;
            case "hardcore":
                if (Game.levelNumber == Level.FIRST_LEVEL)
                    money = 100;
                medicineCount = 10;
                bulletCount = 10;
                enemyDogfight = 20;
                moneyForKillingEnemy = 2;
                characterDogFight = 50;
                livesCount = 0;
                priceForGeneration = 0;
                break;
        }
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

    private void playBookerVoice() {
        if (!Game.enemies.isEmpty() && Game.levelNumber == Level.FIRST_LEVEL)
            if (getTranslateX() == Game.enemies.getFirst().getTranslateX() - Block.BLOCK_SIZE * 15 && canPlayVoice) {
                Sounds.shit.play(Game.menu.voiceSlider.getValue() / 100);
                canPlayVoice = false;
            }

        if (Game.enemies.isEmpty() && !canPlayVoice && Game.levelNumber == Level.FIRST_LEVEL) {
            Sounds.cretins.play(Game.menu.voiceSlider.getValue() / 100);
            canPlayVoice = true;
        }

        if (Game.enemies.isEmpty() && Game.levelNumber == Level.SECOND_LEVEL && canPlayVoice) {
            Sounds.letsGo.play(Game.menu.voiceSlider.getValue() / 100);
            canPlayVoice = false;
        }
    }

    private void playDeath() {
        stopAnimation();
        Game.timer.stop();
        Game.menu.music.pause();
        Game.appRoot.getChildren().add(gameOverText);

        if (money < priceForGeneration) {
            gameOver();
        } else {
            livesCount--;
            money -= priceForGeneration;

            for (Enemy enemy : Game.enemies)
                if (enemy instanceof Animatable animatable)
                    animatable.stopAnimation();

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

    private void playVideoDeath() {
        stopAnimation();
        Game.timer.stop();
        Game.menu.music.pause();

        if (Sounds.whereAreYouFrom.getStatus() == MediaPlayer.Status.PLAYING)
            Sounds.whereAreYouFrom.pause();

        if (money < priceForGeneration) {
            gameOver();
        } else {
            livesCount--;
            money -= priceForGeneration;

            Path videoPath = Paths.get("resources", "videos", "death.mp4");
            MediaPlayer video = new MediaPlayer(new Media(videoPath.toUri().toString()));
            MediaView videoView = new MediaView(video);
            videoView.getMediaPlayer().setVolume(Game.menu.voiceSlider.getValue() / 100);
            Game.appRoot.getChildren().add(videoView);

            video.play();

            video.setOnEndOfMedia(() -> {
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
    }

    private void gameOver() {
        if (Game.levelNumber == Level.BOSS_LEVEL)
            Game.boss.stopAnimation();

        gameOverText.setFont(Font.font("Arial", FontWeight.BOLD, 28));
        gameOverText.setFill(Color.RED);
        gameOverText.setTranslateX(Game.scene.getWidth() / 2 - 100);
        gameOverText.setTranslateY(Game.scene.getHeight() / 2);

        Game.scene.setOnKeyPressed(event -> {
            Game.appRoot.getChildren().remove(gameOverText);
            Game.menu.newGame();
        });
    }

    public void addMoneyForKillingEnemy() {
        money += moneyForKillingEnemy;
    }

    public void minusHPForCamperScream() {
        HP -= enemyDogfight;
    }

    public void update() {
        if (velocity.getY() < 8)
            velocity = velocity.add(0, ANIMATION_SPEED);
        else
            booleanVelocityY = true;
        moveY((int) velocity.getY());

        if (velocity.getX() < 0)
            velocity = velocity.add(ANIMATION_SPEED, 0);
        else if (velocity.getX() > 0)
            velocity = velocity.add(-ANIMATION_SPEED, 0);
        else
            booleanVelocityX = true;
        moveX((int) velocity.getX());

        if (stunnedInterval == 180)
            velocity = velocity.add(0, -20);

        if (!stunned) {
            if (!canJump)
                stopAnimation();
            else
                animation.play();

            if (Game.levelNumber > Level.FIRST_LEVEL)
                takeSupply();

            if (getTranslateY() > Game.scene.getHeight())
                HP = 0;

            playBookerVoice();
        } else {
            stunnedInterval++;
            Game.energetic.updateHypnosisForBooker();
        }

        if (HP <= 0) {
            if (Game.difficultyLevelText.equals("hardcore")) {
                gameOver();
                return;
            }
            if (Game.levelNumber == Level.FIRST_LEVEL)
                playDeath();
            else
                playVideoDeath();
        }
    }
}