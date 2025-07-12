package com.abhai.deadshock.characters.enemies;

import com.abhai.deadshock.Game;
import com.abhai.deadshock.characters.Animatable;
import com.abhai.deadshock.levels.Block;
import com.abhai.deadshock.levels.BlockType;
import com.abhai.deadshock.levels.Level;
import com.abhai.deadshock.utils.Sounds;
import com.abhai.deadshock.utils.SpriteAnimation;
import com.abhai.deadshock.weapons.ComstockWeapon;
import com.abhai.deadshock.weapons.EnemyWeapon;
import javafx.geometry.Point2D;
import javafx.util.Duration;

public class Comstock extends Enemy implements Animatable {
    private static final int SPEED = 3;
    protected static final double JUMP_SPEED = -25;
    protected static final int COUNT_OF_SPRITES = 10;
    protected static final double ANIMATION_SPEED = 0.5;

    private int seeInterval = 0;

    private boolean canSeeBooker = false;
    private boolean booleanVoice = true;
    private boolean booleanVelocity = true;
    private boolean canJump = true;

    protected SpriteAnimation animation;
    protected EnemyWeapon enemyWeapon;

    Comstock() {
        animation = new SpriteAnimation(imageView, Duration.seconds(ANIMATION_SPEED),
                COUNT_OF_SPRITES, COUNT_OF_SPRITES, 0, 0, WIDTH, HEIGHT);
    }

    public Comstock(long x, long y) {
        this();

        HP = 100;
        type = EnemyType.COMSTOCK;
        enemyWeapon = new ComstockWeapon();

        setTranslateX(x);
        setTranslateY(y);

        getChildren().add(imageView);
        Game.gameRoot.getChildren().add(this);
    }

    protected boolean intersectsWithBlock(Block block) {
        return getBoundsInParent().intersects(block.getBoundsInParent()) && !block.getType().equals(BlockType.INVISIBLE);
    }

    @Override
    protected String getImageName() {
        return "comstock.png";
    }

    @Override
    public void stopAnimation() {
        animation.stop();
    }

    private boolean intersectsWithBooker(char coordinate) {
        if (getBoundsInParent().intersects(Game.booker.getBoundsInParent())) {
            if (coordinate == 'Y') {
                if (velocity.getY() > 0) {
                    setTranslateY(getTranslateY() - 1);
                    if (booleanVelocity) {
                        Game.booker.setHP(Game.booker.getHP() - Game.booker.getEnemyDogfight());
                        Sounds.closeCombat.play(Game.menu.fxSlider.getValue() / 100);
                        velocity = velocity.add(getScaleX() * 5, JUMP_SPEED);
                        booleanVelocity = false;
                    }
                } else {
                    setTranslateY(getTranslateY() + 1);
                    if (Game.booker.isBooleanVelocityY()) {
                        HP -= Game.booker.getCharacterDogFight();
                        Sounds.closeCombat.play(Game.menu.fxSlider.getValue() / 100);
                        Game.booker.velocity = Game.booker.velocity.add(0, JUMP_SPEED);
                        Game.booker.setBooleanVelocityY(false);
                    }
                }
            } else {
                setTranslateX(getTranslateX() - getScaleX());
                if (Game.booker.isBooleanVelocityX()) {
                    Game.booker.setHP(Game.booker.getHP() - Game.booker.getEnemyDogfight());
                    Sounds.closeCombat.play(Game.menu.fxSlider.getValue() / 100);
                    Game.booker.velocity = Game.booker.velocity.add(getScaleX() * GRAVITY * 2, 0);
                    Game.booker.setBooleanVelocityX(false);
                }
            }
            return true;
        }
        return false;
    }

