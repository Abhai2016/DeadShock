package com.abhai.deadshock.menus;

import com.abhai.deadshock.Game;
import com.abhai.deadshock.characters.Animatable;
import com.abhai.deadshock.characters.enemies.Enemy;
import com.abhai.deadshock.types.DifficultyType;
import com.abhai.deadshock.utils.GameMedia;
import com.abhai.deadshock.utils.Texts;
import com.abhai.deadshock.world.levels.Level;
import javafx.animation.FadeTransition;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;

public class Menu extends Pane {
    private static final Path ROCK_MUSIC_PATH = Paths.get("resources", "sounds", "music", "rock");
    private static final Path METALCORE_MUSIC_PATH = Paths.get("resources", "sounds", "music", "metalcore");
    private static final Path ELECTRONIC_MUSIC_PATH = Paths.get("resources", "sounds", "music", "electronic");
    private static final Path DEVELOPERS_CHOICE_MUSIC_PATH = Paths.get("resources", "sounds", "music", "developersChoice");
    private static final ImageView cover = new ImageView(new Image(Paths.get("resources", "images", "menu", "cover.jpg").toUri().toString()));

    private Text textCover;
    private MediaPlayer music;
    private ImageView controls;
    private Text typeOfMusicText;
    private Path currentMusicPath;
    private SubMenu currentSubMenu;
    private Text difficultyLevelText;
    private Pane difficultyBackground;
    private HashMap<String, SubMenu> subMenus;
    private FadeTransition fadeTransitionTextCover;

    private Slider fxSlider;
    private Slider musicSlider;
    private Slider voiceSlider;

    private boolean start;
    private boolean isShown;
    private boolean newGame;
    private boolean isCoverShown;

    public Menu() {
        currentMusicPath = Paths.get("resources", "sounds", "music", "developersChoice", "01.mp3");
        music = new MediaPlayer(new Media(Paths.get("resources", "sounds", "music", "mainTheme.mp3").toUri().toString()));
        music.setCycleCount(MediaPlayer.INDEFINITE);
        music.play();

        start = true;
        isShown = true;
        newGame = false;
        isCoverShown = true;

        ImageView menuBackground = new ImageView(new Image(Paths.get("resources", "images", "menu", "menu.png").toUri().toString()));
        getChildren().add(menuBackground);
        Game.getGameWorld().getAppRoot().getChildren().add(this);
        initializeSubMenus();
        createCover();
    }

    public void gameOver() {
        start = true;
        controls.setVisible(false);
        typeOfMusicText.setVisible(false);
        Game.getGameWorld().newGameReset();
        changeSubMenu(subMenus.get(Texts.MAIN_SUBMENU));
        showMenu();
    }

    public void hideMenu() {
        if (!start) {
            FadeTransition ft = new FadeTransition(Duration.seconds(0.5), this);
            ft.setFromValue(1);
            ft.setToValue(0);
            ft.setOnFinished(event -> Game.getGameWorld().getAppRoot().getChildren().remove(this));
            ft.play();

            music.play();
            isShown = false;
            Game.getGameWorld().setActive(true);

            if (GameMedia.WHERE_ARE_YOU_FROM.getStatus() == MediaPlayer.Status.PAUSED)
                GameMedia.WHERE_ARE_YOU_FROM.play();
        }
    }

    public void showMenu() {
        FadeTransition ft = new FadeTransition(Duration.seconds(0.5), this);
        ft.setFromValue(0);
        ft.setToValue(1);
        ft.play();

        music.pause();
        isShown = true;
        Game.getGameWorld().setActive(false);
        Game.getGameWorld().getBooker().stopAnimation();
        Game.getGameWorld().getAppRoot().getChildren().add(this);

        for (Enemy enemy : Game.getGameWorld().getEnemies())
            if (enemy instanceof Animatable animatable)
                animatable.stopAnimation();
        if (GameMedia.WHERE_ARE_YOU_FROM.getStatus() == MediaPlayer.Status.PLAYING)
            GameMedia.WHERE_ARE_YOU_FROM.pause();
    }

    public void hideCover() {
        isCoverShown = false;
        fadeTransitionTextCover.stop();

        FadeTransition fadeTransitionCover = new FadeTransition(Duration.seconds(2), cover);
        fadeTransitionCover.setOnFinished(event -> getChildren().removeAll(cover, textCover));
        fadeTransitionCover.setFromValue(1);
        fadeTransitionCover.setToValue(0);
        fadeTransitionCover.play();
    }

