package com.abhai.deadshock.characters.enemies;

import com.abhai.deadshock.Game;
import com.abhai.deadshock.characters.Animatable;
import com.abhai.deadshock.energetics.EnemyHypnosis;
import com.abhai.deadshock.levels.Level;
import com.abhai.deadshock.utils.Sounds;
import com.abhai.deadshock.utils.SpriteAnimation;
import com.abhai.deadshock.utils.Texts;
import javafx.geometry.Rectangle2D;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.transform.Rotate;
import javafx.util.Duration;

import static com.abhai.deadshock.levels.Block.BLOCK_SIZE;

public class Boss extends Enemy implements Animatable {
    private static final int SPEED = 1;
    private static final int WIDTH = 95;
    private static final int HEIGHT = 100;
    private static final int JUMP_SPEED = 20;
    private static final int ANIMATION_SPEED = 1;
    private static final int COUNT_OF_SPRITES = 3;

    private boolean dead;
    private int stunInterval;
    private int velocityInterval;

    private final Text name;
    private final Rectangle rectHP;
    private final EnemyHypnosis hypnosis;
    private final SpriteAnimation animation;

    public Boss() {
        HP = 5000;
        dead = false;
        stunInterval = 0;
        velocityInterval = 0;
        type = EnemyType.BOSS;

        hypnosis = new EnemyHypnosis();
        name = new Text(Texts.BOSS_NAME);
        rectHP = new Rectangle(500, 5, Color.RED);
        imageView.setViewport(new Rectangle2D(0, 0, WIDTH, HEIGHT));
        animation = new SpriteAnimation(imageView, Duration.seconds(ANIMATION_SPEED),
                COUNT_OF_SPRITES, COUNT_OF_SPRITES, 0, 0, WIDTH, HEIGHT);

        setScaleX(-1);
    }

    @Override
    public void reset() {
        super.reset();

        HP = 5000;
        dead = false;
        stunInterval = 0;
        velocityInterval = 0;
        imageView.setViewport(new Rectangle2D(0, 0, WIDTH, HEIGHT));

        setScaleX(-1);
        Game.appRoot.getChildren().removeAll(rectHP, name);
    }

    @Override
    public void init(int x, int y) {
        super.init(x, y);

        if (Game.levelNumber == Level.BOSS_LEVEL)
            initHp();
    }

    @Override
    public void stopAnimation() {
        animation.stop();
    }

    @Override
    protected String getImageName() {
        return "bigDaddy.png";
    }


    private void die() {
        dead = true;
        stopAnimation();
        rectHP.setWidth(0);
        getTransforms().add(new Rotate(-90));
        setTranslateY(getTranslateY() + HEIGHT);

        Sounds.bossDeath.setVolume(Game.menu.getFxSlider().getValue() / 100);
        Sounds.bossDeath.setOnEndOfMedia(() -> {
            toDelete = true;
            Game.playCutscene();
        });
        Sounds.bossDeath.play();
    }

    private void stun() {
        stunInterval++;
        stopAnimation();
        imageView.setViewport(new Rectangle2D(WIDTH * 2, 0, WIDTH, HEIGHT));

        if (stunInterval > 800) {
            stunInterval = 0;
            hypnosis.hypnotize();
            imageView.setViewport(new Rectangle2D(0, 0, WIDTH, HEIGHT));
        }
    }

    public void unStun() {
        stunInterval = 0;
        hypnosis.delete();
        imageView.setViewport(new Rectangle2D(0, 0, WIDTH, HEIGHT));
    }

    private void initHp() {
        name.setFont(Font.font("Aria", 28));
        name.setFill(Color.WHITE);
        name.setTranslateX(Game.appRoot.getWidth() / 2 - 95);
        name.setTranslateY(30);
        Game.appRoot.getChildren().add(name);

        rectHP.setTranslateX(Game.appRoot.getWidth() / 2 - 240);
        rectHP.setTranslateY(40);
        Game.appRoot.getChildren().add(rectHP);
    }

    private void behave() {
        if (stunInterval > 700) {
            stun();
            return;
        }

        if (velocity.getX() < 0)
            velocity = velocity.add(ANIMATION_SPEED, 0);
        else if (velocity.getX() > 0)
            velocity = velocity.add(-ANIMATION_SPEED, 0);

        if (Game.booker.getTranslateX() < getTranslateX())
            moveX(-SPEED);
        if (Game.booker.getTranslateX() > getTranslateX())
            moveX(SPEED);
        if (Game.booker.getTranslateX() == getTranslateX())
            stopAnimation();

        moveX(velocity.getX());

        if (!Game.booker.isHypnotized() && Game.booker.getTranslateY() < getTranslateY())
            stunInterval++;
        else
            velocityJump();

        hypnosis.update();
    }

    public void changeLevel() {
        initHp();
        stunInterval = 0;
        Game.setBossLevel();
        setTranslateX(BLOCK_SIZE * 10);
        setTranslateY(BLOCK_SIZE * 12 - 5);
    }

    private void velocityJump() {
        velocityInterval++;
        if (velocityInterval > 100) {
            if (Game.booker.getTranslateX() < getTranslateX())
                velocity = velocity.add(-JUMP_SPEED, 0);
            else
                velocity = velocity.add(JUMP_SPEED, 0);

            switch ((int) (Math.random() * 3)) {
                case 0 -> Sounds.bossHit.play(Game.menu.getVoiceSlider().getValue() / 100);
                case 1 -> Sounds.bossHit2.play(Game.menu.getFxSlider().getValue() / 100);
                case 2 -> Sounds.bossHit3.play(Game.menu.getFxSlider().getValue() / 100);
            }
            velocityInterval = 0;
        }
    }

    private void moveX(double x) {
        animation.play();
        for (int i = 0; i < Math.abs(x); i++) {
            if (x > 0 && getTranslateX() < BLOCK_SIZE * 37) {
                setScaleX(1);
                setTranslateX(getTranslateX() + 1);
            } else if (getTranslateX() > 1) {
                setTranslateX(getTranslateX() - 1);
                setScaleX(-1);
            }

            if (getBoundsInParent().intersects(Game.booker.getBoundsInParent())) {
                closeCombat();
                return;
            }
        }
    }

    private void checkOnLevelChange() {
        stopAnimation();
        if (stunInterval == 0) {
            Sounds.ohBooker.setVolume(Game.menu.getVoiceSlider().getValue() / 100);
            Sounds.ohBooker.play();

            Sounds.ohBooker.setOnEndOfMedia(() -> {
                Sounds.fuck.play(Game.menu.getVoiceSlider().getValue() / 100);
            });
        }

        stunInterval++;
        if (stunInterval < 50)
            imageView.setViewport(new Rectangle2D(WIDTH * 2, 0, WIDTH, HEIGHT));

        if (stunInterval > 75) {
            Sounds.bossTromp.play(Game.menu.getFxSlider().getValue() / 100);
            imageView.setViewport(new Rectangle2D(0, 0, WIDTH, HEIGHT));
        }

        if (stunInterval > 100)
            changeLevel();
    }

    public void update() {
        if (HP < 1) {
            if (!dead)
                die();
            return;
        }

        if (Game.levelNumber == Level.THIRD_LEVEL && Game.booker.getTranslateX() > BLOCK_SIZE * 285)
            checkOnLevelChange();

        rectHP.setWidth((double) HP / 10);
        if (!hypnotized && Game.levelNumber == Level.BOSS_LEVEL)
            behave();
        else
            stopAnimation();
    }
}
