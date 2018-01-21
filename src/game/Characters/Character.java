package game.Characters;

import game.Levels.Block;
import game.Levels.Level;
import game.Game;
import game.Sounds;
import game.Supply;
import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.transform.Rotate;
import javafx.util.Duration;

import java.io.File;


public class Character extends Pane {
    private final byte CHARACTER_SPEED = 4;
    private ImageView imgView = new ImageView(new Image("file:/../images/characters/booker.png"));
    Point2D velocity = new Point2D(0, 0);

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
    boolean stayingOnLittlePlatform = false;


    private int money = 0;
    private byte countLives = 0;
    private byte HP = 100;
    private byte salt = 100;
    private byte enemyDogfight = 0;
    private byte characterDogFight = 0;
    private byte medicineCount = 0;
    private byte moneyForKillingEnemy = 0;
    private byte bulletCount = 0;
    private byte priceForGeneration = 0;
    private double stunnedInterval = 0;

    private byte rand = 0;

    private SpriteAnimation withoutGun = new SpriteAnimation(imgView, Duration.seconds(0.5), count, columns, 0, 94, 65, height);
    private SpriteAnimation withPistol = new SpriteAnimation(imgView, Duration.seconds(0.5), count, columns, 3, 318, 65, height);
    private SpriteAnimation withMachineGun = new SpriteAnimation(imgView, Duration.seconds(0.5), count, columns, 0, 173, 65, height);
    private SpriteAnimation withRPG = new SpriteAnimation(imgView, Duration.seconds(0.5), count, columns, 0, 250, 85, height);
    public SpriteAnimation animation = withoutGun;

    private Text gameOver = new Text("Игра окончена!");



    public Character() {
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
                if ( (Game.levelNumber == 3 && getTranslateX() < Level.BLOCK_SIZE * 37 - getWidth()) || Game.levelNumber != 3)
                    setTranslateX(getTranslateX() + 1);
            } else if (getTranslateX() > 1)
                setTranslateX(getTranslateX() - 1);

            for (Block platform : Game.blocks) {
                if (getBoundsInParent().intersects(platform.getBoundsInParent()))
                    if (x > 0) {
                        setTranslateX(getTranslateX() - 1);
                        return;
                    } else {
                        setTranslateX(getTranslateX() + 1);
                        return;
                    }
            }