    public void checkMusic() {
        if (music.getMedia().getSource().contains("rock")) {
            currentMusicPath = ROCK_MUSIC_PATH;
            checkTrack();
            return;
        }

        if (music.getMedia().getSource().contains("metalcore")) {
            currentMusicPath = METALCORE_MUSIC_PATH;
            checkTrack();
            return;
        }

        if (music.getMedia().getSource().contains("electronic")) {
            currentMusicPath = ELECTRONIC_MUSIC_PATH;
            checkTrack();
            return;
        }

        if (music.getMedia().getSource().contains("developersChoice")) {
            currentMusicPath = DEVELOPERS_CHOICE_MUSIC_PATH;
            checkTrack();
        }
    }

    private void startGame() {
        start = false;
        difficultyBackground.setVisible(false);
        Game.getGameWorld().setDifficultyType();
        changeSubMenu(subMenus.get(Texts.MAIN_SUBMENU));
        restartMusicIfNeeded();
        hideMenu();
    }

    private void checkTrack() {
        if (music.getMedia().getSource().contains("01.mp3"))
            currentMusicPath = currentMusicPath.resolve("02.mp3");
        else if (music.getMedia().getSource().contains("02.mp3"))
            currentMusicPath = currentMusicPath.resolve("03.mp3");
        else if (music.getMedia().getSource().contains("03.mp3"))
            currentMusicPath = currentMusicPath.resolve("04.mp3");
        else if (music.getMedia().getSource().contains("04.mp3"))
            currentMusicPath = currentMusicPath.resolve("05.mp3");
        else if (music.getMedia().getSource().contains("05.mp3"))
            currentMusicPath = currentMusicPath.resolve("06.mp3");
        else if (music.getMedia().getSource().contains("06.mp3"))
            currentMusicPath = currentMusicPath.resolve("07.mp3");
        else if (music.getMedia().getSource().contains("07.mp3"))
            currentMusicPath = currentMusicPath.resolve("01.mp3");
        restartMusicIfNeeded();
    }

    private void createCover() {
        textCover = new Text(Texts.PUSH_ENTER_TO_CONTINUE);
        textCover.setFont(Font.font("Arial", FontWeight.BOLD, 28));
        textCover.setFill(Color.AQUA);
        textCover.setTranslateX(Game.SCENE_WIDTH / 3);
        textCover.setTranslateY(Game.SCENE_HEIGHT - 225);
        getChildren().addAll(cover, textCover);

        fadeTransitionTextCover = new FadeTransition(Duration.seconds(1), textCover);
        fadeTransitionTextCover.setFromValue(1);
        fadeTransitionTextCover.setToValue(0);
        fadeTransitionTextCover.setCycleCount(FadeTransition.INDEFINITE);
        fadeTransitionTextCover.setAutoReverse(true);
        fadeTransitionTextCover.play();
    }

    private void createMainSubMenu() {
        SubMenu mainSubMenu = new SubMenu();
        CustomButton continueGame = new CustomButton(Texts.CONTINUE_GAME);
        CustomButton newGame = new CustomButton(Texts.NEW_GAME);
        CustomButton options = new CustomButton(Texts.OPTIONS);
        CustomButton exitGame = new CustomButton(Texts.EXIT);
        mainSubMenu.addCustomButtons(continueGame, newGame, options, exitGame);
        subMenus.put(Texts.MAIN_SUBMENU, mainSubMenu);
        currentSubMenu = mainSubMenu;
        getChildren().add(currentSubMenu);

        continueGame.setOnMouseClicked(event -> {
            if (!start) {
                hideMenu();
            } else if (Game.getGameWorld().getLevel().getCurrentLevelNumber() > Level.FIRST_LEVEL) {
                startGame();
            }
        });
        newGame.setOnMouseClicked(event -> {
            changeSubMenu(subMenus.get(Texts.DIFFICULTY_SUBMENU));
            difficultyBackground.setVisible(true);
        });
        options.setOnMouseClicked(event -> changeSubMenu(subMenus.get(Texts.OPTIONS_SUBMENU)));
        exitGame.setOnMouseClicked(event -> System.exit(0));
    }

