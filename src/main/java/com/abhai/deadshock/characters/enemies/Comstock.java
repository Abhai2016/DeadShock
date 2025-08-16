package com.abhai.deadshock.characters.enemies;

import com.abhai.deadshock.Game;
import com.abhai.deadshock.characters.Animatable;
import com.abhai.deadshock.levels.Block;
import com.abhai.deadshock.levels.BlockType;
import com.abhai.deadshock.utils.Sounds;
import com.abhai.deadshock.utils.SpriteAnimation;
import com.abhai.deadshock.weapons.ComstockWeapon;
import com.abhai.deadshock.weapons.EnemyWeapon;
import javafx.geometry.Point2D;
import javafx.util.Duration;

import java.util.function.BooleanSupplier;

public class Comstock extends Enemy implements Animatable {
    private static final int SPEED = 3;
    private static final int VOICE_INTERVAL = 500;

    protected static final int SPRITES_COUNT = 10;
    protected static final double JUMP_SPEED = -25;
    protected static final double ANIMATION_SPEED = 0.5;

    private int seeInterval;
    private boolean canJump;
    private boolean canSeeBooker;
    private boolean booleanVoice;
    private boolean booleanVelocity;

    private final BooleanSupplier onTheLeft;
    private final BooleanSupplier onTheRight;

    protected EnemyWeapon enemyWeapon;
    protected SpriteAnimation animation;

    public Comstock() {
        HP = 100;
        initWeapon();
        canJump = true;
        seeInterval = 0;
        booleanVoice = true;
        canSeeBooker = false;
        booleanVelocity = true;
        type = EnemyType.COMSTOCK;

        animation = new SpriteAnimation(imageView, Duration.seconds(ANIMATION_SPEED),
                SPRITES_COUNT, SPRITES_COUNT, 0, 0, WIDTH, HEIGHT);
        onTheLeft = () ->
                Game.booker.getTranslateX() > getTranslateX() - 720 && Game.booker.getTranslateX() < getTranslateX();
        onTheRight = () ->
                Game.booker.getTranslateX() < getTranslateX() + 650 && Game.booker.getTranslateX() > getTranslateX();
    }

    private void die() {
        toDelete = true;
        stopAnimation();
        playDeathVoice();
        Game.booker.addMoneyForKillingEnemy();
    }

    @Override
    public void reset() {
        super.reset();

        HP = 100;
        canJump = true;
        seeInterval = 0;
        booleanVoice = true;
        canSeeBooker = false;
        booleanVelocity = true;
    }

    private void moveY() {
        if (velocity.getY() < GRAVITY) {
            velocity = velocity.add(0, ANIMATION_SPEED);
        } else {
            booleanVelocity = true;
        }

        if (getTranslateY() > Game.scene.getHeight()) {
            toDelete = true;
            return;
        }

        for (int i = 0; i < Math.abs(velocity.getY()); i++) {
            if (velocity.getY() > 0)
                setTranslateY(getTranslateY() + 1);
            else
                setTranslateY(getTranslateY() - 1);

            if (intersectsWithEnemies('Y'))
                return;
            if (intersectsWithBlocks('Y'))
                return;
            if (intersectsWithBooker('Y'))
                return;
        }
    }

    private void behave() {
        if (canSeeBooker)
            voiceInterval++;

        if (voiceInterval > VOICE_INTERVAL)
            playAttackVoice();

        if (getTranslateX() > Game.booker.getTranslateX())
            canSeeBooker(onTheLeft);
        if (getTranslateX() < Game.booker.getTranslateX())
            canSeeBooker(onTheRight);
        if (getTranslateX() == Game.booker.getTranslateX())
            cannotSeeBooker();
    }

    private void moveX(int x) {
        animation.play();
        for (int i = 0; i < Math.abs(x); i++) {
            if (x > 0 || getTranslateX() == Game.booker.getTranslateX()) {
                setTranslateX(getTranslateX() + 1);
                setScaleX(1);
            } else {
                setTranslateX(getTranslateX() - 1);
                setScaleX(-1);
            }

            if (intersectsWithEnemies('X'))
                return;
            if (intersectsWithBlocks('X'))
                return;
            if (intersectsWithBooker('X'))
                return;
        }
    }

    private void losingBooker() {
        seeInterval++;
        if (seeInterval > VOICE_INTERVAL) {
            seeInterval = 0;
            booleanVoice = true;
            canSeeBooker = false;
            playVoiceLostBooker();
        }
    }

    private void playAttackVoice() {
        switch ((int) (Math.random() * 10)) {
            case 0 -> Sounds.canYouShoot.play(Game.menu.voiceSlider.getValue() / 100);
            case 1 -> Sounds.dieAlready.play(Game.menu.voiceSlider.getValue() / 100);
            case 2 -> Sounds.dontSpareBullets.play(Game.menu.voiceSlider.getValue() / 100);
            case 3 -> Sounds.keepShooting2.play(Game.menu.voiceSlider.getValue() / 100);
            case 4 -> Sounds.killMe.play(Game.menu.voiceSlider.getValue() / 100);
            case 5 -> Sounds.allYouCan.play(Game.menu.voiceSlider.getValue() / 100);
            case 6 -> Sounds.whoAreYou.play(Game.menu.voiceSlider.getValue() / 100);
            case 7 -> Sounds.stupid.play(Game.menu.voiceSlider.getValue() / 100);
            case 8 -> Sounds.giveHimBullets.play(Game.menu.voiceSlider.getValue() / 100);
            case 9 -> Sounds.keepShooting.play(Game.menu.voiceSlider.getValue() / 100);
        }
        voiceInterval = 0;
    }

