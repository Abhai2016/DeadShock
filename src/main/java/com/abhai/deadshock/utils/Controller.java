package com.abhai.deadshock.utils;

import com.abhai.deadshock.Game;
import com.abhai.deadshock.characters.Character;
import com.abhai.deadshock.weapons.WeaponType;
import javafx.scene.input.KeyCode;

public class Controller {

    public static void update() {
        if (isPressed(KeyCode.ESCAPE) && (Game.vendingMachine == null || !Game.vendingMachine.isShown())) {
            Game.keys.remove(KeyCode.ESCAPE);
            if (Game.menu.isShown())
                Game.menu.hideMenu();
            else
                Game.menu.showMenu();
        }

        if (!Game.active)
            return;

        if (isPressed(KeyCode.D) && Game.booker.getTranslateX() < Game.gameRoot.getWidth() - Game.booker.getWidth()
                && !Game.booker.isHypnotized()) {
            Game.booker.moveX(Character.SPEED);
            Game.booker.setScaleX(1);
        }

        if (isPressed(KeyCode.A) && Game.booker.getTranslateX() > 1 && !Game.booker.isHypnotized()) {
            Game.booker.moveX(-Character.SPEED);
            Game.booker.setScaleX(-1);
        }

        if (isPressed(KeyCode.A) && isPressed(KeyCode.D) && !Game.booker.isHypnotized())
            Game.booker.stopAnimation();

        if (!isPressed(KeyCode.A) && !isPressed(KeyCode.D))
            Game.booker.setIdleAnimation();

        if (isPressed(KeyCode.W) && !Game.booker.isHypnotized())
            Game.booker.jump(false);

        if (Game.energetic.getCountEnergetics() > 0)
            if (isPressed(KeyCode.Q))
                Game.energetic.changeEnergetic();
            else
                Game.energetic.setCanChangeEnergetic(true);

        if (isPressed(KeyCode.E))
            if (Game.booker.getBoundsInParent().intersects(Game.weapon.getBoundsInParent()))
                Game.weapon.pickUpWeapon();
            else if (Game.vendingMachine != null)
                if (Game.booker.getBoundsInParent().intersects(Game.vendingMachine.getVendingMachine().getBoundsInParent())
                        && !Game.vendingMachine.isShown())
                    Game.vendingMachine.openMachineMenu();
                else if (Game.booker.getBoundsInParent().intersects(Game.energetic.getBoundsInParent()))
                    Game.energetic.pickUp();

        if (isPressed(KeyCode.R) && Game.weapon.isCanReload() && !Game.booker.isHypnotized())
            Game.weapon.reload();

        if (!isPressed(KeyCode.R))
            Game.weapon.setCanReload(true);

        if (isPressed(KeyCode.J) && Game.weapon.getWeaponClip() > 0 && !Game.weapon.isNowReloading() && !Game.booker.isHypnotized())
            Game.weapon.shoot();

        if (isPressed(KeyCode.L) && !Game.booker.isHypnotized() && Game.booker.getSalt() >= Game.energetic.getSaltPrice())
            Game.energetic.shoot();

        if (!isPressed(KeyCode.L))
            Game.energetic.setCanShoot(true);

        if (!isPressed(KeyCode.J))
            Game.weapon.setSingleShot(true);

        if (isPressed(KeyCode.DIGIT1) && !Game.weapon.isNowReloading() && !Game.booker.isHypnotized())
            Game.weapon.changeWeapon(WeaponType.PISTOL);

        if (isPressed(KeyCode.DIGIT2) && !Game.weapon.isNowReloading() && !Game.booker.isHypnotized())
            Game.weapon.changeWeapon(WeaponType.MACHINE_GUN);

        if (isPressed(KeyCode.DIGIT3) && !Game.weapon.isNowReloading() && !Game.booker.isHypnotized())
            Game.weapon.changeWeapon(WeaponType.RPG);

        if (isPressed(KeyCode.G)) {
            Game.keys.remove(KeyCode.G);
            Game.menu.checkMusic();
        }
    }

    public static boolean isPressed(KeyCode keyCode) {
        return Game.keys.getOrDefault(keyCode, false);
    }
}
