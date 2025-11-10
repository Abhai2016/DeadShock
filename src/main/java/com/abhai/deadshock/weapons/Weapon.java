package com.abhai.deadshock.weapons;

import com.abhai.deadshock.Game;
import com.abhai.deadshock.types.DifficultyType;
import com.abhai.deadshock.types.WeaponType;
import com.abhai.deadshock.utils.GameMedia;
import com.abhai.deadshock.utils.pools.ObjectPool;
import com.abhai.deadshock.weapons.bullets.Bullet;
import com.abhai.deadshock.world.levels.Level;
import javafx.geometry.Rectangle2D;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
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
    private static final int PISTOL_X = BLOCK_SIZE * 30;
    private static final int FULL_MACHINE_GUN_CLIP = 30;
    private static final int MACHINE_GUN_SHOOT_INTERVAL = 5;
    private static final int PISTOL_Y = BLOCK_SIZE * 12 - 18;
    private static final int START_MACHINE_GUN_BULLETS = 120;

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

    //TODO change canChoose booleans to EnumSets
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
        if (Game.getGameWorld().getLevel().getCurrentLevelNumber() != Level.BOSS_LEVEL)
            Game.getGameWorld().getGameRoot().getChildren().add(this);
    }

    public void init() {
        rpgDamage = 0;
        currentClip = 0;
        bulletDamage = 0;
        canReload = true;
        singleShot = true;
        currentBullets = 0;
        nowReloading = false;
        rpgClip = FULL_RPG_CLIP;
        currentShootInterval = 0;
        bullets = new ArrayList<>();
        pistolClip = FULL_PISTOL_CLIP;
        machineGunClip = FULL_MACHINE_GUN_CLIP;
        imageView = new ImageView(new Image(Paths.get("resources", "images", "weapons", "weapons.png").toUri().toString()));
        bulletsPool = new ObjectPool<>(Bullet::new, 50, 150);
        getChildren().add(imageView);

        switch (Game.getGameWorld().getLevel().getCurrentLevelNumber()) {
            case Level.FIRST_LEVEL -> {
                type = WeaponType.NO_GUN;
                setTranslateX(PISTOL_X);
                setTranslateY(PISTOL_Y);
                imageView.setViewport(new Rectangle2D(265, 88, 33, 18));
            }
            case Level.SECOND_LEVEL -> {
                if (canChoosePistol) {
                    type = WeaponType.PISTOL;
                    currentClip = pistolClip;
                    currentBullets = pistolBullets;
                }
                setTranslateX(BLOCK_SIZE * 26);
                setTranslateY(BLOCK_SIZE * 12 - 20);
                imageView.setViewport(new Rectangle2D(400, 80, 56, 18));
            }
            case Level.THIRD_LEVEL -> {
                if (canChooseMachineGun) {
                    currentClip = machineGunClip;
                    type = WeaponType.MACHINE_GUN;
                    currentBullets = machineGunBullets;
                } else if (canChoosePistol) {
                    type = WeaponType.PISTOL;
                    currentClip = pistolClip;
                    currentBullets = pistolBullets;
                }
                setTranslateX(BLOCK_SIZE * 24);
                setTranslateY(BLOCK_SIZE * 12 - 20);
                imageView.setViewport(new Rectangle2D(680, 80, 65, 18));
            }
            case Level.BOSS_LEVEL -> {
                if (canChooseRpg) {
                    type = WeaponType.RPG;
                    currentClip = rpgClip;
                    currentBullets = rpgBullets;
                } else if (canChooseMachineGun) {
                    currentClip = machineGunClip;
                    type = WeaponType.MACHINE_GUN;
                    currentBullets = machineGunBullets;
                } else if (canChoosePistol) {
                    type = WeaponType.PISTOL;
                    currentClip = pistolClip;
                    currentBullets = pistolBullets;
                }
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
        setTranslateX(PISTOL_X);
        setTranslateY(PISTOL_Y);
        imageView.setViewport(new Rectangle2D(265, 88, 33, 18));

        if (!Game.getGameWorld().getGameRoot().getChildren().contains(this))
            Game.getGameWorld().getGameRoot().getChildren().add(this);
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
                        GameMedia.PISTOL_SHOT.play(Game.getGameWorld().getMenu().getFxSlider().getValue() / 100);
                    }
                }
                case WeaponType.MACHINE_GUN -> {
                    if (currentShootInterval > MACHINE_GUN_SHOOT_INTERVAL) {
                        currentClip--;
                        currentShootInterval = 0;
                        Bullet bullet = bulletsPool.get();
                        bullet.init(WeaponType.MACHINE_GUN);
                        bullets.add(bullet);
                        GameMedia.MACHINE_GUN_SHOT.play(Game.getGameWorld().getMenu().getFxSlider().getValue() / 100);
                    }
                }
                case WeaponType.RPG -> {
                    currentClip--;
                    Bullet bullet = bulletsPool.get();
                    bullet.init(WeaponType.RPG);
                    bullets.add(bullet);
                    reloadMediaPLayer = new MediaPlayer(GameMedia.RPG_SHOT_WITH_RELOAD);
                    reloadMediaPLayer.setVolume(Game.getGameWorld().getMenu().getFxSlider().getValue() / 100);
                    reloadMediaPLayer.play();
                    if (currentBullets > 0)
                        fillClip(WeaponType.RPG);
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
                        reloadMediaPLayer = new MediaPlayer(GameMedia.PISTOL_RELOAD);
                        reloadMediaPLayer.setVolume(Game.getGameWorld().getMenu().getFxSlider().getValue() / 100);
                        reloadMediaPLayer.play();
                        fillClip(type);
                    }
                }
                case WeaponType.MACHINE_GUN -> {
                    if (currentClip < FULL_MACHINE_GUN_CLIP) {
                        reloadMediaPLayer = new MediaPlayer(GameMedia.MACHINE_GUN_RELOAD);
                        reloadMediaPLayer.setVolume(Game.getGameWorld().getMenu().getFxSlider().getValue() / 100);
                        reloadMediaPLayer.play();
                        fillClip(type);
                    }
                }
                case WeaponType.RPG -> {
                    if (currentClip < FULL_RPG_CLIP) {
                        reloadMediaPLayer = new MediaPlayer(GameMedia.RPG_RELOAD);
                        reloadMediaPLayer.setVolume(Game.getGameWorld().getMenu().getFxSlider().getValue() / 100);
                        reloadMediaPLayer.play();
                        fillClip(type);
                    }
                }
            }
        }
    }

    public void changeLevel() {
        if (Game.getGameWorld().getLevel().getCurrentLevelNumber() == Level.BOSS_LEVEL) {
            Game.getGameWorld().getGameRoot().getChildren().remove(this);
            return;
        }

        if (Game.getGameWorld().getLevel().getCurrentLevelNumber() == Level.SECOND_LEVEL) {
            setTranslateX(BLOCK_SIZE * 26);
            setTranslateY(BLOCK_SIZE * 12 - 20);
            imageView.setViewport(new Rectangle2D(400, 80, 56, 18));
        } else if (Game.getGameWorld().getLevel().getCurrentLevelNumber() == Level.THIRD_LEVEL) {
            setTranslateX(BLOCK_SIZE * 24);
            setTranslateY(BLOCK_SIZE * 12 - 20);
            imageView.setViewport(new Rectangle2D(680, 80, 65, 18));
        }
        if (!Game.getGameWorld().getGameRoot().getChildren().contains(this))
            Game.getGameWorld().getGameRoot().getChildren().add(this);
    }

    public void pickUpWeapon() {
        if (!nowReloading) {
            switch (Game.getGameWorld().getLevel().getCurrentLevelNumber()) {
                case Level.FIRST_LEVEL -> {
                    canChoosePistol = true;
                    type = WeaponType.PISTOL;
                    Game.getGameWorld().getBooker().changeWeaponAnimation();
                    pistolClip = currentClip = FULL_PISTOL_CLIP;
                    if (pistolBullets > 0)
                        currentBullets = pistolBullets;
                    else
                        currentBullets = pistolBullets = START_PISTOL_BULLETS;
                    GameMedia.WILL_WORK.play(Game.getGameWorld().getMenu().getVoiceSlider().getValue() / 100);
                }
                case Level.SECOND_LEVEL -> {
                    pistolClip = currentClip;
                    canChooseMachineGun = true;
                    type = WeaponType.MACHINE_GUN;
                    pistolBullets = currentBullets;
                    Game.getGameWorld().getBooker().changeWeaponAnimation();
                    machineGunClip = currentClip = FULL_MACHINE_GUN_CLIP;
                    if (machineGunBullets > 0)
                        currentBullets = machineGunBullets;
                    else
                        machineGunBullets = currentBullets = START_MACHINE_GUN_BULLETS;
                    GameMedia.GREAT.play(Game.getGameWorld().getMenu().getVoiceSlider().getValue() / 100);
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

                    if (rpgBullets > 0)
                        currentBullets = rpgBullets;
                    else
                        rpgBullets = currentBullets = START_RPG_BULLETS;
                    GameMedia.GREAT.play(Game.getGameWorld().getMenu().getVoiceSlider().getValue() / 100);
                }
            }
            setTranslateX(0);
            setTranslateY(0);
            Game.getGameWorld().getGameRoot().getChildren().remove(this);
        }
    }

    public void clearBullets() {
        for (Bullet bullet : bullets)
            bulletsPool.put(bullet);
        Game.getGameWorld().getGameRoot().getChildren().removeAll(bullets);
        bullets.clear();
    }

    public void setDifficultyType() {
        switch (Game.getGameWorld().getDifficultyType()) {
            case DifficultyType.MARIK -> {
                rpgDamage = 300;
                bulletDamage = 30;
            }
            case DifficultyType.EASY -> {
                rpgDamage = 250;
                bulletDamage = 20;
            }
            case DifficultyType.MEDIUM -> {
                rpgDamage = 200;
                bulletDamage = 15;
            }
            case DifficultyType.HARD -> {
                rpgDamage = 150;
                bulletDamage = 10;
            }
            case DifficultyType.HARDCORE -> {
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
                    }
                }
            }
        }
    }

    public int getRpgDamage() {
        return rpgDamage;
    }

    public int getRpgBullets() {
        return rpgBullets;
    }

    public int getCurrentClip() {
        return currentClip;
    }

    public WeaponType getType() {
        return type;
    }

    public int getBulletDamage() {
        return bulletDamage;
    }

    public int getPistolBullets() {
        return pistolBullets;
    }

    public int getCurrentBullets() {
        return currentBullets;
    }

    public boolean isCanChooseRpg() {
        return canChooseRpg;
    }

    public int getMachineGunBullets() {
        return machineGunBullets;
    }

    public boolean isCanChoosePistol() {
        return canChoosePistol;
    }

    public boolean isCanChooseMachineGun() {
        return canChooseMachineGun;
    }

    public void setCanReload(boolean value) {
        canReload = value;
    }

    public void setSingleShot(boolean value) {
        singleShot = value;
    }

    public void setPistolBullets(int bullets) {
        pistolBullets = bullets;
    }

    public void setRpgBullets(int rpgBullets) {
        this.rpgBullets = rpgBullets;
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
                Game.getGameWorld().getGameRoot().getChildren().remove(bullet);
                break;
            }
        }
    }
}



