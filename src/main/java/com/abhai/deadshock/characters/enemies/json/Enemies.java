package com.abhai.deadshock.characters.enemies.json;

public class Enemies {
    private EnemyData[] firstLevel;
    private EnemyData[] secondLevel;
    private EnemyData[] thirdLevel;

    public EnemyData[] getFirstLevel() {
        return firstLevel;
    }

    public void setFirstLevel(EnemyData[] firstLevel) {
        this.firstLevel = firstLevel;
    }

    public EnemyData[] getSecondLevel() {
        return secondLevel;
    }

    public void setSecondLevel(EnemyData[] secondLevel) {
        this.secondLevel = secondLevel;
    }

    public EnemyData[] getThirdLevel() {
        return thirdLevel;
    }

    public void setThirdLevel(EnemyData[] thirdLevel) {
        this.thirdLevel = thirdLevel;
    }
}
