package com.abhai.deadshock.hud;

import com.abhai.deadshock.Game;
import com.abhai.deadshock.utils.Texts;
import com.abhai.deadshock.world.levels.Level;
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
        switch (Game.getGameWorld().getLevel().getCurrentLevelNumber()) {
            case Level.FIRST_LEVEL -> {
                setText(Texts.MOVE_CHARACTER, 100, DEFAULT_OFFSET_Y);
                setText(Texts.JUMP, BLOCK_SIZE * 7, SMALL_OFFSET_Y);
                setText(Texts.SHOOT, BLOCK_SIZE * 27, SMALL_OFFSET_Y);
                setText(Texts.RELOAD, BLOCK_SIZE * 25, DEFAULT_OFFSET_Y);
                setText(Texts.CHANGE_MUSIC, BLOCK_SIZE * 59, DEFAULT_OFFSET_Y);
                setText(Texts.CHANGE_TRACK, BLOCK_SIZE * 65, SMALL_OFFSET_Y);
                setText(Texts.PICK_UP_OBJECT, BLOCK_SIZE * 26, SMALLER_OFFSET_Y);
                setText(Texts.SHOOT_ENERGETIC, BLOCK_SIZE * 41, DEFAULT_OFFSET_Y);
                setText(Texts.TURN_ON_VENDING_MACHINE, BLOCK_SIZE * 182, SMALLEST_OFFSET_Y);
            }
            case Level.SECOND_LEVEL -> {
                setText(Texts.SWITCH_WEAPON, BLOCK_SIZE * 18, DEFAULT_OFFSET_Y);
                setText(Texts.SWITCH_ENERGETIC, BLOCK_SIZE * 46, DEFAULT_OFFSET_Y);
            }
            case Level.THIRD_LEVEL -> {
                setText(Texts.CHANGE_WEAPON, BLOCK_SIZE * 19, DEFAULT_OFFSET_Y);
            }
        }
    }

    public static void delete() {
        Game.getGameWorld().getGameRoot().getChildren().removeAll(Texts.MOVE_CHARACTER, Texts.JUMP,
                Texts.PICK_UP_OBJECT, Texts.SHOOT, Texts.RELOAD, Texts.CHANGE_TRACK, Texts.CHANGE_MUSIC,
                Texts.TURN_ON_VENDING_MACHINE, Texts.SHOOT_ENERGETIC, Texts.VENDING_MACHINE_MENU,
                Texts.SWITCH_WEAPON, Texts.SWITCH_ENERGETIC, Texts.CHANGE_WEAPON);
    }

    public static void changeLevel() {
        delete();
        init();
    }

    public static void setVendingMachineMenu() {
        Texts.VENDING_MACHINE_MENU.setFont(Font.font("Aria", 28));
        Texts.VENDING_MACHINE_MENU.setTranslateX(BLOCK_SIZE * 4.5 + 5);
        Texts.VENDING_MACHINE_MENU.setFill(Color.WHITE);
        Texts.VENDING_MACHINE_MENU.setTranslateY(40);
        Game.getGameWorld().getAppRoot().getChildren().add(Texts.VENDING_MACHINE_MENU);
    }

    public static void deleteVendingMachineMenu() {
        Game.getGameWorld().getAppRoot().getChildren().remove(Texts.VENDING_MACHINE_MENU);
    }

    private static void setText(Text text, int x, int y) {
        text.setFont(Font.font("Aria", 28));
        text.setFill(Color.WHITE);
        text.setTranslateX(x);
        text.setTranslateY(y);
        Game.getGameWorld().getGameRoot().getChildren().add(text);
    }
}