package com.abhai.deadshock.weapons;

import com.abhai.deadshock.menus.DifficultyLevel;
import com.abhai.deadshock.Game;
import com.abhai.deadshock.world.levels.Level;
import com.abhai.deadshock.utils.Sounds;
import com.abhai.deadshock.utils.pools.ObjectPool;
import com.abhai.deadshock.weapons.bullets.Bullet;
import javafx.geometry.Rectangle2D;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.nio.file.Paths;
import java.util.ArrayList;

import static com.abhai.deadshock.world.levels.Block.BLOCK_SIZE;

public class Weapon extends Pane {
    public static class Builder {
        private boolean canChooseRpg;
        private boolean canChoosePistol;
        private boolean canChooseMachineGun;

        private int rpgBullets;
        private int pistolBullets;
        private int machineGunBullets;

        public Builder setCanChoosePistol(boolean canChoosePistol) {
            this.canChoosePistol = canChoosePistol;
            return this;
        }

        public Builder setCanChooseMachineGun(boolean canChooseMachineGun) {
            this.canChooseMachineGun = canChooseMachineGun;
            return this;
        }

        public Builder setCanChooseRpg(boolean canChooseRpg) {
            this.canChooseRpg = canChooseRpg;
            return this;
        }

        public Builder setRpgBullets(int rpgBullets) {
            this.rpgBullets = rpgBullets;
            return this;
        }

        public Builder setPistolBullets(int pistolBullets) {
            this.pistolBullets = pistolBullets;
            return this;
        }

        public Builder setMachineGunBullets(int machineGunBullets) {
            this.machineGunBullets = machineGunBullets;
            return this;
        }

        public Weapon build() {
            return new Weapon(this);
        }
    }

    private static final int FULL_RPG_CLIP = 1;
    private static final int FULL_PISTOL_CLIP = 20;
    private static final int START_RPG_BULLETS = 30;
    private static final int START_PISTOL_BULLETS = 80;
    private static final int FULL_MACHINE_GUN_CLIP = 30;
    private static final int MACHINE_GUN_SHOOT_INTERVAL = 5;
    private static final int START_MACHINE_GUN_BULLETS = 120;
    private static final Image RPG_IMAGE = new Image(Paths.get("resources", "images", "weapons", "rpg.png").toUri().toString());
    private static final Image WEAPON_IMAGE = new Image(Paths.get("resources", "images", "weapons", "weapons.png").toUri().toString());
    private static final Media pistolReload = new Media(Paths.get("resources", "sounds", "fx", "weapons", "pistolReload.mp3").toUri().toString());
    private static final Media machineGunReload = new Media(Paths.get("resources", "sounds", "fx", "weapons", "machineGunReload.mp3").toUri().toString());
    private static final Media rpgShotWithReload = new Media(Paths.get("resources", "sounds", "fx", "weapons", "rpgShotWithReload.mp3").toUri().toString());

    private WeaponType type;
    private ImageView imageView;
    private ArrayList<Bullet> bullets;
    private MediaPlayer reloadMediaPLayer;
    private ObjectPool<Bullet> bulletsPool;

    private int rpgClip;
    private int rpgDamage;
    private int rpgBullets;
    private int pistolClip;
    private int currentClip;
    private int bulletDamage;
    private int pistolBullets;
    private int currentBullets;
    private int machineGunClip;
    private int machineGunBullets;
    private int currentShootInterval;

    private boolean canReload;
    private boolean singleShot;
    private boolean nowReloading;
    private boolean canChooseRpg;
    private boolean canChoosePistol;
    private boolean canChooseMachineGun;

    public Weapon(Builder builder) {
        this.canChooseRpg = builder.canChooseRpg;
        this.canChoosePistol = builder.canChoosePistol;
        this.canChooseMachineGun = builder.canChooseMachineGun;

        this.rpgBullets = builder.rpgBullets;
        this.pistolBullets = builder.pistolBullets;
        this.machineGunBullets = builder.machineGunBullets;

        init();

        if (Game.levelNumber != Level.BOSS_LEVEL) {
            getChildren().add(imageView);
            Game.gameRoot.getChildren().add(this);
        }
    }

