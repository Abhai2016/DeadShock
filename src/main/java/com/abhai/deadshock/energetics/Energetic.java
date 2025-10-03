package com.abhai.deadshock.energetics;

import com.abhai.deadshock.Game;
import com.abhai.deadshock.types.DifficultyType;
import com.abhai.deadshock.types.EnergeticType;
import com.abhai.deadshock.utils.GameMedia;
import com.abhai.deadshock.world.levels.Level;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

import java.nio.file.Path;
import java.nio.file.Paths;

import static com.abhai.deadshock.world.levels.Block.BLOCK_SIZE;

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
    private static final int DEVIL_KISS_X = BLOCK_SIZE * 47;
    private static final int ELECTRICITY_X = BLOCK_SIZE * 50;
    private static final int HYPNOSIS_Y = BLOCK_SIZE * 14 - 27;
    private static final int DEVIL_KISS_Y = BLOCK_SIZE * 9 + 18;
    private static final int ELECTRICITY_Y = BLOCK_SIZE * 9 - 30;
    private static final Path HYPNOSIS_IMAGE_PATH = Paths.get("resources", "images", "energetics", "hypnosis.png");
    private static final Path DEVIL_KISS_IMAGE_PATH = Paths.get("resources", "images", "energetics", "devilKiss.png");
    private static final Path ELECTRICITY_IMAGE_PATH = Paths.get("resources", "images", "energetics", "electricity.png");

    private boolean canShoot;
    private boolean canChooseHypnosis;
    private boolean canChangeEnergetic;
    private boolean canChooseDevilKiss;
    private boolean canChooseElectricity;

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
        getChildren().add(imageView);
        electricity = new Electricity();
        canChooseHypnosis = builder.canChooseHypnosis;
        canChooseDevilKiss = builder.canChooseDevilKiss;
        canChooseElectricity = builder.canChooseElectricity;

        initializePositionAndState();
        if (Game.getGameWorld().getLevel().getCurrentLevelNumber() != Level.BOSS_LEVEL)
            Game.getGameWorld().getGameRoot().getChildren().add(this);
    }

    public void reset() {
        canShoot = true;
        countEnergetics = 0;
        canChooseHypnosis = false;
        canChangeEnergetic = true;
        canChooseDevilKiss = false;
        canChooseElectricity = false;
        Game.getGameWorld().getHud().getHypnosis().setVisible(false);
        Game.getGameWorld().getHud().getDevilKiss().setVisible(false);
        Game.getGameWorld().getHud().getElectricity().setVisible(false);

        clear();
        initializePositionAndState();
        if (!Game.getGameWorld().getGameRoot().getChildren().contains(this))
            Game.getGameWorld().getGameRoot().getChildren().add(this);
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
            Game.getGameWorld().getBooker().minusSaltForUsingEnergetic(saltPrice);
        }
    }

    public void pickUp() {
        switch (Game.getGameWorld().getLevel().getCurrentLevelNumber()) {
            case Level.FIRST_LEVEL -> {
                countEnergetics++;
                canChooseDevilKiss = true;
                type = EnergeticType.DEVIL_KISS;
                Game.getGameWorld().getHud().getDevilKiss().setVisible(true);
                GameMedia.ENERGETIC.play(Game.getGameWorld().getMenu().getVoiceSlider().getValue() / 100);
            }
            case Level.SECOND_LEVEL -> {
                countEnergetics++;
                canChooseElectricity = true;
                type = EnergeticType.ELECTRICITY;
                Game.getGameWorld().getHud().getElectricity().setVisible(true);
                GameMedia.NEW_ENERGETIC.play(Game.getGameWorld().getMenu().getVoiceSlider().getValue() / 100);
            }
            case Level.THIRD_LEVEL -> {
                countEnergetics++;
                canChooseHypnosis = true;
                type = EnergeticType.HYPNOSIS;
                Game.getGameWorld().getHud().getHypnosis().setVisible(true);
                GameMedia.NEW_ENERGETIC.play(Game.getGameWorld().getMenu().getVoiceSlider().getValue() / 100);
            }
        }
        setTranslateY(0);
        Game.getGameWorld().getGameRoot().getChildren().remove(this);
    }

    public void changeLevel() {
        if (Game.getGameWorld().getLevel().getCurrentLevelNumber() == Level.BOSS_LEVEL) {
            Game.getGameWorld().getGameRoot().getChildren().remove(this);
            return;
        }

        if (Game.getGameWorld().getLevel().getCurrentLevelNumber() == Level.SECOND_LEVEL) {
            imageView.setImage(new Image(ELECTRICITY_IMAGE_PATH.toUri().toString()));
            setTranslateX(ELECTRICITY_X);
            setTranslateY(ELECTRICITY_Y);
        } else if (Game.getGameWorld().getLevel().getCurrentLevelNumber() == Level.THIRD_LEVEL) {
            imageView.setImage(new Image(HYPNOSIS_IMAGE_PATH.toUri().toString()));
            setTranslateX(HYPNOSIS_X);
            setTranslateY(HYPNOSIS_Y);
        }

        if (!Game.getGameWorld().getGameRoot().getChildren().contains(this))
            Game.getGameWorld().getGameRoot().getChildren().add(this);
    }

    public void changeEnergetic() {
        if (canChangeEnergetic) {
            switch (type) {
                case EnergeticType.DEVIL_KISS -> {
                    if (canChooseElectricity) {
                        type = EnergeticType.ELECTRICITY;
                        Game.getGameWorld().getHud().getElectricity().setVisible(true);
                        GameMedia.CHANGE_TO_ELECTRICITY.play(Game.getGameWorld().getMenu().getFxSlider().getValue() / 100);
                    } else if (canChooseHypnosis) {
                        type = EnergeticType.HYPNOSIS;
                        Game.getGameWorld().getHud().getHypnosis().setVisible(true);
                        GameMedia.CHANGE_TO_HYPNOSIS.play(Game.getGameWorld().getMenu().getFxSlider().getValue() / 100);
                    }
                }
                case EnergeticType.ELECTRICITY -> {
                    if (canChooseHypnosis) {
                        type = EnergeticType.HYPNOSIS;
                        Game.getGameWorld().getHud().getHypnosis().setVisible(true);
                        GameMedia.CHANGE_TO_HYPNOSIS.play(Game.getGameWorld().getMenu().getFxSlider().getValue() / 100);
                    } else if (canChooseDevilKiss) {
                        type = EnergeticType.DEVIL_KISS;
                        Game.getGameWorld().getHud().getElectricity().setVisible(false);
                        GameMedia.CHANGE_TO_DEVIL_KISS.play(Game.getGameWorld().getMenu().getFxSlider().getValue() / 100);
                    }
                }
                case EnergeticType.HYPNOSIS -> {
                    if (canChooseDevilKiss) {
                        type = EnergeticType.DEVIL_KISS;
                        Game.getGameWorld().getHud().getHypnosis().setVisible(false);
                        Game.getGameWorld().getHud().getElectricity().setVisible(false);
                        GameMedia.CHANGE_TO_DEVIL_KISS.play(Game.getGameWorld().getMenu().getFxSlider().getValue() / 100);
                    } else if (canChooseElectricity) {
                        type = EnergeticType.ELECTRICITY;
                        Game.getGameWorld().getHud().getHypnosis().setVisible(false);
                        GameMedia.CHANGE_TO_ELECTRICITY.play(Game.getGameWorld().getMenu().getFxSlider().getValue() / 100);
                    }
                }
            }
            canChangeEnergetic = false;
        }
    }

    public void setDifficultyType() {
        switch (Game.getGameWorld().getDifficultyType()) {
            case DifficultyType.MARIK -> saltPrice = 0;
            case DifficultyType.EASY -> saltPrice = 10;
            case DifficultyType.MEDIUM -> saltPrice = 15;
            case DifficultyType.HARD -> saltPrice = 20;
            case DifficultyType.HARDCORE -> saltPrice = 25;
        }
    }

    private void initializePositionAndState() {
        switch (Game.getGameWorld().getLevel().getCurrentLevelNumber()) {
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
            Game.getGameWorld().getHud().getDevilKiss().setVisible(true);
        }
        if (canChooseElectricity) {
            countEnergetics++;
            type = EnergeticType.ELECTRICITY;
            Game.getGameWorld().getHud().getElectricity().setVisible(true);
        }
        if (canChooseHypnosis) {
            countEnergetics++;
            type = EnergeticType.HYPNOSIS;
            Game.getGameWorld().getHud().getHypnosis().setVisible(true);
        }
    }

    public int getSaltPrice() {
        return saltPrice;
    }

    public int getCountEnergetics() {
        return countEnergetics;
    }

    public void setCanShoot(boolean value) {
        canShoot = value;
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