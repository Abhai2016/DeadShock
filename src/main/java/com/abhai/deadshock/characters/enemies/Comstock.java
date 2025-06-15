package com.abhai.deadshock.characters.enemies;


import com.abhai.deadshock.characters.SpriteAnimation;
import com.abhai.deadshock.levels.Block;
import com.abhai.deadshock.Game;
import com.abhai.deadshock.levels.Level;
import com.abhai.deadshock.Sounds;
import com.abhai.deadshock.Supply;
import com.abhai.deadshock.weapon.ComstockWeapon;
import com.abhai.deadshock.weapon.EnemyWeapon;
import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

import java.nio.file.Path;
import java.nio.file.Paths;

public class Comstock extends Enemy {
    final byte ENEMY_SPEED = 3;
    EnemyWeapon enemyWeapon;

    private boolean booleanVoice = true;
    private boolean booleanVelocity = true;
    private boolean canJump = true;

    private int seeInterval = 0;

    private Supply supply;


    Comstock() {
    }

    public Comstock(long x, long y) {
        setWidth(62);
        setHeight(65);

        name = "comstock";
        delete = false;
        pickUpSupply = false;
        canSeeBooker = false;
        hypnosis = false;

        voiceInterval = 0;
        HP = 100;

        rectHP = new Rectangle(62, 2, Color.RED);
        rectHP.setTranslateX(getTranslateX());
        rectHP.setVisible(false);

        velocity = new Point2D(0, 10);
        enemyWeapon = new ComstockWeapon();
        Path imagePath = Paths.get("resources","images", "characters", "comstock.png");
        imgView = new ImageView(new Image(imagePath.toUri().toString()));
        imgView.setViewport( new Rectangle2D(0, 0, getWidth(), getHeight()) );
        animation = new SpriteAnimation(imgView, Duration.seconds(0.5), 10, 10, 0, 0, 62, 65);

        setTranslateX(x);
        setTranslateY(y);

        getChildren().add(imgView);
        Game.gameRoot.getChildren().addAll(this, rectHP);
    }

    private void moveX(int x) {
        for (int i = 0; i < Math.abs(x); i++) {
            if (Game.booker.getTranslateX() != getTranslateX()) {
                if (x > 0) {
                    setTranslateX(getTranslateX() + 1);
                    setScaleX(1);
                } else {
                    setTranslateX(getTranslateX() - 1);
                    setScaleX(-1);
                }
            } else
                setTranslateX(getTranslateX() + 1);

            for (Block platform : Game.blocks)
                if (getBoundsInParent().intersects(platform.getBoundsInParent())) {
                    setTranslateX(getTranslateX() - getScaleX());
                    jump();
                    return;
                }

            for (Enemy enemy : Game.enemies)
                if (this != enemy && !enemy.pickUpSupply)
                    if (getBoundsInParent().intersects(enemy.getBoundsInParent())) {
                        if (x > 0)
                            setTranslateX(getTranslateX() - 1);
                        else
                            setTranslateX(getTranslateX() + 1);
                        animation.stop();
                        return;
                    }

            if (Game.booker.getBoundsInParent().intersects(getBoundsInParent())) {
                setTranslateX(getTranslateX() - getScaleX());
                if (Game.booker.isBooleanVelocityX()) {
                    Game.booker.setHP(Game.booker.getHP() - Game.booker.getEnemyDogfight());
                    Sounds.audioClipFight.play(Game.menu.fxSlider.getValue() / 100);
                    Game.booker.velocity = Game.booker.velocity.add(getScaleX() * 15, 0);
                    Game.booker.setBooleanVelocityX(false);
                }
            }

            rectHP.setTranslateX(getTranslateX());
        }
    }

