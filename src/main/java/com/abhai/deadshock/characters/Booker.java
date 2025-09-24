package com.abhai.deadshock.characters;

import com.abhai.deadshock.Game;
import com.abhai.deadshock.characters.enemies.Boss;
import com.abhai.deadshock.characters.enemies.Enemy;
import com.abhai.deadshock.energetics.Energetic;
import com.abhai.deadshock.types.*;
import com.abhai.deadshock.utils.GameMedia;
import com.abhai.deadshock.utils.SpriteAnimation;
import com.abhai.deadshock.utils.Texts;
import com.abhai.deadshock.weapons.Weapon;
import com.abhai.deadshock.world.levels.Block;
import com.abhai.deadshock.world.levels.Level;
import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.util.Duration;

public class Booker extends Character implements Animatable {
    public static final int WIDTH = 60;

    private static final int GRAVITY = 8;
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

    private boolean dead;
    private boolean start;
    private boolean canJump;
    private boolean gameOver;
    private boolean hypnotized;
    private boolean canPlayVoice;
    private boolean booleanVelocityX;
    private boolean booleanVelocityY;

    private int Hp;
    private int salt;
    private int money;
    private int fullLivesCount;
    private int currentLivesCount;
    private int priceForGeneration;
    private int moneyForKillingEnemy;
    private int supplyForKillingEnemy;
    private int closeCombatDamageToEnemies;
    private int closeCombatDamageFromEnemies;

    private Weapon weapon;
    private Point2D velocity;
    private Energetic energetic;
    private final MediaView videoView;
    private SpriteAnimation animation;

    private Text moneyText;
    private Text deathText;
    private Text continueText;

    public Booker() {
        dead = false;
        start = true;
        canJump = true;
        gameOver = false;
        hypnotized = false;
        canPlayVoice = true;
        booleanVelocityX = true;
        booleanVelocityY = true;
        videoView = new MediaView();
        velocity = new Point2D(0, 0);
        imageView.setViewport(new Rectangle2D(0, 0, WIDTH, HEIGHT));
        animation = new SpriteAnimation(imageView, Duration.seconds(ANIMATION_SPEED), SPRITES_COUNT, SPRITES_COUNT, 0, NO_GUN_OFFSET_Y, WIDTH, HEIGHT);

        Hp = 100;
        money = 0;
        salt = 100;
        currentLivesCount = 0;
        priceForGeneration = 0;
        moneyForKillingEnemy = 0;
        supplyForKillingEnemy = 0;
        closeCombatDamageToEnemies = 0;

        initDeathText();
        Game.getGameWorld().getGameRoot().getChildren().add(this);
    }

    @Override
    public void stopAnimation() {
        animation.stop();
    }

    @Override
    protected String getImageName() {
        return "booker.png";
    }

    private void die() {
        dead = true;
        if (Game.getGameWorld().getDifficultyType().equals(DifficultyType.HARDCORE))
            gameOver();
        else {
            Game.getGameWorld().deathPause();
            if (money < priceForGeneration)
                gameOver();
            else {
                currentLivesCount--;
                money -= priceForGeneration;
                Game.getGameWorld().getHud().updateMoneyTextPosition();

                if (Game.getGameWorld().getLevel().getCurrentLevelNumber() > Level.FIRST_LEVEL)
                    playVideoDeath();
                else {
                    Game.getGameWorld().getAppRoot().getChildren().add(deathText);
                    if (start)
                        Game.getGameWorld().getAppRoot().getChildren().add(moneyText);
                    if (currentLivesCount < 0) {
                        continueText.setText(Texts.PUSH_ENTER_TO_START_LEVEL_AGAIN);
                        continueText.setTranslateX(Game.SCENE_WIDTH / 4);
                    } else {
                        continueText.setText(Texts.PUSH_ENTER_TO_CONTINUE);
                        continueText.setTranslateX(Game.SCENE_WIDTH / 3);
                    }
                    Game.getGameWorld().getAppRoot().getChildren().add(continueText);
                }
            }
        }
    }

    public void clear() {
        stopAnimation();
        energetic.clear();
        weapon.clearBullets();
    }