    public void init() {
        rpgClip = 0;
        rpgDamage = 0;
        pistolClip = 0;
        currentClip = 0;
        bulletDamage = 0;
        canReload = true;
        singleShot = true;
        currentBullets = 0;
        machineGunClip = 0;
        nowReloading = false;
        currentShootInterval = 0;
        bullets = new ArrayList<>();
        imageView = new ImageView(WEAPON_IMAGE);
        bulletsPool = new ObjectPool<>(Bullet::new, 50, 150);

        switch (Game.levelNumber) {
            case Level.FIRST_LEVEL -> {
                type = WeaponType.NO_GUN;
                setTranslateX(BLOCK_SIZE * 38);
                setTranslateY(BLOCK_SIZE * 12 - 18);
                imageView.setViewport(new Rectangle2D(265, 88, 33, 18));
            }
            case Level.SECOND_LEVEL -> {
                if (canChoosePistol) {
                    type = WeaponType.PISTOL;
                    currentBullets = pistolBullets;
                    currentClip = FULL_PISTOL_CLIP;
                    Game.booker.changeWeaponAnimation(type);
                }
                setTranslateX(BLOCK_SIZE * 26);
                setTranslateY(BLOCK_SIZE * 12 - 20);
                imageView.setViewport(new Rectangle2D(400, 80, 56, 18));
            }
            case Level.THIRD_LEVEL -> {
                if (canChooseMachineGun) {
                    type = WeaponType.MACHINE_GUN;
                    currentBullets = machineGunBullets;
                    currentClip = FULL_MACHINE_GUN_CLIP;
                } else if (canChoosePistol) {
                    type = WeaponType.PISTOL;
                    currentBullets = pistolBullets;
                    currentClip = FULL_PISTOL_CLIP;
                }
                imageView.setImage(RPG_IMAGE);
                setTranslateX(BLOCK_SIZE * 24);
                setTranslateY(BLOCK_SIZE * 12 - 20);
                Game.booker.changeWeaponAnimation(type);
            }
            case Level.BOSS_LEVEL -> {
                if (canChooseRpg) {
                    type = WeaponType.RPG;
                    currentBullets = rpgBullets;
                    currentClip = FULL_RPG_CLIP;
                } else if (canChooseMachineGun) {
                    type = WeaponType.MACHINE_GUN;
                    currentBullets = machineGunBullets;
                    currentClip = FULL_MACHINE_GUN_CLIP;
                } else if (canChoosePistol) {
                    type = WeaponType.PISTOL;
                    currentBullets = pistolBullets;
                    currentClip = FULL_PISTOL_CLIP;
                }
                Game.booker.changeWeaponAnimation(type);
            }
        }
    }

    public void reset() {
        rpgClip = 0;
        rpgDamage = 0;
        pistolClip = 0;
        clearBullets();
        rpgBullets = 0;
        currentClip = 0;
        bulletDamage = 0;
        canReload = true;
        singleShot = true;
        pistolBullets = 0;
        currentBullets = 0;
        machineGunClip = 0;
        nowReloading = false;
        canChooseRpg = false;
        machineGunBullets = 0;
        canChoosePistol = false;
        currentShootInterval = 0;
        canChooseMachineGun = false;

        type = WeaponType.NO_GUN;
        setTranslateX(BLOCK_SIZE * 38);
        imageView.setImage(WEAPON_IMAGE);
        setTranslateY(BLOCK_SIZE * 12 - 18);
        imageView.setViewport(new Rectangle2D(265, 88, 33, 18));

        if (!Game.gameRoot.getChildren().contains(this))
            Game.gameRoot.getChildren().add(this);
    }

    public void shoot() {
        if (currentClip > 0 && !nowReloading) {
            switch (type) {
                case WeaponType.PISTOL -> {
                    if (singleShot) {
                        currentClip--;
                        singleShot = false;
                        Bullet bullet = bulletsPool.get();
                        bullet.init(WeaponType.PISTOL);
                        bullets.add(bullet);
                        Sounds.pistolShot.play(Game.menu.getFxSlider().getValue() / 100);
                    }
                }
                case WeaponType.MACHINE_GUN -> {
                    if (currentShootInterval > MACHINE_GUN_SHOOT_INTERVAL) {
                        currentClip--;
                        currentShootInterval = 0;
                        Bullet bullet = bulletsPool.get();
                        bullet.init(WeaponType.MACHINE_GUN);
                        bullets.add(bullet);
                        Sounds.machineGunShot.play(Game.menu.getFxSlider().getValue() / 100);
                    }
                }
                case WeaponType.RPG -> {
                    currentClip--;
                    Bullet bullet = bulletsPool.get();
                    bullet.init(WeaponType.RPG);
                    bullets.add(bullet);
                    reloadMediaPLayer = new MediaPlayer(rpgShotWithReload);
                    reloadMediaPLayer.setVolume(Game.menu.getFxSlider().getValue() / 100);
                    reloadMediaPLayer.play();
                    reload();
                }
            }
        }
    }

