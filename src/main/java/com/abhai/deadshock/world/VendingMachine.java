package com.abhai.deadshock.world;

import com.abhai.deadshock.Game;
import com.abhai.deadshock.characters.Animatable;
import com.abhai.deadshock.characters.enemies.Enemy;
import com.abhai.deadshock.hud.Tutorial;
import com.abhai.deadshock.types.PurchaseType;
import com.abhai.deadshock.types.WeaponType;
import com.abhai.deadshock.utils.GameMedia;
import com.abhai.deadshock.world.levels.Level;
import javafx.animation.FadeTransition;
import javafx.geometry.Rectangle2D;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.util.Duration;

import java.nio.file.Paths;

import static com.abhai.deadshock.world.levels.Block.BLOCK_SIZE;

public class VendingMachine extends Pane {
    private static final int WIDTH = 920;
    private static final int HEIGHT = 597;
    private static final int BUTTON_WIDTH = 356;
    private static final int BUTTON_HEIGHT = 53;
    private static final int FIRST_LEVEL_X = BLOCK_SIZE * 188;
    private static final int SECOND_LEVEL_X = BLOCK_SIZE * 157;
    private static final int FIRST_LEVEL_Y = BLOCK_SIZE * 2 - 6;
    private static final int THIRD_LEVEL_Y = BLOCK_SIZE * 4 - 6;
    private static final int SECOND_LEVEL_Y = BLOCK_SIZE * 7 - 6;
    private static final double THIRD_LEVEL_X = BLOCK_SIZE * 137.5;

    private Button bigSaltButton;
    private Button littleSaltButton;
    private Button bigMedicineButton;
    private Button pistolBulletsButton;
    private Button littleMedicineButton;
    private Button machineGunBulletsButton;

    private boolean isShown;
    private PurchaseType purchaseType;
    private FadeTransition fadeTransition;
    private ImageView vendingMachineImage;
    private ImageView vendingMachineMenuImage;

    public VendingMachine() {
        purchaseType = PurchaseType.BIG_MEDICINE;
        initializeImages();
        isShown = false;
        changeLevel();
    }

    public void openMenu() {
        if (Game.getGameWorld().getLevel().getCurrentLevelNumber() == Level.FIRST_LEVEL)
            Tutorial.setVendingMachineMenu();

        isShown = true;
        Tutorial.delete();
        Game.getGameWorld().setActive(false);
        Game.getGameWorld().getMenu().getMusic().pause();

        for (Enemy enemy : Game.getGameWorld().getEnemies())
            if (enemy instanceof Animatable animatable)
                animatable.stopAnimation();

        GameMedia.AUDIO_CLIP_OPEN_MENU.play(Game.getGameWorld().getMenu().getFxSlider().getValue() / 100);
        fadeTransition.setByValue(0);
        fadeTransition.setToValue(1);
        fadeTransition.play();
    }

    public void hideMenu() {
        if (Game.getGameWorld().getLevel().getCurrentLevelNumber() == Level.FIRST_LEVEL)
            Tutorial.deleteVendingMachineMenu();

        isShown = false;
        Game.getGameWorld().setActive(true);
        Game.getGameWorld().getMenu().getMusic().play();
        fadeTransition.setByValue(1);
        fadeTransition.setToValue(0);
        fadeTransition.play();
    }

    public void changeLevel() {
        switch (Game.getGameWorld().getLevel().getCurrentLevelNumber()) {
            case Level.FIRST_LEVEL -> {
                vendingMachineImage.setTranslateX(FIRST_LEVEL_X);
                vendingMachineImage.setTranslateY(FIRST_LEVEL_Y);
                vendingMachineMenuImage.setViewport(new Rectangle2D(0, 0, WIDTH, HEIGHT));
            }
            case Level.SECOND_LEVEL -> {
                vendingMachineImage.setTranslateX(SECOND_LEVEL_X);
                vendingMachineImage.setTranslateY(SECOND_LEVEL_Y);
                vendingMachineMenuImage.setViewport(new Rectangle2D(WIDTH, 0, WIDTH, HEIGHT));
            }
            case Level.THIRD_LEVEL -> {
                vendingMachineImage.setTranslateX(THIRD_LEVEL_X);
                vendingMachineImage.setTranslateY(THIRD_LEVEL_Y);
                vendingMachineMenuImage.setViewport(new Rectangle2D(920, 0, 920, 597));
            }
            case Level.BOSS_LEVEL -> Game.getGameWorld().getGameRoot().getChildren().remove(this);
        }
        if (!Game.getGameWorld().getGameRoot().getChildren().contains(this) && Game.getGameWorld().getLevel().getCurrentLevelNumber() != Level.BOSS_LEVEL)
            Game.getGameWorld().getGameRoot().getChildren().add(this);
    }

