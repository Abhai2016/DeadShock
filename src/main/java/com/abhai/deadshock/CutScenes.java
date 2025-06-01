package com.abhai.deadshock;


import com.abhai.deadshock.Characters.Boss;
import com.abhai.deadshock.Characters.Elizabeth;
import com.abhai.deadshock.Levels.Block;
import com.abhai.deadshock.Levels.Level;
import com.abhai.deadshock.Weapon.Weapon;
import javafx.animation.FadeTransition;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.util.Duration;
import org.json.simple.JSONObject;

import java.io.*;

import static javafx.scene.input.KeyEvent.KEY_PRESSED;

public class CutScenes {
    private MediaPlayer video;
    private MediaView videoView;
    ImageView imageView = new ImageView("file:/../images/black.jpg");

    public CutScenes() {
        Game.booker.setTranslateX(100);
        Game.booker.setTranslateY(500);
        if (Game.elizabeth != null)
            Game.elizabeth.setTranslateX(100);

        Game.menu.music.pause();
        Game.timer.stop();

        for (Block block : Game.blocks)
            Game.gameRoot.getChildren().remove(block);
        Game.blocks.clear();
        Game.clearData();

        Game.stage.setWidth(1235);
        switch ((int)Game.levelNumber) {
            case 0:
                playVideo("file:/../videos/meeting_Elizabeth.mp4");

                video.setOnEndOfMedia( () -> endCutScene1() );

                Game.scene.addEventFilter(KEY_PRESSED, event -> {
                    if (event.getCode() == KeyCode.ENTER || event.getCode() == KeyCode.ESCAPE)
                        endCutScene1();
                });
                break;

            case 1:
                playVideo("file:/../videos/murder_Comstock.mp4");
                video.setOnEndOfMedia( () -> endCutScene2() );

                Game.scene.addEventFilter(KEY_PRESSED,event ->  {
                    if (event.getCode() == KeyCode.ENTER || event.getCode() == KeyCode.ESCAPE)
                        endCutScene2();
                });
                break;
            case 2:
                bossLevel();
                break;
            case 3:
                Game.stage.setWidth(1230);
                playVideo("file:/../videos/end.mp4");
                video.setOnEndOfMedia( () -> endCutScene2() );

                Game.scene.addEventFilter(KEY_PRESSED, event ->  {
                    if (event.getCode() == KeyCode.ENTER || event.getCode() == KeyCode.ESCAPE)
                        endCutScene3();
                });
                break;
        }
    }


    private void playVideo(String str) {
        video = new MediaPlayer(new Media(new File(str).toURI().toString()));
        videoView = new MediaView(video);
        videoView.setFitWidth(Game.scene.getWidth());
        videoView.setFitHeight(Game.scene.getHeight());
        Game.appRoot.getChildren().add(videoView);

        FadeTransition ft = new FadeTransition(Duration.seconds(1), videoView);
        ft.setFromValue(0);
        ft.setToValue(1);
        ft.play();
        video.play();
    }


    private void endCutScene1() {
        if (Game.tutorial != null)
            Game.tutorial.deleteText();
        Game.levelNumber++;
        Game.level.changeLevel(Game.levelNumber);
        Game.weapon.changeLevel(Game.levelNumber);

        video.stop();
        Game.appRoot.getChildren().remove(videoView);
        videoView = null;

        Game.appRoot.getChildren().add(imageView);

        Sounds.bookerVoice = new MediaPlayer(new Media(new File("file:/../sounds/voice/hack_padlock.mp3").toURI().toString()));
        Sounds.bookerVoice.setVolume(Game.menu.voiceSlider.getValue() / 100);
        Sounds.bookerVoice.play();
        Sounds.bookerVoice.setOnEndOfMedia( () -> partEndCutScene1() );

        Game.scene.addEventFilter(KEY_PRESSED,event -> {
            if (event.getCode() == KeyCode.ESCAPE || event.getCode() == KeyCode.ENTER)
                partEndCutScene1();
        });
    }


