package com.abhai.deadshock;

import com.abhai.deadshock.characters.Elizabeth;
import com.abhai.deadshock.characters.enemies.Boss;
import com.abhai.deadshock.hud.Tutorial;
import com.abhai.deadshock.levels.Level;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.animation.FadeTransition;
import javafx.scene.input.KeyCode;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.util.Duration;

import java.nio.file.Path;
import java.nio.file.Paths;

public class CutScenes {
    private MediaPlayer video;
    private MediaView videoView;

    private boolean actionPerformed = false;

    public CutScenes() {
        Game.booker.setTranslateX(100);
        Game.booker.setTranslateY(500);
        if (Game.elizabeth != null)
            Game.elizabeth.setTranslateX(100);

        Game.menu.music.pause();
        Game.timer.stop();
        Game.clearData(false);

        Game.stage.setWidth(1235);
        switch (Game.levelNumber) {
            case Level.FIRST_LEVEL -> {
                playVideo("elizabeth.mp4");
                video.setOnEndOfMedia(() -> {
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
                playVideo("comstock.mp4");
                video.setOnEndOfMedia(() -> {
                    if (!actionPerformed) {
                        actionPerformed = true;
                        endCutScene2();
                    }
                });
                Game.scene.setOnKeyPressed(event -> {
                    if (event.getCode() == KeyCode.ESCAPE || event.getCode() == KeyCode.ENTER
                            && !actionPerformed) {
                        actionPerformed = true;
                        endCutScene2();
                        event.consume();
                    }
                });
            }
            case Level.BOSS_LEVEL -> {
                Game.stage.setWidth(1230);
                playVideo("end.mp4");
                video.setOnEndOfMedia(() -> {
                    if (!actionPerformed) {
                        actionPerformed = true;
                        endCutScene3();
                    }
                });
                Game.scene.setOnKeyPressed(event -> {
                    if (event.getCode() == KeyCode.ESCAPE || event.getCode() == KeyCode.ENTER
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
        Tutorial.delete();
        Game.levelNumber++;
        Game.level.changeLevel();
        Game.weapon.changeLevel(Game.levelNumber);

        video.stop();
        Game.appRoot.getChildren().remove(videoView);
        videoView = null;

        Game.createEnemies();

        Game.timer.start();
        Game.menu.music.play();
        Game.menu.addListener();
        Game.elizabeth = new Elizabeth();
        Game.booker.setCanPlayVoice(true);
        Tutorial.init();
        Game.vendingMachine.changeLevel();
        Game.energetic.changeLevel();
        Game.stage.setWidth(1280);

        ObjectMapper mapper = new ObjectMapper();
        Game.saveSaves(mapper);
        Game.saveOptions(mapper);
    }

    private void endCutScene2() {
        video.stop();
        Game.appRoot.getChildren().remove(videoView);
        videoView = null;

        Tutorial.delete();
        Game.levelNumber++;
        Game.level.changeLevel();
        Game.weapon.changeLevel(Game.levelNumber);
        Game.createEnemies();

        Game.timer.start();
        Game.menu.music.play();
        Game.menu.addListener();
        Tutorial.init();
        Game.vendingMachine.changeLevel();
        Game.energetic.changeLevel();
        Game.stage.setWidth(1280);
        Game.enemies.add(new Boss());

        ObjectMapper mapper = new ObjectMapper();
        Game.saveSaves(mapper);
        Game.saveOptions(mapper);
    }

    private void endCutScene3() {
        Game.appRoot.getChildren().remove(videoView);
        video.stop();
        videoView = null;
        Game.stage.setWidth(1280);
        System.exit(0);
    }
}
