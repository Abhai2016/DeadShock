package com.abhai.deadshock.world.vendingMachine;

import com.abhai.deadshock.Game;
import com.abhai.deadshock.characters.Animatable;
import com.abhai.deadshock.characters.enemies.Enemy;
import com.abhai.deadshock.hud.Tutorial;
import com.abhai.deadshock.utils.Sounds;
import com.abhai.deadshock.weapons.WeaponType;
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
        isShown = false;
        purchaseType = PurchaseType.BIG_MEDICINE;
    }

    public void init() {
        initializeImages();
        initializePosition();
        initializeButtons();
    }

    public void openMenu() {
        if (Game.levelNumber == Level.FIRST_LEVEL)
            Tutorial.setVendingMachineMenu();

        isShown = true;
        Tutorial.delete();
        Game.active = false;
        Game.menu.getMusic().pause();

        for (Enemy enemy : Game.enemies)
            if (enemy instanceof Animatable animatable)
                animatable.stopAnimation();

        Sounds.audioClipOpenMenu.play(Game.menu.getFxSlider().getValue() / 100);
        fadeTransition.setByValue(0);
        fadeTransition.setToValue(1);
        fadeTransition.play();
    }

    public void hideMenu() {
        if (Game.levelNumber == Level.FIRST_LEVEL)
            Tutorial.deleteVendingMachineMenu();

        isShown = false;
        Game.active = true;
        Game.menu.getMusic().play();
        fadeTransition.setByValue(1);
        fadeTransition.setToValue(0);
        fadeTransition.play();
    }

    public void makePurchase() {
        switch (purchaseType) {
            case PurchaseType.BIG_MEDICINE -> {
                if (Game.booker.getMoney() >= PurchaseType.BIG_MEDICINE.price && Game.booker.getHP() < 100) {
                    Game.booker.setMoney(Game.booker.getMoney() - PurchaseType.BIG_MEDICINE.price);
                    Sounds.audioClipPurchase.play(Game.menu.getFxSlider().getValue() / 100);
                    Game.booker.setHP(Game.booker.getHP() + PurchaseType.BIG_MEDICINE.value);
                }
            }
            case PurchaseType.LITTLE_MEDICINE -> {
                if (Game.booker.getMoney() >= PurchaseType.LITTLE_MEDICINE.price && Game.booker.getHP() < 100) {
                    Game.booker.setMoney(Game.booker.getMoney() - PurchaseType.LITTLE_MEDICINE.price);
                    Game.booker.setHP(Game.booker.getHP() + PurchaseType.LITTLE_MEDICINE.value);
                    Sounds.audioClipPurchase.play(Game.menu.getFxSlider().getValue() / 100);
                }
            }
            case PurchaseType.BIG_SALT -> {
                if (Game.booker.getMoney() >= PurchaseType.BIG_SALT.price && Game.booker.getSalt() < 100) {
                    Game.booker.setSalt(Game.booker.getSalt() + PurchaseType.BIG_SALT.value);
                    Game.booker.setMoney(Game.booker.getMoney() - PurchaseType.BIG_SALT.price);
                    Sounds.audioClipPurchase.play(Game.menu.getFxSlider().getValue() / 100);
                }
            }
            case PurchaseType.LITTLE_SALT -> {
                if (Game.booker.getMoney() >= PurchaseType.LITTLE_SALT.price && Game.booker.getSalt() < 100) {
                    Sounds.audioClipPurchase.play(Game.menu.getFxSlider().getValue() / 100);
                    Game.booker.setSalt(Game.booker.getSalt() + PurchaseType.LITTLE_SALT.value);
                    Game.booker.setMoney(Game.booker.getMoney() - PurchaseType.LITTLE_SALT.price);
                }
            }
            case PurchaseType.PISTOL_BULLETS -> {
                if (Game.booker.getMoney() >= PurchaseType.PISTOL_BULLETS.price) {
                    Sounds.audioClipPurchase.play(Game.menu.getFxSlider().getValue() / 100);
                    Game.booker.setMoney(Game.booker.getMoney() - PurchaseType.PISTOL_BULLETS.price);
                    if (Game.booker.getWeapon().getType() == WeaponType.PISTOL)
                        Game.booker.getWeapon().setCurrentBullets(Game.booker.getWeapon().getCurrentBullets() + PurchaseType.PISTOL_BULLETS.value);
                    else
                        Game.booker.getWeapon().setPistolBullets(Game.booker.getWeapon().getPistolBullets() + PurchaseType.PISTOL_BULLETS.value);
                }
            }
            case PurchaseType.MACHINE_GUN_BULLETS -> {
                if (Game.booker.getMoney() >= PurchaseType.MACHINE_GUN_BULLETS.price) {
                    Sounds.audioClipPurchase.play(Game.menu.getFxSlider().getValue() / 100);
                    Game.booker.setMoney(Game.booker.getMoney() - PurchaseType.MACHINE_GUN_BULLETS.price);
                    if (Game.booker.getWeapon().getType() == WeaponType.MACHINE_GUN)
                        Game.booker.getWeapon().setCurrentBullets(Game.booker.getWeapon().getCurrentBullets() + PurchaseType.MACHINE_GUN_BULLETS.value);
                    else
                        Game.booker.getWeapon().setMachineGunBullets(Game.booker.getWeapon().getMachineGunBullets() + PurchaseType.MACHINE_GUN_BULLETS.value);
                }
            }
        }
        Game.hud.update();
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

        if (Game.levelNumber == Level.FIRST_LEVEL)
            addButtonsListeners(0);
        else if (Game.levelNumber != Level.BOSS_LEVEL)
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
        Game.gameRoot.getChildren().add(this);
        Game.appRoot.getChildren().add(vendingMachineMenuImage);
    }

    public void initializePosition() {
        switch (Game.levelNumber) {
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
        }
        if (!Game.gameRoot.getChildren().contains(vendingMachineImage))
            Game.gameRoot.getChildren().add(vendingMachineImage);
    }

    public boolean isShown() {
        return isShown;
    }

    private void addButtonsListeners(int xOffset) {
        bigMedicineButton.setOnMouseClicked(event -> {
            if (isShown) {
                purchaseType = PurchaseType.BIG_MEDICINE;
                Sounds.audioClipChangeItem.play(Game.menu.getFxSlider().getValue() / 100);
                vendingMachineMenuImage.setViewport(new Rectangle2D(xOffset, 0, 920, 597));
            }
        });
        littleMedicineButton.setOnMouseClicked(event -> {
            if (isShown) {
                purchaseType = PurchaseType.LITTLE_MEDICINE;
                Sounds.audioClipChangeItem.play(Game.menu.getFxSlider().getValue() / 100);
                vendingMachineMenuImage.setViewport(new Rectangle2D(xOffset, 597, 920, 597));
            }
        });
        bigSaltButton.setOnMouseClicked(event -> {
            if (isShown) {
                purchaseType = PurchaseType.BIG_SALT;
                Sounds.audioClipChangeItem.play(Game.menu.getFxSlider().getValue() / 100);
                vendingMachineMenuImage.setViewport(new Rectangle2D(xOffset, 1194, 920, 597));
            }
        });
        littleSaltButton.setOnMouseClicked(event -> {
            if (isShown) {
                purchaseType = PurchaseType.LITTLE_SALT;
                Sounds.audioClipChangeItem.play(Game.menu.getFxSlider().getValue() / 100);
                vendingMachineMenuImage.setViewport(new Rectangle2D(xOffset, 1791, 920, 597));
            }
        });
        pistolBulletsButton.setOnMouseClicked(event -> {
            if (isShown) {
                purchaseType = PurchaseType.PISTOL_BULLETS;
                Sounds.audioClipChangeItem.play(Game.menu.getFxSlider().getValue() / 100);
                vendingMachineMenuImage.setViewport(new Rectangle2D(xOffset, 2388, 920, 597));
            }
        });

        if (xOffset == WIDTH)
            machineGunBulletsButton.setOnMouseClicked(event -> {
                if (isShown) {
                    purchaseType = PurchaseType.MACHINE_GUN_BULLETS;
                    Sounds.audioClipChangeItem.play(Game.menu.getFxSlider().getValue() / 100);
                    vendingMachineMenuImage.setViewport(new Rectangle2D(xOffset, 2985, 920, 597));
                }
            });
    }

    private void setButton(Button button, double y) {
        button.setOpacity(0);
        button.setTranslateY(y);
        button.setPrefWidth(BUTTON_WIDTH);
        button.setPrefHeight(BUTTON_HEIGHT);
        Game.appRoot.getChildren().add(button);
        button.setTranslateX(vendingMachineMenuImage.getTranslateX() + 136);
    }

    public ImageView getVendingMachineImage() {
        return vendingMachineImage;
    }
}
