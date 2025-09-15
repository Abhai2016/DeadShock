package com.abhai.deadshock.characters.enemies;

import com.abhai.deadshock.Game;
import com.abhai.deadshock.characters.Animatable;
import com.abhai.deadshock.types.BlockType;
import com.abhai.deadshock.types.EnemyType;
import com.abhai.deadshock.types.SupplyType;
import com.abhai.deadshock.types.WeaponType;
import com.abhai.deadshock.utils.GameMedia;
import com.abhai.deadshock.utils.SpriteAnimation;
import com.abhai.deadshock.weapons.EnemyWeapon;
import com.abhai.deadshock.world.levels.Block;
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
        onTheLeft = () -> Game.getGameWorld().getBooker().getTranslateX() > getTranslateX() - 720
                && Game.getGameWorld().getBooker().getTranslateX() < getTranslateX();
        onTheRight = () -> Game.getGameWorld().getBooker().getTranslateX() < getTranslateX() + 650
                && Game.getGameWorld().getBooker().getTranslateX() > getTranslateX();
    }

    @Override
    public void init(int x, int y) {
        super.init(x, y);

        HP = 100;
        canJump = true;
        seeInterval = 0;
        booleanVoice = true;
        canSeeBooker = false;
        booleanVelocity = true;
    }

    @Override
    public void stopAnimation() {
        animation.stop();
    }

    @Override
    protected String getImageName() {
        return "comstock.png";
    }

    @Override
    protected SupplyType getSupplyType() {
        return SupplyType.PISTOL_BULLETS;
    }

    private void die() {
        toDelete = true;
        stopAnimation();
        playDeathVoice();
        if (Math.random() < 0.75)
            Game.getGameWorld().createSupply(getSupplyType(), getTranslateX(), getTranslateY());
        Game.getGameWorld().getBooker().addMoneyForKillingEnemy();
    }

    private void moveY() {
        if (velocity.getY() < GRAVITY)
            velocity = velocity.add(0, ANIMATION_SPEED);
        else
            booleanVelocity = true;

        if (getTranslateY() > Game.SCENE_HEIGHT) {
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

        if (getTranslateX() > Game.getGameWorld().getBooker().getTranslateX())
            canSeeBooker(onTheLeft);
        if (getTranslateX() < Game.getGameWorld().getBooker().getTranslateX())
            canSeeBooker(onTheRight);
        if (getTranslateX() == Game.getGameWorld().getBooker().getTranslateX())
            cannotSeeBooker();
    }

    private void moveX(int x) {
        animation.play();
        for (int i = 0; i < Math.abs(x); i++) {
            if (x > 0 || getTranslateX() == Game.getGameWorld().getBooker().getTranslateX()) {
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
            case 0 -> GameMedia.CAN_YOU_SHOOT.play(Game.getGameWorld().getMenu().getVoiceSlider().getValue() / 100);
            case 1 -> GameMedia.DIE_ALREADY.play(Game.getGameWorld().getMenu().getVoiceSlider().getValue() / 100);
            case 2 -> GameMedia.DONT_SPARE_BULLETS.play(Game.getGameWorld().getMenu().getVoiceSlider().getValue() / 100);
            case 3 -> GameMedia.KEEP_SHOOTING_2.play(Game.getGameWorld().getMenu().getVoiceSlider().getValue() / 100);
            case 4 -> GameMedia.KILL_ME.play(Game.getGameWorld().getMenu().getVoiceSlider().getValue() / 100);
            case 5 -> GameMedia.ALL_YOU_CAN.play(Game.getGameWorld().getMenu().getVoiceSlider().getValue() / 100);
            case 6 -> GameMedia.WHO_ARE_YOU.play(Game.getGameWorld().getMenu().getVoiceSlider().getValue() / 100);
            case 7 -> GameMedia.STUPID.play(Game.getGameWorld().getMenu().getVoiceSlider().getValue() / 100);
            case 8 -> GameMedia.GIVE_HIM_BULLETS.play(Game.getGameWorld().getMenu().getVoiceSlider().getValue() / 100);
            case 9 -> GameMedia.KEEP_SHOOTING.play(Game.getGameWorld().getMenu().getVoiceSlider().getValue() / 100);
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
                case 0 -> GameMedia.HE_HAS_GONE.play(Game.getGameWorld().getMenu().getVoiceSlider().getValue() / 100);
                case 1 -> GameMedia.HE_HAS_GONE_2.play(Game.getGameWorld().getMenu().getVoiceSlider().getValue() / 100);
                case 2 -> GameMedia.HE_HAS_GONE_3.play(Game.getGameWorld().getMenu().getVoiceSlider().getValue() / 100);
                case 3 -> GameMedia.LOST_HIM.play(Game.getGameWorld().getMenu().getVoiceSlider().getValue() / 100);
                case 4 -> GameMedia.LOST_HIM_2.play(Game.getGameWorld().getMenu().getVoiceSlider().getValue() / 100);
                case 5 -> GameMedia.WERE_HERE.play(Game.getGameWorld().getMenu().getVoiceSlider().getValue() / 100);
            }
            booleanVoice = false;
        }
    }

    private void playVoiceFoundBooker() {
        if (booleanVoice) {
            switch ((int) (Math.random() * 8)) {
                case 0 -> GameMedia.WONT_GO_AWAY.play(Game.getGameWorld().getMenu().getVoiceSlider().getValue() / 100);
                case 1 ->
                        GameMedia.AUDIO_CLIP_FIRE.play(Game.getGameWorld().getMenu().getVoiceSlider().getValue() / 100);
                case 2 -> GameMedia.HE_IS_HERE.play(Game.getGameWorld().getMenu().getVoiceSlider().getValue() / 100);
                case 3 ->
                        GameMedia.GET_DOWN_WEAPON.play(Game.getGameWorld().getMenu().getVoiceSlider().getValue() / 100);
                case 4 -> GameMedia.DIE.play(Game.getGameWorld().getMenu().getVoiceSlider().getValue() / 100);
                case 5 -> GameMedia.ATTACK.play(Game.getGameWorld().getMenu().getVoiceSlider().getValue() / 100);
                case 6 -> GameMedia.THEY_ARE_HERE.play(Game.getGameWorld().getMenu().getVoiceSlider().getValue() / 100);
                case 7 -> GameMedia.TAKE_THEM.play(Game.getGameWorld().getMenu().getVoiceSlider().getValue() / 100);
            }
        }
        booleanVoice = false;
    }

    protected boolean intersectsWithBlock(Block block) {
        return getBoundsInParent().intersects(block.getBoundsInParent()) && !block.getType().equals(BlockType.INVISIBLE);
    }

    private boolean intersectsWithBlocks(char coordinate) {
        for (Block block : Game.getGameWorld().getLevel().getBlocks()) {
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
        if (getBoundsInParent().intersects(Game.getGameWorld().getBooker().getBoundsInParent())) {
            if (coordinate == 'Y') {
                if (velocity.getY() > 0) {
                    setTranslateY(getTranslateY() - 1);
                    if (booleanVelocity) {
                        Game.getGameWorld().getBooker().setHp(Game.getGameWorld().getBooker().getHp() - Game.getGameWorld().getBooker().getCloseCombatDamageFromEnemies());
                        GameMedia.CLOSE_COMBAT.play(Game.getGameWorld().getMenu().getFxSlider().getValue() / 100);
                        velocity = velocity.add(getScaleX() * 5, JUMP_SPEED);
                        booleanVelocity = false;
                    }
                } else
                    setTranslateY(getTranslateY() + 1);
            } else
                closeCombat();
            return true;
        }
        return false;
    }

    private boolean intersectsWithEnemies(char coordinate) {
        for (Enemy enemy : Game.getGameWorld().getEnemies()) {
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
                if (Game.getGameWorld().getBooker().getTranslateY() >= getTranslateY()) {
                    canSeeBooker = true;
                    playVoiceFoundBooker();
                }
            }
        } else {
            if (getTranslateY() <= Game.getGameWorld().getBooker().getTranslateY())
                enemyWeapon.shoot(getScaleX(), getTranslateX(), getTranslateY());
            else
                losingBooker();

            if (booleanSupplier.equals(onTheLeft))
                moveX(-SPEED);
            else
                moveX(SPEED);
        }
    }

    protected void initWeapon() {
        enemyWeapon = new EnemyWeapon(WeaponType.PISTOL);
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