    private void createMusicSubMenu() {
        createTypeOfMusicText();
        SubMenu musicSubMenu = new SubMenu();
        CustomButton rockItem = new CustomButton(Texts.ROCK);
        CustomButton post_hardcoreItem = new CustomButton(Texts.METALCORE);
        CustomButton electronicItem = new CustomButton(Texts.ELECTRO);
        CustomButton developersChoice = new CustomButton(Texts.DEVELOPERS_CHOICE);
        CustomButton musicMenuBackItem = new CustomButton(Texts.BACK);
        musicSubMenu.addCustomButtons(rockItem, post_hardcoreItem, electronicItem, developersChoice, musicMenuBackItem);
        subMenus.put(Texts.SOUNDS_MUSIC_SUBMENU, musicSubMenu);

        rockItem.setOnMouseClicked(event -> {
            currentMusicPath = Paths.get("resources", "sounds", "music", "rock", "01.mp3");
            restartMusicIfNeeded();
            typeOfMusicText.setText(Texts.CHOSEN_ROCK);
        });
        post_hardcoreItem.setOnMouseClicked(event -> {
            currentMusicPath = Paths.get("resources", "sounds", "music", "metalcore", "01.mp3");
            restartMusicIfNeeded();
            typeOfMusicText.setText(Texts.CHOSEN_METALCORE);
        });
        electronicItem.setOnMouseClicked(event -> {
            currentMusicPath = Paths.get("resources", "sounds", "music", "electronic", "01.mp3");
            restartMusicIfNeeded();
            typeOfMusicText.setText(Texts.CHOSEN_ELECTRO);
        });
        developersChoice.setOnMouseClicked(event -> {
            currentMusicPath = Paths.get("resources", "sounds", "music", "developersChoice", "01.mp3");
            restartMusicIfNeeded();
            typeOfMusicText.setText(Texts.CHOSEN_DEVELOPER_CHOICE);
        });
        musicMenuBackItem.setOnMouseClicked(event -> {
            changeSubMenu(subMenus.get(Texts.SOUNDS_OPTIONS_SUBMENU));
            typeOfMusicText.setVisible(false);
        });
    }

    private void initializeSubMenus() {
        subMenus = new HashMap<>();
        createMainSubMenu();
        createMusicSubMenu();
        createVolumeSubMenu();
        createSoundsSubMenu();
        createOptionsSubMenu();
        createControlsSubMenu();
        createDifficultySubMenu();
    }

    private void createVolumeSubMenu() {
        SubMenu volumeSubMenu = new SubMenu();
        volumeSubMenu.addLabel(new Label(Texts.MUSIC_SLIDER_NAME));
        musicSlider = new Slider(0, 100, 100);
        volumeSubMenu.getChildren().add(musicSlider);

        volumeSubMenu.addLabel(new Label(Texts.FX_SLIDER_NAME));
        fxSlider = new Slider(0, 100, 100);
        volumeSubMenu.getChildren().add(fxSlider);

        volumeSubMenu.addLabel(new Label(Texts.VOICE_SLIDER_NAME));
        voiceSlider = new Slider(0, 100, 100);
        volumeSubMenu.getChildren().add(voiceSlider);

        musicSlider.valueProperty().addListener((ov, old_val, new_val) -> {
            music.setVolume(new_val.doubleValue() / 100);
        });

        CustomButton soundMenuBack = new CustomButton(Texts.BACK);
        volumeSubMenu.addCustomButtons(soundMenuBack);
        subMenus.put(Texts.SOUNDS_VOLUME_SUBMENU, volumeSubMenu);
        soundMenuBack.setOnMouseClicked(event -> changeSubMenu(subMenus.get(Texts.SOUNDS_OPTIONS_SUBMENU)));
    }

    private void createSoundsSubMenu() {
        SubMenu soundOptionSubMenu = new SubMenu();
        CustomButton volume = new CustomButton(Texts.VOLUME);
        CustomButton music = new CustomButton(Texts.MUSIC);
        CustomButton soundOptionBack = new CustomButton(Texts.BACK);
        soundOptionSubMenu.addCustomButtons(volume, music, soundOptionBack);
        subMenus.put(Texts.SOUNDS_OPTIONS_SUBMENU, soundOptionSubMenu);

        volume.setOnMouseClicked(event -> changeSubMenu(subMenus.get(Texts.SOUNDS_VOLUME_SUBMENU)));
        music.setOnMouseClicked(event -> {
            changeSubMenu(subMenus.get(Texts.SOUNDS_MUSIC_SUBMENU));
            typeOfMusicText.setVisible(true);
        });
        soundOptionBack.setOnMouseClicked(event -> changeSubMenu(subMenus.get(Texts.OPTIONS_SUBMENU)));
    }

