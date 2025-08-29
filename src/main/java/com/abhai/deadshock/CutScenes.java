package com.abhai.deadshock;

import com.abhai.deadshock.hud.Tutorial;
import com.abhai.deadshock.levels.Level;
import javafx.animation.FadeTransition;
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

        Game.menu.getMusic().pause();
        Game.timer.stop();
        Game.active = false;
        Game.clearData(false);

        switch (Game.levelNumber) {
            case Level.FIRST_LEVEL -> {
                playVideo("elizabeth.mp4");
                video.setOnEndOfMedia(() -> {
                    if (!actionPerformed) {
                        actionPerformed = true;
                        endCutScene1();
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
            }
            case Level.BOSS_LEVEL -> {
                playVideo("end.mp4");
                video.setOnEndOfMedia(() -> {
                    if (!actionPerformed) {
                        actionPerformed = true;
                        endCutScene3();
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
        videoView.getMediaPlayer().setVolume(Game.menu.getVoiceSlider().getValue() / 100);
        Game.appRoot.getChildren().add(videoView);

        FadeTransition ft = new FadeTransition(Duration.seconds(1), videoView);
        ft.setFromValue(0);
        ft.setToValue(1);
        ft.play();
        video.play();
    }

    private void endCutScene1() {
        Game.booker.setCanPlayVoice(true);
        initLevel();
    }

    private void endCutScene2() {
        initLevel();
    }

    private void endCutScene3() {
        Game.appRoot.getChildren().remove(videoView);
        video.stop();
        videoView = null;
        System.exit(0);
    }

    private void initLevel() {
        video.stop();
        Game.appRoot.getChildren().remove(videoView);
        videoView = null;

        Tutorial.delete();
        Game.levelNumber++;
        Game.level.changeLevel();
        Game.weapon.changeLevel();
        Game.createEnemies();

        Game.timer.start();
        Game.active = true;
        Game.menu.getMusic().play();
        Tutorial.init();
        Game.elizabeth.init();
        Game.vendingMachine.changeLevel();
        Game.energetic.changeLevel();

        Game.saveSaves();
        Game.saveOptions();
    }
}
