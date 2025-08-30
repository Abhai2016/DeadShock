package com.abhai.deadshock;

import com.abhai.deadshock.characters.Animatable;
import com.abhai.deadshock.characters.enemies.Enemy;
import com.abhai.deadshock.hud.Tutorial;
import com.abhai.deadshock.levels.Level;
import com.abhai.deadshock.utils.Sounds;
import com.abhai.deadshock.weapons.Weapon;
import com.abhai.deadshock.weapons.WeaponType;
import javafx.animation.FadeTransition;
import javafx.event.EventHandler;
import javafx.geometry.Rectangle2D;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.util.Duration;

import java.nio.file.Path;
import java.nio.file.Paths;

import static com.abhai.deadshock.levels.Block.BLOCK_SIZE;
import static javafx.scene.input.KeyEvent.KEY_PRESSED;

public class VendingMachine extends Pane {
    private final int BUTTON_HEIGHT = 53;
    private final int BUTTON_WIDTH = 356;

    private Path vendingMachinePath = Paths.get("resources", "images", "vendingMachine", "vendingMachine.png");
    private ImageView vendingMachine = new ImageView(new Image(vendingMachinePath.toUri().toString()));

    private Path vendingMachineMenuImagePath = Paths.get("resources", "images", "vendingMachine", "menu.png");
    private ImageView vendingMachineMenu = new ImageView(new Image(vendingMachineMenuImagePath.toUri().toString()));

    private FadeTransition ft = new FadeTransition(Duration.seconds(0.3), vendingMachineMenu);
    private Button btnBigMedicine = new Button();
    private Button btnLittleMedicine = new Button();
    private Button btnBigSalt = new Button();
    private Button btnLittleSalt = new Button();
    private Button btnPistolBullets = new Button();
    private Button btnMachineGunBullets = new Button();

    private Text moneyText = new Text();

    private String chosenElementMenu = "bigMedicine";
    private boolean isShown = false;

    private EventHandler<KeyEvent> eventHandler = new EventHandler<>() {
        @Override
        public void handle(KeyEvent event) {
            if (isShown) {
                if (event.getCode() == KeyCode.ENTER)
                    keyAction();
                else if (event.getCode() == KeyCode.ESCAPE) {
                    Game.active = true;
                    Game.menu.getMusic().play();
                    ft.setByValue(1);
                    ft.setToValue(0);
                    ft.play();
                    moneyText.setVisible(false);
                    if (Game.levelNumber == Level.FIRST_LEVEL)
                        Tutorial.deleteVendingMachineMenu();
                    Game.scene.removeEventFilter(KEY_PRESSED, this);
                    isShown = false;
                    event.consume();
                }
            }
        }
    };

    VendingMachine() {
        vendingMachineMenu.setTranslateX(200);
        vendingMachineMenu.setTranslateY(50);

        moneyText.setTranslateX(vendingMachineMenu.getTranslateX() + 291);
        moneyText.setTranslateY(vendingMachineMenu.getTranslateY() + 541);
        moneyText.setFont(Font.font("Arial", FontWeight.BOLD , 28));
        moneyText.setFill(Color.PALEGOLDENROD);
        Game.appRoot.getChildren().addAll(vendingMachineMenu, moneyText);

        ft.setByValue(1);
        ft.setToValue(0);
        ft.play();

        switch (Game.levelNumber) {
            case Level.FIRST_LEVEL:
                vendingMachine.setTranslateX(BLOCK_SIZE * 188);
                vendingMachine.setTranslateY(BLOCK_SIZE * 2 - 6);
                vendingMachineMenu.setViewport( new Rectangle2D(0, 0, 920, 597) );
                break;
            case Level.SECOND_LEVEL:
                vendingMachine.setTranslateX(BLOCK_SIZE * 157);
                vendingMachine.setTranslateY(BLOCK_SIZE * 7 - 6);
                vendingMachineMenu.setViewport( new Rectangle2D(920, 0, 920, 597) );
                break;
            case Level.THIRD_LEVEL:
                vendingMachine.setTranslateX(BLOCK_SIZE * 137.5);
                vendingMachine.setTranslateY(BLOCK_SIZE * 4 - 6);
                vendingMachineMenu.setViewport( new Rectangle2D(920, 0, 920, 597) );
                break;
        }

        getChildren().addAll(vendingMachine);
        Game.gameRoot.getChildren().add(this);
        addListener();
    }

