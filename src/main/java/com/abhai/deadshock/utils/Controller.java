package com.abhai.deadshock.utils;

import com.abhai.deadshock.Game;
import com.abhai.deadshock.characters.Character;
import javafx.scene.input.KeyCode;

public class Controller {

    public static void update() {
        if (isPressed(KeyCode.D) && Game.booker.getTranslateX() < Game.gameRoot.getWidth() - Game.booker.getWidth()
                && !Game.booker.isStunned()) {
            Game.booker.moveX(Character.SPEED);
            Game.booker.setScaleX(1);
        }

        if (isPressed(KeyCode.A) && Game.booker.getTranslateX() > 1 && !Game.booker.isStunned()) {
            Game.booker.moveX(-Character.SPEED);
            Game.booker.setScaleX(-1);
        }

        if (isPressed(KeyCode.A) && isPressed(KeyCode.D) && !Game.booker.isStunned())
            Game.booker.stopAnimation();

        if (!isPressed(KeyCode.A) && !isPressed(KeyCode.D))
            Game.booker.setIdleAnimation();

        if (isPressed(KeyCode.W) && !Game.booker.isStunned())
            Game.booker.jump(false);

        if (isPressed(KeyCode.Q) && Game.energetic.isCanChangeEnergetic())
            Game.energetic.changeEnergetic();

        if (!isPressed(KeyCode.Q) && Game.energetic.getCountEnergetics() > 1)
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


        if (isPressed(KeyCode.R) && Game.weapon.isCanReload() && !Game.booker.isStunned())
            Game.weapon.reload();

        if (!isPressed(KeyCode.R))
            Game.weapon.setCanReload(true);


        if (isPressed(KeyCode.J) && Game.weapon.getWeaponClip() > 0 && !Game.weapon.isNowReloading() && !Game.booker.isStunned())
            Game.weapon.shoot();


        if (isPressed(KeyCode.L) && Game.energetic.getType() != null && !Game.booker.isStunned()
                && Game.booker.getSalt() >= Game.energetic.getPriceForUsing() && Game.energetic.isShoot())
            Game.energetic.shoot();


        if (!isPressed(KeyCode.L))
            Game.energetic.setShoot(true);


        if (!isPressed(KeyCode.J))
            Game.weapon.setSingleShot(true);

        if (isPressed(KeyCode.DIGIT1) && !Game.weapon.isNowReloading() && !Game.booker.isStunned())
            Game.weapon.changeWeapon("pistol");

        if (isPressed(KeyCode.DIGIT2) && !Game.weapon.isNowReloading() && !Game.booker.isStunned())
            Game.weapon.changeWeapon("machine_gun");

        if (isPressed(KeyCode.DIGIT3) && !Game.weapon.isNowReloading() && !Game.booker.isStunned())
            Game.weapon.changeWeapon("rpg");
    }


    public static boolean isPressed(KeyCode keyCode) {
        return Game.keys.getOrDefault(keyCode, false);
    }
}