    public void reset() {
        dead = false;
        start = true;
        canJump = true;
        weapon.reset();
        energetic.reset();
        hypnotized = false;
        canPlayVoice = true;
        booleanVelocityX = true;
        booleanVelocityY = true;
        velocity = new Point2D(0, 0);
        animation.setOffsetY(NO_GUN_OFFSET_Y);
        imageView.setViewport(new Rectangle2D(0, 0, WIDTH, HEIGHT));

        if (GameMedia.WHERE_ARE_YOU_FROM.getStatus() == MediaPlayer.Status.PLAYING)
            GameMedia.WHERE_ARE_YOU_FROM.stop();

        Hp = 100;
        salt = 100;
        setTranslateX(100);
        setTranslateY(500);
    }

    private void behave() {
        weapon.update();
        energetic.update();
        moveY(velocity.getY());
        moveX(velocity.getX());

        if (!hypnotized) {
            if (!canJump)
                stopAnimation();
            else
                animation.play();

            if (Game.getGameWorld().getLevel().getCurrentLevelNumber() < Level.THIRD_LEVEL)
                playVoice();
        }
    }

    public void newGame() {
        gameOver = false;
        deathText.setText(Texts.YOU_ARE_DEAD);
        Game.getGameWorld().getMenu().gameOver();
        Game.getGameWorld().getAppRoot().getChildren().remove(deathText);
    }

    public void hypnotize() {
        hypnotized = true;
        velocity = velocity.add(0, JUMP_SPEED);
    }

    private void gameOver() {
        gameOver = true;
        deathText.setText(Texts.GAME_OVER);
        deathText.setFont(Font.font("Arial", FontWeight.BOLD, 28));
        deathText.setFill(Color.RED);
        deathText.setTranslateX(Game.SCENE_WIDTH / 2 - 100);
        deathText.setTranslateY(Game.SCENE_HEIGHT / 2);
        Game.getGameWorld().getAppRoot().getChildren().add(deathText);
    }

    private void playVoice() {
        if (!Game.getGameWorld().getEnemies().isEmpty() && Game.getGameWorld().getLevel().getCurrentLevelNumber() == Level.FIRST_LEVEL)
            if (getTranslateX() > Game.getGameWorld().getEnemies().getFirst().getTranslateX() - Block.BLOCK_SIZE * 15 && canPlayVoice) {
                GameMedia.SHIT.play(Game.getGameWorld().getMenu().getVoiceSlider().getValue() / 100);
                canPlayVoice = false;
                return;
            }

        if (Game.getGameWorld().getEnemies().isEmpty() && !canPlayVoice && Game.getGameWorld().getLevel().getCurrentLevelNumber() == Level.FIRST_LEVEL) {
            GameMedia.CRETINS.play(Game.getGameWorld().getMenu().getVoiceSlider().getValue() / 100);
            canPlayVoice = true;
            return;
        }

        if (Game.getGameWorld().getEnemies().isEmpty() && Game.getGameWorld().getLevel().getCurrentLevelNumber() == Level.SECOND_LEVEL && canPlayVoice) {
            GameMedia.LETS_GO.play(Game.getGameWorld().getMenu().getVoiceSlider().getValue() / 100);
            canPlayVoice = false;
        }
    }

    public void deathReset() {
        if (Game.getGameWorld().getLevel().getCurrentLevelNumber() == Level.FIRST_LEVEL) {
            Game.getGameWorld().deathUnpause();
            Game.getGameWorld().getAppRoot().getChildren().removeAll(deathText, continueText);

            if (start) {
                Game.getGameWorld().getAppRoot().getChildren().remove(moneyText);
                start = false;
            }
        }
        Hp = 100;
        dead = false;
        setTranslateX(100);
        setTranslateY(200);
        velocity = new Point2D(0, 0);

        if (currentLivesCount < 0) {
            salt = 100;
            currentLivesCount = fullLivesCount;
            switch (Game.getGameWorld().getDifficultyType()) {
                case DifficultyType.MARIK, DifficultyType.EASY -> currentLivesCount = 4;
                case DifficultyType.MEDIUM -> currentLivesCount = 2;
                case DifficultyType.HARD -> currentLivesCount = 1;
                case DifficultyType.HARDCORE -> currentLivesCount = 0;
            }
            canPlayVoice = true;
            Game.getGameWorld().resetLevel();
        }
    }

