package com.abhai.deadshock.levels;

import com.abhai.deadshock.characters.enemies.json.EnemyData;

public class LevelData {
    private String[] firstLevel;
    private String[] secondLevel;
    private String[] thirdLevel;
    private String[] bossLevel;

    private EnemyData[] enemyBlocksForTheSecondLevel;
    private EnemyData[] enemyBlocksForTheThirdLevel;


    public String[] getFirstLevel() {
        return firstLevel;
    }

    public void setFirstLevel(String[] firstLevel) {
        this.firstLevel = firstLevel;
    }

    public String[] getSecondLevel() {
        return secondLevel;
    }

    public void setSecondLevel(String[] secondLevel) {
        this.secondLevel = secondLevel;
    }

    public String[] getThirdLevel() {
        return thirdLevel;
    }

    public void setThirdLevel(String[] thirdLevel) {
        this.thirdLevel = thirdLevel;
    }

    public String[] getBossLevel() {
        return bossLevel;
    }

    public void setBossLevel(String[] bossLevel) {
        this.bossLevel = bossLevel;
    }

    public EnemyData[] getEnemyBlocksForTheSecondLevel() {
        return enemyBlocksForTheSecondLevel;
    }

    public void setEnemyBlocksForTheSecondLevel(EnemyData[] enemyBlocksForTheSecondLevel) {
        this.enemyBlocksForTheSecondLevel = enemyBlocksForTheSecondLevel;
    }

    public EnemyData[] getEnemyBlocksForTheThirdLevel() {
        return enemyBlocksForTheThirdLevel;
    }

    public void setEnemyBlocksForTheThirdLevel(EnemyData[] enemyBlocksForTheThirdLevel) {
        this.enemyBlocksForTheThirdLevel = enemyBlocksForTheThirdLevel;
    }
}