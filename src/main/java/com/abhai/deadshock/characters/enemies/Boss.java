package com.abhai.deadshock.characters.enemies;

import com.abhai.deadshock.Game;
import com.abhai.deadshock.characters.Animatable;
import com.abhai.deadshock.energetics.EnemyHypnosis;
import com.abhai.deadshock.types.EnemyType;
import com.abhai.deadshock.utils.GameMedia;
import com.abhai.deadshock.utils.SpriteAnimation;
import com.abhai.deadshock.utils.Texts;
import com.abhai.deadshock.world.levels.Level;
import javafx.geometry.Rectangle2D;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.transform.Rotate;
import javafx.util.Duration;

import static com.abhai.deadshock.world.levels.Block.BLOCK_SIZE;

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
        setScaleX(-1);
        stunInterval = 0;
        velocityInterval = 0;
        type = EnemyType.BOSS;

        hypnosis = new EnemyHypnosis();
        name = new Text(Texts.BOSS_NAME);
        rectHP = new Rectangle(500, 5, Color.RED);
        imageView.setViewport(new Rectangle2D(0, 0, WIDTH, HEIGHT));
        animation = new SpriteAnimation(imageView, Duration.seconds(ANIMATION_SPEED),
                COUNT_OF_SPRITES, COUNT_OF_SPRITES, 0, 0, WIDTH, HEIGHT);
    }

    @Override
    protected void die() {
        dead = true;
        stopAnimation();
        rectHP.setWidth(0);
        getTransforms().add(new Rotate(-90));
        setTranslateY(getTranslateY() + HEIGHT);

        GameMedia.BOSS_DEATH.setVolume(Game.getGameWorld().getMenu().getFxSlider().getValue() / 100);
        GameMedia.BOSS_DEATH.setOnEndOfMedia(() -> Game.getGameWorld().playCutscene());
        GameMedia.BOSS_DEATH.play();
    }

    @Override
    public void init(int x, int y) {
        super.init(x, y);

        HP = 5000;
        dead = false;
        setScaleX(-1);
        stunInterval = 0;
        velocityInterval = 0;
        imageView.setViewport(new Rectangle2D(0, 0, WIDTH, HEIGHT));

        if (Game.getGameWorld().getLevel().getCurrentLevelNumber() == Level.BOSS_LEVEL)
            initHpUi();
    }

    @Override
    public void stopAnimation() {
        animation.stop();
    }

    @Override
    protected String getImageName() {
        return "bigDaddy.png";
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

    private void behave() {
        if (stunInterval > 700) {
            stun();
            return;
        }

        if (velocity.getX() < 0)
            velocity = velocity.add(ANIMATION_SPEED, 0);
        else if (velocity.getX() > 0)
            velocity = velocity.add(-ANIMATION_SPEED, 0);

        if (Game.getGameWorld().getBooker().getTranslateX() < getTranslateX())
            moveX(-SPEED);
        if (Game.getGameWorld().getBooker().getTranslateX() > getTranslateX())
            moveX(SPEED);
        if (Game.getGameWorld().getBooker().getTranslateX() == getTranslateX())
            stopAnimation();

        moveX(velocity.getX());

        if (!Game.getGameWorld().getBooker().isHypnotized() && Game.getGameWorld().getBooker().getTranslateY() < getTranslateY())
            stunInterval++;
        else
            velocityJump();

        hypnosis.update();
    }

    public void initHpUi() {
        name.setFont(Font.font("Aria", 28));
        name.setFill(Color.WHITE);
        name.setTranslateX(Game.SCENE_WIDTH / 2 - 95);
        name.setTranslateY(30);
        Game.getGameWorld().getAppRoot().getChildren().add(name);

        rectHP.setTranslateX(Game.SCENE_WIDTH / 2 - 240);
        rectHP.setTranslateY(40);
        Game.getGameWorld().getAppRoot().getChildren().add(rectHP);
    }

    private void velocityJump() {
        velocityInterval++;
        if (velocityInterval > 100) {
            if (Game.getGameWorld().getBooker().getTranslateX() < getTranslateX())
                velocity = velocity.add(-JUMP_SPEED, 0);
            else
                velocity = velocity.add(JUMP_SPEED, 0);

            switch ((int) (Math.random() * 3)) {
                case 0 -> GameMedia.BOSS_HIT.play(Game.getGameWorld().getMenu().getVoiceSlider().getValue() / 100);
                case 1 -> GameMedia.BOSS_HIT_2.play(Game.getGameWorld().getMenu().getFxSlider().getValue() / 100);
                case 2 -> GameMedia.BOSS_HIT_3.play(Game.getGameWorld().getMenu().getFxSlider().getValue() / 100);
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

            if (getBoundsInParent().intersects(Game.getGameWorld().getBooker().getBoundsInParent())) {
                closeCombat();
                return;
            }
        }
    }

    public void checkOnLevelChange() {
        stopAnimation();
        if (stunInterval == 0) {
            GameMedia.OH_BOOKER.setOnEndOfMedia(() -> GameMedia.FUCK.play(Game.getGameWorld().getMenu().getVoiceSlider().getValue() / 100));
            GameMedia.OH_BOOKER.setVolume(Game.getGameWorld().getMenu().getVoiceSlider().getValue() / 100);
            GameMedia.OH_BOOKER.play();
        }

        stunInterval++;
        if (stunInterval < 50)
            imageView.setViewport(new Rectangle2D(WIDTH * 2, 0, WIDTH, HEIGHT));

        if (stunInterval > 75) {
            GameMedia.BOSS_TROMP.play(Game.getGameWorld().getMenu().getFxSlider().getValue() / 100);
            imageView.setViewport(new Rectangle2D(0, 0, WIDTH, HEIGHT));
        }

        if (stunInterval > 100) {
            initHpUi();
            stunInterval = 0;
            setTranslateX(BLOCK_SIZE * 10);
            setTranslateY(BLOCK_SIZE * 12 - 5);
            Game.getGameWorld().setBossLevel();
        }
    }

    public void deleteHpUi() {
        Game.getGameWorld().getAppRoot().getChildren().removeAll(rectHP, name);
    }

    @Override
    public void update() {
        if (HP < 1) {
            if (!dead)
                die();
            return;
        }

        rectHP.setWidth(HP / 10);
        if (Game.getGameWorld().getLevel().getCurrentLevelNumber() == Level.THIRD_LEVEL
                && Game.getGameWorld().getBooker().getTranslateX() > BLOCK_SIZE * 285)
            checkOnLevelChange();

        if (!hypnotized && Game.getGameWorld().getLevel().getCurrentLevelNumber() == Level.BOSS_LEVEL)
            behave();
        else
            stopAnimation();
    }
}