    public void unhypnotize() {
        hypnotized = false;
        velocity = velocity.add(0, JUMP_SPEED);
    }

    public void changeLevel() {
        weapon.changeLevel();
        energetic.changeLevel();
        currentLivesCount = fullLivesCount;
    }

    public void moveX(double x) {
        if (velocity.getX() < 0)
            velocity = velocity.add(ANIMATION_SPEED, 0);
        else if (velocity.getX() > 0)
            velocity = velocity.add(-ANIMATION_SPEED, 0);
        else
            booleanVelocityX = true;

        for (int i = 0; i < Math.abs(x); i++) {
            if (x > 0) {
                if ((Game.getGameWorld().getLevel().getCurrentLevelNumber() != Level.BOSS_LEVEL
                        && getTranslateX() < Block.BLOCK_SIZE * 300 - WIDTH)
                        || (Game.getGameWorld().getLevel().getCurrentLevelNumber() == Level.BOSS_LEVEL
                        && getTranslateX() < Block.BLOCK_SIZE * 37 - WIDTH))
                    setTranslateX(getTranslateX() + 1);
            } else if (getTranslateX() > 1)
                setTranslateX(getTranslateX() - 1);

            if (intersectsWithBlocks('X', x))
                return;
            if (intersectsWithEnemies('X', x))
                return;
        }
    }

