package com.abhai.deadshock.characters;

import com.abhai.deadshock.characters.enemies.Enemy;
import com.abhai.deadshock.levels.Block;
import com.abhai.deadshock.levels.BlockType;
import com.abhai.deadshock.levels.Level;
import com.abhai.deadshock.Game;
import com.abhai.deadshock.Sounds;
import com.abhai.deadshock.Supply;
import javafx.event.EventHandler;
import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
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


public class Booker extends Pane {
    private final byte CHARACTER_SPEED = 4;
    private Path imagePath = Paths.get("resources", "images", "characters", "booker.png");
    private ImageView imgView = new ImageView(new Image(imagePath.toUri().toString()));
    public Point2D velocity = new Point2D(0, 0);

    private final byte count = 6;
    private final byte columns = 6;
    private short offSetX = 0;
    private short offSetY = 20;
    private byte width = 55;
    private final byte height = 69;

    private boolean stunned = false;
    private boolean canJump = true;
    private boolean canChangeAnimation = true;
    private boolean playVoice = true;
    private boolean booleanVelocityX = true;
    private boolean booleanVelocityY = true;
    private boolean start = true;
    public boolean stayingOnLittlePlatform = false;


    private int money = 0;
    private int countLives = 0;
    private int HP = 100;
    private int salt = 100;
    private int enemyDogfight = 0;
    private int characterDogFight = 0;
    private int medicineCount = 0;
    private int moneyForKillingEnemy = 0;
    private int bulletCount = 0;
    private int priceForGeneration = 0;
    private double stunnedInterval = 0;

    private byte rand = 0;

    private SpriteAnimation withoutGun = new SpriteAnimation(imgView, Duration.seconds(0.5), count, columns, 0, 94, 65, height);
    private SpriteAnimation withPistol = new SpriteAnimation(imgView, Duration.seconds(0.5), count, columns, 3, 318, 65, height);
    private SpriteAnimation withMachineGun = new SpriteAnimation(imgView, Duration.seconds(0.5), count, columns, 0, 173, 65, height);
    private SpriteAnimation withRPG = new SpriteAnimation(imgView, Duration.seconds(0.5), count, columns, 0, 250, 85, height);
    public SpriteAnimation animation = withoutGun;

    private Text gameOver = new Text("Игра окончена!");



    public Booker() {
        imgView.setFitWidth(55);
        imgView.setFitHeight(63);
        imgView.setViewport(new Rectangle2D(offSetX, offSetY, width, height));
        getChildren().add(imgView);

        setTranslateX(100);
        setTranslateY(500);

        setWidth(imgView.getFitWidth());
        setHeight(imgView.getFitHeight());

        Game.gameRoot.getChildren().add(this);
        Game.appRoot.getChildren().add(gameOver);
    }