    private void partEndCutScene1() {
        Sounds.bookerVoice.stop();
        Game.level.createLevels(Game.levelNumber);
        Game.createEnemies();

        Game.timer.start();
        Game.menu.music.play();
        Game.menu.addListener();
        Game.elizabeth = new Elizabeth();
        Game.tutorial = new Tutorial();
        Game.vendingMachine.changeLevel();
        Game.energetic.changeLevel();
        Game.stage.setWidth(1280);
        Game.appRoot.getChildren().remove(imageView);

        try (FileWriter fileWriter = new FileWriter("file:/../data/saves.dat")) {
            JSONObject levelData = new JSONObject();
            levelData.put("difficultyLevel", Game.difficultyLevelText);
            levelData.put("levelNumber", Game.levelNumber);

            JSONObject character = new JSONObject();
            character.put("money", Game.booker.getMoney());
            character.put("salt", Game.booker.getSalt());
            character.put("pistolClip", Game.weapon.getWeaponClip());
            character.put("pistolBullets", Game.weapon.getBullets());
            character.put("canChooseDevilKiss", Game.energetic.isCanChooseDevilKiss());
            character.put("canChoosePistol", Game.weapon.isCanChoosePistol());

            JSONObject result = new JSONObject();
            result.put("character", character);
            result.put("levelData", levelData);

            fileWriter.write(result.toString());
        } catch (Exception e) {
            System.exit(0);
        }

        try (FileWriter fileWriter = new FileWriter("file:/../data/options.dat")) {
            JSONObject optionsData = new JSONObject();
            optionsData.put("musicVolume", Game.menu.musicSlider.getValue());
            optionsData.put("FXVolume", Game.menu.fxSlider.getValue());
            optionsData.put("voiceVolume", Game.menu.voiceSlider.getValue());
            optionsData.put("track", Game.menu.music.getMedia().getSource());

            fileWriter.write(optionsData.toString());
        } catch (Exception e) {
            System.exit(0);
        }
    }


    private void endCutScene2() {
        video.stop();
        Game.appRoot.getChildren().remove(videoView);
        videoView = null;

        Game.levelNumber++;
        Game.level.changeLevel(Game.levelNumber);
        Game.level.createLevels(Game.levelNumber);
        Game.weapon.changeLevel(Game.levelNumber);
        Game.createEnemies();

        Game.timer.start();
        Game.menu.music.play();
        Game.menu.addListener();
        Game.tutorial = new Tutorial();
        Game.vendingMachine.changeLevel();
        Game.energetic.changeLevel();
        Game.stage.setWidth(1280);
        Game.appRoot.getChildren().remove(imageView);
        Game.boss = new Boss(Level.BLOCK_SIZE * 299, Level.BLOCK_SIZE * 13);

        try (FileWriter fileWriter = new FileWriter("file:/../data/saves.dat")) {
            JSONObject levelData = new JSONObject();
            levelData.put("difficultyLevel", Game.difficultyLevelText);
            levelData.put("levelNumber", Game.levelNumber);

            JSONObject character = new JSONObject();
            character.put("money", Game.booker.getMoney());
            character.put("salt", Game.booker.getSalt());

            if (Game.weapon.getName().equals("pistol")) {
                character.put("pistolClip", Game.weapon.getWeaponClip());
                character.put("pistolBullets", Game.weapon.getBullets());
                character.put("machineGunClip", Weapon.WeaponData.machineGunClip);
                character.put("machineGunBullets", Weapon.WeaponData.machineGunBullets);
            } else if (Game.weapon.getName().equals("machine_gun")) {
                character.put("pistolClip", Weapon.WeaponData.pistolClip);
                character.put("pistolBullets", Weapon.WeaponData.pistolBullets);
                character.put("machineGunClip", Game.weapon.getWeaponClip());
                character.put("machineGunBullets", Game.weapon.getBullets());
            }

            character.put("canChooseMachineGun", Game.weapon.isCanChooseMachineGun());
            character.put("canChooseElectricity", Game.energetic.isCanChooseElectricity());
            character.put("canChooseDevilKiss", Game.energetic.isCanChooseDevilKiss());
            character.put("canChoosePistol", Game.weapon.isCanChoosePistol());

            JSONObject result = new JSONObject();
            result.put("character", character);
            result.put("levelData", levelData);

            fileWriter.write(result.toString());
        } catch (Exception e) {
            System.exit(0);
        }

        try (FileWriter fileWriter = new FileWriter("file:/../data/options.dat")) {
            JSONObject optionsData = new JSONObject();
            optionsData.put("musicVolume", Game.menu.musicSlider.getValue());
            optionsData.put("FXVolume", Game.menu.fxSlider.getValue());
            optionsData.put("voiceVolume", Game.menu.voiceSlider.getValue());
            optionsData.put("track", Game.menu.music.getMedia().getSource());

            fileWriter.write(optionsData.toString());
        } catch (Exception e) {
            System.exit(0);
        }
    }