            for (EnemyBase enemy : Game.enemies)
                if (getBoundsInParent().intersects(enemy.getBoundsInParent()) && !enemy.pickUpSupply) {
                    setTranslateX(getTranslateX() - getScaleX());
                    HP -= enemyDogfight;
                    Sounds.audioClipFight.play(Game.menu.fxSlider.getValue() / 100);
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
                    if (getBoundsInParent().intersects(block.getBoundsInParent()))
                        if (y > 0) {
                            setTranslateY(getTranslateY() - 1);
                            canJump = true;
                            if (Game.levelNumber != 2)
                                if (block.getBlockType().equals("LITTLE_BOX") || block.getBlockType().equals("LITTLE_STONE")
                                        || block.getBlockType().equals("LITTLE_BRICK") || block.getBlockType().equals("LITTLE_LAND")
                                        || block.getBlockType().equals("LITTLE_METAL"))
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

            for (EnemyBase enemy : Game.enemies)
                if (getBoundsInParent().intersects(enemy.getBoundsInParent()) && !enemy.pickUpSupply) {
                    HP -= enemyDogfight;
                    Sounds.audioClipFight.play(Game.menu.fxSlider.getValue() / 100);
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



            if (Game.levelNumber == 3) {
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
                        Sounds.audioClipFight.play(Game.menu.fxSlider.getValue() / 100);
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


    public byte getHP() {
        return HP;
    }


    public void setHP(int value) {
        HP = (byte)value;
    }


    public byte getSalt() {
        return salt;
    }


    public void setSalt(long value) {
        salt = (byte)value;
    }


    boolean isBooleanVelocityX() {
        return booleanVelocityX;
    }


    void setBooleanVelocityX(boolean value) {
        booleanVelocityX = value;
    }


    boolean isBooleanVelocityY() {
        return booleanVelocityY;
    }


    void setBooleanVelocityY(boolean value) {
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


    byte getEnemyDogfight() {
        return enemyDogfight;
    }


    byte getMedicineCount() {
        return medicineCount;
    }


    byte getCharacterDogFight() {
        return characterDogFight;
    }


    public byte getBulletCount() {
        return bulletCount;
    }


    byte getMoneyForKillingEnemy() {
        return moneyForKillingEnemy;
    }

    public boolean isStunned() {
        return stunned;
    }

    void setStunned(boolean value) {
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
                    Sounds.excellentVoice.play(Game.menu.voiceSlider.getValue() / 100);
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
        if (rand == 0)
            Sounds.doctorVoice.play(Game.menu.voiceSlider.getValue() / 100);
        else
            Sounds.feelBetterVoice.play(Game.menu.voiceSlider.getValue() / 100);
    }


    public void setDifficultyLevel() {
        switch (Game.difficultyLevelText) {
            case "marik":
                if (Game.levelNumber == 0)
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
                if (Game.levelNumber == 0)
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
                if (Game.levelNumber == 0)
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
                if (Game.levelNumber == 0)
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
                if (Game.levelNumber == 0)
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
        if (!Game.enemies.isEmpty() && Game.levelNumber == 0)
            if (Game.enemies.get(0).canSeeBooker && playVoice) {
                Sounds.bookerVoice = new MediaPlayer(new Media(new File("file:/../sounds/voice/booker/shit.mp3").toURI().toString()));
                Sounds.bookerVoice.setVolume(Game.menu.voiceSlider.getValue() / 100);
                Sounds.bookerVoice.play();
                playVoice = false;
            }

        if (Game.enemies.isEmpty() && !playVoice && Game.levelNumber == 0) {
                Sounds.bookerVoice = new MediaPlayer(new Media(
                        new File("file:/../sounds/voice/booker/creep_even_in_flying_town.mp3").toURI().toString()));
                Sounds.bookerVoice.setVolume(Game.menu.voiceSlider.getValue() / 100);
                Sounds.bookerVoice.play();
                playVoice = true;
            }

        if (Game.enemies.isEmpty() && Game.levelNumber == 1 && playVoice) {
            Sounds.audioClipLetsGo.play(Game.menu.voiceSlider.getValue() / 100);
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

            for(EnemyBase enemy : Game.enemies)
                enemy.animation.stop();

            if (countLives < 0) {
                gameOver.setText("Вы мертвы!");
                textContinue.setText("Для того, что начать уровень заново нажмите ввод");
                textContinue.setTranslateX(Game.scene.getWidth() / 4);
            }

            Game.scene.setOnKeyPressed(event -> {
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
                }
            });
        } else
            gameOver();
    }


    private void playVideoDeath() {
        Game.timer.stop();
        Game.menu.music.pause();
        if (Sounds.elizabethMediaPlayer != null)
            Sounds.elizabethMediaPlayer.stop();
        if (money >= priceForGeneration) {
            Game.stage.setWidth(1235);
            countLives--;
            money -= priceForGeneration;

            MediaPlayer video = new MediaPlayer(new Media(new File("file:/../videos/death.mp4").toURI().toString()));
            MediaView videoView = new MediaView(video);
            videoView.setFitWidth(Game.scene.getWidth());
            videoView.setFitHeight(Game.scene.getHeight());
            Game.appRoot.getChildren().add(videoView);

            video.play();

            video.setOnEndOfMedia(() -> {
                Game.timer.start();
                Game.menu.music.play();
                Game.appRoot.getChildren().removeAll(videoView);

                Game.gameRoot.setLayoutX(0);
                Game.level.getBackground().setLayoutX(0);
                setTranslateX(100);
                setTranslateY(300);
                Game.elizabeth.y = (short) Game.elizabeth.getTranslateY();
                Game.elizabeth.setTranslateX(getTranslateX());
                Game.elizabeth.setTranslateY(Game.scene.getHeight() - Level.BLOCK_SIZE * 2);
                Game.elizabeth.canMove = true;
                Game.elizabeth.giveSupply = false;
                Game.elizabeth.countMedicine = 0;
                Game.elizabeth.imgView.setImage(new Image("file:/../images/characters/elizabeth.png"));

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
        if (Game.levelNumber == 3)
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

            if (Game.levelNumber > 0)
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
            if (Game.levelNumber == 0)
                playDeath();
            else
                playVideoDeath();
        }
    }
}