    private void moveY(int y) {
        for (int i = 0; i < Math.abs(y); i++) {
            if (y > 0) {
                setTranslateY(getTranslateY() + 1);
                canJump = false;
            } else
                setTranslateY(getTranslateY() - 1);

            for (Block block : Game.blocks) {
                if (getBoundsInParent().intersects(block.getBoundsInParent()))
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
                if (this != enemy && !enemy.pickUpSupply)
                    if (getBoundsInParent().intersects(enemy.getBoundsInParent()))
                        if (y > 0 && getTranslateY() < enemy.getTranslateY()) {
                            setTranslateY(getTranslateY() - 1);
                            if (booleanVelocity) {
                                if (Game.difficultyLevelText.equals("marik") || Game.difficultyLevelText.equals("easy"))
                                    HP -= Game.booker.getCharacterDogFight();
                                velocity = velocity.add(getScaleX() * 5, -24);
                                booleanVelocity = false;
                                return;
                            }
                        } else {
                            setTranslateY(getTranslateY() + 1);
                            return;
                        }

            if (getBoundsInParent().intersects(Game.booker.getBoundsInParent()) && !pickUpSupply)
                if (y > 0) {
                    setTranslateY(getTranslateY() - 1);
                    if (booleanVelocity) {
                        Game.booker.setHP(Game.booker.getHP() - Game.booker.getEnemyDogfight());
                        Sounds.audioClipFight.play(Game.menu.fxSlider.getValue() / 100);
                        velocity = velocity.add(getScaleX() * 5, -24);
                        booleanVelocity = false;
                    }
                } else {
                    setTranslateY(getTranslateY() + 1);
                    if (Game.booker.isBooleanVelocityY()) {
                        HP -= Game.booker.getCharacterDogFight();
                        Sounds.audioClipFight.play(Game.menu.fxSlider.getValue() / 100);
                        Game.booker.velocity = Game.booker.velocity.add(0, -22);
                        Game.booker.setBooleanVelocityY(false);
                    }
                }

            rectHP.setTranslateY(getTranslateY() - 15);
        }
    }

    void jump() {
        if (canJump) {
            velocity = velocity.add(0, -24);
            canJump = false;
        }
    }

    private void createSupply(int randSupply) {
        if (randSupply == 0)
            Game.elizabeth.countMedicine++;
        else {
            rand = (byte) (Math.random() * 2);
            if (rand == 0)
                Sounds.audioClipAmmo.play(Game.menu.voiceSlider.getValue() / 100);
            else
                Sounds.audioClipAmmo2.play(Game.menu.voiceSlider.getValue() / 100);
            if (Game.elizabeth.getAmmo() != null)
                Game.gameRoot.getChildren().remove(Game.elizabeth.getAmmo());
            Game.elizabeth.setAmmo(new Supply(Game.elizabeth.getTranslateX(), Game.elizabeth.getTranslateY(), name));
        }

        Game.gameRoot.getChildren().remove(this);
        delete = true;
    }

    private void pickUpSupply() {
        if (pickUpSupply) {
            if (Game.booker.getBoundsInParent().intersects(getBoundsInParent())) {
                if (supply.getSupply().equals("medicine")) {
                    Sounds.doctorVoice.play(Game.menu.voiceSlider.getValue() / 100);
                    for (int count = 0; count < Game.booker.getMedicineCount(); count++)
                        if (Game.booker.getHP() < 100)
                            Game.booker.setHP(Game.booker.getHP() + 1);
                        else
                            break;
                } else {
                    Sounds.excellentVoice.play(Game.menu.voiceSlider.getValue() / 100);
                    Game.weapon.setBullets(Game.weapon.getBullets() + Game.booker.getBulletCount());
                }
                Game.gameRoot.getChildren().remove(this);
                pickUpSupply = false;
                delete = true;
            }

            if (Game.difficultyLevelText.equals("high") || Game.difficultyLevelText.equals("hardcore"))
                for (Enemy enemy : Game.enemies)
                    if (enemy != this && enemy.getBoundsInParent().intersects(getBoundsInParent())) {
                        if (supply.getSupply().equals("medicine")) {
                            for (int count = 0; count < Game.booker.getMedicineCount(); count++)
                                if (enemy.getHP() < 100)
                                    enemy.setHP(enemy.getHP() + 1);
                                else
                                    break;
                        }
                        Game.gameRoot.getChildren().remove(this);
                        pickUpSupply = false;
                        delete = true;
                    }
        }
    }

    private void notSeePlayer() {
        if (Game.booker.getTranslateY() > getTranslateY()) {
            canSeeBooker = true;
            booleanVoice = true;
            playVoiceFoundPlayer();
        }
        moveX(ENEMY_SPEED);
        animation.play();
    }

    private void canSeePlayer() {
        if (Game.booker.getTranslateY() < getTranslateY() || Game.booker.getTranslateX() == getTranslateX())
            if ( (Game.levelNumber == Level.THIRD_LEVEL && Game.booker.getTranslateY()
                    < getTranslateY() - Level.BLOCK_SIZE * 2) || Game.booker.stayingOnLittlePlatform) {
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
            rand = (byte) (Math.random() * 6);
            switch (rand) {
                case 0:
                    Sounds.audioClipILostHim.play(Game.menu.voiceSlider.getValue() / 100);
                    break;
                case 1:
                    Sounds.audioClipWhereHeGoes.play(Game.menu.voiceSlider.getValue() / 100);
                    break;
                case 2:
                    Sounds.audioClipJustBeenHere.play(Game.menu.voiceSlider.getValue() / 100);
                    break;
                case 3:
                    Sounds.audioClipHeIsGone.play(Game.menu.voiceSlider.getValue() / 100);
                    break;
                case 4:
                    Sounds.audioClipILostHim2.play(Game.menu.voiceSlider.getValue() / 100);
                    break;
                case 5:
                    Sounds.audioClipHeHasGone.play(Game.menu.voiceSlider.getValue() / 100);
                    break;
            }
            booleanVoice = false;
        }
    }

    private void playVoiceFoundPlayer() {
        if (booleanVoice) {
            if (Game.levelNumber == Level.FIRST_LEVEL) {
                rand = (byte) (Math.random() * 6);
                switch (rand) {
                    case 0:
                        Sounds.audioClipDontGoAway.play(Game.menu.voiceSlider.getValue() / 100);
                        break;
                    case 1:
                        Sounds.audioClipFire.play(Game.menu.voiceSlider.getValue() / 100);
                        break;
                    case 2:
                        Sounds.audioClipHeAlreadyHere.play(Game.menu.voiceSlider.getValue() / 100);
                        break;
                    case 3:
                        Sounds.audioClipThrowWeapon.play(Game.menu.voiceSlider.getValue() / 100);
                        break;
                    case 4:
                        Sounds.audioClipYouDieHere.play(Game.menu.voiceSlider.getValue() / 100);
                        break;
                    case 5:
                        Sounds.audioClipInFight.play(Game.menu.voiceSlider.getValue() / 100);
                        break;
                }
            }
            else {
                rand = (byte) (Math.random() * 2);
                if (rand == 0)
                    Sounds.audioClipTheyHere.play(Game.menu.voiceSlider.getValue() / 100);
                else
                    Sounds.audioClipTakeThem.play(Game.menu.voiceSlider.getValue() / 100);
            }
            booleanVoice = false;
        }
    }

    private void playEnemyVoice() {
        rand = (byte) (Math.random() * 10);
        switch (rand) {
            case 0:
                Sounds.enemyVoice.play(Game.menu.voiceSlider.getValue() / 100);
                break;
            case 1:
                Sounds.enemyVoice2.play(Game.menu.voiceSlider.getValue() / 100);
                break;
            case 2:
                Sounds.enemyVoice3.play(Game.menu.voiceSlider.getValue() / 100);
                break;
            case 3:
                Sounds.enemyVoice4.play(Game.menu.voiceSlider.getValue() / 100);
                break;
            case 4:
                Sounds.enemyVoice5.play(Game.menu.voiceSlider.getValue() / 100);
                break;
            case 5:
                Sounds.enemyVoice6.play(Game.menu.voiceSlider.getValue() / 100);
                break;
            case 6:
                Sounds.enemyVoice7.play(Game.menu.voiceSlider.getValue() / 100);
                break;
            case 7:
                Sounds.enemyVoice8.play(Game.menu.voiceSlider.getValue() / 100);
                break;
            case 8:
                Sounds.enemyVoice9.play(Game.menu.voiceSlider.getValue() / 100);
                break;
            case 9:
                Sounds.enemyVoice10.play(Game.menu.voiceSlider.getValue() / 100);
                break;
        }
        voiceInterval = 0;
    }

    private void playDeath() {
        canSeeBooker = false;
        animation.stop();

        deathVoice();

        Game.booker.setMoney(Game.booker.getMoney() + Game.booker.getMoneyForKillingEnemy());
        getChildren().remove(imgView);
        Game.gameRoot.getChildren().remove(rectHP);

        byte randSupply = (byte) (Math.random() * 2);
        if (Game.levelNumber != Level.FIRST_LEVEL) {
            createSupply(randSupply);
            return;
        }
        supply = new Supply(randSupply, getTranslateX(), getTranslateY() + getHeight());
        getChildren().add(supply.getImageSupply());
        pickUpSupply = true;
    }

    private void behave() {
        if (getTranslateY() > Game.scene.getHeight()) {
            Game.gameRoot.getChildren().remove(rectHP);
            delete = true;
            return;
        }

        if (canSeeBooker)
            voiceInterval++;
        else
            voiceInterval = 0;

        if (Game.booker.getTranslateX() > getTranslateX() - 720 && Game.booker.getTranslateX() < getTranslateX()) {
            if (canSeeBooker) {
                canSeePlayer();
                moveX(-ENEMY_SPEED);
                animation.play();
                enemyWeapon.shoot(getScaleX(), getTranslateX(), getTranslateY());
            } else
                notSeePlayer();
        } else if (Game.booker.getTranslateX() < getTranslateX() + 650 && Game.booker.getTranslateX() > getTranslateX()) {
            if (canSeeBooker) {
                canSeePlayer();
                moveX(ENEMY_SPEED);
                animation.play();
                enemyWeapon.shoot(getScaleX(), getTranslateX(), getTranslateY());
            } else
                notSeePlayer();
        } else if (Game.booker.getTranslateX() == getTranslateX()) {
            animation.stop();
            if (canSeeBooker)
                canSeePlayer();
            else
                notSeePlayer();
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
        moveY((int) velocity.getY());
        if (HP > 0) {
            if (velocity.getY() < 8)
                velocity = velocity.add(0, 0.6);
            else
                booleanVelocity = true;

            if (HP < 100)
                rectHP.setVisible(true);
            rectHP.setWidth(HP / 100 * 62);

            if (!hypnosis) {
                enemyWeapon.update();
                if (booleanVelocity)
                    behave();
            } else
                animation.stop();
        } else {
            if (!pickUpSupply)
                playDeath();
            pickUpSupply();
        }


    }
}