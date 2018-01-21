package game.Weapon;

import game.Characters.SpriteAnimation;
import game.Levels.Level;
import game.Game;
import game.Sounds;
import javafx.geometry.Rectangle2D;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

import java.io.File;

public class Weapon extends Pane {
    ImageView explosion;
    SpriteAnimation explosionAnimation;

    private ImageView gun = new ImageView(new Image("file:/../images/weapons/weapons.png"));

    private int clip = 0;
    private int shootInterval = 0;
    private int bullets = 0;
    private byte damage = 0;
    private short rpgDamage = 0;

    private boolean nowReloading = false;
    private boolean canReload = true;
    private boolean singleShot = true;
    private boolean canChoosePistol = false;
    private boolean canChooseMachineGun = false;
    private boolean canChooseRPG = false;

    private String name = "";


    //for level1
    public Weapon() {
        gun.setViewport( new Rectangle2D(265, 88, 33, 18) );
        setTranslateX(Level.BLOCK_SIZE * 38);
        setTranslateY(Level.BLOCK_SIZE * 12 - 18);

        getChildren().add(gun);
        Game.gameRoot.getChildren().add(this);
    }



    //for level2
    public Weapon(boolean value) {
        canChoosePistol = value;
        if (canChoosePistol) {
            name = "pistol";
            Game.booker.changeAnimation(name);
        }
        gun.setViewport( new Rectangle2D(400, 80, 56, 18) );
        setTranslateX(Level.BLOCK_SIZE * 26);
        setTranslateY(Level.BLOCK_SIZE * 12 - 20);

        getChildren().add(gun);
        Game.gameRoot.getChildren().add(this);
    }

    //for level3
    public Weapon(boolean pistol, boolean machineGun) {
        canChoosePistol = pistol;
        canChooseMachineGun = machineGun;
        if (canChooseMachineGun)
            name = "machine_gun";
        else if (canChoosePistol)
            name = "pistol";
        Game.booker.changeAnimation(name);
        gun = new ImageView("file:/../images/weapons/rpg.png");
        setTranslateX(Level.BLOCK_SIZE * 24);
        setTranslateY(Level.BLOCK_SIZE * 12 - 20);

        explosion = new ImageView("file:/../images/explosion.png");
        explosion.setFitWidth(128);
        explosion.setFitHeight(128);
        explosionAnimation = new SpriteAnimation(explosion, Duration.seconds(1), 16, 4, 0, 0, 128, 128);
        explosionAnimation.setCycleCount(1);

        getChildren().add(gun);
        Game.gameRoot.getChildren().add(this);
    }

    //for bossLevel
    public Weapon(boolean pistol, boolean machineGun, boolean rpg) {
        canChoosePistol = pistol;
        canChooseMachineGun = machineGun;
        canChooseRPG = rpg;
        if (canChooseRPG)
            name = "rpg";
        else if (canChooseMachineGun)
            name = "machine_gun";
        else if (canChoosePistol)
            name = "pistol";
        Game.booker.changeAnimation(name);

        explosion = new ImageView("file:/../images/explosion.png");
        explosion.setFitWidth(128);
        explosion.setFitHeight(128);
        explosionAnimation = new SpriteAnimation(explosion, Duration.seconds(1), 16, 4, 0, 0, 128, 128);
        explosionAnimation.setCycleCount(1);
    }




    public String getName() {
        return name;
    }


    public int getWeaponClip() {
        return clip;
    }


    public void setWeaponClip(long value) {
        clip = (byte)value;
    }


    public int getBullets() {
        return bullets;
    }


    public void setBullets(long value) {
        bullets = (short)value;
    }


    public void setCanReload(boolean value) {
        canReload = value;
    }


    public boolean isCanReload() {
        return canReload;
    }


    public boolean isNowReloading() {
        return nowReloading;
    }


    public void setSingleShot(boolean value) {
        singleShot = value;
    }


    public byte getDamage() {
        return damage;
    }


