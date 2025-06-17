package com.abhai.deadshock;

import com.abhai.deadshock.characters.enemies.Boss;
import com.abhai.deadshock.characters.Elizabeth;
import com.abhai.deadshock.levels.Block;
import com.abhai.deadshock.levels.Level;
import com.abhai.deadshock.weapon.Weapon;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.animation.FadeTransition;
import javafx.scene.input.KeyCode;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.util.Duration;

import java.io.FileWriter;
import java.nio.file.Path;
import java.nio.file.Paths;

public class CutScenes {
    private MediaPlayer video;
    private MediaView videoView;

    private Path savesPath = Paths.get("resources", "data", "saves.dat");
    private Path optionsPath = Paths.get("resources", "data", "options.dat");
    private boolean actionPerformed = false;

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
        switch (Game.levelNumber) {
            case Level.FIRST_LEVEL -> {
                playVideo("meeting_Elizabeth.mp4");
                video.setOnEndOfMedia( () -> {
                    if (!actionPerformed) {
                        actionPerformed = true;
                        endCutScene1();
                    }
                });
                Game.scene.setOnKeyPressed(event -> {
                    if (event.getCode() == KeyCode.ESCAPE || event.getCode() == KeyCode.ENTER
                            && !actionPerformed) {
                        actionPerformed = true;
                        endCutScene1();
                        event.consume();
                    }
                });
            }
            case Level.SECOND_LEVEL -> {
                playVideo("murder_Comstock.mp4");
                video.setOnEndOfMedia( () -> {
                    if (!actionPerformed) {
                        actionPerformed = true;
                        endCutScene2();
                    }
                });
                Game.scene.setOnKeyPressed(event -> {
                    if (event.getCode() == KeyCode.ESCAPE  || event.getCode() == KeyCode.ENTER
                            && !actionPerformed) {
                        actionPerformed = true;
                        endCutScene2();
                        event.consume();
                    }
                });
            }
            case Level.THIRD_LEVEL -> bossLevel();
            case Level.BOSS_LEVEL -> {
                Game.stage.setWidth(1230);
                playVideo("end.mp4");
                video.setOnEndOfMedia( () -> {
                    if (!actionPerformed) {
                        actionPerformed = true;
                        endCutScene3();
                    }
                });
                Game.scene.setOnKeyPressed(event -> {
                    if (event.getCode() == KeyCode.ESCAPE  || event.getCode() == KeyCode.ENTER
                            && !actionPerformed) {
                        actionPerformed = true;
                        endCutScene3();
                        event.consume();
                    }
                });
            }
        }
    }

    private void playVideo(String str) {
        Path videoPath = Paths.get("resources", "videos", str);
        video = new MediaPlayer(new Media(videoPath.toUri().toString()));
        videoView = new MediaView(video);
        videoView.setFitWidth(Game.scene.getWidth());
        videoView.setFitHeight(Game.scene.getHeight());
        videoView.getMediaPlayer().setVolume(Game.menu.voiceSlider.getValue() / 100);
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
        Game.level.changeLevel();
        Game.weapon.changeLevel(Game.levelNumber);

        video.stop();
        Game.appRoot.getChildren().remove(videoView);
        videoView = null;

        Sounds.bookerVoice.stop();
        Game.level.createLevels();
        Game.createEnemies();

        Game.timer.start();
        Game.menu.music.play();
        Game.menu.addListener();
        Game.elizabeth = new Elizabeth();
        Game.tutorial = new Tutorial();
        Game.vendingMachine.changeLevel();
        Game.energetic.changeLevel();
        Game.stage.setWidth(1280);

        ObjectMapper mapper = new ObjectMapper();
        saveSaves(mapper);
        saveOptions(mapper);
    }

    private void endCutScene2() {
        video.stop();
        Game.appRoot.getChildren().remove(videoView);
        videoView = null;

        Game.levelNumber++;
        Game.level.changeLevel();
        Game.level.createLevels();
        Game.weapon.changeLevel(Game.levelNumber);
        Game.createEnemies();

        Game.timer.start();
        Game.menu.music.play();
        Game.menu.addListener();
        Game.tutorial = new Tutorial();
        Game.vendingMachine.changeLevel();
        Game.energetic.changeLevel();
        Game.stage.setWidth(1280);
        Game.boss = new Boss(Block.BLOCK_SIZE * 299, Block.BLOCK_SIZE * 13);

        ObjectMapper mapper = new ObjectMapper();
        saveSaves(mapper);
        saveOptions(mapper);
    }

    private void bossLevel() {
        Game.tutorial.deleteText();
        Game.boss.setTrompInterval(0);
        Game.levelNumber++;
        Game.level.changeLevel();
        Game.level.createLevels();

        Game.timer.start();
        Game.menu.music.play();
        Game.menu.addListener();
        Game.stage.setWidth(1280);
        Game.boss.setBoss();

        ObjectMapper mapper = new ObjectMapper();
        saveSaves(mapper);
        saveOptions(mapper);
    }

    private void endCutScene3() {
        Game.appRoot.getChildren().remove(videoView);
        video.stop();
        videoView = null;
        Game.stage.setWidth(1280);
        System.exit(0);
    }

    private void saveSaves(ObjectMapper mapper) {
        try (FileWriter fileWriter = new FileWriter(savesPath.toFile())) {
            if (savesPath.toFile().exists()) {
                savesPath.toFile().delete();
            }

            if (!savesPath.toFile().exists()) {
                savesPath.toFile().createNewFile();
            }

            Saves saves = new Saves();
            saves.setDifficultyLevel(Game.difficultyLevelText);
            saves.setLevelNumber(Game.levelNumber);
            saves.setMoney(Game.booker.getMoney());
            saves.setSalt(Game.booker.getSalt());

            saves.setPistolClip(Weapon.WeaponData.pistolClip);
            saves.setPistolBullets(Weapon.WeaponData.pistolBullets);
            saves.setMachineGunClip(Weapon.WeaponData.machineGunClip);
            saves.setMachineGunBullets(Weapon.WeaponData.machineGunBullets);
            saves.setRpgClip(Weapon.WeaponData.rpgClip);
            saves.setRpgBullets(Weapon.WeaponData.rpgBullets);

            saves.setCanChoosePistol(Game.weapon.isCanChoosePistol());
            saves.setCanChooseMachineGun(Game.weapon.isCanChooseMachineGun());
            saves.setCanChooseRPG(Game.weapon.isCanChooseRPG());
            saves.setCanChooseDevilKiss(Game.energetic.isCanChooseDevilKiss());
            saves.setCanChooseElectricity(Game.energetic.isCanChooseElectricity());
            saves.setCanChooseHypnotist(Game.energetic.isCanChooseHypnotist());

            fileWriter.write(mapper.writeValueAsString(saves));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void saveOptions(ObjectMapper mapper) {
        try (FileWriter fileWriter = new FileWriter(optionsPath.toFile())) {
            if (optionsPath.toFile().exists()) {
                optionsPath.toFile().delete();
            }

            if (!optionsPath.toFile().exists()) {
                optionsPath.toFile().createNewFile();
            }

            Options options = new Options();
            options.setFxVolume(Game.menu.fxSlider.getValue());
            options.setMusicVolume(Game.menu.musicSlider.getValue());
            options.setVoiceVolume(Game.menu.voiceSlider.getValue());
            options.setTrack(Game.menu.music.getMedia().getSource());

            fileWriter.write(mapper.writeValueAsString(options));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