    public void reload() {
        if (canReload && !nowReloading && currentBullets > 0) {
            canReload = false;
            switch (type) {
                case WeaponType.PISTOL -> {
                    if (currentClip < FULL_PISTOL_CLIP) {
                        reloadMediaPLayer = new MediaPlayer(pistolReload);
                        reloadMediaPLayer.setVolume(Game.menu.getFxSlider().getValue() / 100);
                        reloadMediaPLayer.play();
                        fillClip(type);
                    }
                }
                case WeaponType.MACHINE_GUN -> {
                    if (currentClip < FULL_MACHINE_GUN_CLIP) {
                        reloadMediaPLayer = new MediaPlayer(machineGunReload);
                        reloadMediaPLayer.setVolume(Game.menu.getFxSlider().getValue() / 100);
                        reloadMediaPLayer.play();
                        fillClip(type);
                    }
                }
                case WeaponType.RPG -> {
                    if (currentClip < FULL_RPG_CLIP)
                        fillClip(type);
                }
            }
        }
    }

    public void changeLevel() {
        if (Game.levelNumber == Level.SECOND_LEVEL) {
            setTranslateX(BLOCK_SIZE * 26);
            setTranslateY(BLOCK_SIZE * 12 - 20);
            imageView.setViewport(new Rectangle2D(400, 80, 56, 18));
        } else if (Game.levelNumber == Level.THIRD_LEVEL) {
            imageView.setImage(RPG_IMAGE);
            setTranslateX(BLOCK_SIZE * 24);
            setTranslateY(BLOCK_SIZE * 12 - 20);
        }
        if (!Game.gameRoot.getChildren().contains(this))
            Game.gameRoot.getChildren().add(this);
    }

    public void pickUpWeapon() {
        if (!nowReloading) {
            switch (Game.levelNumber) {
                case Level.FIRST_LEVEL -> {
                    canChoosePistol = true;
                    type = WeaponType.PISTOL;
                    Game.booker.changeWeaponAnimation(type);
                    pistolClip = currentClip = FULL_PISTOL_CLIP;
                    if (pistolBullets > 0)
                        currentBullets = pistolBullets;
                    else
                        currentBullets = pistolBullets = START_PISTOL_BULLETS;
                    Sounds.willWork.play(Game.menu.getVoiceSlider().getValue() / 100);
                }
                case Level.SECOND_LEVEL -> {
                    pistolClip = currentClip;
                    canChooseMachineGun = true;
                    type = WeaponType.MACHINE_GUN;
                    pistolBullets = currentBullets;
                    Game.booker.changeWeaponAnimation(type);
                    machineGunClip = currentClip = FULL_MACHINE_GUN_CLIP;
                    if (machineGunBullets > 0)
                        currentBullets = machineGunBullets;
                    else
                        machineGunBullets = currentBullets = START_MACHINE_GUN_BULLETS;
                    Sounds.great.play(Game.menu.getVoiceSlider().getValue() / 100);
                }
                case Level.THIRD_LEVEL -> {
                    canChooseRpg = true;

                    if (type == WeaponType.PISTOL) {
                        pistolBullets = currentBullets;
                        pistolClip = currentClip;
                    } else {
                        machineGunBullets = currentBullets;
                        machineGunClip = currentClip;
                    }

                    type = WeaponType.RPG;
                    rpgClip = currentClip = FULL_RPG_CLIP;
                    Game.booker.changeWeaponAnimation(type);

                    if (rpgBullets > 0)
                        currentBullets = rpgBullets;
                    else
                        rpgBullets = currentBullets = START_RPG_BULLETS;
                    Sounds.great.play(Game.menu.getVoiceSlider().getValue() / 100);
                }
            }

            setTranslateX(0);
            setTranslateY(0);
            Game.gameRoot.getChildren().remove(this);
        }
    }

    public void clearBullets() {
        for (Bullet bullet : bullets)
            bulletsPool.put(bullet);
        Game.gameRoot.getChildren().removeAll(bullets);
        bullets.clear();
    }

