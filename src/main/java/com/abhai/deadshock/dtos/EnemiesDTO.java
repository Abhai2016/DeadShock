package com.abhai.deadshock.dtos;

import java.util.ArrayList;

public class EnemiesDTO {
    private ArrayList<EnemyDTO> bossLevel;
    private ArrayList<EnemyDTO> firstLevel;
    private ArrayList<EnemyDTO> thirdLevel;
    private ArrayList<EnemyDTO> secondLevel;

    public ArrayList<EnemyDTO> getBossLevel() {
        return bossLevel;
    }

    public ArrayList<EnemyDTO> getFirstLevel() {
        return firstLevel;
    }

    public ArrayList<EnemyDTO> getThirdLevel() {
        return thirdLevel;
    }

    public ArrayList<EnemyDTO> getSecondLevel() {
        return secondLevel;
    }
}