    private void createOptionsSubMenu() {
        SubMenu optionsSubMenu = new SubMenu();
        CustomButton sound = new CustomButton(Texts.SOUND);
        CustomButton control = new CustomButton(Texts.CONTROLS);
        CustomButton optionsBack = new CustomButton(Texts.BACK);
        optionsSubMenu.addCustomButtons(sound, control, optionsBack);
        subMenus.put(Texts.OPTIONS_SUBMENU, optionsSubMenu);

        sound.setOnMouseClicked(event -> changeSubMenu(subMenus.get(Texts.SOUNDS_OPTIONS_SUBMENU)));
        control.setOnMouseClicked(event -> {
            changeSubMenu(subMenus.get(Texts.CONTROLS_SUBMENU));
            controls.setVisible(true);
        });
        optionsBack.setOnMouseClicked(event -> changeSubMenu(subMenus.get(Texts.MAIN_SUBMENU)));
    }

    private void restartMusicIfNeeded() {
        if (!start) {
            music.stop();
            music = new MediaPlayer(new javafx.scene.media.Media(currentMusicPath.toUri().toString()));
            music.setVolume(musicSlider.getValue() / 100);
            music.play();
            music.setOnEndOfMedia(this::checkMusic);
        }
    }

    private void createTypeOfMusicText() {
        typeOfMusicText = new Text(Texts.CHOSEN_DEVELOPER_CHOICE);
        typeOfMusicText.setTranslateX(Game.SCENE_WIDTH / 5);
        typeOfMusicText.setTranslateY(Game.SCENE_HEIGHT / 6);
        typeOfMusicText.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        typeOfMusicText.setFill(Color.WHITE);
        typeOfMusicText.setVisible(false);
        getChildren().add(typeOfMusicText);
    }

    private void createControlsSubMenu() {
        controls = new ImageView(new Image(Paths.get("resources", "images", "menu", "controls.png").toUri().toString()));
        controls.setVisible(false);
        controls.setTranslateX(50);
        controls.setTranslateY(100);
        getChildren().add(controls);

        SubMenu controlsSubMenu = new SubMenu();
        CustomButton controlBack = new CustomButton(Texts.BACK);
        controlBack.setTranslateX(50);
        controlBack.setTranslateY(280);
        controlsSubMenu.addCustomButtons(controlBack);
        subMenus.put(Texts.CONTROLS_SUBMENU, controlsSubMenu);

        controlBack.setOnMouseClicked(event -> {
            changeSubMenu(subMenus.get(Texts.OPTIONS_SUBMENU));
            controls.setVisible(false);
        });
    }

    private void createDifficultySubMenu() {
        createDifficultyLevelDescription();
        SubMenu difficultySubMenu = new SubMenu();
        CustomButton marik = new CustomButton(Texts.MARIK);
        CustomButton easy = new CustomButton(Texts.EASY);
        CustomButton normal = new CustomButton(Texts.MEDIUM);
        CustomButton high = new CustomButton(Texts.HARD);
        CustomButton hardcore = new CustomButton(Texts.HARDCORE);
        CustomButton back = new CustomButton(Texts.BACK);
        CustomButton ready = new CustomButton(Texts.APPLY);
        difficultySubMenu.addCustomButtons(marik, easy, normal, high, hardcore, back, ready);
        subMenus.put(Texts.DIFFICULTY_SUBMENU, difficultySubMenu);

        marik.setOnMouseClicked(event -> {
            difficultyLevelText.setText(Texts.MARIK_DIFFICULTY_DESCRIPTION);
            Game.getGameWorld().setDifficultyType(DifficultyType.MARIK);
        });
        easy.setOnMouseClicked(event -> {
            difficultyLevelText.setText(Texts.EASY_DIFFICULTY_DESCRIPTION);
            Game.getGameWorld().setDifficultyType(DifficultyType.EASY);
        });
        normal.setOnMouseClicked(event -> {
            difficultyLevelText.setText(Texts.MEDIUM_DIFFICULTY_DESCRIPTION);
            Game.getGameWorld().setDifficultyType(DifficultyType.MEDIUM);
        });
        high.setOnMouseClicked(event -> {
            difficultyLevelText.setText(Texts.HARD_DIFFICULTY_DESCRIPTION);
            Game.getGameWorld().setDifficultyType(DifficultyType.HARD);
        });
        hardcore.setOnMouseClicked(event -> {
            difficultyLevelText.setText(Texts.HARDCORE_DIFFICULTY_DESCRIPTION);
            Game.getGameWorld().setDifficultyType(DifficultyType.HARDCORE);
        });
        back.setOnMouseClicked(event -> {
            changeSubMenu(subMenus.get(Texts.MAIN_SUBMENU));
            difficultyBackground.setVisible(false);
        });
        ready.setOnMouseClicked(event -> {
            createNewGameConfirmationWindow();
            if (newGame) {
                if (start && Game.getGameWorld().getLevel().getCurrentLevelNumber() == Level.FIRST_LEVEL)
                    start = false;
                else
                    Game.getGameWorld().newGameReset();
                startGame();
            }
        });
    }

