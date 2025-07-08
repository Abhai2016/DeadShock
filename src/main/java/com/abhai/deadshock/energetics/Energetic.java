package com.abhai.deadshock.energetics;

import com.abhai.deadshock.levels.Level;
import com.abhai.deadshock.Game;
import com.abhai.deadshock.utils.Sounds;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

import static com.abhai.deadshock.levels.Block.BLOCK_SIZE;

public class Energetic extends Pane {
    private ArrayList<DevilKissShot> devilKissShots = new ArrayList<>();
    private Electricity electricity;
    private Path devilKissImagePath = Paths.get("resources", "images", "energetics", "devilKiss.png");
    private Path electricityImagePath = Paths.get("resources", "images", "energetics", "electricity.png");
    private Path hypnosisImagePath = Paths.get("resources", "images", "energetics", "hypnosis.png");
    private ImageView imgView = new ImageView(new Image(devilKissImagePath.toUri().toString()));
    private EnergeticType type;
    private Hypnosis hypnosis = new Hypnosis();

    private boolean shoot = true;
    private boolean canChangeEnergetic = false;
    private boolean canChooseDevilKiss = false;
    private boolean canChooseElectricity = false;
    private boolean canChooseHypnosis = false;
    private byte countEnergetics = 0;
    private byte priceForUsing = 0;


    public Energetic() {
        setTranslateX(BLOCK_SIZE * 50);
        setTranslateY(BLOCK_SIZE * 9 + 18);

        getChildren().add(imgView);
        Game.gameRoot.getChildren().add(this);
    }


    public Energetic(boolean value) {
        canChooseDevilKiss = value;
        if (canChooseDevilKiss) {
            type = EnergeticType.DEVIL_KISS;
            countEnergetics++;
            Game.hud.getDevilKiss().setVisible(true);
        }

        imgView.setImage(new Image(electricityImagePath.toUri().toString()));
        setTranslateX(BLOCK_SIZE * 42);
        setTranslateY(BLOCK_SIZE * 9 - 30);

        getChildren().add(imgView);
        Game.gameRoot.getChildren().add(this);
    }


    public Energetic(boolean devilKiss, boolean electricity) {
        canChooseDevilKiss = devilKiss;
        canChooseElectricity = electricity;
        if (canChooseDevilKiss) {
            type = EnergeticType.DEVIL_KISS;
            Game.hud.getDevilKiss().setVisible(true);
            countEnergetics++;
        }
        if (canChooseElectricity) {
            type = EnergeticType.ELECTRICITY;
            Game.hud.getElectricity().setVisible(true);
            countEnergetics++;
        }

        imgView.setImage(new Image(hypnosisImagePath.toUri().toString()));
        setTranslateX(BLOCK_SIZE * 40);
        setTranslateY(BLOCK_SIZE * 14 - 27);

        getChildren().add(imgView);
        Game.gameRoot.getChildren().add(this);
    }


    public Energetic(boolean devilKiss, boolean electricity, boolean hypnosis) {
        canChooseDevilKiss = devilKiss;
        canChooseElectricity = electricity;
        canChooseHypnosis = hypnosis;
        if (canChooseDevilKiss) {
            type = EnergeticType.DEVIL_KISS;
            Game.hud.getDevilKiss().setVisible(true);
            countEnergetics++;
        }
        if (canChooseElectricity) {
            type = EnergeticType.ELECTRICITY;
            Game.hud.getElectricity().setVisible(true);
            countEnergetics++;
        }
        if (canChooseHypnosis) {
            type = EnergeticType.HYPNOSIS;
            Game.hud.getHypnosis().setVisible(true);
            countEnergetics++;
        }
    }


    public EnergeticType getType() {
        return type;
    }

    public void setShoot(boolean value) {
        shoot = value;
    }

    public void setCanChangeEnergetic(boolean value) {
        canChangeEnergetic = value;
    }

    public boolean isCanChangeEnergetic() {
        return canChangeEnergetic;
    }

    public boolean isCanChooseElectricity() {
        return canChooseElectricity;
    }

    public byte getCountEnergetics() {
        return countEnergetics;
    }

    public boolean isShoot() {
        return shoot;
    }

    public byte getPriceForUsing() {
        return priceForUsing;
    }

    public boolean isCanChooseDevilKiss() {
        return canChooseDevilKiss;
    }

    public boolean isCanChooseHypnosis() {
        return canChooseHypnosis;
    }

    public void pickUp() {
        if (Game.booker.getBoundsInParent().intersects(getBoundsInParent())) {
            switch (Game.levelNumber) {
                case Level.FIRST_LEVEL:
                    Sounds.energetic.play(Game.menu.voiceSlider.getValue() / 100);
                    type = EnergeticType.DEVIL_KISS;
                    countEnergetics++;
                    Game.hud.getDevilKiss().setVisible(true);
                    canChooseDevilKiss = true;
                    break;
                case Level.SECOND_LEVEL:
                    Sounds.newEnergetic.play(Game.menu.voiceSlider.getValue() / 100);
                    type = EnergeticType.ELECTRICITY;
                    countEnergetics++;
                    canChangeEnergetic = true;
                    Game.hud.getElectricity().setVisible(true);
                    canChooseElectricity = true;
                    break;
                case Level.THIRD_LEVEL:
                    Sounds.newEnergetic.play(Game.menu.voiceSlider.getValue() / 100);
                    type = EnergeticType.HYPNOSIS;
                    countEnergetics++;
                    canChangeEnergetic = true;
                    Game.hud.getHypnosis().setVisible(true);
                    canChooseHypnosis = true;
                    break;
            }
            setTranslateY(0);
            setVisible(false);
        }
    }