    public void moveX(int x) {
        for (int i = 0; i < Math.abs(x); i++) {
            if (x > 0) {
                if (Game.levelNumber != Level.BOSS_LEVEL || getTranslateX() < Block.BLOCK_SIZE * 37 - getWidth())
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
                if (getBoundsInParent().intersects(enemy.getBoundsInParent()) && !enemy.pickUpSupply) {
                    setTranslateX(getTranslateX() - getScaleX());
                    HP -= enemyDogfight;
                    Sounds.closeCombat.play(Game.menu.fxSlider.getValue() / 100);
                    if (enemy.name.equals("camper"))
                        velocity = velocity.add( - getScaleX() * 15, 0);
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
                            if (Game.levelNumber != Level.THIRD_LEVEL)
                                if (block.getType().equals(BlockType.LITTLE_BOX)
                                        || block.getType().equals(BlockType.LITTLE_STONE)
                                        || block.getType().equals(BlockType.LITTLE_BRICK)
                                        || block.getType().equals(BlockType.LITTLE_LAND)
                                        || block.getType().equals(BlockType.LITTLE_METAL))
                                    stayingOnLittlePlatform = true;
                                else
                                    stayingOnLittlePlatform = false;
                            return;
                        } else {
                            setTranslateY(getTranslateY() + 1);
                            velocity = new Point2D(0, 8);
                            return;
                        }
                }

            for (Enemy enemy : Game.enemies)
                if (getBoundsInParent().intersects(enemy.getBoundsInParent()) && !enemy.pickUpSupply) {
                    HP -= enemyDogfight;
                    Sounds.closeCombat.play(Game.menu.fxSlider.getValue() / 100);
                    if (y > 0) {
                        setTranslateY(getTranslateY() - 1);
                        if (enemy.name.equals("camper"))
                            enemy.HP -= characterDogFight * 3;
                        else
                            enemy.HP -= characterDogFight;
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
                    if (Game.boss.alive) {
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
            velocity = velocity.add(0, -23);
            canJump = false;
        }
    }

    public short getOffSetY() {
        return offSetY;
    }

    public byte getCHARACTER_SPEED() {
        return CHARACTER_SPEED;
    }

    public int getHP() {
        return HP;
    }

    public void setHP(int value) {
        HP = (byte)value;
    }

    public int getSalt() {
        return salt;
    }

    public void setSalt(long value) {
        salt = (byte)value;
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

    public void setMoney(long value) {
        money = (int)value;
    }

    public void setCanChangeAnimation(boolean value) {
        canChangeAnimation = value;
    }

    public boolean isCanChangeAnimation() {
        return canChangeAnimation;
    }

    public ImageView getImgView() {
        return imgView;
    }

    public int getEnemyDogfight() {
        return enemyDogfight;
    }

    public int getMedicineCount() {
        return medicineCount;
    }

    public int getCharacterDogFight() {
        return characterDogFight;
    }

    public int getBulletCount() {
        return bulletCount;
    }

    public int getMoneyForKillingEnemy() {
        return moneyForKillingEnemy;
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
        if (Game.elizabeth.giveSupply)
            if (getBoundsInParent().intersects(Game.elizabeth.getBoundsInParent())) {
                takeMedicine();
                Game.elizabeth.giveSupply = false;
                Game.elizabeth.canMove = true;
                Game.elizabeth.countMedicine -= 2;
                Game.elizabeth.imgView.setImage(Game.elizabeth.imgElizabeth);
            }
        for (Supply supply : Game.supplies)
            if (supply.getBoundsInParent().intersects(getBoundsInParent())) {
                if (supply.getSupply().equals("medicine") && Game.booker.getHP() < 100) {
                    takeMedicine();
                    Game.gameRoot.getChildren().remove(supply);
                    Game.supplies.remove(supply);
                }
                else if (supply.getSupply().equals("ammo")) {
                    Sounds.great.play(Game.menu.voiceSlider.getValue() / 100);
                    Game.weapon.setBullets(Game.weapon.getBullets() + Game.booker.getBulletCount());
                    Game.gameRoot.getChildren().remove(supply);
                    Game.supplies.remove(supply);
                }
                break;
            }
    }

    private void takeMedicine() {
        for (int count = 0; count < medicineCount; count++)
            if (HP < 100)
                HP += 1;
            else
                break;
        rand = (byte) (Math.random() * 2);
        if (rand == 0) {
            Sounds.feelsBetter.setVolume(Game.menu.voiceSlider.getValue() / 100);
            Sounds.feelsBetter.play();
        }
        else
            Sounds.feelingBetter.play(Game.menu.voiceSlider.getValue() / 100);
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
                countLives = 4;
                break;
            case "easy":
                if (Game.levelNumber == Level.FIRST_LEVEL)
                    money = 300;
                medicineCount = 20;
                bulletCount = 20;
                enemyDogfight = 5;
                moneyForKillingEnemy = 10;
                characterDogFight = 75;
                countLives = 4;
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
                countLives = 2;
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
                countLives = 1;
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
                countLives = 0;
                priceForGeneration = 0;
                break;
        }
    }

    public void changeViewPort(int x, int y, int width) {
        offSetX = (short) x;
        offSetY = (short) y;
        this.width = (byte) width;
        if (Game.weapon.getName().equals("rpg"))
            imgView.setFitWidth(75);
        else
            imgView.setFitWidth(55);

        imgView.setViewport(new Rectangle2D(offSetX, offSetY, this.width, height));

        animation.stop();
    }

    public void changeAnimation(String weaponName) {
        animation.stop();
        switch (weaponName) {
            case "pistol":
                if (!canJump)
                    Game.booker.changeViewPort(3, 318, 65);
                animation = withPistol;
                imgView.setFitWidth(55);
                break;
            case "machine_gun":
                if (!canJump)
                    Game.booker.changeViewPort(0, 173, 65);
                animation = withMachineGun;
                imgView.setFitWidth(55);
                break;
            case "rpg":
                if (!canJump)
                    Game.booker.changeViewPort(0, 250, 85);
                animation = withRPG;
                imgView.setFitWidth(75);
                break;
            default:
                if (!canJump)
                    Game.booker.changeViewPort(0, 94, 65);
                animation = withoutGun;
                imgView.setFitWidth(55);
        }
        animation.play();
        canChangeAnimation = false;
    }

    private void playBookerVoice() {
        if (!Game.enemies.isEmpty() && Game.levelNumber == Level.FIRST_LEVEL)
            if (Game.enemies.get(0).isCanSeeBooker() && playVoice) {
                Path soundPath = Paths.get("resources", "sounds", "voices", "booker", "shit.mp3");
                Sounds.feelsBetter = new MediaPlayer(new Media(soundPath.toUri().toString()));
                Sounds.feelsBetter.setVolume(Game.menu.voiceSlider.getValue() / 100);
                Sounds.feelsBetter.play();
                playVoice = false;
            }

        if (Game.enemies.isEmpty() && !playVoice && Game.levelNumber == Level.FIRST_LEVEL) {
            Path soundPath = Paths.get("resources", "sounds", "voices", "booker", "cretins.mp3");
            Sounds.feelsBetter = new MediaPlayer(new Media(soundPath.toUri().toString()));
            Sounds.feelsBetter.setVolume(Game.menu.voiceSlider.getValue() / 100);
            Sounds.feelsBetter.play();
            playVoice = true;
        }

        if (Game.enemies.isEmpty() && Game.levelNumber == Level.SECOND_LEVEL && playVoice) {
            Sounds.letsGo.play(Game.menu.voiceSlider.getValue() / 100);
            playVoice = false;
        }
    }

    private void playDeath() {
        Game.timer.stop();
        Game.menu.music.pause();
        if (money >= priceForGeneration) {
            Text textMoney = new Text("  С каждой смертью вы теряете " + priceForGeneration + " монет, если\nваши монеты закончатся - игра будет окончена!");

            Text gameOver = new Text("Вы мертвы!");
            gameOver.setFont(Font.font("Arial", FontWeight.BOLD, 28));
            gameOver.setFill(Color.RED);
            gameOver.setTranslateX(Game.scene.getWidth() / 2 - 75);
            gameOver.setTranslateY(Game.scene.getHeight() / 2);
            Game.appRoot.getChildren().add(gameOver);

            Text textContinue = new Text("Для продолжения нажмите ввод");
            textContinue.setFont(Font.font("Arial", FontWeight.BOLD, 28));
            textContinue.setFill(Color.WHITE);
            textContinue.setTranslateX(Game.scene.getWidth() / 3);
            textContinue.setTranslateY(Game.scene.getHeight() / 2 + 40);
            Game.appRoot.getChildren().add(textContinue);

            if (start) {
                textMoney.setFont(Font.font("Arial", FontWeight.BOLD, 28));
                textMoney.setFill(Color.RED);
                textMoney.setTranslateX(Game.scene.getWidth() / 4);
                textMoney.setTranslateY(Game.scene.getHeight() / 1.5 - 30);
                Game.appRoot.getChildren().add(textMoney);
            }

            countLives--;
            money -= priceForGeneration;

            for(Enemy enemy : Game.enemies)
                enemy.animation.stop();

            if (countLives < 0) {
                gameOver.setText("Вы мертвы!");
                textContinue.setText("Для того, что начать уровень заново нажмите ввод");
                textContinue.setTranslateX(Game.scene.getWidth() / 4);
            }

            EventHandler<KeyEvent> removeCoverFilter = new EventHandler<>() {
                @Override
                public void handle(KeyEvent event) {
                    if (event.getCode() == KeyCode.ENTER) {
                        Game.timer.start();
                        Game.menu.music.play();
                        Game.appRoot.getChildren().removeAll(gameOver, textContinue);
                        if (start) {
                            Game.appRoot.getChildren().remove(textMoney);
                            start = false;
                        }

                        setTranslateX(100);
                        setTranslateY(200);

                        Game.gameRoot.setLayoutX(0);
                        Game.level.getBackground().setLayoutX(0);
                        Game.menu.addListener();
                        HP = 100;
                        velocity = new Point2D(0, 0);

                        if (countLives < 0) {
                            playVoice = true;
                            countLives = 1;
                            Game.clearData();
                            Game.createEnemies();
                            salt = 100;
                        }

                        Game.scene.removeEventFilter(KEY_PRESSED, this);
                    }
                }
            };

            Game.scene.addEventFilter(KEY_PRESSED, removeCoverFilter);
        } else
            gameOver();
    }

    private void playVideoDeath() {
        Game.timer.stop();
        Game.menu.music.pause();
        if (Sounds.elizabethMediaPlayer != null)
            Sounds.elizabethMediaPlayer.stop();
        if (money >= priceForGeneration) {
            Game.stage.setWidth(1230);
            countLives--;
            money -= priceForGeneration;

            Path videoPath = Paths.get("resources", "videos", "death.mp4");
            MediaPlayer video = new MediaPlayer(new Media(videoPath.toUri().toString()));
            MediaView videoView = new MediaView(video);
            videoView.setFitWidth(Game.scene.getWidth());
            videoView.setFitHeight(Game.scene.getHeight());
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
                Game.elizabeth.y = (short) Game.elizabeth.getTranslateY();
                Game.elizabeth.setTranslateX(getTranslateX());
                Game.elizabeth.setTranslateY(Game.scene.getHeight() - Block.BLOCK_SIZE * 2);
                Game.elizabeth.canMove = true;
                Game.elizabeth.giveSupply = false;
                Game.elizabeth.countMedicine = 0;
                Path imagePath = Paths.get("resources", "images", "characters", "elizabeth.png");
                Game.elizabeth.imgView.setImage(new Image(imagePath.toUri().toString()));

                Game.menu.addListener();
                HP = 100;
                velocity = new Point2D(0, 0);
                Game.stage.setWidth(1280);
                stunnedInterval = 0;
                stunned = false;
                Game.energetic.deleteHypnosis();

                if (countLives < 0) {
                    playVoice = true;
                    countLives = 2;
                    Game.clearData();
                    Game.createEnemies();
                    salt = 100;
                }
            });
        } else
            gameOver();
    }

    private void gameOver() {
        if (Game.levelNumber == Level.BOSS_LEVEL)
            Game.boss.animation.stop();

        gameOver.setFont(Font.font("Arial", FontWeight.BOLD, 28));
        gameOver.setFill(Color.RED);
        gameOver.setTranslateX(Game.scene.getWidth() / 2 - 100);
        gameOver.setTranslateY(Game.scene.getHeight() / 2);
        gameOver.setVisible(true);

        Game.scene.setOnKeyPressed(event -> {
            Game.appRoot.getChildren().remove(gameOver);
            Game.menu.newGame();
        });
    }

    public void update() {
        if (velocity.getY() < 8)
            velocity = velocity.add(0, 0.5);
        else
            booleanVelocityY = true;
        moveY((int)velocity.getY());

        if (velocity.getX() < 0)
            velocity = velocity.add(0.5,0);
        else if (velocity.getX() > 0)
            velocity = velocity.add(-0.5, 0);
        else
            booleanVelocityX = true;
        moveX((int)velocity.getX());

        if (stunnedInterval == 180)
            velocity = velocity.add(0, -20);

        if (!stunned) {
            if (!canJump)
                animation.stop();
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