package com.abhai.deadshock.weapons;

import com.abhai.deadshock.DifficultyLevel;
import com.abhai.deadshock.Game;
import com.abhai.deadshock.levels.Level;
import com.abhai.deadshock.utils.Sounds;
import com.abhai.deadshock.utils.SpriteAnimation;
import com.abhai.deadshock.utils.pools.ObjectPool;
import com.abhai.deadshock.weapons.bullets.Bullet;
import javafx.geometry.Rectangle2D;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.util.Duration;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

import static com.abhai.deadshock.levels.Block.BLOCK_SIZE;

public class Weapon extends Pane {
    private WeaponType type = WeaponType.NO_GUN;
    public final ArrayList<Bullet> bulletsList = new ArrayList<>();
    private final ObjectPool<Bullet> bulletsPool = new ObjectPool<>(Bullet::new, 50, 150);

    public ImageView explosion;
    public SpriteAnimation explosionAnimation;

    private Path imagePath = Paths.get("resources", "images", "weapons", "weapons.png");
    private ImageView gun = new ImageView(new Image(imagePath.toUri().toString()));

    private Path explosionImagePath = Paths.get("resources", "images", "weapons", "explosion.png");
    private Path rpgImagePath = Paths.get("resources", "images", "weapons", "rpg.png");

    private int clip = 0;
    private int shootInterval = 0;
    private int bullets = 0;
    private int damage = 0;
    private int rpgDamage = 0;

    private boolean nowReloading = false;
    private boolean canReload = true;
    private boolean singleShot = true;
    private boolean canChoosePistol = false;
    private boolean canChooseMachineGun = false;
    private boolean canChooseRPG = false;

    public Weapon() {
        gun.setViewport(new Rectangle2D(265, 88, 33, 18));
        setTranslateX(BLOCK_SIZE * 38);
        setTranslateY(BLOCK_SIZE * 12 - 18);

        getChildren().add(gun);
        Game.gameRoot.getChildren().add(this);
    }

    public Weapon(boolean value) {
        canChoosePistol = value;
        if (canChoosePistol) {
            type = WeaponType.PISTOL;
            Game.booker.changeWeaponAnimation(type);
        }
        gun.setViewport(new Rectangle2D(400, 80, 56, 18));
        setTranslateX(BLOCK_SIZE * 26);
        setTranslateY(BLOCK_SIZE * 12 - 20);

        getChildren().add(gun);
        Game.gameRoot.getChildren().add(this);
    }

    public Weapon(boolean pistol, boolean machineGun) {
        canChoosePistol = pistol;
        canChooseMachineGun = machineGun;
        if (canChooseMachineGun)
            type = WeaponType.MACHINE_GUN;
        else if (canChoosePistol)
            type = WeaponType.PISTOL;
        Game.booker.changeWeaponAnimation(type);
        gun = new ImageView(new Image(rpgImagePath.toUri().toString()));
        setTranslateX(BLOCK_SIZE * 24);
        setTranslateY(BLOCK_SIZE * 12 - 20);

        explosion = new ImageView(new Image(explosionImagePath.toUri().toString()));
        explosion.setFitWidth(128);
        explosion.setFitHeight(128);
        explosionAnimation = new SpriteAnimation(explosion, Duration.seconds(1), 16, 4, 0, 0, 128, 128);
        explosionAnimation.setCycleCount(1);

        getChildren().add(gun);
        Game.gameRoot.getChildren().add(this);
    }

    public Weapon(boolean pistol, boolean machineGun, boolean rpg) {
        canChoosePistol = pistol;
        canChooseMachineGun = machineGun;
        canChooseRPG = rpg;
        if (canChooseRPG)
            type = WeaponType.RPG;
        else if (canChooseMachineGun)
            type = WeaponType.MACHINE_GUN;
        else if (canChoosePistol)
            type = WeaponType.PISTOL;
        Game.booker.changeWeaponAnimation(type);

        explosion = new ImageView(new Image(explosionImagePath.toUri().toString()));
        explosion.setFitWidth(128);
        explosion.setFitHeight(128);
        explosionAnimation = new SpriteAnimation(explosion, Duration.seconds(1), 16, 4, 0, 0, 128, 128);
        explosionAnimation.setCycleCount(1);
    }

    public WeaponType getType() {
        return type;
    }

    public int getWeaponClip() {
        return clip;
    }

    public void setWeaponClip(long value) {
        clip = (byte) value;
    }

    public int getBullets() {
        return bullets;
    }

    public void setBullets(long value) {
        bullets = (short) value;
    }

    public void setCanReload(boolean value) {
        canReload = value;
    }

