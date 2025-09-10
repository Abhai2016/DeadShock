package com.abhai.deadshock.utils;

import com.abhai.deadshock.Game;
import com.abhai.deadshock.characters.Character;
import com.abhai.deadshock.types.WeaponType;
import com.abhai.deadshock.world.levels.Level;
import javafx.scene.input.KeyCode;

import java.util.HashMap;

public class Controller {
    public static HashMap<KeyCode, Boolean> keys = new HashMap<>();

    public static void update() {
        menuListener();

        if (!isPressed(KeyCode.A) && !isPressed(KeyCode.D))
            Game.getGameWorld().getBooker().setIdleAnimation();

        if (Game.getGameWorld().isActive() && !Game.getGameWorld().getBooker().isHypnotized()) {
            weaponListener();
            movementListener();
            energeticListener();
            interactionListener();
        }
    }

    private static void menuListener() {
        if (isPressed(KeyCode.ESCAPE)) {
            keys.remove(KeyCode.ESCAPE);
            if (Game.getGameWorld().getVendingMachine().isShown())
                Game.getGameWorld().getVendingMachine().hideMenu();
            else if (Game.getGameWorld().getMenu().isShown())
                Game.getGameWorld().getMenu().hideMenu();
            else
                Game.getGameWorld().getMenu().showMenu();
            return;
        }

        if (isPressed(KeyCode.ENTER)) {
            keys.remove(KeyCode.ENTER);
            if (Game.getGameWorld().getVendingMachine().isShown())
                Game.getGameWorld().getVendingMachine().makePurchase();
            else if (Game.getGameWorld().getMenu().isCoverShown())
                Game.getGameWorld().getMenu().hideCover();
        }
    }

    private static void weaponListener() {
        if (isPressed(KeyCode.R))
            Game.getGameWorld().getBooker().getWeapon().reload();
        if (!isPressed(KeyCode.R))
            Game.getGameWorld().getBooker().getWeapon().setCanReload(true);
        if (isPressed(KeyCode.J))
            Game.getGameWorld().getBooker().getWeapon().shoot();
        if (!isPressed(KeyCode.J))
            Game.getGameWorld().getBooker().getWeapon().setSingleShot(true);
        if (isPressed(KeyCode.DIGIT1))
            Game.getGameWorld().getBooker().getWeapon().changeWeapon(WeaponType.PISTOL);
        if (isPressed(KeyCode.DIGIT2))
            Game.getGameWorld().getBooker().getWeapon().changeWeapon(WeaponType.MACHINE_GUN);
        if (isPressed(KeyCode.DIGIT3))
            Game.getGameWorld().getBooker().getWeapon().changeWeapon(WeaponType.RPG);
    }

    private static void movementListener() {
        if (isPressed(KeyCode.D) && Game.getGameWorld().getBooker().getTranslateX() < Game.getGameWorld().getGameRoot().getWidth() - Game.getGameWorld().getBooker().getWidth()) {
            Game.getGameWorld().getBooker().moveX(Character.SPEED);
            Game.getGameWorld().getBooker().setScaleX(1);
        }
        if (isPressed(KeyCode.A) && Game.getGameWorld().getBooker().getTranslateX() > 1) {
            Game.getGameWorld().getBooker().moveX(-Character.SPEED);
            Game.getGameWorld().getBooker().setScaleX(-1);
        }
        if (isPressed(KeyCode.A) && isPressed(KeyCode.D))
            Game.getGameWorld().getBooker().stopAnimation();
        if (isPressed(KeyCode.W))
            Game.getGameWorld().getBooker().jump(false);
    }

    private static void energeticListener() {
        if (Game.getGameWorld().getBooker().getEnergetic().getCountEnergetics() > 1 && isPressed(KeyCode.Q))
            Game.getGameWorld().getBooker().getEnergetic().changeEnergetic();
        if (!isPressed(KeyCode.Q))
            Game.getGameWorld().getBooker().getEnergetic().setCanChangeEnergetic(true);
        if (!isPressed(KeyCode.L))
            Game.getGameWorld().getBooker().getEnergetic().setCanShoot(true);
        if (isPressed(KeyCode.L) && !Game.getGameWorld().getBooker().isHypnotized() && Game.getGameWorld().getBooker().getSalt() >= Game.getGameWorld().getBooker().getEnergetic().getSaltPrice())
            Game.getGameWorld().getBooker().getEnergetic().shoot();
    }

    private static void interactionListener() {
        if (isPressed(KeyCode.E) && Game.getGameWorld().getLevel().getCurrentLevelNumber() < Level.BOSS_LEVEL)
            if (Game.getGameWorld().getBooker().getBoundsInParent().intersects(Game.getGameWorld().getBooker().getWeapon().getBoundsInParent()))
                Game.getGameWorld().getBooker().getWeapon().pickUpWeapon();
            else if (Game.getGameWorld().getBooker().getBoundsInParent().intersects(Game.getGameWorld().getBooker().getEnergetic().getBoundsInParent()))
                Game.getGameWorld().getBooker().getEnergetic().pickUp();
            else if (Game.getGameWorld().getBooker().getBoundsInParent().intersects(Game.getGameWorld().getVendingMachine().getVendingMachineImage().getBoundsInParent()) && !Game.getGameWorld().getVendingMachine().isShown())
                Game.getGameWorld().getVendingMachine().openMenu();
        if (isPressed(KeyCode.G)) {
            keys.remove(KeyCode.G);
            Game.getGameWorld().getMenu().checkMusic();
        }
        if (isPressed(KeyCode.ESCAPE) || isPressed(KeyCode.ENTER) || isPressed(KeyCode.SPACE) && Game.getGameWorld().getBooker().isDead()) {
            if (Game.getGameWorld().getBooker().isGameOver())
                Game.getGameWorld().getBooker().newGame();
            else if (Game.getGameWorld().getLevel().getCurrentLevelNumber() == Level.FIRST_LEVEL)
                Game.getGameWorld().getBooker().deathReset();
        }
    }

    private static boolean isPressed(KeyCode keyCode) {
        return keys.getOrDefault(keyCode, false);
    }
}
