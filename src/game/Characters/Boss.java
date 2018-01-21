package game.Characters;


import game.CutScenes;
import game.Game;
import game.Levels.Level;
import game.Sounds;
import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.transform.Rotate;
import javafx.util.Duration;

import java.io.File;

public class Boss extends Pane {
    private final byte BOSS_SPEED = 1;

    private MediaPlayer mediaPlayer;
    private ImageView imgView = new ImageView("file:/../images/characters/bigDaddy.png");
    private Point2D velocity = new Point2D(0, 0);
    SpriteAnimation animation = new SpriteAnimation(imgView, Duration.seconds(2), 3, 3, 0, 100, 93, 100);

    private int stunInterval = 0;
    private int velocityInterval = 0;
    private int trompInterval = 0;
    private double HP = 5000;
    private boolean moveRight = false;
    private boolean booleanVelocityX = true;
    private boolean hypnosis = false;
    boolean alive = true;

    private Rectangle rectHP = new Rectangle(500, 5, Color.RED);
    private Text name = new Text("Большой папочка");


    public Boss(int x, int y) {
        imgView.setFitWidth(92);
        imgView.setFitHeight(100);
        imgView.setViewport(new Rectangle2D(0, 0, 92, 100));

        setTranslateX(x - imgView.getFitWidth());
        setTranslateY(y - imgView.getFitHeight());

        if (Game.levelNumber == 3)
            setBoss();

        getChildren().add(imgView);
        Game.gameRoot.getChildren().addAll(this);
    }




    public void setHP(double value) {
        HP = value;
    }

    public double getHP() {
        return HP;
    }

    public void setHypnosis(boolean value) {
        hypnosis = value;
    }

    public void setTrompInterval(int value) {
        trompInterval = value;
    }

    public int getTrompInterval() {
        return trompInterval;
    }

    public MediaPlayer getMediaPlayer() {
        return mediaPlayer;
    }




    public void setBoss() {
        imgView.setViewport(new Rectangle2D(0, 0, 100, 100));
        imgView.setFitWidth(100);
        setTranslateX(Level.BLOCK_SIZE * 14);
        setTranslateY(Level.BLOCK_SIZE * 14 - imgView.getFitHeight());
        name.setFont( Font.font("Aria", 28) );
        name.setFill(Color.WHITE);
        name.setTranslateX(Game.appRoot.getWidth() / 2 - 95);
        name.setTranslateY(30);
        Game.appRoot.getChildren().add(name);

        rectHP.setTranslateX(Game.appRoot.getWidth() / 2 - 240);
        rectHP.setTranslateY(40);
        Game.appRoot.getChildren().add(rectHP);
    }


    private void moveX(int x) {
        for (int i = 0; i < Math.abs(x); i++) {
            if (x > 0 && getTranslateX() < Level.BLOCK_SIZE * 37 + imgView.getFitWidth()) {
                setScaleX(-1);
                setTranslateX(getTranslateX() + 1);
            } else if (getTranslateX() > 1){
                setTranslateX(getTranslateX() - 1);
                setScaleX(1);
            }

            if (getBoundsInParent().intersects(Game.booker.getBoundsInParent())) {
                setTranslateX(getTranslateX() + getScaleX());
                if (Game.booker.isBooleanVelocityX()) {
                    if (!Game.difficultyLevelText.equals("marik"))
                        Game.booker.setHP(Game.booker.getHP() - Game.booker.getEnemyDogfight() / 2);
                    else
                        Game.booker.setHP(Game.booker.getHP() - 5);
                    Sounds.audioClipFight.play(Game.menu.fxSlider.getValue() / 100);
                    Game.booker.velocity = Game.booker.velocity.add(- getScaleX() * 15, 0);
                    Game.booker.setBooleanVelocityX(false);
                }
            }
        }
    }


    private void behave() {
        if (stunInterval > 900) {
            imgView.setViewport(new Rectangle2D(0, 0, 92, 100));
            Sounds.bossTromp.play(Game.menu.fxSlider.getValue() / 100);
            Game.booker.setStunned(true);
            if (Game.booker.velocity.getY() > 0)
                Game.booker.velocity = Game.booker.velocity.add(0, -20);
            stunInterval = 0;
            Game.energetic.setHypnosisForBooker();
        } else if (stunInterval > 780) {
            animation.stop();
            imgView.setViewport(new Rectangle2D(176, 0, 92, 100));
            stunInterval++;
        } else {
            if (Game.booker.getTranslateY() < getTranslateY() - imgView.getFitHeight() * 1.5)
                if (!moveRight) {
                    moveX(-BOSS_SPEED);
                    animation.play();
                } else {
                    moveX(BOSS_SPEED);
                    animation.play();
                }
            else {
                if (Game.booker.getTranslateX() < getTranslateX()) {
                    moveX(-BOSS_SPEED);
                    animation.play();
                } else {
                    moveX(BOSS_SPEED);
                    animation.play();
                }

                if (booleanVelocityX) {
                    animation.stop();
                    if (Game.booker.getTranslateX() < getTranslateX())
                        velocity = velocity.add(-17, 0);
                    else
                        velocity = velocity.add(17, 0);

                    int rand = (int) (Math.random() * 3);
                    if (rand == 0)
                        Sounds.bossHit.play(Game.menu.voiceSlider.getValue() / 100);
                    else if (rand == 1)
                        Sounds.bossHit2.play(Game.menu.voiceSlider.getValue() / 100);
                    else
                        Sounds.bossHit3.play(Game.menu.voiceSlider.getValue() / 100);

                    booleanVelocityX = false;
                } else
                    velocityInterval++;
            }
        }
    }


    public void update() {
        if (HP > 0) {
            if (Game.levelNumber == 2) {
                if (trompInterval > 0 && trompInterval < 60)
                    imgView.setViewport(new Rectangle2D(176, 0, 92, 100));
                else if (trompInterval > 60) {
                    Sounds.bossTromp.play(Game.menu.fxSlider.getValue() / 100);
                    imgView.setViewport(new Rectangle2D(0, 0, 92, 100));
                }
            } else {
                    if (!hypnosis) {
                        behave();

                        if (!Game.booker.isStunned() && Game.booker.getTranslateY() < getTranslateY())
                            stunInterval++;

                        if (velocityInterval > 120) {
                            booleanVelocityX = true;
                            velocityInterval = 0;
                        }

                        if (getTranslateX() < imgView.getFitWidth())
                            moveRight = true;
                        else if (getTranslateX() > Level.BLOCK_SIZE * 37 - imgView.getFitWidth())
                            moveRight = false;

                        if (velocity.getX() < 0)
                            velocity = velocity.add(0.4, 0);
                        else if (velocity.getX() > 0)
                            velocity = velocity.add(-0.4, 0);
                        moveX((int) velocity.getX());
                    } else
                        animation.stop();
                    rectHP.setWidth(HP / 10);
                }
        } else
            if (alive) {
                animation.stop();
                rectHP.setWidth(0);
                getTransforms().add(new Rotate(90));

                mediaPlayer = new MediaPlayer(new Media(new File("file:/../sounds/fx/boss/death.mp3").toURI().toString()));
                mediaPlayer.setVolume(Game.menu.fxSlider.getValue() / 100);
                mediaPlayer.play();

                mediaPlayer.setOnEndOfMedia( () -> Game.cutScene = new CutScenes());
                alive = false;
            }
    }
}