    void changeLevel() {
        if (Game.levelNumber == Level.SECOND_LEVEL) {
            vendingMachine.setTranslateX(BLOCK_SIZE * 157);
            vendingMachine.setTranslateY(BLOCK_SIZE * 7 - 6);
            vendingMachineMenu.setViewport( new Rectangle2D(920, 0, 920, 597) );
        } else if (Game.levelNumber == Level.THIRD_LEVEL) {
            vendingMachine.setTranslateX(BLOCK_SIZE * 137.5);
            vendingMachine.setTranslateY(BLOCK_SIZE * 4 - 6);
            vendingMachineMenu.setViewport( new Rectangle2D(920, 0, 920, 597) );
        }
    }

    public void openMachineMenu() {
        if (!isShown) {
            isShown = true;

            if (Game.levelNumber == Level.FIRST_LEVEL)
                Tutorial.setVendingMachineMenu();

            Tutorial.delete();
            Game.active = false;
            Game.menu.getMusic().pause();
            Sounds.audioClipOpenMenu.play(Game.menu.getFxSlider().getValue() / 100);
            moneyText.setText(String.valueOf(Game.booker.getMoney()));

            for (Enemy enemy : Game.enemies)
                if (enemy instanceof Animatable animatable)
                    animatable.stopAnimation();

            ft.setByValue(0);
            ft.setToValue(1);
            ft.play();

            moneyText.setVisible(true);
        }
    }

    void createButtons() {
        btnBigMedicine.setPrefWidth(BUTTON_WIDTH);
        btnBigMedicine.setPrefHeight(BUTTON_HEIGHT);
        btnBigMedicine.setTranslateX(vendingMachineMenu.getTranslateX() + 136);
        btnBigMedicine.setTranslateY(vendingMachineMenu.getTranslateY() + 166);
        btnBigMedicine.setOpacity(0);
        Game.appRoot.getChildren().add(btnBigMedicine);

        setButton(btnLittleMedicine, btnBigMedicine);
        setButton(btnBigSalt, btnLittleMedicine);
        setButton(btnLittleSalt, btnBigSalt);
        setButton(btnPistolBullets, btnLittleSalt);
        setButton(btnMachineGunBullets, btnPistolBullets);
    }

    public void setDifficultyLevel() {
        if (Game.difficultyLevel == DifficultyLevel.MARIK)
            Game.appRoot.getChildren().remove(moneyText);
        else if (!Game.appRoot.getChildren().contains(moneyText))
            Game.appRoot.getChildren().add(moneyText);
    }

    private void setButton(Button button, Button preButton) {
        button.setPrefWidth(BUTTON_WIDTH);
        button.setPrefHeight(BUTTON_HEIGHT);
        button.setTranslateX(vendingMachineMenu.getTranslateX() + 136);
        button.setTranslateY(preButton.getTranslateY() + BUTTON_HEIGHT);
        button.setOpacity(0);
        Game.appRoot.getChildren().add(button);
    }

