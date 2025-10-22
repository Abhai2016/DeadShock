package com.abhai.deadshock.utils;

import com.abhai.deadshock.Game;
import com.abhai.deadshock.dtos.EnemiesDTO;
import com.abhai.deadshock.dtos.LevelsDTO;
import com.abhai.deadshock.dtos.MenuOptionsDTO;
import com.abhai.deadshock.dtos.SavesDTO;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

public class SaveManager {
    private static final Path SAVES_PATH = Paths.get("resources", "data", "saves.dat");
    private static final Path OPTIONS_PATH = Paths.get("resources", "data", "options.dat");
    private static final Path ENEMIES_PATH = Paths.get("resources", "data", "enemies.dat");
    private static final Path LEVELS_DATA_PATH = Paths.get("resources", "data", "levels.dat");

    private final ObjectMapper mapper;

    public SaveManager() {
        mapper = new ObjectMapper();
    }

    public void deleteSaves() {
        if (SAVES_PATH.toFile().exists())
            SAVES_PATH.toFile().delete();
    }

    public void saveProgress() {
        try (FileWriter fileWriter = new FileWriter(SAVES_PATH.toFile())) {
            deleteSaves();

            if (!SAVES_PATH.toFile().exists())
                SAVES_PATH.toFile().createNewFile();

            fileWriter.write(mapper.writeValueAsString(Game.getGameWorld().generateSavesDTO()));
        } catch (Exception e) {
            System.out.println(e.getLocalizedMessage());
            throw new RuntimeException("Could not save progress");
        }
    }

    public void saveMenuOptions() {
        try (FileWriter fileWriter = new FileWriter(OPTIONS_PATH.toFile())) {
            if (OPTIONS_PATH.toFile().exists())
                OPTIONS_PATH.toFile().delete();

            if (!OPTIONS_PATH.toFile().exists())
                OPTIONS_PATH.toFile().createNewFile();

            fileWriter.write(mapper.writeValueAsString(Game.getGameWorld().generateMenuOptionsDTO()));
        } catch (Exception e) {
            System.out.println(e.getLocalizedMessage());
            throw new RuntimeException("Could not save options");
        }
    }

    public SavesDTO loadProgress() {
        try {
            if (SAVES_PATH.toFile().exists())
                return mapper.readValue(SAVES_PATH.toFile(), SavesDTO.class);
            else
                return null;
        } catch (IOException e) {
            System.out.println(e.getLocalizedMessage());
            throw new RuntimeException("Could not retrieve saves");
        }
    }

    public EnemiesDTO getEnemies() {
        try {
            if (ENEMIES_PATH.toFile().exists())
                return mapper.readValue(ENEMIES_PATH.toFile(), EnemiesDTO.class);
            else
                return null;
        } catch (IOException e) {
            System.out.println(e.getLocalizedMessage());
            throw new RuntimeException("Could not retrieve enemies");
        }
    }

    public LevelsDTO getLevels() {
        try {
            if (LEVELS_DATA_PATH.toFile().exists())
                return mapper.readValue(LEVELS_DATA_PATH.toFile(), LevelsDTO.class);
            else
                return null;
        } catch (IOException e) {
            System.out.println(e.getLocalizedMessage());
            throw new RuntimeException("Could not retrieve levels");
        }
    }

    public MenuOptionsDTO loadMenuOptions() {
        try {
            if (OPTIONS_PATH.toFile().exists())
                return mapper.readValue(OPTIONS_PATH.toFile(), MenuOptionsDTO.class);
            else
                return null;
        } catch (IOException e) {
            System.out.println(e.getLocalizedMessage());
            throw new RuntimeException("Could not retrieve menu options");
        }
    }
}