    private boolean intersectsWithEnemies(char coordinate) {
        for (Enemy enemy : Game.enemies) {
            if (this != enemy) {
                if (getBoundsInParent().intersects(enemy.getBoundsInParent())) {
                    if (coordinate == 'Y') {
                        if (Game.difficultyLevelText.equals("marik") || Game.difficultyLevelText.equals("easy"))
                            HP -= Game.booker.getCharacterDogFight();
                        if (velocity.getY() > 0) {
                            setTranslateY(getTranslateY() - 1);
                            if (booleanVelocity) {
                                velocity = velocity.add(getScaleX() * 5, JUMP_SPEED);
                                booleanVelocity = false;
                            }
                        } else {
                            setTranslateY(getTranslateY() + 1);
                        }
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

    private boolean intersectsWithBlocks(char coordinate) {
        for (Block block : Game.blocks) {
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

    private void moveY() {
        if (velocity.getY() < GRAVITY) {
            velocity = velocity.add(0, ANIMATION_SPEED);
        } else {
            booleanVelocity = true;
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

    private void moveX(int x) {
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

    //TODO separate supply logic from enemies
    /*private void createSupply(int randSupply) {
        if (randSupply == 0)
            Game.elizabeth.countMedicine++;
        else {
            switch ((int) (Math.random() * 2)) {
                case 0:
                    Sounds.audioClipAmmo.play(Game.menu.voiceSlider.getValue() / 100);
                case 1:
                    Sounds.audioClipAmmo2.play(Game.menu.voiceSlider.getValue() / 100);
            }
            if (Game.elizabeth.getAmmo() != null)
                Game.gameRoot.getChildren().remove(Game.elizabeth.getAmmo());
            Game.elizabeth.setAmmo(new Supply(Game.elizabeth.getTranslateX(), Game.elizabeth.getTranslateY(), type));
        }

        Game.gameRoot.getChildren().remove(this);
        toDelete = true;
    }

    private void pickUpSupply() {
        boolean pickUpSupply = true;
        if (pickUpSupply) {
            if (Game.booker.getBoundsInParent().intersects(getBoundsInParent())) {
                if (supply.getSupply().equals("medicine")) {
                    Sounds.feelsBetter.setVolume(Game.menu.voiceSlider.getValue() / 100);
                    Sounds.feelsBetter.play();
                    for (int count = 0; count < Game.booker.getMedicineCount(); count++)
                        if (Game.booker.getHP() < 100)
                            Game.booker.setHP(Game.booker.getHP() + 1);
                        else
                            break;
                } else {
                    Sounds.great.play(Game.menu.voiceSlider.getValue() / 100);
                    Game.weapon.setBullets(Game.weapon.getBullets() + Game.booker.getBulletCount());
                }
                Game.gameRoot.getChildren().remove(this);
                pickUpSupply = false;
                toDelete = true;
            }

            if (Game.difficultyLevelText.equals("high") || Game.difficultyLevelText.equals("hardcore"))
                for (Enemy enemy : Game.enemies)
                    if (enemy != this && enemy.getBoundsInParent().intersects(getBoundsInParent())) {
                        if (supply.getSupply().equals("medicine")) {
                            for (int count = 0; count < Game.booker.getMedicineCount(); count++)
                                if (enemy.HP < 100)
                                    enemy.HP++;
                                else
                                    break;
                        }
                        Game.gameRoot.getChildren().remove(this);
                        pickUpSupply = false;
                        toDelete = true;
                    }
        }
    }*/

    private void lookingForBooker() {
        if (Game.booker.getTranslateY() > getTranslateY()) {
            canSeeBooker = true;
            booleanVoice = true;
            playVoiceFoundPlayer();
        }
        moveX(SPEED);
        animation.play();
    }

    private void losingBooker() {
        if (Game.booker.getTranslateY() < getTranslateY() || Game.booker.getTranslateX() == getTranslateX())
            if ((Game.levelNumber == Level.THIRD_LEVEL && Game.booker.getTranslateY()
                    < getTranslateY() - Block.BLOCK_SIZE * 2) || Game.booker.stayingOnLittlePlatform) {
                seeInterval++;
                if (seeInterval > 180) {
                    booleanVoice = true;
                    canSeeBooker = false;
                    seeInterval = 0;
                    playVoiceLostPlayer();
                }
            } else
                seeInterval = 0;
    }

    private void playVoiceLostPlayer() {
        if (booleanVoice) {
            switch ((int) (Math.random() * 6)) {
                case 0 -> Sounds.lostHim.play(Game.menu.voiceSlider.getValue() / 100);
                case 1 -> Sounds.heHasGone3.play(Game.menu.voiceSlider.getValue() / 100);
                case 2 -> Sounds.wereHere.play(Game.menu.voiceSlider.getValue() / 100);
                case 3 -> Sounds.heHasGone.play(Game.menu.voiceSlider.getValue() / 100);
                case 4 -> Sounds.lostHim2.play(Game.menu.voiceSlider.getValue() / 100);
                case 5 -> Sounds.heHasGone2.play(Game.menu.voiceSlider.getValue() / 100);
            }
            booleanVoice = false;
        }
    }

    private void playVoiceFoundPlayer() {
        if (booleanVoice) {
            if (Game.levelNumber == Level.FIRST_LEVEL) {
                switch ((int) (Math.random() * 6)) {
                    case 0 -> Sounds.wontGoAway.play(Game.menu.voiceSlider.getValue() / 100);
                    case 1 -> Sounds.audioClipFire.play(Game.menu.voiceSlider.getValue() / 100);
                    case 2 -> Sounds.heIsHere.play(Game.menu.voiceSlider.getValue() / 100);
                    case 3 -> Sounds.getDownWeapon.play(Game.menu.voiceSlider.getValue() / 100);
                    case 4 -> Sounds.die.play(Game.menu.voiceSlider.getValue() / 100);
                    case 5 -> Sounds.attack.play(Game.menu.voiceSlider.getValue() / 100);
                }
            } else {
                switch ((int) (Math.random() * 2)) {
                    case 0:
                        Sounds.theyAreHere.play(Game.menu.voiceSlider.getValue() / 100);
                    case 1:
                        Sounds.takeThem.play(Game.menu.voiceSlider.getValue() / 100);
                }
            }
            booleanVoice = false;
        }
    }

    private void playEnemyVoice() {
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

    private void die() {
        canSeeBooker = false;
        toDelete = true;
        stopAnimation();
        playDeathVoice();
        Game.booker.setMoney(Game.booker.getMoney() + Game.booker.getMoneyForKillingEnemy());
    }

    private void behave() {
        if (getTranslateY() > Game.scene.getHeight()) {
            toDelete = true;
            return;
        }

        enemyWeapon.update();

        if (canSeeBooker)
            voiceInterval++;
        else
            voiceInterval = 0;

        if (Game.booker.getTranslateX() > getTranslateX() - 720 && Game.booker.getTranslateX() < getTranslateX()) {
            if (canSeeBooker) {
                losingBooker();
                moveX(-SPEED);
                animation.play();
                enemyWeapon.shoot(getScaleX(), getTranslateX(), getTranslateY());
            } else
                lookingForBooker();
        } else if (Game.booker.getTranslateX() < getTranslateX() + 650 && Game.booker.getTranslateX() > getTranslateX()) {
            if (canSeeBooker) {
                losingBooker();
                moveX(SPEED);
                animation.play();
                enemyWeapon.shoot(getScaleX(), getTranslateX(), getTranslateY());
            } else
                lookingForBooker();
        } else if (Game.booker.getTranslateX() == getTranslateX()) {
            animation.stop();
            if (canSeeBooker)
                losingBooker();
            else
                lookingForBooker();
        } else if (canSeeBooker && Game.booker.getTranslateX() != getTranslateX()) {
            canSeeBooker = false;
            booleanVoice = true;
            playVoiceLostPlayer();
        }

        if (voiceInterval > 240)
            playEnemyVoice();
    }

    @Override
    public void update() {
        if (HP < 1) {
            die();
            return;
        }

        moveY();
        if (!hypnotized) {
            behave();
        } else {
            stopAnimation();
        }
    }
}