    public void setSingleShot(boolean value) {
        singleShot = value;
    }

    public int getDamage() {
        return damage;
    }

    public int getRpgDamage() {
        return rpgDamage;
    }

    public boolean isCanChoosePistol() {
        return canChoosePistol;
    }

    public boolean isCanChooseMachineGun() {
        return canChooseMachineGun;
    }

    public boolean isCanChooseRPG() {
        return canChooseRPG;
    }

    public void changeLevel() {
        if (Game.levelNumber == Level.SECOND_LEVEL) {
            gun.setViewport(new Rectangle2D(400, 80, 56, 18));
            setTranslateX(BLOCK_SIZE * 26);
            setTranslateY(BLOCK_SIZE * 12 - 20);
        } else if (Game.levelNumber == Level.THIRD_LEVEL) {
            getChildren().remove(gun);
            gun = new ImageView(new Image(rpgImagePath.toUri().toString()));
            getChildren().add(gun);
            setTranslateX(BLOCK_SIZE * 24);
            setTranslateY(BLOCK_SIZE * 12 - 20);
        }
        if (!Game.gameRoot.getChildren().contains(this))
            Game.gameRoot.getChildren().add(this);
    }

    public void pickUpWeapon() {
        if (!nowReloading) {
            switch (Game.levelNumber) {
                case Level.FIRST_LEVEL:
                    Sounds.willWork.play(Game.menu.getVoiceSlider().getValue() / 100);
                    type = WeaponType.PISTOL;
                    Game.booker.changeWeaponAnimation(type);
                    canChoosePistol = true;
                    WeaponData.pistolClip = clip = 20;
                    WeaponData.pistolBullets = bullets = 80;
                    break;
                case Level.SECOND_LEVEL:
                    Sounds.great.play(Game.menu.getVoiceSlider().getValue() / 100);
                    type = WeaponType.MACHINE_GUN;
                    Game.booker.changeWeaponAnimation(type);
                    canChooseMachineGun = true;
                    WeaponData.pistolClip = clip;
                    WeaponData.pistolBullets = bullets;
                    WeaponData.machineGunClip = clip = 30;
                    WeaponData.machineGunBullets = bullets = 120;
                    break;
                case Level.THIRD_LEVEL:
                    explosion = new ImageView(new Image(explosionImagePath.toUri().toString()));
                    explosion.setFitWidth(128);
                    explosion.setFitHeight(128);
                    explosionAnimation = new SpriteAnimation(explosion, Duration.seconds(1), 16, 4, 0, 0, 128, 128);
                    explosionAnimation.setCycleCount(1);

                    Sounds.great.play(Game.menu.getVoiceSlider().getValue() / 100);
                    if (type.equals(WeaponType.PISTOL)) {
                        WeaponData.pistolClip = clip;
                        WeaponData.pistolBullets = bullets;
                    } else {
                        WeaponData.machineGunClip = clip;
                        WeaponData.machineGunBullets = bullets;
                    }
                    type = WeaponType.RPG;
                    Game.booker.changeWeaponAnimation(type);
                    canChooseRPG = true;
                    WeaponData.rpgClip = clip = 1;
                    WeaponData.rpgBullets = bullets = 30;
                    break;
            }

            setTranslateX(0);
            setTranslateY(0);
            Game.gameRoot.getChildren().remove(this);
        }
    }

    public void setDifficultyLevel() {
        switch (Game.difficultyLevel) {
            case DifficultyLevel.MARIK -> {
                damage = 30;
                rpgDamage = 300;
            }
            case DifficultyLevel.EASY -> {
                damage = 20;
                rpgDamage = 250;
            }
            case DifficultyLevel.MEDIUM -> {
                damage = 15;
                rpgDamage = 200;
            }
            case DifficultyLevel.HARD -> {
                damage = 10;
                rpgDamage = 150;
            }
            case DifficultyLevel.HARDCORE -> {
                damage = 8;
                rpgDamage = 150;
            }
        }
    }

    public void reload() {
        if (canReload && !nowReloading && bullets > 0) {
            canReload = false;
            switch (type) {
                case WeaponType.PISTOL -> {
                    if (clip < 20) {
                        Sounds.pistolReload.setVolume(Game.menu.getFxSlider().getValue() / 100);
                        Sounds.pistolReload.play();
                        fillClip(type, 20);
                    }
                }
                case WeaponType.MACHINE_GUN -> {
                    if (clip < 30) {
                        Sounds.machineGunReload.setVolume(Game.menu.getFxSlider().getValue() / 100);
                        Sounds.machineGunReload.play();
                        fillClip(type, 30);
                    }
                }
                case WeaponType.RPG -> {
                    if (clip < 1)
                        fillClip(type, 1);
                }
            }
        }
    }

