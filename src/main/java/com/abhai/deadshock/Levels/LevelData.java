package com.abhai.deadshock.Levels;

import com.abhai.deadshock.Characters.Data;

public class LevelData {
    private String[] level1;
    private String[] level2;
    private String[] level3;
    private String[] bossLevel;

    private Data[] enemyBlocksForLevel2;
    private Data[] enemyBlocksForLevel3;


    public String[] getLevel1() {
        return level1;
    }

    public void setLevel1(String[] level1) {
        this.level1 = level1;
    }

    public String[] getLevel2() {
        return level2;
    }

    public void setLevel2(String[] level2) {
        this.level2 = level2;
    }

    public String[] getLevel3() {
        return level3;
    }

    public void setLevel3(String[] level3) {
        this.level3 = level3;
    }

    public String[] getBossLevel() {
        return bossLevel;
    }

    public void setBossLevel(String[] bossLevel) {
        this.bossLevel = bossLevel;
    }

    public Data[] getEnemyBlocksForLevel2() {
        return enemyBlocksForLevel2;
    }

    public void setEnemyBlocksForLevel2(Data[] enemyBlocksForLevel2) {
        this.enemyBlocksForLevel2 = enemyBlocksForLevel2;
    }

    public Data[] getEnemyBlocksForLevel3() {
        return enemyBlocksForLevel3;
    }

    public void setEnemyBlocksForLevel3(Data[] enemyBlocksForLevel3) {
        this.enemyBlocksForLevel3 = enemyBlocksForLevel3;
    }
}