    private void bossLevel() {
        Game.boss.setTrompInterval(0);
        Game.levelNumber++;
        Game.level.changeLevel(Game.levelNumber);
        Game.level.createLevels(Game.levelNumber);

        Game.timer.start();
        Game.menu.music.play();
        Game.menu.addListener();
        Game.stage.setWidth(1280);
        Game.boss.setBoss();

        try (FileWriter fileWriter = new FileWriter("file:/../data/saves.dat")) {
            JSONObject levelData = new JSONObject();
            levelData.put("difficultyLevel", Game.difficultyLevelText);
            levelData.put("levelNumber", Game.levelNumber);

            JSONObject character = new JSONObject();
            character.put("money", Game.booker.getMoney());
            character.put("salt", Game.booker.getSalt());

            switch (Game.weapon.getName()) {
                case "pistol":
                    character.put("pistolClip", Game.weapon.getWeaponClip());
                    character.put("pistolBullets", Game.weapon.getBullets());
                    character.put("machineGunClip", Weapon.WeaponData.machineGunClip);
                    character.put("machineGunBullets", Weapon.WeaponData.machineGunBullets);
                    break;
                case "machine_gun":
                    character.put("pistolClip", Weapon.WeaponData.pistolClip);
                    character.put("pistolBullets", Weapon.WeaponData.pistolBullets);
                    character.put("machineGunClip", Game.weapon.getWeaponClip());
                    character.put("machineGunBullets", Game.weapon.getBullets());
                    break;
                case "rpg":
                    character.put("pistolClip", Weapon.WeaponData.pistolClip);
                    character.put("pistolBullets", Weapon.WeaponData.pistolBullets);
                    character.put("machineGunClip", Weapon.WeaponData.machineGunClip);
                    character.put("machineGunBullets", Weapon.WeaponData.machineGunBullets);
                    if (Game.weapon.getWeaponClip() < 1)
                        Game.weapon.setWeaponClip(1);
                    character.put("rpgClip", Game.weapon.getWeaponClip());
                    character.put("rpgBullets", Game.weapon.getBullets());
                    break;
            }

            character.put("canChoosePistol", Game.weapon.isCanChoosePistol());
            character.put("canChooseMachineGun", Game.weapon.isCanChooseMachineGun());
            character.put("canChooseRPG", Game.weapon.isCanChooseRPG());
            character.put("canChooseDevilKiss", Game.energetic.isCanChooseDevilKiss());
            character.put("canChooseElectricity", Game.energetic.isCanChooseElectricity());
            character.put("canChooseHypnotist", Game.energetic.isCanChooseHypnotist());

            JSONObject result = new JSONObject();
            result.put("character", character);
            result.put("levelData", levelData);

            fileWriter.write(result.toString());
        } catch (Exception e) {
            System.exit(0);
        }

        try (FileWriter fileWriter = new FileWriter("file:/../data/options.dat")) {
            JSONObject optionsData = new JSONObject();
            optionsData.put("musicVolume", Game.menu.musicSlider.getValue());
            optionsData.put("FXVolume", Game.menu.fxSlider.getValue());
            optionsData.put("voiceVolume", Game.menu.voiceSlider.getValue());
            optionsData.put("track", Game.menu.music.getMedia().getSource());

            fileWriter.write(optionsData.toString());
        } catch (Exception e) {
            System.exit(0);
        }
    }


    private void endCutScene3() {
        Game.clearDataForNewGame("Continue Game");
        Game.appRoot.getChildren().remove(Game.gameRoot);
        video.stop();
        Game.appRoot.getChildren().remove(videoView);
        videoView = null;
        Game.stage.setWidth(1280);
        Game.initContent();
    }
}