    public void setDifficultyLevel() {
        switch (Game.difficultyLevelText) {
            case "marik":
                priceForUsing = 10;
                break;
            case "easy":
                priceForUsing = 15;
                break;
            case "normal":
                priceForUsing = 20;
                break;
            case "high":
                priceForUsing = 25;
                break;
            case "hardcore":
                priceForUsing = 30;
                break;
        }
    }

    public void changeLevel() {
        if (Game.levelNumber == Level.SECOND_LEVEL) {
            imgView.setImage(new Image(electricityImagePath.toUri().toString()));
            setTranslateX(BLOCK_SIZE * 42);
            setTranslateY(BLOCK_SIZE * 9 - 30);
        } else if (Game.levelNumber == Level.THIRD_LEVEL) {
            imgView.setImage(new Image(hypnosisImagePath.toUri().toString()));
            setTranslateX(BLOCK_SIZE * 40);
            setTranslateY(BLOCK_SIZE * 14 - 27);
        }
        setVisible(true);
    }

    public void changeEnergetic() {
        if (canChangeEnergetic) {
            switch (type) {
                case EnergeticType.DEVIL_KISS:
                    if (canChooseElectricity) {
                        type = EnergeticType.ELECTRICITY;
                        Sounds.changeToElectricity.play(Game.menu.fxSlider.getValue() / 100);
                        Game.hud.getElectricity().setVisible(true);
                    } else if (canChooseHypnosis) {
                        type = EnergeticType.HYPNOSIS;
                        Sounds.changeToHypnosis.play(Game.menu.fxSlider.getValue() / 100);
                        Game.hud.getHypnosis().setVisible(true);
                    }
                    break;
                case EnergeticType.ELECTRICITY:
                    if (canChooseHypnosis) {
                        type = EnergeticType.HYPNOSIS;
                        Sounds.changeToHypnosis.play(Game.menu.fxSlider.getValue() / 100);
                        Game.hud.getHypnosis().setVisible(true);
                    } else if (canChooseDevilKiss) {
                        type = EnergeticType.DEVIL_KISS;
                        Sounds.changeToDevilKiss.play(Game.menu.fxSlider.getValue() / 100);
                        Game.hud.getElectricity().setVisible(false);
                    }
                    break;
                case EnergeticType.HYPNOSIS:
                    if (canChooseDevilKiss) {
                        type = EnergeticType.DEVIL_KISS;
                        Sounds.changeToDevilKiss.play(Game.menu.fxSlider.getValue() / 100);
                        Game.hud.getElectricity().setVisible(false);
                        Game.hud.getHypnosis().setVisible(false);
                    } else if (canChooseElectricity) {
                        type = EnergeticType.ELECTRICITY;
                        Sounds.changeToElectricity.play(Game.menu.fxSlider.getValue() / 100);
                        Game.hud.getHypnosis().setVisible(false);
                    }
            }
            canChangeEnergetic = false;
        }
    }

    public void shoot() {
        switch (type) {
            case EnergeticType.DEVIL_KISS:
                devilKissShots.add(new DevilKissShot(Game.booker.getTranslateX(), Game.booker.getTranslateY() + 5));
                Sounds.devilKissShot.play(Game.menu.fxSlider.getValue() / 100);
                break;
            case EnergeticType.ELECTRICITY:
                if (electricity != null) {
                    Game.gameRoot.getChildren().remove(electricity);
                    electricity = null;
                }
                electricity = new Electricity(Game.booker.getTranslateX(), Game.booker.getTranslateY());
                break;
            case EnergeticType.HYPNOSIS:
                hypnosis.setHypnosis();
                Sounds.hypnosis.play(Game.menu.fxSlider.getValue() / 100);
                break;
        }
        Game.booker.setSalt(Game.booker.getSalt() - priceForUsing);
        shoot = false;
    }

    public void setHypnosisForBooker() {
        hypnosis.setHypnosisForBooker();
    }

    public void updateHypnosisForBooker() {
        hypnosis.updateHypnosis();
    }

    public void deleteHypnosis() {
        hypnosis.deleteDeleteHypnosis();
    }

    public void update() {
        if (!devilKissShots.isEmpty())
            for (DevilKissShot devilKissShot : devilKissShots)
                if (devilKissShot.update()) {
                    Game.energetic.devilKissShots.remove(devilKissShot);
                    Game.gameRoot.getChildren().remove(devilKissShot);
                    return;
                }
        if (electricity != null) {
            electricity.update();
            if (electricity.isDelete())
                electricity = null;
        }

        if (hypnosis.isHypnosis())
            hypnosis.update();
    }
}