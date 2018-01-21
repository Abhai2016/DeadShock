package game;

import game.Characters.EnemyBase;
import javafx.animation.Animation;
import javafx.animation.FadeTransition;
import javafx.animation.FillTransition;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.util.Duration;

import java.io.File;


public class Menu {
    private ImageView controls = new ImageView("file:/../images/controls.png");
    private Pane description = new Pane();

    public MediaPlayer music = new MediaPlayer(new Media(new File("file:/../sounds/music/Main_Theme.mp3").toURI().toString()));
    private String rock = "file:/../sounds/music/rock/";
    private String post = "file:/../sounds/music/post-hardcore_metalcore/";
    private String electronic = "file:/../sounds/music/electronic/";
    private String developer_choice = "file:/../sounds/music/developer_choice/";

    private String tempMusic = "file:/../sounds/music/developer_choice/01.mp3";

    private Text musicText = new Text("Выбрано : ВЫБОР РАЗРАБОТЧИКА");
    private Text descriptionDifficultyLevel = new Text("УРОВЕНЬ СЛОЖНОСТИ 'СРЕДНИЙ':\n" +
            "150 монет в начале игры;\nУ противников выключен friendly fire;\nИгрок получает обычный " +
            "урон;\nЭнергетики расходуют 20% солей;\nАптечки пополняют 15% " +
            "жизней;\nВ ящиках с боеприпасами вы найдете 15 патронов;\nПри прыжке на противника у игрока " +
            "отнимается 10% жизней;\nЗа убийство каждого противника получаете 5 монет;" +
            "\n3 попытки на прохождение уровня(потом пересоздаются противники);" +
            "\nКаждая регенерация отнимает 20 монет");

    MenuBox menuBox;

    private SubMenu mainMenu;
    private SubMenu optionsMenu;
    private SubMenu controlMenu;
    private SubMenu soundOptionMenu;
    private SubMenu soundVolumeMenu;
    private SubMenu musicMenu;
    private SubMenu difficultyLevelMenu;

    private MenuItem continueGame;

    Slider musicSlider;
    public Slider voiceSlider;
    public Slider fxSlider;

    private boolean start = true;
    boolean booleanNewGame = false;


    Menu() {
        music.setCycleCount(MediaPlayer.INDEFINITE);
        music.play();

        createMainMenu();
        createDifficultyLevelMenu();
        createOptionsMenu();
        createControlMenu();
        createMusicMenu();
        createSoundVolume();
        createSoundOptionMenu();
        Game.appRoot.getChildren().add(menuBox);

        createCover();
    }


    public void addListener() {
        Game.scene.setOnKeyPressed(event -> Game.keys.put(event.getCode(), true) );
        Game.scene.setOnKeyReleased(event -> Game.keys.put(event.getCode(), false) );
    }


