package com.abhai.deadshock;

import com.abhai.deadshock.Characters.EnemyBase;
import com.abhai.deadshock.Levels.Level;
import com.abhai.deadshock.Weapon.Weapon;
import javafx.animation.FadeTransition;
import javafx.geometry.Rectangle2D;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.util.Duration;

import static javafx.scene.input.KeyEvent.KEY_PRESSED;

class VendingMachine extends Pane {
    private final int BUTTON_HEIGHT = 53;
    private final int BUTTON_WIDTH = 356;

    private ImageView vendingMachine = new ImageView("file:/../images/vending_machine/vending_machine.png");
    private ImageView vendingMachineMenu = new ImageView("file:/../images/vending_machine/menu.png");

    private FadeTransition ft = new FadeTransition(Duration.seconds(0.3), vendingMachineMenu);
    private Button btnBigMedicine = new Button();
    private Button btnLittleMedicine = new Button();
    private Button btnBigSalt = new Button();
    private Button btnLittleSalt = new Button();
    private Button btnPistolBullets = new Button();
    private Button btnMachineGunBullets = new Button();

    private Text moneyText = new Text();

    private String chosenElementMenu = "bigMedicine";
    private boolean startMenu = true;
    private boolean machineMenu = true;



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

        switch ((int)Game.levelNumber) {
            case 0:
                vendingMachine.setTranslateX(Level.BLOCK_SIZE * 187 + 15);
                vendingMachine.setTranslateY(Level.BLOCK_SIZE * 3 - 6);
                vendingMachineMenu.setViewport( new Rectangle2D(0, 0, 920, 597) );
                break;
            case 1:
                vendingMachine.setTranslateX(Level.BLOCK_SIZE * 157);
                vendingMachine.setTranslateY(Level.BLOCK_SIZE * 7 - 6);
                vendingMachineMenu.setViewport( new Rectangle2D(920, 0, 920, 597) );
                break;
            case 2:
                vendingMachine.setTranslateX(Level.BLOCK_SIZE * 137.5);
                vendingMachine.setTranslateY(Level.BLOCK_SIZE * 4 - 6);
                vendingMachineMenu.setViewport( new Rectangle2D(920, 0, 920, 597) );
                break;
        }

