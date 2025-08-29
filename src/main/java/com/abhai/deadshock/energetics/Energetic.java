package com.abhai.deadshock.energetics;

import com.abhai.deadshock.DifficultyLevel;
import com.abhai.deadshock.Game;
import com.abhai.deadshock.levels.Level;
import com.abhai.deadshock.utils.Sounds;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

import java.nio.file.Path;
import java.nio.file.Paths;

import static com.abhai.deadshock.levels.Block.BLOCK_SIZE;

public class Energetic extends Pane {
    public static class Builder {
        private boolean canChooseDevilKiss;
        private boolean canChooseElectricity;
        private boolean canChooseHypnosis;

        public Builder canChooseDevilKiss(boolean canChooseDevilKiss) {
            this.canChooseDevilKiss = canChooseDevilKiss;
            return this;
        }

        public Builder canChooseElectricity(boolean canChooseElectricity) {
            this.canChooseElectricity = canChooseElectricity;
            return this;
        }

        public Builder canChooseHypnosis(boolean canChooseHypnosis) {
            this.canChooseHypnosis = canChooseHypnosis;
            return this;
        }

        public Energetic build() {
            return new Energetic(this);
        }
    }

    private static final int HYPNOSIS_X = BLOCK_SIZE * 40;
    private static final int DEVIL_KISS_X = BLOCK_SIZE * 50;
    private static final int ELECTRICITY_X = BLOCK_SIZE * 42;
    private static final int HYPNOSIS_Y = BLOCK_SIZE * 14 - 27;
    private static final int DEVIL_KISS_Y = BLOCK_SIZE * 9 + 18;
    private static final int ELECTRICITY_Y = BLOCK_SIZE * 9 - 30;

    private static final Path HYPNOSIS_IMAGE_PATH = Paths.get("resources", "images", "energetics", "hypnosis.png");
    private static final Path DEVIL_KISS_IMAGE_PATH = Paths.get("resources", "images", "energetics", "devilKiss.png");
    private static final Path ELECTRICITY_IMAGE_PATH = Paths.get("resources", "images", "energetics", "electricity.png");

    private boolean canShoot;
    private boolean canChangeEnergetic;
    private boolean canChooseDevilKiss;
    private boolean canChooseElectricity;
    private boolean canChooseHypnosis;

    private int saltPrice;
    private int countEnergetics;

    private EnergeticType type;
    private final Hypnosis hypnosis;
    private final ImageView imageView;
    private final DevilKiss devilKiss;
    private final Electricity electricity;

    public Energetic(Builder builder) {
        saltPrice = 0;
        canShoot = true;
        countEnergetics = 0;
        hypnosis = new Hypnosis();
        canChangeEnergetic = true;
        imageView = new ImageView();
        devilKiss = new DevilKiss();
        electricity = new Electricity();
        canChooseHypnosis = builder.canChooseHypnosis;
        canChooseDevilKiss = builder.canChooseDevilKiss;
        canChooseElectricity = builder.canChooseElectricity;

        initializePositionAndState();

        if (Game.levelNumber != Level.BOSS_LEVEL) {
            getChildren().add(imageView);
            Game.gameRoot.getChildren().add(this);
        }
    }

    public void reset() {
        canShoot = true;
        countEnergetics = 0;
        canChooseHypnosis = false;
        canChangeEnergetic = true;
        canChooseDevilKiss = false;
        canChooseElectricity = false;

        Game.hud.getHypnosis().setVisible(false);
        Game.hud.getDevilKiss().setVisible(false);
        Game.hud.getElectricity().setVisible(false);

        clear();
        initializePositionAndState();

        if (!Game.gameRoot.getChildren().contains(this))
            Game.gameRoot.getChildren().add(this);
    }

    public void clear() {
        hypnosis.delete();
        devilKiss.delete();
        electricity.delete();
    }

    public void shoot() {
        if (canShoot && countEnergetics > 0) {
            switch (type) {
                case EnergeticType.DEVIL_KISS -> devilKiss.shoot();
                case EnergeticType.ELECTRICITY -> electricity.use();
                case EnergeticType.HYPNOSIS -> hypnosis.hypnotize();
            }
            canShoot = false;
            Game.booker.minusSaltForUsingEnergetic(saltPrice);
        }
    }

    public void pickUp() {
        switch (Game.levelNumber) {
            case Level.FIRST_LEVEL -> {
                countEnergetics++;
                canChooseDevilKiss = true;
                type = EnergeticType.DEVIL_KISS;
                Game.hud.getDevilKiss().setVisible(true);
                Sounds.energetic.play(Game.menu.getVoiceSlider().getValue() / 100);
            }
            case Level.SECOND_LEVEL -> {
                countEnergetics++;
                canChooseElectricity = true;
                type = EnergeticType.ELECTRICITY;
                Game.hud.getElectricity().setVisible(true);
                Sounds.newEnergetic.play(Game.menu.getVoiceSlider().getValue() / 100);
            }
            case Level.THIRD_LEVEL -> {
                countEnergetics++;
                canChooseHypnosis = true;
                type = EnergeticType.HYPNOSIS;
                Game.hud.getHypnosis().setVisible(true);
                Sounds.newEnergetic.play(Game.menu.getVoiceSlider().getValue() / 100);
            }
        }
        setTranslateY(0);
        Game.gameRoot.getChildren().remove(this);
    }

