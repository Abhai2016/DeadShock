package com.abhai.deadshock.hud;

import com.abhai.deadshock.Game;
import com.abhai.deadshock.world.levels.Level;
import com.abhai.deadshock.utils.Texts;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import static com.abhai.deadshock.world.levels.Block.BLOCK_SIZE;

public class Tutorial {
    private static final int SMALL_OFFSET_Y = 150;
    private static final int SMALLER_OFFSET_Y = 100;
    private static final int DEFAULT_OFFSET_Y = 200;
    private static final int SMALLEST_OFFSET_Y = 50;

    public static void init() {
        switch (Game.levelNumber) {
            case Level.FIRST_LEVEL -> {
                setText(Texts.MOVE_CHARACTER, 100, DEFAULT_OFFSET_Y);
                setText(Texts.JUMP, BLOCK_SIZE * 23, DEFAULT_OFFSET_Y);
                setText(Texts.SHOOT, BLOCK_SIZE * 45, DEFAULT_OFFSET_Y);
                setText(Texts.RELOAD, BLOCK_SIZE * 55, DEFAULT_OFFSET_Y);
                setText(Texts.CHANGE_MUSIC, BLOCK_SIZE * 73, SMALL_OFFSET_Y);
                setText(Texts.CHANGE_TRACK, BLOCK_SIZE * 74, SMALLER_OFFSET_Y);
                setText(Texts.PICK_UP_WEAPON, BLOCK_SIZE * 33, DEFAULT_OFFSET_Y);
                setText(Texts.RELOAD_ENERGETIC, BLOCK_SIZE * 43, SMALL_OFFSET_Y);
                setText(Texts.SHOOT_ENERGETIC, BLOCK_SIZE * 43, SMALLER_OFFSET_Y);
                setText(Texts.GET_MONEY_FROM_ENEMY, BLOCK_SIZE * 95, DEFAULT_OFFSET_Y);
                setText(Texts.TURN_ON_VENDING_MACHINE, BLOCK_SIZE * 182, SMALLEST_OFFSET_Y);
            }
            case Level.SECOND_LEVEL -> {
                setText(Texts.SAVES, BLOCK_SIZE, 200);
                setText(Texts.SWITCH_WEAPON, BLOCK_SIZE * 18, DEFAULT_OFFSET_Y);
                setText(Texts.TAKE_MEDICINE, BLOCK_SIZE * 55, SMALLEST_OFFSET_Y);
                setText(Texts.SWITCH_ENERGETIC, BLOCK_SIZE * 46, DEFAULT_OFFSET_Y);
            }
            case Level.THIRD_LEVEL -> {
                setText(Texts.RPG_BULLETS, BLOCK_SIZE * 33, SMALLEST_OFFSET_Y);
                setText(Texts.CHANGE_WEAPON, BLOCK_SIZE * 19, DEFAULT_OFFSET_Y);
            }
        }
    }

    public static void delete() {
        switch (Game.levelNumber) {
            case Level.FIRST_LEVEL ->
                    Game.gameRoot.getChildren().removeAll(Texts.MOVE_CHARACTER, Texts.JUMP, Texts.PICK_UP_WEAPON,
                            Texts.SHOOT, Texts.RELOAD, Texts.CHANGE_TRACK, Texts.CHANGE_MUSIC,
                            Texts.GET_MONEY_FROM_ENEMY, Texts.TURN_ON_VENDING_MACHINE, Texts.SHOOT_ENERGETIC,
                            Texts.RELOAD_ENERGETIC, Texts.VENDING_MACHINE_MENU);
            case Level.SECOND_LEVEL ->
                    Game.gameRoot.getChildren().removeAll(Texts.TAKE_MEDICINE, Texts.SWITCH_WEAPON, Texts.SWITCH_ENERGETIC, Texts.SAVES);
            case Level.THIRD_LEVEL -> Game.gameRoot.getChildren().removeAll(Texts.CHANGE_WEAPON, Texts.RPG_BULLETS);
        }
    }

    public static void setVendingMachineMenu() {
        Texts.VENDING_MACHINE_MENU.setFont(Font.font("Aria", 28));
        Texts.VENDING_MACHINE_MENU.setFill(Color.WHITE);
        Texts.VENDING_MACHINE_MENU.setTranslateX(50);
        Texts.VENDING_MACHINE_MENU.setTranslateY(50);
        Game.appRoot.getChildren().add(Texts.VENDING_MACHINE_MENU);
    }

    private static void setText(Text text, int x, int y) {
        text.setFont(Font.font("Aria", 28));
        text.setFill(Color.WHITE);
        text.setTranslateX(x);
        text.setTranslateY(y);
        Game.gameRoot.getChildren().add(text);
    }

    public static void deleteVendingMachineMenu() {
        Game.appRoot.getChildren().remove(Texts.VENDING_MACHINE_MENU);
    }
}