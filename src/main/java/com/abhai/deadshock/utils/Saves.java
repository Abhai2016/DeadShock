package com.abhai.deadshock.utils;

public class Saves {
    private String difficultyLevel;
    private int levelNumber;
    private int money;
    private int salt;
    private int pistolClip;
    private int pistolBullets;
    private int machineGunClip;
    private int machineGunBullets;
    private int rpgClip;
    private int rpgBullets;

    private boolean canChoosePistol;
    private boolean canChooseMachineGun;
    private boolean canChooseRPG;
    private boolean canChooseDevilKiss;
    private boolean canChooseElectricity;
    private boolean canChooseHypnosis;


    public boolean isCanChooseHypnosis() {
        return canChooseHypnosis;
    }

    public void setCanChooseHypnosis(boolean canChooseHypnosis) {
        this.canChooseHypnosis = canChooseHypnosis;
    }

    public boolean isCanChooseRPG() {
        return canChooseRPG;
    }

    public void setCanChooseRPG(boolean canChooseRPG) {
        this.canChooseRPG = canChooseRPG;
    }

    public int getRpgClip() {
        return rpgClip;
    }

    public void setRpgClip(int rpgClip) {
        this.rpgClip = rpgClip;
    }

    public int getRpgBullets() {
        return rpgBullets;
    }

    public void setRpgBullets(int rpgBullets) {
        this.rpgBullets = rpgBullets;
    }

    public boolean isCanChooseMachineGun() {
        return canChooseMachineGun;
    }

    public void setCanChooseMachineGun(boolean canChooseMachineGun) {
        this.canChooseMachineGun = canChooseMachineGun;
    }

    public boolean isCanChooseElectricity() {
        return canChooseElectricity;
    }

    public void setCanChooseElectricity(boolean canChooseElectricity) {
        this.canChooseElectricity = canChooseElectricity;
    }

    public int getMachineGunClip() {
        return machineGunClip;
    }

    public void setMachineGunClip(int machineGunClip) {
        this.machineGunClip = machineGunClip;
    }

    public int getMachineGunBullets() {
        return machineGunBullets;
    }

    public void setMachineGunBullets(int machineGunBullets) {
        this.machineGunBullets = machineGunBullets;
    }

    public String getDifficultyLevel() {
        return difficultyLevel;
    }

    public void setDifficultyLevel(String difficultyLevel) {
        this.difficultyLevel = difficultyLevel;
    }

    public int getLevelNumber() {
        return levelNumber;
    }

    public void setLevelNumber(int levelNumber) {
        this.levelNumber = levelNumber;
    }

    public int getMoney() {
        return money;
    }

    public void setMoney(int money) {
        this.money = money;
    }

    public int getSalt() {
        return salt;
    }

    public void setSalt(int salt) {
        this.salt = salt;
    }

    public int getPistolClip() {
        return pistolClip;
    }

    public void setPistolClip(int pistolClip) {
        this.pistolClip = pistolClip;
    }

    public int getPistolBullets() {
        return pistolBullets;
    }

    public void setPistolBullets(int pistolBullets) {
        this.pistolBullets = pistolBullets;
    }

    public boolean isCanChooseDevilKiss() {
        return canChooseDevilKiss;
    }

    public void setCanChooseDevilKiss(boolean canChooseDevilKiss) {
        this.canChooseDevilKiss = canChooseDevilKiss;
    }

    public boolean isCanChoosePistol() {
        return canChoosePistol;
    }

    public void setCanChoosePistol(boolean canChoosePistol) {
        this.canChoosePistol = canChoosePistol;
    }
}
