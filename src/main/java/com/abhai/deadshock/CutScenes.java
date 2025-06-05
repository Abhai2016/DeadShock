package com.abhai.deadshock;

import com.abhai.deadshock.Characters.Boss;
import com.abhai.deadshock.Characters.Elizabeth;
import com.abhai.deadshock.Levels.Block;
import com.abhai.deadshock.Levels.Level;
import com.abhai.deadshock.Weapon.Weapon;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.animation.FadeTransition;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.util.Duration;

import java.io.FileWriter;
import java.nio.file.Path;
import java.nio.file.Paths;

import static javafx.scene.input.KeyEvent.KEY_PRESSED;

public class CutScenes {
    private MediaPlayer video;
    private MediaView videoView;

    private Path savesPath = Paths.get("resources", "data", "saves.dat");
    private Path optionsPath = Paths.get("resources", "data", "options.dat");

    private Path imagePath = Paths.get("resources", "images", "black.jpg");
    ImageView imageView = new ImageView(new Image(imagePath.toUri().toString()));

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
            case 0 -> {
                playVideo("meeting_Elizabeth.mp4");
                video.setOnEndOfMedia( () -> endCutScene1() );
            }
            case 1 -> {
                playVideo("murder_Comstock.mp4");
                video.setOnEndOfMedia( () -> endCutScene2() );
            }
            case 2 -> bossLevel();
            case 3 -> {
                Game.stage.setWidth(1230);
                playVideo("end.mp4");
                video.setOnEndOfMedia( () -> endCutScene3() );
            }
        }
    }


    private void playVideo(String str) {
        Path videoPath = Paths.get("resources", "videos", str);
        video = new MediaPlayer(new Media(videoPath.toUri().toString()));
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

        Path soundPath = Paths.get("resources", "sounds", "voice", "hack_padlock.mp3");
        Sounds.bookerVoice = new MediaPlayer(new Media(soundPath.toUri().toString()));
        Sounds.bookerVoice.setVolume(Game.menu.voiceSlider.getValue() / 100);
        Sounds.bookerVoice.play();
        Sounds.bookerVoice.setOnEndOfMedia(this::partEndCutScene1);
    }


    private void partEndCutScene1() {
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
        Game.appRoot.getChildren().remove(imageView);

        ObjectMapper mapper = new ObjectMapper();
        saveSaves(mapper);
        saveOptions(mapper);
    }

    private void endCutScene2() {
        video.stop();
        Game.appRoot.getChildren().remove(videoView);
        videoView = null;

        Game.levelNumber++;
        Game.level.changeLevel(Game.levelNumber);
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
        Game.appRoot.getChildren().remove(imageView);
        Game.boss = new Boss(Level.BLOCK_SIZE * 299, Level.BLOCK_SIZE * 13);

        ObjectMapper mapper = new ObjectMapper();
        saveSaves(mapper);
        saveOptions(mapper);
    }


    private void bossLevel() {
        Game.boss.setTrompInterval(0);
        Game.levelNumber++;
        Game.level.changeLevel(Game.levelNumber);
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