    public void makePurchase() {
        switch (purchaseType) {
            case PurchaseType.BIG_MEDICINE -> {
                if (Game.getGameWorld().getBooker().getMoney() >= PurchaseType.BIG_MEDICINE.price && Game.getGameWorld().getBooker().getHp() < 100) {
                    Game.getGameWorld().getBooker().setMoney(Game.getGameWorld().getBooker().getMoney() - PurchaseType.BIG_MEDICINE.price);
                    GameMedia.AUDIO_CLIP_PURCHASE.play(Game.getGameWorld().getMenu().getFxSlider().getValue() / 100);
                    Game.getGameWorld().getBooker().setHp(Game.getGameWorld().getBooker().getHp() + PurchaseType.BIG_MEDICINE.value);
                }
            }
            case PurchaseType.LITTLE_MEDICINE -> {
                if (Game.getGameWorld().getBooker().getMoney() >= PurchaseType.LITTLE_MEDICINE.price && Game.getGameWorld().getBooker().getHp() < 100) {
                    Game.getGameWorld().getBooker().setMoney(Game.getGameWorld().getBooker().getMoney() - PurchaseType.LITTLE_MEDICINE.price);
                    Game.getGameWorld().getBooker().setHp(Game.getGameWorld().getBooker().getHp() + PurchaseType.LITTLE_MEDICINE.value);
                    GameMedia.AUDIO_CLIP_PURCHASE.play(Game.getGameWorld().getMenu().getFxSlider().getValue() / 100);
                }
            }
            case PurchaseType.BIG_SALT -> {
                if (Game.getGameWorld().getBooker().getMoney() >= PurchaseType.BIG_SALT.price && Game.getGameWorld().getBooker().getSalt() < 100) {
                    Game.getGameWorld().getBooker().setSalt(Game.getGameWorld().getBooker().getSalt() + PurchaseType.BIG_SALT.value);
                    Game.getGameWorld().getBooker().setMoney(Game.getGameWorld().getBooker().getMoney() - PurchaseType.BIG_SALT.price);
                    GameMedia.AUDIO_CLIP_PURCHASE.play(Game.getGameWorld().getMenu().getFxSlider().getValue() / 100);
                }
            }
            case PurchaseType.LITTLE_SALT -> {
                if (Game.getGameWorld().getBooker().getMoney() >= PurchaseType.LITTLE_SALT.price && Game.getGameWorld().getBooker().getSalt() < 100) {
                    GameMedia.AUDIO_CLIP_PURCHASE.play(Game.getGameWorld().getMenu().getFxSlider().getValue() / 100);
                    Game.getGameWorld().getBooker().setSalt(Game.getGameWorld().getBooker().getSalt() + PurchaseType.LITTLE_SALT.value);
                    Game.getGameWorld().getBooker().setMoney(Game.getGameWorld().getBooker().getMoney() - PurchaseType.LITTLE_SALT.price);
                }
            }
            case PurchaseType.PISTOL_BULLETS -> {
                if (Game.getGameWorld().getBooker().getMoney() >= PurchaseType.PISTOL_BULLETS.price) {
                    GameMedia.AUDIO_CLIP_PURCHASE.play(Game.getGameWorld().getMenu().getFxSlider().getValue() / 100);
                    Game.getGameWorld().getBooker().setMoney(Game.getGameWorld().getBooker().getMoney() - PurchaseType.PISTOL_BULLETS.price);
                    if (Game.getGameWorld().getBooker().getWeapon().getType() == WeaponType.PISTOL)
                        Game.getGameWorld().getBooker().getWeapon().setCurrentBullets(Game.getGameWorld().getBooker().getWeapon().getCurrentBullets() + PurchaseType.PISTOL_BULLETS.value);
                    else
                        Game.getGameWorld().getBooker().getWeapon().setPistolBullets(Game.getGameWorld().getBooker().getWeapon().getPistolBullets() + PurchaseType.PISTOL_BULLETS.value);
                }
            }
            case PurchaseType.MACHINE_GUN_BULLETS -> {
                if (Game.getGameWorld().getBooker().getMoney() >= PurchaseType.MACHINE_GUN_BULLETS.price) {
                    GameMedia.AUDIO_CLIP_PURCHASE.play(Game.getGameWorld().getMenu().getFxSlider().getValue() / 100);
                    Game.getGameWorld().getBooker().setMoney(Game.getGameWorld().getBooker().getMoney() - PurchaseType.MACHINE_GUN_BULLETS.price);
                    if (Game.getGameWorld().getBooker().getWeapon().getType() == WeaponType.MACHINE_GUN)
                        Game.getGameWorld().getBooker().getWeapon().setCurrentBullets(Game.getGameWorld().getBooker().getWeapon().getCurrentBullets() + PurchaseType.MACHINE_GUN_BULLETS.value);
                    else
                        Game.getGameWorld().getBooker().getWeapon().setMachineGunBullets(Game.getGameWorld().getBooker().getWeapon().getMachineGunBullets() + PurchaseType.MACHINE_GUN_BULLETS.value);
                }
            }
        }
        Game.getGameWorld().getHud().update();
    }

