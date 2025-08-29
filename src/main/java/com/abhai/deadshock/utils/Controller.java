package com.abhai.deadshock.utils;

import com.abhai.deadshock.Game;
import com.abhai.deadshock.characters.Character;
import com.abhai.deadshock.levels.Level;
import com.abhai.deadshock.weapons.WeaponType;
import javafx.scene.input.KeyCode;

public class Controller {

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
        if (isPressed(KeyCode.ESCAPE) && (Game.vendingMachine == null || !Game.vendingMachine.isShown())) {
            Game.keys.remove(KeyCode.ESCAPE);
            if (Game.menu.isShown())
                Game.menu.hideMenu();
            else
                Game.menu.showMenu();
        }
    }

    private static void weaponListener() {
        if (isPressed(KeyCode.R))
            Game.weapon.reload();
        if (!isPressed(KeyCode.R))
            Game.weapon.setCanReload(true);
        if (isPressed(KeyCode.J))
            Game.weapon.shoot();
        if (!isPressed(KeyCode.J))
            Game.weapon.setSingleShot(true);
        if (isPressed(KeyCode.DIGIT1))
            Game.weapon.changeWeapon(WeaponType.PISTOL);
        if (isPressed(KeyCode.DIGIT2))
            Game.weapon.changeWeapon(WeaponType.MACHINE_GUN);
        if (isPressed(KeyCode.DIGIT3))
            Game.weapon.changeWeapon(WeaponType.RPG);
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
        if (Game.energetic.getCountEnergetics() > 1 && isPressed(KeyCode.Q))
            Game.energetic.changeEnergetic();
        if (!isPressed(KeyCode.L))
            Game.energetic.setCanShoot(true);
        if (isPressed(KeyCode.L) && !Game.booker.isHypnotized() && Game.booker.getSalt() >= Game.energetic.getSaltPrice())
            Game.energetic.shoot();
    }

    private static void interactionListener() {
        if (isPressed(KeyCode.E) && Game.levelNumber < Level.BOSS_LEVEL)
            if (Game.booker.getBoundsInParent().intersects(Game.weapon.getBoundsInParent()))
                Game.weapon.pickUpWeapon();
            else if (Game.booker.getBoundsInParent().intersects(Game.vendingMachine.getVendingMachine().getBoundsInParent()))
                Game.vendingMachine.openMachineMenu();
            else if (Game.booker.getBoundsInParent().intersects(Game.energetic.getBoundsInParent()))
                Game.energetic.pickUp();
        if (isPressed(KeyCode.G)) {
            Game.keys.remove(KeyCode.G);
            Game.menu.checkMusic();
        }
    }

    private static boolean isPressed(KeyCode keyCode) {
        return Game.keys.getOrDefault(keyCode, false);
    }
}