    public void changeLevel() {
        if (Game.levelNumber == Level.SECOND_LEVEL) {
            imageView.setImage(new Image(ELECTRICITY_IMAGE_PATH.toUri().toString()));
            setTranslateX(ELECTRICITY_X);
            setTranslateY(ELECTRICITY_Y);
        } else if (Game.levelNumber == Level.THIRD_LEVEL) {
            imageView.setImage(new Image(HYPNOSIS_IMAGE_PATH.toUri().toString()));
            setTranslateX(HYPNOSIS_X);
            setTranslateY(HYPNOSIS_Y);
        }
        if (!Game.gameRoot.getChildren().contains(this))
            Game.gameRoot.getChildren().add(this);
    }

    public void changeEnergetic() {
        if (canChangeEnergetic) {
            switch (type) {
                case EnergeticType.DEVIL_KISS -> {
                    if (canChooseElectricity) {
                        type = EnergeticType.ELECTRICITY;
                        Game.hud.getElectricity().setVisible(true);
                        Sounds.changeToElectricity.play(Game.menu.getFxSlider().getValue() / 100);
                    } else if (canChooseHypnosis) {
                        type = EnergeticType.HYPNOSIS;
                        Game.hud.getHypnosis().setVisible(true);
                        Sounds.changeToHypnosis.play(Game.menu.getFxSlider().getValue() / 100);
                    }
                }
                case EnergeticType.ELECTRICITY -> {
                    if (canChooseHypnosis) {
                        type = EnergeticType.HYPNOSIS;
                        Game.hud.getHypnosis().setVisible(true);
                        Sounds.changeToHypnosis.play(Game.menu.getFxSlider().getValue() / 100);
                    } else if (canChooseDevilKiss) {
                        type = EnergeticType.DEVIL_KISS;
                        Game.hud.getElectricity().setVisible(false);
                        Sounds.changeToDevilKiss.play(Game.menu.getFxSlider().getValue() / 100);
                    }
                }
                case EnergeticType.HYPNOSIS -> {
                    if (canChooseDevilKiss) {
                        type = EnergeticType.DEVIL_KISS;
                        Game.hud.getHypnosis().setVisible(false);
                        Game.hud.getElectricity().setVisible(false);
                        Sounds.changeToDevilKiss.play(Game.menu.getFxSlider().getValue() / 100);
                    } else if (canChooseElectricity) {
                        type = EnergeticType.ELECTRICITY;
                        Game.hud.getHypnosis().setVisible(false);
                        Sounds.changeToElectricity.play(Game.menu.getFxSlider().getValue() / 100);
                    }
                }
            }
            canChangeEnergetic = false;
        }
    }

    public void setDifficultyLevel() {
        switch (Game.difficultyLevel) {
            case DifficultyLevel.MARIK -> saltPrice = 10;
            case DifficultyLevel.EASY -> saltPrice = 15;
            case DifficultyLevel.MEDIUM -> saltPrice = 20;
            case DifficultyLevel.HARD -> saltPrice = 25;
            case DifficultyLevel.HARDCORE -> saltPrice = 30;
        }
    }

    private void initializePositionAndState() {
        switch (Game.levelNumber) {
            case Level.FIRST_LEVEL -> {
                imageView.setImage(new Image(DEVIL_KISS_IMAGE_PATH.toUri().toString()));
                setTranslateX(DEVIL_KISS_X);
                setTranslateY(DEVIL_KISS_Y);
            }
            case Level.SECOND_LEVEL -> {
                imageView.setImage(new Image(ELECTRICITY_IMAGE_PATH.toUri().toString()));
                setTranslateX(ELECTRICITY_X);
                setTranslateY(ELECTRICITY_Y);
            }
            case Level.THIRD_LEVEL -> {
                imageView.setImage(new Image(HYPNOSIS_IMAGE_PATH.toUri().toString()));
                setTranslateX(HYPNOSIS_X);
                setTranslateY(HYPNOSIS_Y);
            }
        }

        if (canChooseDevilKiss) {
            countEnergetics++;
            type = EnergeticType.DEVIL_KISS;
            Game.hud.getDevilKiss().setVisible(true);
        }
        if (canChooseElectricity) {
            countEnergetics++;
            type = EnergeticType.ELECTRICITY;
            Game.hud.getElectricity().setVisible(true);
        }
        if (canChooseHypnosis) {
            countEnergetics++;
            type = EnergeticType.HYPNOSIS;
            Game.hud.getHypnosis().setVisible(true);
        }
    }

    public int getSaltPrice() {
        return saltPrice;
    }

    public void setCanShoot(boolean value) {
        canShoot = value;
    }

    public int getCountEnergetics() {
        return countEnergetics;
    }

    public boolean canChooseHypnosis() {
        return canChooseHypnosis;
    }

    public boolean canChooseDevilKiss() {
        return canChooseDevilKiss;
    }

    public boolean canChooseElectricity() {
        return canChooseElectricity;
    }

    public void setCanChangeEnergetic(boolean value) {
        canChangeEnergetic = value;
    }

    public void update() {
        devilKiss.update();
        electricity.update();
        hypnosis.update();
    }
}