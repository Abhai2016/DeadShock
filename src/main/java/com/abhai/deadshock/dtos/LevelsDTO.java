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

    public void setBossLevel(String[] bossLevel) {
        this.bossLevel = bossLevel;
    }

    public void setFirstLevel(String[] firstLevel) {
        this.firstLevel = firstLevel;
    }

    public void setThirdLevel(String[] thirdLevel) {
        this.thirdLevel = thirdLevel;
    }

    public void setSecondLevel(String[] secondLevel) {
        this.secondLevel = secondLevel;
    }
}