        getChildren().addAll(vendingMachine);
        Game.gameRoot.getChildren().add(this);
    }


    void changeLevel() {
        if (Game.levelNumber == 1) {
            vendingMachine.setTranslateX(Level.BLOCK_SIZE * 157);
            vendingMachine.setTranslateY(Level.BLOCK_SIZE * 7 - 6);
            vendingMachineMenu.setViewport( new Rectangle2D(920, 0, 920, 597) );
        } else if (Game.levelNumber == 2) {
            vendingMachine.setTranslateX(Level.BLOCK_SIZE * 137.5);
            vendingMachine.setTranslateY(Level.BLOCK_SIZE * 4 - 6);
            vendingMachineMenu.setViewport( new Rectangle2D(920, 0, 920, 597) );
        }
    }


    void openMachineMenu() {
        if (Game.levelNumber == 0 && startMenu) {
            Game.tutorial.deleteText();
            Game.tutorial.addMenuText();
            startMenu = false;
        }

        Game.timer.stop();
        Game.menu.music.pause();
        Sounds.audioClipOpenMenu.play(Game.menu.fxSlider.getValue() / 100);
        moneyText.setText(String.valueOf(Game.booker.getMoney()));

        for (EnemyBase enemy : Game.enemies)
            if (enemy.animation != null)
                enemy.animation.stop();

        ft.setByValue(0);
        ft.setToValue(1);
        ft.play();


        moneyText.setVisible(true);
        addListener();
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


    void setMarikLevel() {
        Game.appRoot.getChildren().remove(moneyText);
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
        switch ((int)Game.levelNumber) {
            case 0:
                btnBigMedicine.setOnMouseClicked( event -> {
                    Sounds.audioClipChangeItem.play(Game.menu.fxSlider.getValue() / 100);
                    vendingMachineMenu.setViewport( new Rectangle2D(0, 0, 920, 597) );
                    chosenElementMenu = "bigMedicine";
                });
                btnLittleMedicine.setOnMouseClicked( event -> {
                    Sounds.audioClipChangeItem.play(Game.menu.fxSlider.getValue() / 100);
                    vendingMachineMenu.setViewport( new Rectangle2D(0, 597, 920, 597) );
                    chosenElementMenu = "littleMedicine";
                });
                btnBigSalt.setOnMouseClicked( event -> {
                    Sounds.audioClipChangeItem.play(Game.menu.fxSlider.getValue() / 100);
                    vendingMachineMenu.setViewport( new Rectangle2D(0, 1194, 920, 597) );
                    chosenElementMenu = "bigSalt";
                });
                btnLittleSalt.setOnMouseClicked( event -> {
                    Sounds.audioClipChangeItem.play(Game.menu.fxSlider.getValue() / 100);
                    vendingMachineMenu.setViewport( new Rectangle2D(0, 1791, 920, 597) );
                    chosenElementMenu = "littleSalt";
                });
                btnPistolBullets.setOnMouseClicked( event -> {
                    Sounds.audioClipChangeItem.play(Game.menu.fxSlider.getValue() / 100);
                    vendingMachineMenu.setViewport( new Rectangle2D(0, 2388, 920, 597) );
                    chosenElementMenu = "pistolBullets";
                });
                break;
            case 1:
                btnBigMedicine.setOnMouseClicked( event -> {
                    Sounds.audioClipChangeItem.play(Game.menu.fxSlider.getValue() / 100);
                    vendingMachineMenu.setViewport( new Rectangle2D(920, 0, 920, 597) );
                    chosenElementMenu = "bigMedicine";
                });
                btnLittleMedicine.setOnMouseClicked( event -> {
                    Sounds.audioClipChangeItem.play(Game.menu.fxSlider.getValue() / 100);
                    vendingMachineMenu.setViewport( new Rectangle2D(920, 597, 920, 597) );
                    chosenElementMenu = "littleMedicine";
                });
                btnBigSalt.setOnMouseClicked( event -> {
                    Sounds.audioClipChangeItem.play(Game.menu.fxSlider.getValue() / 100);
                    vendingMachineMenu.setViewport( new Rectangle2D(920, 1194, 920, 597) );
                    chosenElementMenu = "bigSalt";
                });
                btnLittleSalt.setOnMouseClicked( event -> {
                    Sounds.audioClipChangeItem.play(Game.menu.fxSlider.getValue() / 100);
                    vendingMachineMenu.setViewport( new Rectangle2D(920, 1791, 920, 597) );
                    chosenElementMenu = "littleSalt";
                });
                btnPistolBullets.setOnMouseClicked( event -> {
                    Sounds.audioClipChangeItem.play(Game.menu.fxSlider.getValue() / 100);
                    vendingMachineMenu.setViewport( new Rectangle2D(920, 2388, 920, 597) );
                    chosenElementMenu = "pistolBullets";
                });
                btnMachineGunBullets.setOnMouseClicked( event -> {
                    Sounds.audioClipChangeItem.play(Game.menu.fxSlider.getValue() / 100);
                    vendingMachineMenu.setViewport( new Rectangle2D(920, 2985, 920, 597) );
                    chosenElementMenu = "machineGunBullets";
                });
                break;
            case 2:
                btnBigMedicine.setOnMouseClicked( event -> {
                    Sounds.audioClipChangeItem.play(Game.menu.fxSlider.getValue() / 100);
                    vendingMachineMenu.setViewport( new Rectangle2D(920, 0, 920, 597) );
                    chosenElementMenu = "bigMedicine";
                });
                btnLittleMedicine.setOnMouseClicked( event -> {
                    Sounds.audioClipChangeItem.play(Game.menu.fxSlider.getValue() / 100);
                    vendingMachineMenu.setViewport( new Rectangle2D(920, 597, 920, 597) );
                    chosenElementMenu = "littleMedicine";
                });
                btnBigSalt.setOnMouseClicked( event -> {
                    Sounds.audioClipChangeItem.play(Game.menu.fxSlider.getValue() / 100);
                    vendingMachineMenu.setViewport( new Rectangle2D(920, 1194, 920, 597) );
                    chosenElementMenu = "bigSalt";
                });
                btnLittleSalt.setOnMouseClicked( event -> {
                    Sounds.audioClipChangeItem.play(Game.menu.fxSlider.getValue() / 100);
                    vendingMachineMenu.setViewport( new Rectangle2D(920, 1791, 920, 597) );
                    chosenElementMenu = "littleSalt";
                });
                btnPistolBullets.setOnMouseClicked( event -> {
                    Sounds.audioClipChangeItem.play(Game.menu.fxSlider.getValue() / 100);
                    vendingMachineMenu.setViewport( new Rectangle2D(920, 2388, 920, 597) );
                    chosenElementMenu = "pistolBullets";
                });
                btnMachineGunBullets.setOnMouseClicked( event -> {
                    Sounds.audioClipChangeItem.play(Game.menu.fxSlider.getValue() / 100);
                    vendingMachineMenu.setViewport( new Rectangle2D(920, 2985, 920, 597) );
                    chosenElementMenu = "machineGunBullets";
                });
                break;
        }
        Game.scene.addEventFilter(KEY_PRESSED, event -> {
            if (event.getCode() == KeyCode.ENTER)
                keyAction();
            else if (event.getCode() == KeyCode.ESCAPE) {
                Game.timer.start();
                Game.menu.music.play();
                Game.menu.addListener();

                ft.setByValue(1);
                ft.setToValue(0);
                ft.play();

                moneyText.setVisible(false);

                changeListener();
                if (!startMenu && machineMenu) {
                    Game.tutorial.deleteMenuText();
                    Game.tutorial = null;
                    machineMenu = false;
                }
            }
        });
    }


    private void keyAction() {
        switch (chosenElementMenu) {
            case "bigMedicine":
                if (Game.booker.getMoney() >= 36) {
                    if (Game.booker.getHP() <= 20) {
                        Sounds.audioClipPurchase.play(Game.menu.fxSlider.getValue() / 100);
                        Game.booker.setMoney(Game.booker.getMoney() - 36);
                        Game.booker.setHP(Game.booker.getHP() + 80);
                        Game.hud.update();
                        moneyText.setText(String.valueOf(Game.booker.getMoney()));
                        moneyText.setTranslateX(Game.hud.getTextMoney().getTranslateX() + 470);
                    } else if (Game.booker.getHP() == 100)
                        full("У вас полная жизнь! Для продолжения кликните по этому сообщению");
                    else {
                        Sounds.audioClipPurchase.play(Game.menu.fxSlider.getValue() / 100);
                        Game.booker.setMoney(Game.booker.getMoney() - 36);
                        Game.booker.setHP(100);
                        Game.hud.update();
                        moneyText.setText(String.valueOf(Game.booker.getMoney()));
                        moneyText.setTranslateX(Game.hud.getTextMoney().getTranslateX() + 470);
                    }
                } else
                    full("У вас недостаточно средств! Для продолжения кликните по этому сообщению");
                break;
            case "littleMedicine":
                if (Game.booker.getMoney() >= 14) {
                    if (Game.booker.getHP() == 100)
                        full("У вас полная жизнь! Для продолжения кликните по этому сообщению");
                    else if (Game.booker.getHP() <= 80) {
                        Sounds.audioClipPurchase.play(Game.menu.fxSlider.getValue() / 100);
                        Game.booker.setMoney(Game.booker.getMoney() - 14);
                        Game.booker.setHP(Game.booker.getHP() + 20);
                        Game.hud.update();
                        moneyText.setText(String.valueOf(Game.booker.getMoney()));
                        moneyText.setTranslateX(Game.hud.getTextMoney().getTranslateX() + 470);
                    } else {
                        Sounds.audioClipPurchase.play(Game.menu.fxSlider.getValue() / 100);
                        Game.booker.setMoney(Game.booker.getMoney() - 14);
                        Game.booker.setHP(100);
                        Game.hud.update();
                        moneyText.setText(String.valueOf(Game.booker.getMoney()));
                        moneyText.setTranslateX(Game.hud.getTextMoney().getTranslateX() + 470);
                    }
                } else
                    full("У вас недостаточно средств! Для продолжения кликните по этому сообщению");
                break;
            case "bigSalt":
                if (Game.booker.getMoney() >= 67) {
                    if (Game.booker.getSalt() != 100) {
                        Sounds.audioClipPurchase.play(Game.menu.fxSlider.getValue() / 100);
                        Game.booker.setSalt(100);
                        Game.booker.setMoney(Game.booker.getMoney() - 67);
                        Game.hud.update();
                        moneyText.setText(String.valueOf(Game.booker.getMoney()));
                        moneyText.setTranslateX(Game.hud.getTextMoney().getTranslateX() + 470);
                    } else
                        full("У вас полные соли! Для продолжения кликните по этому сообщению");
                } else
                    full("У вас недостаточно средств! Для продолжения кликните по этому сообщению");
                break;
            case "littleSalt":
                if (Game.booker.getMoney() >= 19) {
                    if (Game.booker.getSalt() <= 75) {
                        Sounds.audioClipPurchase.play(Game.menu.fxSlider.getValue() / 100);
                        Game.booker.setMoney(Game.booker.getMoney() - 19);
                        Game.booker.setSalt(Game.booker.getSalt() + 25);
                        Game.hud.update();
                        moneyText.setText(String.valueOf(Game.booker.getMoney()));
                        moneyText.setTranslateX(Game.hud.getTextMoney().getTranslateX() + 470);
                    } else if (Game.booker.getSalt() != 100) {
                        Sounds.audioClipPurchase.play(Game.menu.fxSlider.getValue() / 100);
                        Game.booker.setMoney(Game.booker.getMoney() - 19);
                        Game.booker.setSalt(100);
                        Game.hud.update();
                        moneyText.setText(String.valueOf(Game.booker.getMoney()));
                        moneyText.setTranslateX(Game.hud.getTextMoney().getTranslateX() + 470);
                    } else
                        full("У вас полные соли! Для продолжения кликните по этому сообщению");
                } else
                    full("У вас недостаточно средств! Для продолжения кликните по этому сообщению");
                break;
            case "pistolBullets":
                if (Game.booker.getMoney() >= 8) {
                    if (Game.weapon.getName().equals("pistol"))
                        Game.weapon.setBullets(Game.weapon.getBullets() + 12);
                    else
                        Weapon.WeaponData.pistolBullets += 12;
                    Sounds.audioClipPurchase.play(Game.menu.fxSlider.getValue() / 100);
                    Game.booker.setMoney(Game.booker.getMoney() - 8);
                    Game.hud.update();
                    moneyText.setText(String.valueOf(Game.booker.getMoney()));
                    moneyText.setTranslateX(Game.hud.getTextMoney().getTranslateX() + 470);
                } else
                    full("У вас недостаточно средств! Для продолжения кликните по этому сообщению");
                break;
            case "machineGunBullets":
                if (Game.booker.getMoney() >= 8) {
                    if (Game.weapon.getName().equals("machine_gun"))
                        Game.weapon.setBullets(Game.weapon.getBullets() + 35);
                    else
                        Weapon.WeaponData.machineGunBullets += 35;
                    Sounds.audioClipPurchase.play(Game.menu.fxSlider.getValue() / 100);
                    Game.booker.setMoney(Game.booker.getMoney() - 8);
                    Game.hud.update();
                    moneyText.setText(String.valueOf(Game.booker.getMoney()));
                    moneyText.setTranslateX(Game.hud.getTextMoney().getTranslateX() + 470);
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
        changeListener();
        Game.scene.setOnKeyPressed(event -> Game.nothing() );
        text.setOnMouseClicked( event -> {
            Game.appRoot.getChildren().remove(text);
            addListener();
        });
    }


    private void changeListener() {
        btnBigMedicine.setOnMouseClicked( event -> Game.nothing() );
        btnLittleMedicine.setOnMouseClicked( event -> Game.nothing() );
        btnBigSalt.setOnMouseClicked( event -> Game.nothing() );
        btnLittleSalt.setOnMouseClicked( event -> Game.nothing() );
        btnPistolBullets.setOnMouseClicked( event -> Game.nothing() );
        if (Game.levelNumber > 0)
            btnMachineGunBullets.setOnMouseClicked( event -> Game.nothing() );
    }



    ImageView getVendingMachine() {
        return vendingMachine;
    }
}