    public short getRpgDamage() {
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





    public void changeLevel(long level) {
        if (level == 1) {
            gun.setViewport( new Rectangle2D(400, 80, 56, 18) );
            setTranslateX(Level.BLOCK_SIZE * 26);
            setTranslateY(Level.BLOCK_SIZE * 12 - 20);
            setVisible(true);
        }else if (level == 2) {
            getChildren().remove(gun);
            gun = new ImageView("file:/../images/weapons/rpg.png");
            getChildren().add(gun);
            setTranslateX(Level.BLOCK_SIZE * 24);
            setTranslateY(Level.BLOCK_SIZE * 12 - 20);
            setVisible(true);
        }
    }


    public void pickUpWeapon() {
        if (!nowReloading) {
            Game.booker.setCanChangeAnimation(true);
            switch ((int) Game.levelNumber) {
                case 0:
                    Sounds.audioClipFit.play(Game.menu.voiceSlider.getValue() / 100);
                    name = "pistol";
                    Game.booker.changeAnimation(name);
                    canChoosePistol = true;
                    WeaponData.pistolClip = clip = 20;
                    WeaponData.pistolBullets = bullets = 80;
                    break;
                case 1:
                    Sounds.excellentVoice.play(Game.menu.voiceSlider.getValue() / 100);
                    name = "machine_gun";
                    Game.booker.changeAnimation(name);
                    canChooseMachineGun = true;
                    WeaponData.pistolClip = clip;
                    WeaponData.pistolBullets = bullets;
                    WeaponData.machineGunClip = clip = 30;
                    WeaponData.machineGunBullets = bullets = 120;
                    break;
                case 2:
                    explosion = new ImageView("file:/../images/explosion.png");
                    explosion.setFitWidth(128);
                    explosion.setFitHeight(128);
                    explosionAnimation = new SpriteAnimation(explosion, Duration.seconds(1), 16, 4, 0, 0, 128, 128);
                    explosionAnimation.setCycleCount(1);

                    Sounds.excellentVoice.play(Game.menu.voiceSlider.getValue() / 100);
                    if (name.equals("pistol")) {
                        WeaponData.pistolClip = clip;
                        WeaponData.pistolBullets = bullets;
                    } else {
                        WeaponData.machineGunClip = clip;
                        WeaponData.machineGunBullets = bullets;
                    }
                    name = "rpg";
                    Game.booker.changeAnimation(name);
                    canChooseRPG = true;
                    WeaponData.rpgClip = clip = 1;
                    WeaponData.rpgBullets = bullets = 30;
                    break;
            }

            setTranslateX(0);
            setTranslateY(0);
            setVisible(false);
        }
    }


    public void setDamage() {
        switch (Game.difficultyLevelText) {
            case "marik":
                damage = 30;
                rpgDamage = 300;
                break;
            case "easy":
                damage = 20;
                rpgDamage = 250;
                break;
            case "normal":
                damage = 15;
                rpgDamage = 200;
                break;
            case "high":
                damage = 10;
                rpgDamage = 150;
                break;
            case "hardcore":
                damage = 8;
                rpgDamage = 150;
                break;
        }
    }


    public void reload() {
        if (canReload && !nowReloading && bullets > 0) {
            canReload = false;
            switch (name) {
                case "pistol":
                    if (clip < 20) {
                        Sounds.pistolReload = new MediaPlayer(new Media(
                                new File("file:/../sounds/fx/weapon/pistol_reload.mp3").toURI().toString()));
                        Sounds.pistolReload.setVolume(Game.menu.fxSlider.getValue() / 100);
                        Sounds.pistolReload.play();
                        fillClip(name, 20);
                    }
                    break;
                case "machine_gun":
                    if (clip < 30) {
                        Sounds.machineGunReload = new MediaPlayer(new Media(
                                new File("file:/../sounds/fx/weapon/machine_gun_reload.mp3").toURI().toString()));
                        Sounds.machineGunReload.setVolume(Game.menu.fxSlider.getValue() / 100);
                        Sounds.machineGunReload.play();
                        fillClip(name, 30);
                    }
                    break;
                case "rpg":
                    if (clip < 1)
                        fillClip(name, 1);
                    break;
            }
        }
    }


    private void fillClip(String name, int value) {
        nowReloading = true;
        canReload = false;
        switch(name) {
            case "pistol":
                Sounds.pistolReload.setOnEndOfMedia( () -> {
                    while (clip < value)
                        if (bullets > 0) {
                            clip++;
                            bullets--;
                        } else
                            break;
                    nowReloading = false;
                });
                break;
            case "machine_gun":
                Sounds.machineGunReload.setOnEndOfMedia( () -> {
                    while (clip < value)
                        if (bullets > 0) {
                            clip++;
                            bullets--;
                        } else
                            break;
                    nowReloading = false;
                });
                break;
            case "rpg":
                Sounds.rpgShootAndReload.setOnEndOfMedia( () -> {
                    while (clip < value)
                        if (bullets > 0) {
                            clip++;
                            bullets--;
                        } else
                            break;
                    nowReloading = false;
                });
                break;
        }
    }


    public void shoot() {
        switch (name) {
            case "machine_gun":
                if (shootInterval > 5) {
                    Sounds.machineGunShoot.play(Game.menu.fxSlider.getValue() / 100);
                    Game.bullets.add(new Bullet(name));
                    clip--;
                    shootInterval = 0;
                }
                break;
            case "pistol":
                if (singleShot) {
                    Sounds.pistolShoot.play(Game.menu.fxSlider.getValue() / 100);
                    Game.bullets.add(new Bullet(name));
                    clip--;
                    singleShot = false;
                }
                break;
            case "rpg":
                Sounds.rpgShootAndReload = new MediaPlayer(new Media(
                        new File("file:/../sounds/fx/weapon/rpgShootAndReload.mp3").toURI().toString()));
                Sounds.rpgShootAndReload.setVolume(Game.menu.fxSlider.getValue() / 100);
                Sounds.rpgShootAndReload.play();
                Game.bullets.add(new RpgBullet());
                clip--;
                reload();
                break;
        }
    }


    public void changeWeapon(String str) {
        if (!name.equals("")) {
            switch (str) {
                case "pistol":
                    if (canChoosePistol) {
                        partChangeWeapon();
                        name = "pistol";
                        clip = (int)WeaponData.pistolClip;
                        bullets = (int)WeaponData.pistolBullets;
                    }
                    break;
                case "machine_gun":
                    if (canChooseMachineGun) {
                        partChangeWeapon();
                        name = "machine_gun";
                        clip = (int)WeaponData.machineGunClip;
                        bullets = (int)WeaponData.machineGunBullets;
                    }
                    break;
                case "rpg":
                    if (canChooseRPG) {
                        partChangeWeapon();
                        name = "rpg";
                        clip = WeaponData.rpgClip;
                        bullets = WeaponData.rpgBullets;
                    }
                    break;
            }
            Game.booker.setCanChangeAnimation(true);
            Game.booker.changeAnimation(name);
        }
    }


    private void partChangeWeapon() {
        switch (name) {
            case "pistol":
                WeaponData.pistolClip = clip;
                WeaponData.pistolBullets = bullets;
                break;
            case "machine_gun":
                WeaponData.machineGunClip = clip;
                WeaponData.machineGunBullets = bullets;
                break;
            case "rpg":
                WeaponData.rpgClip = clip;
                WeaponData.rpgBullets = bullets;
                break;
        }
    }


    public void update() {
        shootInterval++;
    }


    public static class WeaponData {
        public static long pistolClip = 0;
        public static long machineGunClip = 0;
        static int rpgClip = 0;

        public static long pistolBullets = 0;
        public static long machineGunBullets = 0;
        static int rpgBullets = 0;
    }
}



