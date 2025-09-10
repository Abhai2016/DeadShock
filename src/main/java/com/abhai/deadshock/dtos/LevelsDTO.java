package com.abhai.deadshock.dtos;

public class LevelsDTO {
    private String[] firstLevel;
    private String[] secondLevel;
    private String[] thirdLevel;
    private String[] bossLevel;

    public String[] getBossLevel() {
        return bossLevel;
    }

    public String[] getFirstLevel() {
        return firstLevel;
    }

    public String[] getThirdLevel() {
        return thirdLevel;
    }

    public String[] getSecondLevel() {
        return secondLevel;
    }
}
