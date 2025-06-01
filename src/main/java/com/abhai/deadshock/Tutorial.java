package com.abhai.deadshock;


import com.abhai.deadshock.Levels.Level;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

class Tutorial {
    private Text moveCharacter = new Text("Чтобы идти влево и вправо, нажимайте A и D соответственно");
    private Text jump = new Text("Для прыжка нажимайте W");
    private Text pickUpWeapon = new Text("Чтобы поднять оружие, нажмите E");
    private Text shoot = new Text("Для стрельбы нажимайте J");
    private Text shootEnergetic = new Text("Для использования энергетика нажимайте L");
    private Text reloadEnergetic = new Text("Пополнить соли можно в торговом автомате");
    private Text reload = new Text("Чтобы перезарядить оружие, нажмите R");
    private Text changeTrack = new Text("Чтобы переключить трек, нажимайте G");
    private Text changeMusic = new Text("Так же можно поменять плейлист, для этого перейдите" + "\n"
            + " в меню(Esc) -> НАСТРОЙКИ -> ЗВУК -> МУЗЫКА");
    private Text getMoneyFromEnemy = new Text("За каждого убитого противника вы получаете монеты");
    private Text turnOnVendingMachine = new Text("Нажмите E для открытия меню торгового автомата");

    private Text menu = new Text("Для выбора нужного пункта нажмите на него левой" +
            " кнопкой мыши и ввод для его покупки");

    private Text saves = new Text("При прохождении каждого уровня ваш прогресс сохраняется");
    private Text switchWeapon = new Text("Нажимайте 1 и 2 для выбора пистолета и автомата соответственно");
    private Text switchEnergetic = new Text("Для смены энергетика нажимайте Q");
    private Text takeMedicine = new Text("Чтобы словить аптечку подойдите к Элизабет");

    private Text changeWeapon = new Text("Для смены оружия нажимайте 1, 2 и 3");
    private Text rpgBullets = new Text("       Боеприпасы для РПГ вы можете найти на этом уровне, они не \n" +
            "покупаются в тороговом автомате и не даются за убийство противника,\n" +
            "   для этого поднимите ящик с патронами с выбранным оружием РПГ");


    Tutorial() {
        switch((int)Game.levelNumber) {
            case 0:
                setText(moveCharacter, 100);
                setText(jump, Level.BLOCK_SIZE * 23);
                setText(pickUpWeapon, Level.BLOCK_SIZE * 33);
                setText(shoot, Level.BLOCK_SIZE * 45);
                setText(shootEnergetic, Level.BLOCK_SIZE * 43, 100);
                setText(reloadEnergetic, Level.BLOCK_SIZE * 43, 150);
                setText(reload, Level.BLOCK_SIZE * 55);
                setText(changeTrack, Level.BLOCK_SIZE * 74, 100);
                setText(changeMusic, Level.BLOCK_SIZE * 73, 150);
                setText(getMoneyFromEnemy, Level.BLOCK_SIZE * 95);
                setText(turnOnVendingMachine, Level.BLOCK_SIZE * 181, 100);
                break;
            case 1:
                setText(saves, Level.BLOCK_SIZE);
                setText(switchWeapon, Level.BLOCK_SIZE * 18);
                setText(switchEnergetic, Level.BLOCK_SIZE * 46);
                setText(takeMedicine, Level.BLOCK_SIZE * 55, 50);
                break;
            case 2:
                setText(changeWeapon, Level.BLOCK_SIZE * 19);
                setText(rpgBullets, Level.BLOCK_SIZE * 33, 50);
                break;
        }
    }


    private void setText(Text text, int x) {
        text.setFont( Font.font("Aria", 28) );
        text.setFill(Color.WHITE);
        text.setTranslateX(x);
        text.setTranslateY(200);
        Game.gameRoot.getChildren().add(text);
    }


    private void setText(Text text, int x, int y) {
        text.setFont( Font.font("Aria", 28) );
        text.setFill(Color.WHITE);
        text.setTranslateX(x);
        text.setTranslateY(y);
        Game.gameRoot.getChildren().add(text);
    }


    void addMenuText() {
        menu.setFont( Font.font("Aria", 28) );
        menu.setFill(Color.WHITE);
        menu.setTranslateX(50);
        menu.setTranslateY(50);
        Game.appRoot.getChildren().add(menu);

    }


    void deleteMenuText() {
        Game.appRoot.getChildren().remove(menu);
    }


    void deleteText() {
        switch ((int)Game.levelNumber) {
            case 0:
                Game.gameRoot.getChildren().removeAll(moveCharacter, jump, pickUpWeapon, shoot, reload, changeTrack, changeMusic, getMoneyFromEnemy, turnOnVendingMachine, shootEnergetic, reloadEnergetic);
                break;
            case 1:
                Game.gameRoot.getChildren().removeAll(takeMedicine, switchWeapon, switchEnergetic, saves);
                break;
            case 2:
                Game.gameRoot.getChildren().removeAll(changeWeapon, rpgBullets);
        }
    }
}