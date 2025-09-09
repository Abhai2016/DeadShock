package com.abhai.deadshock.dtos;

import com.abhai.deadshock.types.DifficultyType;

public class SavesDTO {
    private int salt;
    private int money;
    private int rpgBullets;
    private int levelNumber;
    private int pistolBullets;
    private int machineGunBullets;
    private DifficultyType difficultyType;

    private boolean canChooseRPG;
    private boolean canChoosePistol;
    private boolean canChooseHypnosis;
    private boolean canChooseDevilKiss;
    private boolean canChooseMachineGun;
    private boolean canChooseElectricity;

    public int getSalt() {
        return salt;
    }

    public int getMoney() {
        return money;
    }

    public int getRpgBullets() {
        return rpgBullets;
    }

    public void setSalt(int salt) {
        this.salt = salt;
    }

    public int getLevelNumber() {
        return levelNumber;
    }

    public void setMoney(int money) {
        this.money = money;
    }

    public int getPistolBullets() {
        return pistolBullets;
    }

    public boolean isCanChooseRPG() {
        return canChooseRPG;
    }

    public boolean isCanChoosePistol() {
        return canChoosePistol;
    }

    public int getMachineGunBullets() {
        return machineGunBullets;
    }

    public boolean isCanChooseHypnosis() {
        return canChooseHypnosis;
    }

    public boolean isCanChooseDevilKiss() {
        return canChooseDevilKiss;
    }

    public DifficultyType getDifficultyType() {
        return difficultyType;
    }

    public boolean isCanChooseMachineGun() {
        return canChooseMachineGun;
    }

    public void setCanChooseElectricity(boolean canChooseElectricity) {
        this.canChooseElectricity = canChooseElectricity;
    }

    public boolean isCanChooseElectricity() {
        return canChooseElectricity;
    }

    public void setRpgBullets(int rpgBullets) {
        this.rpgBullets = rpgBullets;
    }

    public void setLevelNumber(int levelNumber) {
        this.levelNumber = levelNumber;
    }

    public void setPistolBullets(int pistolBullets) {
        this.pistolBullets = pistolBullets;
    }

    public void setCanChooseRPG(boolean canChooseRPG) {
        this.canChooseRPG = canChooseRPG;
    }

    public void setCanChoosePistol(boolean canChoosePistol) {
        this.canChoosePistol = canChoosePistol;
    }

    public void setDifficultyType(DifficultyType difficultyType) {
        this.difficultyType = difficultyType;
    }

    public void setMachineGunBullets(int machineGunBullets) {
        this.machineGunBullets = machineGunBullets;
    }

    public void setCanChooseHypnosis(boolean canChooseHypnosis) {
        this.canChooseHypnosis = canChooseHypnosis;
    }

    public void setCanChooseDevilKiss(boolean canChooseDevilKiss) {
        this.canChooseDevilKiss = canChooseDevilKiss;
    }

    public void setCanChooseMachineGun(boolean canChooseMachineGun) {
        this.canChooseMachineGun = canChooseMachineGun;
    }
}