    private void changeSubMenu(SubMenu subMenu) {
        getChildren().remove(currentSubMenu);
        currentSubMenu = subMenu;
        getChildren().add(subMenu);
    }

    private void createNewGameConfirmationWindow() {
        Stage stage = new Stage();
        stage.setWidth(217);
        stage.setHeight(113);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setResizable(false);

        Text areYouSureText = new Text(Texts.ARE_YOU_SURE);
        areYouSureText.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        areYouSureText.setFill(Color.BLACK);
        areYouSureText.setTranslateX(39);
        areYouSureText.setTranslateY(20);

        Text savesWillBeLostText = new Text(Texts.SAVES_WILL_BE_LOST);
        savesWillBeLostText.setFont(Font.font("Arial", FontWeight.BOLD, 12));
        savesWillBeLostText.setFill(Color.BLACK);
        savesWillBeLostText.setTranslateX(5);
        savesWillBeLostText.setTranslateY(40);

        Button yesButton = new Button(Texts.YES);
        yesButton.setPrefWidth(50);
        yesButton.setTranslateX(5);
        yesButton.setTranslateY(50);
        yesButton.setOnMouseClicked(event -> {
            newGame = true;
            stage.close();
        });

        Button noButton = new Button(Texts.NO);
        noButton.setPrefWidth(50);
        noButton.setTranslateX(stage.getWidth() - 70);
        noButton.setTranslateY(50);
        noButton.setOnMouseClicked(event -> {
            newGame = false;
            stage.close();
        });

        Pane rootPane = new Pane();
        rootPane.getChildren().addAll(areYouSureText, savesWillBeLostText, yesButton, noButton);
        Scene scene = new Scene(rootPane);
        stage.setScene(scene);
        stage.setTitle(Texts.NEW_GAME);
        Path imagePath = Paths.get("resources", "images", "icons", "icon.jpg");
        stage.getIcons().add(new Image(imagePath.toUri().toString()));
        stage.showAndWait();
    }

    private void createDifficultyLevelDescription() {
        difficultyBackground = new Pane();
        difficultyBackground.setVisible(false);
        Rectangle rectangle = new Rectangle(Game.SCENE_WIDTH / 2, Game.SCENE_HEIGHT / 5, Color.WHITE);
        rectangle.setTranslateX(Game.SCENE_WIDTH / 4.5);
        rectangle.setTranslateY(Game.SCENE_HEIGHT / 5);
        rectangle.setOpacity(0.75);
        rectangle.setArcHeight(20);
        rectangle.setArcWidth(20);
        difficultyBackground.getChildren().add(rectangle);

        difficultyLevelText = new Text(Texts.MEDIUM_DIFFICULTY_DESCRIPTION);
        difficultyLevelText.setTranslateX(Game.SCENE_WIDTH / 4.4);
        difficultyLevelText.setTranslateY(Game.SCENE_HEIGHT / 4.4);
        difficultyLevelText.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        difficultyLevelText.setFill(Color.BLACK);
        difficultyBackground.getChildren().add(difficultyLevelText);
        getChildren().add(difficultyBackground);
    }

    public void checkMusicForContinueGame(String text) {
        if (text.contains("rock")) {
            currentMusicPath = ROCK_MUSIC_PATH;
            typeOfMusicText.setText(Texts.CHOSEN_ROCK);
        } else if (text.contains("metalcore")) {
            currentMusicPath = METALCORE_MUSIC_PATH;
            typeOfMusicText.setText(Texts.CHOSEN_METALCORE);
        } else if (text.contains("electronic")) {
            currentMusicPath = ELECTRONIC_MUSIC_PATH;
            typeOfMusicText.setText(Texts.CHOSEN_ELECTRO);
        } else {
            currentMusicPath = DEVELOPERS_CHOICE_MUSIC_PATH;
            typeOfMusicText.setText(Texts.CHOSEN_DEVELOPER_CHOICE);
        }
        currentMusicPath = currentMusicPath.resolve(text.substring(text.length() - 6));
    }

    public boolean isShown() {
        return isShown;
    }

    public MediaPlayer getMusic() {
        return music;
    }

    public Slider getFxSlider() {
        return fxSlider;
    }

    public boolean isCoverShown() {
        return isCoverShown;
    }

    public Slider getMusicSlider() {
        return musicSlider;
    }

    public Slider getVoiceSlider() {
        return voiceSlider;
    }
}