    public void setDifficultyLevel() {
        switch (Game.difficultyLevel) {
            case DifficultyLevel.MARIK -> {
                rpgDamage = 300;
                bulletDamage = 30;
            }
            case DifficultyLevel.EASY -> {
                rpgDamage = 250;
                bulletDamage = 20;
            }
            case DifficultyLevel.MEDIUM -> {
                rpgDamage = 200;
                bulletDamage = 15;
            }
            case DifficultyLevel.HARD -> {
                rpgDamage = 150;
                bulletDamage = 10;
            }
            case DifficultyLevel.HARDCORE -> {
                rpgDamage = 150;
                bulletDamage = 8;
            }
        }
    }

    private void fillClip(WeaponType type) {
        nowReloading = true;
        switch (type) {
            case WeaponType.PISTOL -> reloadMediaPLayer.setOnEndOfMedia(() -> {
                while (currentClip < FULL_PISTOL_CLIP)
                    if (currentBullets > 0) {
                        currentClip++;
                        currentBullets--;
                    } else
                        break;
                canReload = true;
                nowReloading = false;
            });
            case WeaponType.MACHINE_GUN -> reloadMediaPLayer.setOnEndOfMedia(() -> {
                while (currentClip < FULL_MACHINE_GUN_CLIP)
                    if (currentBullets > 0) {
                        currentClip++;
                        currentBullets--;
                    } else
                        break;
                canReload = true;
                nowReloading = false;
            });
            case WeaponType.RPG -> reloadMediaPLayer.setOnEndOfMedia(() -> {
                if (currentBullets > 0) {
                    currentClip++;
                    currentBullets--;
                }
                canReload = true;
                nowReloading = false;
            });
        }
    }

    public WeaponType getType() {
        return type;
    }

    public int getRpgDamage() {
        return rpgDamage;
    }

    public void changeWeapon(WeaponType type) {
        if (!nowReloading && this.type != type) {
            switch (type) {
                case WeaponType.PISTOL -> {
                    if (canChoosePistol) {
                        if (this.type == WeaponType.MACHINE_GUN) {
                            machineGunBullets = currentBullets;
                            machineGunClip = currentClip;
                        } else
                            rpgBullets = currentBullets;
                        currentClip = pistolClip;
                        this.type = WeaponType.PISTOL;
                        currentBullets = pistolBullets;
                        Game.booker.changeWeaponAnimation(type);
                    }
                }
                case WeaponType.MACHINE_GUN -> {
                    if (canChooseMachineGun) {
                        if (this.type == WeaponType.PISTOL) {
                            pistolClip = currentClip;
                            pistolBullets = currentBullets;
                        } else
                            rpgBullets = currentBullets;
                        this.type = WeaponType.MACHINE_GUN;
                        currentClip = machineGunClip;
                        currentBullets = machineGunBullets;
                        Game.booker.changeWeaponAnimation(type);
                    }
                }
                case WeaponType.RPG -> {
                    if (canChooseRpg) {
                        if (this.type == WeaponType.PISTOL) {
                            pistolClip = currentClip;
                            pistolBullets = currentBullets;
                        } else {
                            machineGunClip = currentClip;
                            machineGunBullets = currentBullets;
                        }
                        currentClip = rpgClip;
                        this.type = WeaponType.RPG;
                        currentBullets = rpgBullets;
                        Game.booker.changeWeaponAnimation(type);
                    }
                }
            }
        }
    }

    public int getRpgBullets() {
        return rpgBullets;
    }

    public int getCurrentClip() {
        return currentClip;
    }

    public int getBulletDamage() {
        return bulletDamage;
    }

    public int getPistolBullets() {
        return pistolBullets;
    }

    public boolean isCanChooseRpg() {
        return canChooseRpg;
    }

    public int getCurrentBullets() {
        return currentBullets;
    }

    public void setCanReload(boolean value) {
        canReload = value;
    }

    public boolean isCanChoosePistol() {
        return canChoosePistol;
    }

    public int getMachineGunBullets() {
        return machineGunBullets;
    }

    public void setSingleShot(boolean value) {
        singleShot = value;
    }

    public void setPistolBullets(int bullets) {
        pistolBullets = bullets;
    }

    public boolean isCanChooseMachineGun() {
        return canChooseMachineGun;
    }

    public void setCurrentBullets(long value) {
        currentBullets = (short) value;
    }

    public void setMachineGunBullets(int bullets) {
        machineGunBullets = bullets;
    }

    public void update() {
        currentShootInterval++;
        for (Bullet bullet : bullets) {
            bullet.update();
            if (bullet.isDelete()) {
                bulletsPool.put(bullet);
                bullets.remove(bullet);
                Game.gameRoot.getChildren().remove(bullet);
                break;
            }
        }
    }
}



