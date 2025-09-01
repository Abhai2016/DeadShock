package com.abhai.deadshock.utils;

import com.abhai.deadshock.Game;
import com.abhai.deadshock.characters.Character;
import com.abhai.deadshock.world.levels.Level;
import com.abhai.deadshock.weapons.WeaponType;
import javafx.scene.input.KeyCode;

import java.util.HashMap;

public class Controller {
    public static HashMap<KeyCode, Boolean> keys = new HashMap<>();

    public static void update() {
        menuListener();

        if (!isPressed(KeyCode.A) && !isPressed(KeyCode.D))
            Game.booker.setIdleAnimation();

        if (Game.active && !Game.booker.isHypnotized()) {
            weaponListener();
            movementListener();
            energeticListener();
            interactionListener();
        }
    }

    private static void menuListener() {
        if (isPressed(KeyCode.ESCAPE)) {
            keys.remove(KeyCode.ESCAPE);
            if (Game.vendingMachine.isShown())
                Game.vendingMachine.hideMenu();
            else if (Game.menu.isShown())
                Game.menu.hideMenu();
            else
                Game.menu.showMenu();
            return;
        }

        if (isPressed(KeyCode.ENTER) && Game.vendingMachine.isShown()) {
            keys.remove(KeyCode.ENTER);
            Game.vendingMachine.makePurchase();
        }
    }

    private static void weaponListener() {
        if (isPressed(KeyCode.R))
            Game.booker.getWeapon().reload();
        if (!isPressed(KeyCode.R))
            Game.booker.getWeapon().setCanReload(true);
        if (isPressed(KeyCode.J))
            Game.booker.getWeapon().shoot();
        if (!isPressed(KeyCode.J))
            Game.booker.getWeapon().setSingleShot(true);
        if (isPressed(KeyCode.DIGIT1))
            Game.booker.getWeapon().changeWeapon(WeaponType.PISTOL);
        if (isPressed(KeyCode.DIGIT2))
            Game.booker.getWeapon().changeWeapon(WeaponType.MACHINE_GUN);
        if (isPressed(KeyCode.DIGIT3))
            Game.booker.getWeapon().changeWeapon(WeaponType.RPG);
    }

    private static void movementListener() {
        if (isPressed(KeyCode.D) && Game.booker.getTranslateX() < Game.gameRoot.getWidth() - Game.booker.getWidth()) {
            Game.booker.moveX(Character.SPEED);
            Game.booker.setScaleX(1);
        }
        if (isPressed(KeyCode.A) && Game.booker.getTranslateX() > 1) {
            Game.booker.moveX(-Character.SPEED);
            Game.booker.setScaleX(-1);
        }
        if (isPressed(KeyCode.A) && isPressed(KeyCode.D))
            Game.booker.stopAnimation();
        if (isPressed(KeyCode.W))
            Game.booker.jump(false);
    }

    private static void energeticListener() {
        if (Game.booker.getEnergetic().getCountEnergetics() > 1 && isPressed(KeyCode.Q))
            Game.booker.getEnergetic().changeEnergetic();
        if (!isPressed(KeyCode.Q))
            Game.booker.getEnergetic().setCanChangeEnergetic(true);
        if (!isPressed(KeyCode.L))
            Game.booker.getEnergetic().setCanShoot(true);
        if (isPressed(KeyCode.L) && !Game.booker.isHypnotized() && Game.booker.getSalt() >= Game.booker.getEnergetic().getSaltPrice())
            Game.booker.getEnergetic().shoot();
    }

    private static void interactionListener() {
        if (isPressed(KeyCode.E) && Game.levelNumber < Level.BOSS_LEVEL)
            if (Game.booker.getBoundsInParent().intersects(Game.booker.getWeapon().getBoundsInParent()))
                Game.booker.getWeapon().pickUpWeapon();
            else if (Game.booker.getBoundsInParent().intersects(Game.booker.getEnergetic().getBoundsInParent()))
                Game.booker.getEnergetic().pickUp();
            else if (Game.booker.getBoundsInParent().intersects(Game.vendingMachine.getVendingMachineImage().getBoundsInParent()) && !Game.vendingMachine.isShown())
                Game.vendingMachine.openMenu();
        if (isPressed(KeyCode.G)) {
            keys.remove(KeyCode.G);
            Game.menu.checkMusic();
        }
    }

    private static boolean isPressed(KeyCode keyCode) {
        return keys.getOrDefault(keyCode, false);
    }
}