    public void initializeButtons() {
        bigSaltButton = new Button();
        littleSaltButton = new Button();
        bigMedicineButton = new Button();
        pistolBulletsButton = new Button();
        littleMedicineButton = new Button();
        machineGunBulletsButton = new Button();

        setButton(bigMedicineButton, vendingMachineMenuImage.getTranslateY() + 166);
        setButton(littleMedicineButton, bigMedicineButton.getTranslateY() + BUTTON_HEIGHT);
        setButton(bigSaltButton, littleMedicineButton.getTranslateY() + BUTTON_HEIGHT);
        setButton(littleSaltButton, bigSaltButton.getTranslateY() + BUTTON_HEIGHT);
        setButton(pistolBulletsButton, littleSaltButton.getTranslateY() + BUTTON_HEIGHT);
        setButton(machineGunBulletsButton, pistolBulletsButton.getTranslateY() + BUTTON_HEIGHT);

        if (Game.getGameWorld().getLevel().getCurrentLevelNumber() == Level.FIRST_LEVEL)
            addButtonsListeners(0);
        else if (Game.getGameWorld().getLevel().getCurrentLevelNumber() != Level.BOSS_LEVEL)
            addButtonsListeners(920);
    }

    private void initializeImages() {
        vendingMachineMenuImage = new ImageView(new Image(Paths.get("resources", "images", "vendingMachine", "menu.png").toUri().toString()));
        vendingMachineMenuImage.setTranslateX(200);
        vendingMachineMenuImage.setTranslateY(50);

        vendingMachineImage = new ImageView(new Image(Paths.get("resources", "images", "vendingMachine", "vendingMachine.png").toUri().toString()));
        fadeTransition = new FadeTransition(Duration.seconds(0.3), vendingMachineMenuImage);
        fadeTransition.setByValue(1);
        fadeTransition.setToValue(0);
        fadeTransition.play();

        getChildren().add(vendingMachineImage);
        Game.getGameWorld().getAppRoot().getChildren().add(vendingMachineMenuImage);
    }

    public boolean isShown() {
        return isShown;
    }

    private void addButtonsListeners(int xOffset) {
        bigMedicineButton.setOnMouseClicked(event -> {
            if (isShown) {
                purchaseType = PurchaseType.BIG_MEDICINE;
                GameMedia.AUDIO_CLIP_CHANGE_ITEM.play(Game.getGameWorld().getMenu().getFxSlider().getValue() / 100);
                vendingMachineMenuImage.setViewport(new Rectangle2D(xOffset, 0, 920, 597));
            }
        });
        littleMedicineButton.setOnMouseClicked(event -> {
            if (isShown) {
                purchaseType = PurchaseType.LITTLE_MEDICINE;
                GameMedia.AUDIO_CLIP_CHANGE_ITEM.play(Game.getGameWorld().getMenu().getFxSlider().getValue() / 100);
                vendingMachineMenuImage.setViewport(new Rectangle2D(xOffset, 597, 920, 597));
            }
        });
        bigSaltButton.setOnMouseClicked(event -> {
            if (isShown) {
                purchaseType = PurchaseType.BIG_SALT;
                GameMedia.AUDIO_CLIP_CHANGE_ITEM.play(Game.getGameWorld().getMenu().getFxSlider().getValue() / 100);
                vendingMachineMenuImage.setViewport(new Rectangle2D(xOffset, 1194, 920, 597));
            }
        });
        littleSaltButton.setOnMouseClicked(event -> {
            if (isShown) {
                purchaseType = PurchaseType.LITTLE_SALT;
                GameMedia.AUDIO_CLIP_CHANGE_ITEM.play(Game.getGameWorld().getMenu().getFxSlider().getValue() / 100);
                vendingMachineMenuImage.setViewport(new Rectangle2D(xOffset, 1791, 920, 597));
            }
        });
        pistolBulletsButton.setOnMouseClicked(event -> {
            if (isShown) {
                purchaseType = PurchaseType.PISTOL_BULLETS;
                GameMedia.AUDIO_CLIP_CHANGE_ITEM.play(Game.getGameWorld().getMenu().getFxSlider().getValue() / 100);
                vendingMachineMenuImage.setViewport(new Rectangle2D(xOffset, 2388, 920, 597));
            }
        });
        if (xOffset == WIDTH)
            machineGunBulletsButton.setOnMouseClicked(event -> {
                if (isShown) {
                    purchaseType = PurchaseType.MACHINE_GUN_BULLETS;
                    GameMedia.AUDIO_CLIP_CHANGE_ITEM.play(Game.getGameWorld().getMenu().getFxSlider().getValue() / 100);
                    vendingMachineMenuImage.setViewport(new Rectangle2D(xOffset, 2985, 920, 597));
                }
            });
    }

    private void setButton(Button button, double y) {
        button.setOpacity(0);
        button.setTranslateY(y);
        button.setPrefWidth(BUTTON_WIDTH);
        button.setPrefHeight(BUTTON_HEIGHT);
        Game.getGameWorld().getAppRoot().getChildren().add(button);
        button.setTranslateX(vendingMachineMenuImage.getTranslateX() + 136);
    }

    public ImageView getVendingMachineImage() {
        return vendingMachineImage;
    }
}