    private void addListener() {
        switch (Game.levelNumber) {
            case Level.FIRST_LEVEL -> {
                btnBigMedicine.setOnMouseClicked( event -> {
                    if (isShown) {
                        Sounds.audioClipChangeItem.play(Game.menu.getFxSlider().getValue() / 100);
                        vendingMachineMenu.setViewport( new Rectangle2D(0, 0, 920, 597) );
                        chosenElementMenu = "bigMedicine";
                    }
                });
                btnLittleMedicine.setOnMouseClicked( event -> {
                    if (isShown) {
                        Sounds.audioClipChangeItem.play(Game.menu.getFxSlider().getValue() / 100);
                        vendingMachineMenu.setViewport( new Rectangle2D(0, 597, 920, 597) );
                        chosenElementMenu = "littleMedicine";
                    }
                });
                btnBigSalt.setOnMouseClicked( event -> {
                    if (isShown) {
                        Sounds.audioClipChangeItem.play(Game.menu.getFxSlider().getValue() / 100);
                        vendingMachineMenu.setViewport( new Rectangle2D(0, 1194, 920, 597) );
                        chosenElementMenu = "bigSalt";
                    }
                });
                btnLittleSalt.setOnMouseClicked( event -> {
                    if (isShown) {
                        Sounds.audioClipChangeItem.play(Game.menu.getFxSlider().getValue() / 100);
                        vendingMachineMenu.setViewport( new Rectangle2D(0, 1791, 920, 597) );
                        chosenElementMenu = "littleSalt";
                    }
                });
                btnPistolBullets.setOnMouseClicked( event -> {
                    if (isShown) {
                        Sounds.audioClipChangeItem.play(Game.menu.getFxSlider().getValue() / 100);
                        vendingMachineMenu.setViewport( new Rectangle2D(0, 2388, 920, 597) );
                        chosenElementMenu = "pistolBullets";
                    }
                });
            }
            case Level.SECOND_LEVEL, Level.THIRD_LEVEL -> {
                btnBigMedicine.setOnMouseClicked( event -> {
                    if (isShown) {
                        Sounds.audioClipChangeItem.play(Game.menu.getFxSlider().getValue() / 100);
                        vendingMachineMenu.setViewport( new Rectangle2D(920, 0, 920, 597) );
                        chosenElementMenu = "bigMedicine";
                    }
                });
                btnLittleMedicine.setOnMouseClicked( event -> {
                    if (isShown) {
                        Sounds.audioClipChangeItem.play(Game.menu.getFxSlider().getValue() / 100);
                        vendingMachineMenu.setViewport( new Rectangle2D(920, 597, 920, 597) );
                        chosenElementMenu = "littleMedicine";
                    }
                });
                btnBigSalt.setOnMouseClicked( event -> {
                    if (isShown) {
                        Sounds.audioClipChangeItem.play(Game.menu.getFxSlider().getValue() / 100);
                        vendingMachineMenu.setViewport( new Rectangle2D(920, 1194, 920, 597) );
                        chosenElementMenu = "bigSalt";
                    }
                });
                btnLittleSalt.setOnMouseClicked( event -> {
                    if (isShown) {
                        Sounds.audioClipChangeItem.play(Game.menu.getFxSlider().getValue() / 100);
                        vendingMachineMenu.setViewport( new Rectangle2D(920, 1791, 920, 597) );
                        chosenElementMenu = "littleSalt";
                    }
                });
                btnPistolBullets.setOnMouseClicked( event -> {
                    if (isShown) {
                        Sounds.audioClipChangeItem.play(Game.menu.getFxSlider().getValue() / 100);
                        vendingMachineMenu.setViewport( new Rectangle2D(920, 2388, 920, 597) );
                        chosenElementMenu = "pistolBullets";
                    }
                });
                btnMachineGunBullets.setOnMouseClicked( event -> {
                    if (isShown) {
                        Sounds.audioClipChangeItem.play(Game.menu.getFxSlider().getValue() / 100);
                        vendingMachineMenu.setViewport( new Rectangle2D(920, 2985, 920, 597) );
                        chosenElementMenu = "machineGunBullets";
                    }
                });
            }
        }

        Game.scene.addEventFilter(KEY_PRESSED, eventHandler);
    }

