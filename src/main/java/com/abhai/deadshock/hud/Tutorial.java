package com.abhai.deadshock.hud;

import com.abhai.deadshock.Game;
import com.abhai.deadshock.levels.Level;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import static com.abhai.deadshock.levels.Block.BLOCK_SIZE;

public class Tutorial {
    private static final int SMALL_OFFSET_Y = 150;
    private static final int SMALLER_OFFSET_Y = 100;
    private static final int DEFAULT_OFFSET_Y = 200;
    private static final int SMALLEST_OFFSET_Y = 50;

    private static final Text jump = new Text("Для прыжка нажимайте W");
    private static final Text shoot = new Text("Для стрельбы нажимайте J");
    private static final Text reload = new Text("Чтобы перезарядить оружие, нажмите R");
    private static final Text pickUpWeapon = new Text("Чтобы поднять оружие, нажмите E");
    private static final Text changeTrack = new Text("Чтобы переключить трек, нажимайте G");
    private static final Text changeWeapon = new Text("Для смены оружия нажимайте 1, 2 и 3");
    private static final Text switchEnergetic = new Text("Для смены энергетика нажимайте Q");
    private static final Text takeMedicine = new Text("Чтобы словить аптечку подойдите к Элизабет");
    private static final Text shootEnergetic = new Text("Для использования энергетика нажимайте L");
    private static final Text reloadEnergetic = new Text("Пополнить соли можно в торговом автомате");
    private static final Text saves = new Text("При прохождении каждого уровня ваш прогресс сохраняется");
    private static final Text getMoneyFromEnemy = new Text("За каждого убитого противника вы получаете монеты");
    private static final Text turnOnVendingMachine = new Text("Нажмите E для открытия меню торгового автомата");
    private static final Text moveCharacter = new Text("Чтобы идти влево и вправо, нажимайте A и D соответственно");
    private static final Text switchWeapon = new Text("Нажимайте 1 и 2 для выбора пистолета и автомата соответственно");
    private static final Text vendingMachineMenu = new Text("Для выбора нужного пункта нажмите на него левой кнопкой мыши и ввод для его покупки");
    private static final Text changeMusic = new Text("Так же можно поменять плейлист, для этого перейдите" + "\n" + " в меню(Esc) -> НАСТРОЙКИ -> ЗВУК -> МУЗЫКА");
    private static final Text rpgBullets = new Text("Боеприпасы для РПГ вы можете найти на этом уровне, они не \n" + "покупаются в тороговом автомате и не даются за убийство противника, \n" + " для этого поднимите ящик с патронами с выбранным оружием РПГ");

    public static void init() {
        switch (Game.levelNumber) {
            case Level.FIRST_LEVEL -> {
                setText(moveCharacter, 100, DEFAULT_OFFSET_Y);
                setText(jump, BLOCK_SIZE * 23, DEFAULT_OFFSET_Y);
                setText(shoot, BLOCK_SIZE * 45, DEFAULT_OFFSET_Y);
                setText(reload, BLOCK_SIZE * 55, DEFAULT_OFFSET_Y);
                setText(changeMusic, BLOCK_SIZE * 73, SMALL_OFFSET_Y);
                setText(changeTrack, BLOCK_SIZE * 74, SMALLER_OFFSET_Y);
                setText(pickUpWeapon, BLOCK_SIZE * 33, DEFAULT_OFFSET_Y);
                setText(reloadEnergetic, BLOCK_SIZE * 43, SMALL_OFFSET_Y);
                setText(shootEnergetic, BLOCK_SIZE * 43, SMALLER_OFFSET_Y);
                setText(getMoneyFromEnemy, BLOCK_SIZE * 95, DEFAULT_OFFSET_Y);
                setText(turnOnVendingMachine, BLOCK_SIZE * 182, SMALLEST_OFFSET_Y);
            }
            case Level.SECOND_LEVEL -> {
                setText(saves, BLOCK_SIZE, 200);
                setText(switchWeapon, BLOCK_SIZE * 18, DEFAULT_OFFSET_Y);
                setText(takeMedicine, BLOCK_SIZE * 55, SMALLEST_OFFSET_Y);
                setText(switchEnergetic, BLOCK_SIZE * 46, DEFAULT_OFFSET_Y);
            }
            case Level.THIRD_LEVEL -> {
                setText(rpgBullets, BLOCK_SIZE * 33, SMALLEST_OFFSET_Y);
                setText(changeWeapon, BLOCK_SIZE * 19, DEFAULT_OFFSET_Y);
            }
        }
    }

    public static void delete() {
        switch (Game.levelNumber) {
            case Level.FIRST_LEVEL ->
                    Game.gameRoot.getChildren().removeAll(moveCharacter, jump, pickUpWeapon, shoot, reload, changeTrack, changeMusic, getMoneyFromEnemy, turnOnVendingMachine, shootEnergetic, reloadEnergetic, vendingMachineMenu);
            case Level.SECOND_LEVEL ->
                    Game.gameRoot.getChildren().removeAll(takeMedicine, switchWeapon, switchEnergetic, saves);
            case Level.THIRD_LEVEL -> Game.gameRoot.getChildren().removeAll(changeWeapon, rpgBullets);
        }
    }

    public static void setVendingMachineMenu() {
        vendingMachineMenu.setFont(Font.font("Aria", 28));
        vendingMachineMenu.setFill(Color.WHITE);
        vendingMachineMenu.setTranslateX(50);
        vendingMachineMenu.setTranslateY(50);
        Game.appRoot.getChildren().add(vendingMachineMenu);
    }

    private static void setText(Text text, int x, int y) {
        text.setFont(Font.font("Aria", 28));
        text.setFill(Color.WHITE);
        text.setTranslateX(x);
        text.setTranslateY(y);
        Game.gameRoot.getChildren().add(text);
    }

    public static void deleteVendingMachineMenu() {
        Game.appRoot.getChildren().remove(vendingMachineMenu);
    }
}