    private void cannotSeeBooker() {
        stopAnimation();

        if (canSeeBooker)
            losingBooker();
        else
            moveX(SPEED);
    }

    private void playVoiceLostBooker() {
        if (booleanVoice) {
            switch ((int) (Math.random() * 6)) {
                case 0 -> Sounds.heHasGone.play(Game.menu.voiceSlider.getValue() / 100);
                case 1 -> Sounds.heHasGone2.play(Game.menu.voiceSlider.getValue() / 100);
                case 2 -> Sounds.heHasGone3.play(Game.menu.voiceSlider.getValue() / 100);
                case 3 -> Sounds.lostHim.play(Game.menu.voiceSlider.getValue() / 100);
                case 4 -> Sounds.lostHim2.play(Game.menu.voiceSlider.getValue() / 100);
                case 5 -> Sounds.wereHere.play(Game.menu.voiceSlider.getValue() / 100);
            }
            booleanVoice = false;
        }
    }

    private void playVoiceFoundBooker() {
        if (booleanVoice) {
            switch ((int) (Math.random() * 8)) {
                case 0 -> Sounds.wontGoAway.play(Game.menu.voiceSlider.getValue() / 100);
                case 1 -> Sounds.audioClipFire.play(Game.menu.voiceSlider.getValue() / 100);
                case 2 -> Sounds.heIsHere.play(Game.menu.voiceSlider.getValue() / 100);
                case 3 -> Sounds.getDownWeapon.play(Game.menu.voiceSlider.getValue() / 100);
                case 4 -> Sounds.die.play(Game.menu.voiceSlider.getValue() / 100);
                case 5 -> Sounds.attack.play(Game.menu.voiceSlider.getValue() / 100);
                case 6 -> Sounds.theyAreHere.play(Game.menu.voiceSlider.getValue() / 100);
                case 7 -> Sounds.takeThem.play(Game.menu.voiceSlider.getValue() / 100);
            }
        }
        booleanVoice = false;
    }

    @Override
    public void stopAnimation() {
        animation.stop();
    }

    protected boolean intersectsWithBlock(Block block) {
        return getBoundsInParent().intersects(block.getBoundsInParent()) && !block.getType().equals(BlockType.INVISIBLE);
    }

    @Override
    protected String getImageName() {
        return "comstock.png";
    }

    private boolean intersectsWithBlocks(char coordinate) {
        for (Block block : Game.level.getBlocks()) {
            if (intersectsWithBlock(block)) {
                if (coordinate == 'Y') {
                    if (velocity.getY() > 0) {
                        setTranslateY(getTranslateY() - 1);
                        canJump = true;
                    } else {
                        setTranslateY(getTranslateY() + 1);
                        velocity = new Point2D(0, GRAVITY);
                    }
                } else {
                    setTranslateX(getTranslateX() - getScaleX());
                    if (canJump) {
                        velocity = velocity.add(0, JUMP_SPEED);
                        canJump = false;
                    }
                }
                return true;
            }
        }
        return false;
    }

    private boolean intersectsWithBooker(char coordinate) {
        if (getBoundsInParent().intersects(Game.booker.getBoundsInParent())) {
            if (coordinate == 'Y') {
                setTranslateY(getTranslateY() - 1);
                if (booleanVelocity) {
                    Sounds.closeCombat.play(Game.menu.fxSlider.getValue() / 100);
                    velocity = velocity.add(getScaleX() * 5, JUMP_SPEED);
                    booleanVelocity = false;
                }
            } else
                closeCombat();
            return true;
        }
        return false;
    }

    private boolean intersectsWithEnemies(char coordinate) {
        for (Enemy enemy : Game.enemies) {
            if (this != enemy) {
                if (getBoundsInParent().intersects(enemy.getBoundsInParent())) {
                    if (coordinate == 'Y') {
                        if (velocity.getY() > 0) {
                            setTranslateY(getTranslateY() - 1);
                            if (booleanVelocity) {
                                velocity = velocity.add(getScaleX() * 5, JUMP_SPEED);
                                booleanVelocity = false;
                            }
                        } else
                            setTranslateY(getTranslateY() + 1);
                    } else {
                        setTranslateX(getTranslateX() - getScaleX());
                        stopAnimation();
                    }
                    return true;
                }
            }
        }
        return false;
    }

    private void canSeeBooker(BooleanSupplier booleanSupplier) {
        if (!canSeeBooker) {
            if (booleanSupplier.getAsBoolean()) {
                moveX(SPEED);
                if (Game.booker.getTranslateY() >= getTranslateY()) {
                    canSeeBooker = true;
                    playVoiceFoundBooker();
                }
            }
        } else {
            if (getTranslateY() <= Game.booker.getTranslateY()) {
                enemyWeapon.shoot(getScaleX(), getTranslateX(), getTranslateY());
            } else
                losingBooker();

            if (booleanSupplier.equals(onTheLeft))
                moveX(-SPEED);
            else
                moveX(SPEED);
        }
    }

    protected void initWeapon() {
        enemyWeapon = new ComstockWeapon();
    }

    public void setCanSeeBooker(boolean canSeeBooker) {
        this.canSeeBooker = canSeeBooker;
    }

    @Override
    public void update() {
        if (HP < 1) {
            die();
            return;
        }

        moveY();
        if (!hypnotized)
            behave();
        else
            stopAnimation();
    }
}