    private void keyAction() {
        switch (chosenElementMenu) {
            case "bigMedicine":
                if (Game.booker.getMoney() >= 36) {
                    if (Game.booker.getHP() <= 20) {
                        Sounds.audioClipPurchase.play(Game.menu.getFxSlider().getValue() / 100);
                        Game.booker.setMoney(Game.booker.getMoney() - 36);
                        Game.booker.setHP(Game.booker.getHP() + 80);
                        Game.hud.update();
                        moneyText.setText(String.valueOf(Game.booker.getMoney()));
                        moneyText.setTranslateX(Game.hud.getMoneyText().getTranslateX() + 470);
                    } else if (Game.booker.getHP() == 100)
                        full("У вас полная жизнь! Для продолжения кликните по этому сообщению");
                    else {
                        Sounds.audioClipPurchase.play(Game.menu.getFxSlider().getValue() / 100);
                        Game.booker.setMoney(Game.booker.getMoney() - 36);
                        Game.booker.setHP(100);
                        Game.hud.update();
                        moneyText.setText(String.valueOf(Game.booker.getMoney()));
                        moneyText.setTranslateX(Game.hud.getMoneyText().getTranslateX() + 470);
                    }
                } else
                    full("У вас недостаточно средств! Для продолжения кликните по этому сообщению");
                break;
            case "littleMedicine":
                if (Game.booker.getMoney() >= 14) {
                    if (Game.booker.getHP() == 100)
                        full("У вас полная жизнь! Для продолжения кликните по этому сообщению");
                    else if (Game.booker.getHP() <= 80) {
                        Sounds.audioClipPurchase.play(Game.menu.getFxSlider().getValue() / 100);
                        Game.booker.setMoney(Game.booker.getMoney() - 14);
                        Game.booker.setHP(Game.booker.getHP() + 20);
                        Game.hud.update();
                        moneyText.setText(String.valueOf(Game.booker.getMoney()));
                        moneyText.setTranslateX(Game.hud.getMoneyText().getTranslateX() + 470);
                    } else {
                        Sounds.audioClipPurchase.play(Game.menu.getFxSlider().getValue() / 100);
                        Game.booker.setMoney(Game.booker.getMoney() - 14);
                        Game.booker.setHP(100);
                        Game.hud.update();
                        moneyText.setText(String.valueOf(Game.booker.getMoney()));
                        moneyText.setTranslateX(Game.hud.getMoneyText().getTranslateX() + 470);
                    }
                } else
                    full("У вас недостаточно средств! Для продолжения кликните по этому сообщению");
                break;
            case "bigSalt":
                if (Game.booker.getMoney() >= 67) {
                    if (Game.booker.getSalt() != 100) {
                        Sounds.audioClipPurchase.play(Game.menu.getFxSlider().getValue() / 100);
                        Game.booker.setSalt(100);
                        Game.booker.setMoney(Game.booker.getMoney() - 67);
                        Game.hud.update();
                        moneyText.setText(String.valueOf(Game.booker.getMoney()));
                        moneyText.setTranslateX(Game.hud.getMoneyText().getTranslateX() + 470);
                    } else
                        full("У вас полные соли! Для продолжения кликните по этому сообщению");
                } else
                    full("У вас недостаточно средств! Для продолжения кликните по этому сообщению");
                break;
            case "littleSalt":
                if (Game.booker.getMoney() >= 19) {
                    if (Game.booker.getSalt() <= 75) {
                        Sounds.audioClipPurchase.play(Game.menu.getFxSlider().getValue() / 100);
                        Game.booker.setMoney(Game.booker.getMoney() - 19);
                        Game.booker.setSalt(Game.booker.getSalt() + 25);
                        Game.hud.update();
                        moneyText.setText(String.valueOf(Game.booker.getMoney()));
                        moneyText.setTranslateX(Game.hud.getMoneyText().getTranslateX() + 470);
                    } else if (Game.booker.getSalt() != 100) {
                        Sounds.audioClipPurchase.play(Game.menu.getFxSlider().getValue() / 100);
                        Game.booker.setMoney(Game.booker.getMoney() - 19);
                        Game.booker.setSalt(100);
                        Game.hud.update();
                        moneyText.setText(String.valueOf(Game.booker.getMoney()));
                        moneyText.setTranslateX(Game.hud.getMoneyText().getTranslateX() + 470);
                    } else
                        full("У вас полные соли! Для продолжения кликните по этому сообщению");
                } else
                    full("У вас недостаточно средств! Для продолжения кликните по этому сообщению");
                break;
            case "pistolBullets":
                if (Game.booker.getMoney() >= 8) {
                    if (Game.booker.getWeapon().getType() == WeaponType.PISTOL)
                        Game.booker.getWeapon().setCurrentBullets(Game.booker.getWeapon().getCurrentBullets() + 12);
                    else
                        Game.booker.getWeapon().setPistolBullets(Game.booker.getWeapon().getPistolBullets() + 12);
                    Sounds.audioClipPurchase.play(Game.menu.getFxSlider().getValue() / 100);
                    Game.booker.setMoney(Game.booker.getMoney() - 8);
                    Game.hud.update();
                    moneyText.setText(String.valueOf(Game.booker.getMoney()));
                    moneyText.setTranslateX(Game.hud.getMoneyText().getTranslateX() + 470);
                } else
                    full("У вас недостаточно средств! Для продолжения кликните по этому сообщению");
                break;
            case "machineGunBullets":
                if (Game.booker.getMoney() >= 8) {
                    if (Game.booker.getWeapon().getType() == WeaponType.MACHINE_GUN)
                        Game.booker.getWeapon().setCurrentBullets(Game.booker.getWeapon().getCurrentBullets() + 35);
                    else
                        Game.booker.getWeapon().setMachineGunBullets(Game.booker.getWeapon().getMachineGunBullets() + 35);
                    Sounds.audioClipPurchase.play(Game.menu.getFxSlider().getValue() / 100);
                    Game.booker.setMoney(Game.booker.getMoney() - 8);
                    Game.hud.update();
                    moneyText.setText(String.valueOf(Game.booker.getMoney()));
                    moneyText.setTranslateX(Game.hud.getMoneyText().getTranslateX() + 470);
                } else
                    full("У вас недостаточно средств! Для продолжения кликните по этому сообщению");
                break;
        }
    }

    private void full(String str) {
        Text text = new Text(str);
        text.setFont(Font.font("Arial", 26));
        text.setFill(Color.RED);
        text.setTranslateX(vendingMachineMenu.getTranslateX() + 45);
        text.setTranslateY(vendingMachineMenu.getTranslateY() + 300);
        if (str.equals("У вас недостаточно средств! Для продолжения кликните по этому сообщению"))
            text.setTranslateX(vendingMachineMenu.getTranslateX() - 10);
        Game.appRoot.getChildren().add(text);
        text.setOnMouseClicked( event -> Game.appRoot.getChildren().remove(text));
    }

    public ImageView getVendingMachine() {
        return vendingMachine;
    }

    public boolean isShown() {
        return isShown;
    }
}