    private void createCover() {
        ImageView cover = new ImageView(new Image("file:/../images/backgrounds/cover.jpg"));
        Text textCover = new Text("Для продолжения нажмите ввод");
        textCover.setFont(Font.font("Arial", FontWeight.BOLD, 28));
        textCover.setFill(Color.AQUA);
        textCover.setTranslateX(Game.scene.getWidth() / 3);
        textCover.setTranslateY(Game.scene.getHeight() - 225);
        Game.appRoot.getChildren().addAll(cover, textCover);

        FadeTransition fadeTransitionTextCover = new FadeTransition(Duration.seconds(1), textCover);
        fadeTransitionTextCover.setFromValue(1);
        fadeTransitionTextCover.setToValue(0);
        fadeTransitionTextCover.setCycleCount(FadeTransition.INDEFINITE);
        fadeTransitionTextCover.setAutoReverse(true);
        fadeTransitionTextCover.play();

        Game.scene.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                fadeTransitionTextCover.stop();

                FadeTransition fadeTransitionCover = new FadeTransition(Duration.seconds(2), cover);
                fadeTransitionCover.setFromValue(1);
                fadeTransitionCover.setToValue(0);
                fadeTransitionCover.play();
                fadeTransitionCover.setOnFinished( event1 -> Game.appRoot.getChildren().removeAll(cover, textCover) );

                FadeTransition fadeTransitionMenu = new FadeTransition(Duration.seconds(2), menuBox);
                fadeTransitionMenu.setFromValue(0);
                fadeTransitionMenu.setToValue(1);
                fadeTransitionMenu.play();
                Game.scene.setOnKeyPressed(event1 -> Game.nothing() );
            }
        });
    }


    private void createMainMenu() {
        continueGame = new MenuItem("ПРОДОЛЖИТЬ ИГРУ");
        MenuItem newGame = new MenuItem("НОВАЯ ИГРА");
        MenuItem options = new MenuItem("НАСТРОЙКИ");
        MenuItem exitGame = new MenuItem("ВЫХОД");
        mainMenu = new SubMenu(continueGame, newGame, options, exitGame);
        menuBox = new MenuBox(mainMenu);

        if (Game.levelNumber > 0)
            continueGame.setOnMouseClicked( event -> {
                continueGame.setOnMouseClicked( event1 -> hideMenu() );
                startGame();
            });

        newGame.setOnMouseClicked(event -> {
            menuBox.setSubMenu(difficultyLevelMenu);
            description.setVisible(true);
        });

        options.setOnMouseClicked( event -> menuBox.setSubMenu(optionsMenu) );
        exitGame.setOnMouseClicked( event -> System.exit(0) );
    }


    private void createDifficultyLevelMenu() {
        description.setVisible(false);
        Rectangle rectangle = new Rectangle(775, 280, Color.WHITE);
        rectangle.setTranslateX(Game.scene.getWidth() / 5 + 20);
        rectangle.setTranslateY(Game.scene.getHeight() / 10);
        rectangle.setOpacity(0.5);
        description.getChildren().add(rectangle);

        descriptionDifficultyLevel.setTranslateX(Game.scene.getWidth() / 5 + 25);
        descriptionDifficultyLevel.setTranslateY(Game.scene.getHeight() / 8);
        descriptionDifficultyLevel.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        descriptionDifficultyLevel.setFill(Color.BLACK);
        description.getChildren().add(descriptionDifficultyLevel);
        menuBox.getChildren().add(description);

        MenuItem marik = new MenuItem("МАРИК");
        MenuItem easy = new MenuItem("ЛЕГКИЙ");
        MenuItem normal = new MenuItem("СРЕДНИЙ");
        MenuItem high = new MenuItem("ВЫСОКИЙ");
        MenuItem hardcore = new MenuItem("ХАРДКОР");
        MenuItem back = new MenuItem("НАЗАД");
        MenuItem ready = new MenuItem("ПРИНЯТЬ");
        difficultyLevelMenu = new SubMenu(marik, easy, normal, high, hardcore, back, ready);

        marik.setOnMouseClicked( event -> {
            descriptionDifficultyLevel.setText("УРОВЕНЬ СЛОЖНОСТИ 'МАРИК':\n" +
                    "Бесконечные монеты;\nУ противников включен friendly fire;\nИгрок получает на 100% " +
                    "меньше урона;\nЭнергетики расходуют 10% солей;\nАптечки дают 30% " +
                    "жизней;\nВ ящиках с боеприпасами вы найдете 30 патронов;\nПри прыжке на противника у игрока " +
                    "не отнимаются жизни;\n5 попыток на прохождение уровня(потом пересоздаются противники);" +
                    "\nРегенерируйтесь сколько угодно, у вас бесконечно монет;)");
            Game.difficultyLevelText = "marik";
        });

        easy.setOnMouseClicked( event ->  {
            descriptionDifficultyLevel.setText("УРОВЕНЬ СЛОЖНОСТИ 'ЛЕГКИЙ':\n" +
                    "300 монет в начале игры;\nУ противников включен friendly fire;\nИгрок получает на 50% " +
                    "меньше урона;\nЭнергетики расходуют 15% солей;\nАптечки пополняют 20% " +
                    "жизней;\nВ ящиках с боеприпасами вы найдете 20 патронов;\nПри прыжке на противника у игрока " +
                    "отнимаются 5% жизней;\nЗа убийство каждого противника получаете 10 монет;" +
                    "\n5 попыток на прохождение уровня(потом пересоздаются противники);" +
                    "\nКаждая регенерация отнимает 15 монет");
            Game.difficultyLevelText = "easy";
        });

        normal.setOnMouseClicked( event -> {
            descriptionDifficultyLevel.setText("УРОВЕНЬ СЛОЖНОСТИ 'СРЕДНИЙ':\n" +
                    "150 монет в начале игры;\nУ противников выключен friendly fire;\nИгрок получает обычный " +
                    "урон;\nЭнергетики расходуют 20% солей;\nАптечки пополняют 15% " +
                    "жизней;\nВ ящиках с боеприпасами вы найдете 15 патронов;\nПри прыжке на противника у игрока " +
                    "отнимается 10% жизней;\nЗа убийство каждого противника получаете 5 монет;" +
                    "\n3 попытки на прохождение уровня(потом пересоздаются противники);" +
                    "\nКаждая регенерация отнимает 20 монет");
            Game.difficultyLevelText = "normal";
        } );
        high.setOnMouseClicked( event -> {
            descriptionDifficultyLevel.setText("УРОВЕНЬ СЛОЖНОСТИ 'ВЫСОКИЙ':\n" +
                    "100 монет в начале игры;\nУ противников выключен friendly fire;\nПротивники умеют поднимать аптечки и патроны;" +
                    "\nИгрок получает на 50% больше урона;\nЭнергетики расходуют 25% солей;\nАптечки пополняют 10% " +
                    "жизней;\nВ ящиках с боеприпасами вы найдете 10 патронов;\nПри прыжке на противника у игрока " +
                    "отнимается 15% жизней;\nЗа убийство каждого противника получаете 3 монеты;" +
                    "\n2 попытки на прохождение уровня(потом пересоздаются противники);" +
                    "\nКаждая регенерация отнимает 25 монет");
            Game.difficultyLevelText = "high";
        });
        hardcore.setOnMouseClicked( event -> {
            descriptionDifficultyLevel.setText("УРОВЕНЬ СЛОЖНОСТИ 'ХАРДКОР':\n" +
                    "100 монет в начале игры;\nУ противников выключен friendly fire;\nПротивники умеют поднимать аптечки и патроны;" +
                    "\nИгрок получает на 100% больше урона;\nЭнергетики расходуют 30% солей;\nАптечки пополняют 10% " +
                    "жизней;\nВ ящиках с боеприпасами вы найдете 10 патронов;\nПри прыжке на противника у игрока " +
                    "отнимается 20% жизней;\nЗа убийство каждого противника получаете 2 монеты;" +
                    "\n1 попытка на прохождение уровня(потом пересоздаются противники);" +
                    "\nНикакой регенирации, никаких сохранений, умрете - начинайте игру заново");
            Game.difficultyLevelText = "hardcore";
        });

        back.setOnMouseClicked( event ->  {
            menuBox.setSubMenu(mainMenu);
            description.setVisible(false);
        });
        ready.setOnMouseClicked( event -> {
            ModalWindow.createNewWindows("НОВАЯ ИГРА");
            if (booleanNewGame) {
                if (start && Game.levelNumber == 0) {
                    continueGame.setOnMouseClicked(event1 -> hideMenu());
                    start = false;
                } else {
                    Game.clearDataForNewGame("New Game");
                    Game.initContentForNewGame();
                }
                startGame();
            }
        });
    }


    private void startGame() {
        menuBox.setSubMenu(mainMenu);
        description.setVisible(false);

        Game.booker.setDifficultyLevel();
        Game.energetic.setDifficultyLevel();
        Game.weapon.setDamage();
        if (Game.difficultyLevelText.equals("marik")) {
            Game.hud.setMarikLevel();
            Game.vendingMachine.setMarikLevel();
        }
        music.stop();
        music = new MediaPlayer(new Media(new File(tempMusic).toURI().toString()));
        music.setVolume(musicSlider.getValue() / 100);
        music.play();
        addMediaListener();
        Game.hud.setVisible(true);
        start = false;
        hideMenu();
    }


    public void newGame() {
        Game.clearDataForNewGame("New Game");
        Game.initContentForNewGame();

        continueGame.setOnMouseClicked( event -> Game.nothing() );
        Game.scene.setOnKeyPressed( event -> Game.nothing());

        menuBox.setSubMenu(mainMenu);
        music.stop();
        showMenu();
    }


    private void createOptionsMenu() {
        MenuItem sound = new MenuItem("ЗВУК");
        MenuItem control = new MenuItem("УПРАВЛЕНИЕ");
        MenuItem optionsBack = new MenuItem("НАЗАД");
        optionsMenu = new SubMenu(sound, control, optionsBack);

        sound.setOnMouseClicked( event -> menuBox.setSubMenu(soundOptionMenu) );
        control.setOnMouseClicked( event -> {
            menuBox.setSubMenu(controlMenu);
            controls.setVisible(true);
        });
        optionsBack.setOnMouseClicked( event -> menuBox.setSubMenu(mainMenu) );
    }


    private void createSoundOptionMenu() {
        MenuItem volume = new MenuItem("ГРОМКОСТЬ");
        MenuItem music = new MenuItem("МУЗЫКА");
        MenuItem soundOptionBack = new MenuItem("НАЗАД");
        soundOptionMenu = new SubMenu(volume, music, soundOptionBack);

        volume.setOnMouseClicked( event -> menuBox.setSubMenu(soundVolumeMenu) );
        music.setOnMouseClicked( event -> {
            menuBox.setSubMenu(musicMenu);
            musicText.setVisible(true);
        });
        soundOptionBack.setOnMouseClicked( event -> menuBox.setSubMenu(mainMenu) );
    }


    private void createMusicMenu() {
        musicText.setTranslateX(Game.scene.getWidth() / 5);
        musicText.setTranslateY(Game.scene.getHeight() / 6);
        musicText.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        musicText.setFill(Color.WHITE);
        musicText.setVisible(false);
        menuBox.getChildren().add(musicText);

        MenuItem rock = new MenuItem("РОК");
        MenuItem post_hardcore = new MenuItem("Post-Hardcore/Metalcore");
        MenuItem electronic = new MenuItem("ЭЛЕКТРОННАЯ");
        MenuItem developer_choice = new MenuItem("ВЫБОР РАЗРАБОТЧИКА");
        MenuItem musicMenuBack = new MenuItem("НАЗАД");
        musicMenu = new SubMenu(rock, post_hardcore, electronic, developer_choice, musicMenuBack);

        rock.setOnMouseClicked( event -> {
            if (!start) {
                music.stop();
                music = new MediaPlayer(new Media(new File("file:/../sounds/music/rock/01.mp3").toURI().toString()));
                music.setVolume(musicSlider.getValue() / 100);
            }
            else
                tempMusic = "file:/../sounds/music/rock/01.mp3";

            addMediaListener();
            musicText.setText("Выбрано : РОК");
        });

        post_hardcore.setOnMouseClicked( event -> {
            if (!start) {
                music.stop();
                music = new MediaPlayer(new Media(new File("file:/../sounds/music/post-hardcore_metalcore/01.mp3").toURI().toString()));
                music.setVolume(musicSlider.getValue() / 100);
            }
            else
                tempMusic = "file:/../sounds/music/post-hardcore_metalcore/01.mp3";

            addMediaListener();
            musicText.setText("Выбрано : Post-Hardcore/Metalcore");
        });

        electronic.setOnMouseClicked( event -> {
            if (!start) {
                music.stop();
                music = new MediaPlayer(new Media(new File("file:/../sounds/music/electronic/01.mp3").toURI().toString()));
                music.setVolume(musicSlider.getValue() / 100);
            }
            else
                tempMusic = "file:/../sounds/music/electronic/01.mp3";

            addMediaListener();
            musicText.setText("Выбрано : ЭЛЕКТРОННАЯ");
        });

        developer_choice.setOnMouseClicked( event -> {
            if (!start) {
                music.stop();
                music = new MediaPlayer(new Media(new File("file:/../sounds/music/developer_choice/01.mp3").toURI().toString()));
                music.setVolume(musicSlider.getValue() / 100);
            }
            else
                tempMusic = "file:/../sounds/music/developer_choice/01.mp3";

            addMediaListener();
            musicText.setText("Выбрано : ВЫБОР РАЗРАБОТЧИКА");
        });

        musicMenuBack.setOnMouseClicked( event -> {
            menuBox.setSubMenu(soundOptionMenu);
            musicText.setVisible(false);
        });
    }


    private void createControlMenu() {
        controls.setVisible(false);
        controls.setTranslateX(50);
        controls.setTranslateY(100);
        menuBox.getChildren().add(controls);

        MenuItem controlBack = new MenuItem("НАЗАД");
        controlBack.setTranslateX(50);
        controlBack.setTranslateY(280);
        controlMenu = new SubMenu(controlBack);

        controlBack.setOnMouseClicked( event ->  {
            menuBox.setSubMenu(optionsMenu);
            controls.setVisible(false);
        });
    }


    private void createSoundVolume() {
        soundVolumeMenu = new SubMenu();

        createMusicVolume();
        createFxVolume();
        createVoiceVolume();

        MenuItem soundMenuBack = new MenuItem("НАЗАД");
        soundVolumeMenu.addItem(soundMenuBack);

        soundMenuBack.setOnMouseClicked( event -> menuBox.setSubMenu(soundOptionMenu) );
    }


    private void createMusicVolume() {
        Label musicLabel = new Label("Музыка");
        soundVolumeMenu.addItem(musicLabel);
        HBox musicBox = new HBox(10);
        musicSlider = new Slider(0, 100, 100);
        Text musicText = new Text(String.valueOf((int) musicSlider.getValue()));
        musicText.setFill(Color.WHITE);
        musicText.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        musicBox.getChildren().addAll(musicSlider, musicText);
        soundVolumeMenu.getChildren().add(musicBox);

        musicSlider.valueProperty().addListener((ov, old_val, new_val) -> {
            musicText.setText(String.valueOf(new_val.intValue()));
            music.setVolume(new_val.doubleValue() / 100);
        });
    }


    private void createFxVolume() {
        Label fxLabel = new Label("Звуковые эффекты");
        soundVolumeMenu.addItem(fxLabel);
        HBox fxBox = new HBox(10);
        fxSlider = new Slider(0, 100, 100);
        Text fxText = new Text(String.valueOf((int) fxSlider.getValue()));
        fxText.setFill(Color.WHITE);
        fxText.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        fxBox.getChildren().addAll(fxSlider, fxText);
        soundVolumeMenu.getChildren().add(fxBox);

        fxSlider.valueProperty().addListener((ov, old_val, new_val) -> {
            fxText.setText(String.valueOf(new_val.intValue()));
            Sounds.pistolShoot.setVolume(new_val.doubleValue() / 100);
        });
    }


    private void createVoiceVolume() {
        Label voiceLabel = new Label("Голос");
        soundVolumeMenu.addItem(voiceLabel);
        HBox voiceBox = new HBox(10);
        voiceSlider = new Slider(0, 100, 100);
        Text voiceText = new Text(String.valueOf((int) voiceSlider.getValue()));
        voiceText.setFill(Color.WHITE);
        voiceText.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        voiceBox.getChildren().addAll(voiceSlider, voiceText);
        soundVolumeMenu.getChildren().add(voiceBox);

        voiceSlider.valueProperty().addListener((ov, old_val, new_val) -> {
            voiceText.setText(String.valueOf(new_val.intValue()));
            Sounds.bookerVoice.setVolume(new_val.doubleValue() / 100);
        });
    }


    private void hideMenu() {
        FadeTransition ft = new FadeTransition(Duration.seconds(0.5), menuBox);
        ft.setFromValue(1);
        ft.setToValue(0);
        ft.setOnFinished( event -> Game.appRoot.getChildren().remove(menuBox) );
        ft.play();

        addListener();
        music.play();
        if (Game.levelNumber > 0)
            if (Sounds.elizabethMediaPlayer != null)
                Sounds.elizabethMediaPlayer.play();
        if (Game.boss != null)
            if (Game.boss.getMediaPlayer() != null)
                Game.boss.getMediaPlayer().play();
        Game.timer.start();
    }


    private void showMenu() {
        FadeTransition ft = new FadeTransition(Duration.seconds(0.5), menuBox);
        ft.setFromValue(0);
        ft.setToValue(1);
        ft.play();
        music.pause();
        Game.booker.animation.stop();
        for (EnemyBase enemy : Game.enemies)
            if (enemy.animation != null)
                enemy.animation.stop();
        if (Sounds.elizabethMediaPlayer != null)
            Sounds.elizabethMediaPlayer.pause();
        if (!Game.appRoot.getChildren().contains(menuBox))
            Game.appRoot.getChildren().add(menuBox);
        if (Game.boss != null)
            if (Game.boss.getMediaPlayer() != null)
                Game.boss.getMediaPlayer().pause();
        Game.timer.stop();
    }


    void update() {
        if (Controller.isPressed(KeyCode.ESCAPE)) {
            Game.keys.remove(KeyCode.ESCAPE);
            showMenu();
            Game.scene.setOnKeyPressed(event -> {
                if (event.getCode() == KeyCode.ESCAPE)
                    hideMenu();
            });
        }

        if ( Controller.isPressed(KeyCode.G) ) {
            Game.keys.remove(KeyCode.G);
            checkMusic();
        }
    }


    private void checkMusic() {
        if ( music.getMedia().getSource().contains(rock) )
            checkTrack(rock);
        if ( music.getMedia().getSource().contains(post) )
            checkTrack(post);
        if ( music.getMedia().getSource().contains(electronic) )
            checkTrack(electronic);
        if ( music.getMedia().getSource().contains(developer_choice) )
            checkTrack(developer_choice);

        addListener();
    }


    void checkMusicForContinueGame(String text) {
        if (text.contains("rock")) {
            tempMusic = rock;
            musicText.setText("Выбрано : РОК");
        } else if (text.contains("post-hardcore_metalcore")) {
            tempMusic = post;
            musicText.setText("Выбрано : Post-Hardcore/Metalcore");
        } else if (text.contains("electronic")) {
            tempMusic = electronic;
            musicText.setText("Выбрано : ЭЛЕКТРОННАЯ");
        } else {
            tempMusic = developer_choice;
            musicText.setText("Выбрано : ВЫБОР РАЗРАБОТЧИКА");
        }

        tempMusic += text.substring(text.length() - 6, text.length());
    }


    private void addMediaListener() {
        music.setOnEndOfMedia( () -> checkMusic() );
    }


    private void checkTrack(String text) {
        if (music.getMedia().getSource().contains(text + "01.mp3")) {
            music.stop();
            music = null;
            music = new MediaPlayer(new Media(new File(text + "02.mp3").toURI().toString()));
            music.setVolume(musicSlider.getValue() / 100);
            music.play();
            addMediaListener();
            return;
        }
        if (music.getMedia().getSource().contains(text + "02.mp3")) {
            music.stop();
            music = null;
            music = new MediaPlayer(new Media(new File(text + "03.mp3").toURI().toString()));
            music.setVolume(musicSlider.getValue() / 100);
            music.play();
            addMediaListener();
            return;
        }
        if (music.getMedia().getSource().contains(text + "03.mp3")) {
            music.stop();
            music = null;
            music = new MediaPlayer(new Media(new File(text + "04.mp3").toURI().toString()));
            music.setVolume(musicSlider.getValue() / 100);
            music.play();
            addMediaListener();
            return;
        }
        if (music.getMedia().getSource().contains(text + "04.mp3")) {
            music.stop();
            music = null;
            music = new MediaPlayer(new Media(new File(text + "05.mp3").toURI().toString()));
            music.setVolume(musicSlider.getValue() / 100);
            music.play();
            addMediaListener();
            return;
        }
        if (music.getMedia().getSource().contains(text + "05.mp3")) {
            music.stop();
            music = null;
            music = new MediaPlayer(new Media(new File(text + "06.mp3").toURI().toString()));
            music.setVolume(musicSlider.getValue() / 100);
            music.play();
            addMediaListener();
            return;
        }
        if (music.getMedia().getSource().contains(text + "06.mp3")) {
            music.stop();
            music = null;
            music = new MediaPlayer(new Media(new File(text + "07.mp3").toURI().toString()));
            music.setVolume(musicSlider.getValue() / 100);
            music.play();
            addMediaListener();
            return;
        }
        if (music.getMedia().getSource().contains(text + "07.mp3")) {
            music.stop();
            music = null;
            music = new MediaPlayer(new Media(new File(text + "01.mp3").toURI().toString()));
            music.setVolume(musicSlider.getValue() / 100);
            music.play();
            addMediaListener();
        }
    }






    private class MenuItem extends StackPane {

        private Text text;
        private Rectangle button;

        MenuItem(String name) {
            button = new Rectangle(200, 20, Color.WHITE);
            button.setOpacity(0.5);

            text = new Text(name);
            text.setFill(Color.WHITE);
            text.setFont(Font.font("Arial", FontWeight.BOLD, 14));

            setAlignment(Pos.CENTER);
            getChildren().addAll(button, text);

            FillTransition fillTransition = new FillTransition(Duration.seconds(0.5), button);
            addListeners(button, fillTransition);
        }


        private void addListeners(Rectangle bg, FillTransition ft) {
            setOnMouseEntered(event -> {
                ft.setFromValue(Color.WHITE);
                ft.setToValue(Color.DARKGOLDENROD);
                ft.setCycleCount(Animation.INDEFINITE);
                ft.setAutoReverse(true);
                ft.play();
            });

            setOnMouseExited(event -> {
                ft.stop();
                bg.setFill(Color.WHITE);
            });
        }
    }


    private class SubMenu extends VBox {

        SubMenu() {
        }

        SubMenu(MenuItem... items) {
            setSpacing(15);
            setTranslateX(50);
            setTranslateY(100);

            for (MenuItem item : items)
                getChildren().addAll(item);
        }


        void addItem(MenuItem menuItem) {
            setSpacing(5);
            setTranslateX(50);
            setTranslateY(100);

            getChildren().add(menuItem);
        }


        void addItem(Label lbl) {
            lbl.setTextFill(Color.WHITE);
            lbl.setFont(Font.font("Arial", FontWeight.BOLD, 14));
            getChildren().add(lbl);
        }
    }


    private class MenuBox extends Pane {

        private SubMenu subMenu;

        MenuBox(SubMenu sm) {
            subMenu = sm;

            ImageView imgView = new ImageView("file:/../images/backgrounds/menu.png");
            getChildren().addAll(imgView, subMenu);
        }


        void setSubMenu(SubMenu sm) {
            getChildren().remove(subMenu);
            subMenu = sm;
            getChildren().addAll(subMenu);
        }
    }
}