    private void fillClip(WeaponType type, int value) {
        nowReloading = true;
        canReload = false;
        switch (type) {
            case WeaponType.PISTOL -> Sounds.pistolReload.setOnEndOfMedia(() -> {
                while (clip < value)
                    if (bullets > 0) {
                        clip++;
                        bullets--;
                    } else
                        break;
                nowReloading = false;
            });
            case WeaponType.MACHINE_GUN -> Sounds.machineGunReload.setOnEndOfMedia(() -> {
                while (clip < value)
                    if (bullets > 0) {
                        clip++;
                        bullets--;
                    } else
                        break;
                nowReloading = false;
            });
            case WeaponType.RPG -> Sounds.rpgShotWithReload.setOnEndOfMedia(() -> {
                while (clip < value)
                    if (bullets > 0) {
                        clip++;
                        bullets--;
                    } else
                        break;
                nowReloading = false;
            });
        }
    }

    public void shoot() {
        if (clip > 0 && !nowReloading) {
            switch (type) {
                case WeaponType.PISTOL -> {
                    if (singleShot) {
                        Sounds.pistolShot.play(Game.menu.getFxSlider().getValue() / 100);
                        Bullet bullet = bulletsPool.get();
                        bullet.init(WeaponType.PISTOL);
                        bulletsList.add(bullet);
                        clip--;
                        singleShot = false;
                    }
                }
                case WeaponType.MACHINE_GUN -> {
                    if (shootInterval > 5) {
                        Sounds.machineGunShot.play(Game.menu.getFxSlider().getValue() / 100);
                        Bullet bullet = bulletsPool.get();
                        bullet.init(WeaponType.MACHINE_GUN);
                        bulletsList.add(bullet);
                        clip--;
                        shootInterval = 0;
                    }
                }
                case WeaponType.RPG -> {
                    Sounds.rpgShotWithReload.setVolume(Game.menu.getFxSlider().getValue() / 100);
                    Sounds.rpgShotWithReload.play();
                    Bullet bullet = bulletsPool.get();
                    bullet.init(WeaponType.RPG);
                    bulletsList.add(bullet);
                    clip--;
                    reload();
                }
            }
        }
    }

    public void changeWeapon(WeaponType type) {
        if (!type.equals(WeaponType.NO_GUN) && !nowReloading) {
            switch (type) {
                case WeaponType.PISTOL -> {
                    if (canChoosePistol) {
                        partChangeWeapon();
                        this.type = WeaponType.PISTOL;
                        clip = WeaponData.pistolClip;
                        bullets = WeaponData.pistolBullets;
                    }
                }
                case WeaponType.MACHINE_GUN -> {
                    if (canChooseMachineGun) {
                        partChangeWeapon();
                        this.type = WeaponType.MACHINE_GUN;
                        clip = WeaponData.machineGunClip;
                        bullets = WeaponData.machineGunBullets;
                    }
                }
                case WeaponType.RPG -> {
                    if (canChooseRPG) {
                        partChangeWeapon();
                        this.type = WeaponType.RPG;
                        clip = WeaponData.rpgClip;
                        bullets = WeaponData.rpgBullets;
                    }
                }
            }
            Game.booker.changeWeaponAnimation(type);
        }
    }

    private void partChangeWeapon() {
        switch (type) {
            case WeaponType.PISTOL -> {
                WeaponData.pistolClip = clip;
                WeaponData.pistolBullets = bullets;
            }
            case WeaponType.MACHINE_GUN -> {
                WeaponData.machineGunClip = clip;
                WeaponData.machineGunBullets = bullets;
            }
            case WeaponType.RPG -> {
                WeaponData.rpgClip = clip;
                WeaponData.rpgBullets = bullets;
            }
        }
    }

    public void clearBullets() {
        for (Bullet bullet : bulletsList)
            bulletsPool.put(bullet);
        Game.gameRoot.getChildren().removeAll(bulletsList);
        bulletsList.clear();
    }

    public void update() {
        shootInterval++;
        for (Bullet bullet : bulletsList) {
            bullet.update();
            if (bullet.isDelete()) {
                bulletsPool.put(bullet);
                bulletsList.remove(bullet);
                Game.gameRoot.getChildren().remove(bullet);
                break;
            }
        }
    }

    public static class WeaponData {
        public static int pistolClip = 0;
        public static int machineGunClip = 0;
        public static int rpgClip = 0;

        public static int pistolBullets = 0;
        public static int machineGunBullets = 0;
        public static int rpgBullets = 0;
    }
}