    private void initDeathText() {
        deathText = new Text(Texts.YOU_ARE_DEAD);
        deathText.setFill(Color.RED);
        deathText.setTranslateY(Game.SCENE_HEIGHT / 2);
        deathText.setTranslateX(Game.SCENE_WIDTH / 2 - 75);
        deathText.setFont(Font.font("Arial", FontWeight.BOLD, 28));

        continueText = new Text(Texts.PUSH_ENTER_TO_CONTINUE);
        continueText.setFill(Color.WHITE);
        continueText.setTranslateX(Game.SCENE_WIDTH / 3);
        continueText.setTranslateY(Game.SCENE_HEIGHT / 2 + 40);
        continueText.setFont(Font.font("Arial", FontWeight.BOLD, 28));

        moneyText = new Text();
        moneyText.setFill(Color.RED);
        moneyText.setTranslateX(Game.SCENE_WIDTH / 4);
        moneyText.setTranslateY(Game.SCENE_HEIGHT / 1.5 - 30);
        moneyText.setFont(Font.font("Arial", FontWeight.BOLD, 28));
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
            if (intersectsWithEnemies('Y', y))
                return;
        }
    }

    private void playVideoDeath() {
        if (GameMedia.WHERE_ARE_YOU_FROM.getStatus() == MediaPlayer.Status.PLAYING)
            GameMedia.WHERE_ARE_YOU_FROM.pause();

        if (Game.getGameWorld().getLevel().getCurrentLevelNumber() == Level.BOSS_LEVEL
                && Game.getGameWorld().getEnemies().getFirst() instanceof Boss boss)
            boss.unStun();

        MediaPlayer videoDeath = new MediaPlayer(GameMedia.deathMedia);
        videoDeath.setVolume(Game.getGameWorld().getMenu().getVoiceSlider().getValue() / 100);
        videoDeath.setOnEndOfMedia(() -> {
            deathReset();
            Game.getGameWorld().deathUnpause();
            Game.getGameWorld().getAppRoot().getChildren().remove(videoView);

            if (GameMedia.WHERE_ARE_YOU_FROM.getStatus() == MediaPlayer.Status.PAUSED)
                GameMedia.WHERE_ARE_YOU_FROM.play();
        });

        videoDeath.play();
        videoView.setMediaPlayer(videoDeath);
        Game.getGameWorld().getAppRoot().getChildren().add(videoView);
    }

    public void setIdleAnimation() {
        switch (weapon.getType()) {
            case WeaponType.MACHINE_GUN -> imageView.setViewport(new Rectangle2D(IDLE_MACHINE_OFFSET_X, 0, WIDTH, HEIGHT));
            case WeaponType.PISTOL -> imageView.setViewport(new Rectangle2D(IDLE_PISTOL_OFFSET_X, 0, WIDTH, HEIGHT));
            case WeaponType.RPG -> imageView.setViewport(new Rectangle2D(IDLE_RPG_OFFSET_X, 0, WIDTH, HEIGHT));
            default -> imageView.setViewport(new Rectangle2D(IDLE_WITH_NO_GUN_OFFSET_X, 0, WIDTH, HEIGHT));
        }
        stopAnimation();
    }

    public void setDifficultyType() {
        int money = 0;
        switch (Game.getGameWorld().getDifficultyType()) {
            case DifficultyType.MARIK -> {
                money = 1000000;
                fullLivesCount = 4;
                priceForGeneration = 0;
                moneyForKillingEnemy = 0;
                supplyForKillingEnemy = 30;
                closeCombatDamageToEnemies = 75;
                closeCombatDamageFromEnemies = 0;
            }
            case DifficultyType.EASY -> {
                money = 300;
                fullLivesCount = 3;
                priceForGeneration = 15;
                moneyForKillingEnemy = 10;
                supplyForKillingEnemy = 25;
                closeCombatDamageToEnemies = 50;
                closeCombatDamageFromEnemies = 5;
            }
            case DifficultyType.MEDIUM -> {
                money = 150;
                fullLivesCount = 2;
                priceForGeneration = 20;
                moneyForKillingEnemy = 5;
                supplyForKillingEnemy = 20;
                closeCombatDamageToEnemies = 40;
                closeCombatDamageFromEnemies = 10;
            }
            case DifficultyType.HARD -> {
                money = 100;
                fullLivesCount = 1;
                priceForGeneration = 25;
                moneyForKillingEnemy = 3;
                supplyForKillingEnemy = 15;
                closeCombatDamageToEnemies = 30;
                closeCombatDamageFromEnemies = 15;
            }
            case DifficultyType.HARDCORE -> {
                money = 100;
                fullLivesCount = 0;
                priceForGeneration = 0;
                moneyForKillingEnemy = 2;
                supplyForKillingEnemy = 10;
                closeCombatDamageToEnemies = 25;
                closeCombatDamageFromEnemies = 20;
            }
        }
        if (Game.getGameWorld().getLevel().getCurrentLevelNumber() == Level.FIRST_LEVEL)
            this.money = money;

        weapon.setDifficultyType();
        energetic.setDifficultyType();
        currentLivesCount = fullLivesCount;
        Game.getGameWorld().getHud().updateMoneyTextPosition();
        moneyText.setText(Texts.WITH_EACH_DEATH_YOU_LOSE_MONEY + priceForGeneration + Texts.NO_MONEY_NO_GAME);
    }

    public void jump(boolean enemyJump) {
        if (enemyJump) {
            if (booleanVelocityY) {
                booleanVelocityY = false;
                velocity = velocity.add(0, JUMP_SPEED);
            }
        } else if (canJump) {
            canJump = false;
            velocity = velocity.add(0, JUMP_SPEED);
        }
    }

    public void takeAmmo(SupplySubType subType) {
        switch (subType) {
            case SupplySubType.RPG_BULLETS -> weapon.setRpgBullets(weapon.getRpgBullets() + supplyForKillingEnemy / 5);
            case SupplySubType.PISTOL_BULLETS -> weapon.setPistolBullets(weapon.getPistolBullets() + supplyForKillingEnemy);
            case SupplySubType.MACHINE_GUN_BULLETS -> weapon.setMachineGunBullets(weapon.getMachineGunBullets() + supplyForKillingEnemy);
        }

        switch (weapon.getType()) {
            case WeaponType.RPG -> weapon.setCurrentBullets(weapon.getRpgBullets());
            case WeaponType.PISTOL -> weapon.setCurrentBullets(weapon.getPistolBullets());
            case WeaponType.MACHINE_GUN -> weapon.setCurrentBullets(weapon.getMachineGunBullets());
        }
    }

    public void closeCombat(double scaleX) {
        Hp -= closeCombatDamageFromEnemies;

        if (booleanVelocityX) {
            booleanVelocityX = false;
            velocity = velocity.add(scaleX * -JUMP_SPEED, 0);
            GameMedia.CLOSE_COMBAT.play(Game.getGameWorld().getMenu().getFxSlider().getValue() / 100);
        }
    }

    public void changeWeaponAnimation(WeaponType weaponType) {
        stopAnimation();
        switch (weaponType) {
            case WeaponType.MACHINE_GUN -> animation.setOffsetY(MACHINE_GUN_OFFSET_Y);
            case WeaponType.PISTOL -> animation.setOffsetY(PISTOL_OFFSET_Y);
            case WeaponType.RPG -> animation.setOffsetY(RPG_OFFSET_Y);
            default -> animation.setOffsetY(NO_GUN_OFFSET_Y);
        }
        animation.play();
    }

    private boolean intersectsWithBlocks(char typeOfCoordinate, double coordinate) {
        if (hypnotized) {
            for (Block block : Game.getGameWorld().getLevel().getBlocks())
                if (block.getType().equals(BlockType.METAL) && getBoundsInParent().intersects(block.getBoundsInParent())) {
                    setTranslateY(getTranslateY() - 1);
                    return true;
                }
        } else {
            for (Block block : Game.getGameWorld().getLevel().getBlocks())
                if (!block.getType().equals(BlockType.INVISIBLE) && getBoundsInParent().intersects(block.getBoundsInParent())) {
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

    private boolean intersectsWithEnemies(char typeOfCoordinate, double coordinate) {
        for (Enemy enemy : Game.getGameWorld().getEnemies())
            if (getBoundsInParent().intersects(enemy.getBoundsInParent())) {
                if (typeOfCoordinate == 'Y') {
                    if (coordinate > 0)
                        setTranslateY(getTranslateY() - 1);
                    else
                        setTranslateY(getTranslateY() + 1);
                    if ((enemy.getType() == EnemyType.BOSS && enemy.getHP() < 1) || enemy.isHypnotized())
                        canJump = true;
                    else {
                        jump(true);
                        Hp -= closeCombatDamageFromEnemies;
                        enemy.setHP(enemy.getHP() - closeCombatDamageToEnemies);
                        GameMedia.CLOSE_COMBAT.play(Game.getGameWorld().getMenu().getFxSlider().getValue() / 100);
                    }
                    setTranslateY(getTranslateY() - 1);
                } else
                    setTranslateX(getTranslateX() - getScaleX());
                return true;
            }
        return false;
    }

    public int getHp() {
        return Hp;
    }

    public void setHp(int value) {
        Hp = value;
        if (Hp > 100)
            Hp = 100;
    }

    public void setSalt(int value) {
        salt = value;
        if (salt > 100)
            salt = 100;
    }

    public int getSalt() {
        return salt;
    }

    public void setMoney(int value) {
        money = value;
        Game.getGameWorld().getHud().updateMoneyTextPosition();
    }

    public int getMoney() {
        return money;
    }

    public boolean isDead() {
        return dead;
    }

    public void addMoneyForKillingEnemy() {
        money += moneyForKillingEnemy;
        Game.getGameWorld().getHud().updateMoneyTextPosition();
    }

    public Weapon getWeapon() {
        return weapon;
    }

    public void addMedicineForKillingEnemy() {
        setHp(Hp + supplyForKillingEnemy);

        switch ((int) (Math.random() * 2)) {
            case 0 -> GameMedia.FEELS_BETTER.play(Game.getGameWorld().getMenu().getVoiceSlider().getValue() / 100);
            case 1 -> GameMedia.FEELING_BETTER.play(Game.getGameWorld().getMenu().getVoiceSlider().getValue() / 100);
        }

    }

    public boolean isGameOver() {
        return gameOver;
    }

    public boolean isHypnotized() {
        return hypnotized;
    }

    public Energetic getEnergetic() {
        return energetic;
    }

    public void setCanPlayVoice(boolean value) {
        canPlayVoice = value;
    }

    public void addSaltForKillingEnemy() {
        salt += supplyForKillingEnemy / 2;
    }

    public void setWeapon(Weapon.Builder builder) {
        weapon = builder.build();
    }

    public void minusSaltForUsingEnergetic(int saltPrice) {
        salt -= saltPrice;
    }

    public void setEnergetic(Energetic.Builder builder) {
        energetic = builder.build();
    }

    public int getCloseCombatDamageFromEnemies() {
        return closeCombatDamageFromEnemies;
    }

    public void update() {
        if ((Hp < 1 || getTranslateY() > Game.SCENE_HEIGHT) && !dead)
            die();
        else
            behave();
    }
}