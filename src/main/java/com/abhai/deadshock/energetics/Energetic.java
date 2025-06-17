package com.abhai.deadshock.energetics;

import com.abhai.deadshock.levels.Level;
import com.abhai.deadshock.Game;
import com.abhai.deadshock.Sounds;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

import static com.abhai.deadshock.levels.Block.BLOCK_SIZE;

public class Energetic extends Pane {
    private ArrayList<FireBall> fireBalls = new ArrayList<>();
    private Lightning lightning;
    private Path devilKissImagePath = Paths.get("resources", "images", "energetics", "devilKiss.png");
    private Path electricityImagePath = Paths.get("resources", "images", "energetics", "electricity.png");
    private Path hypnotistImagePath = Paths.get("resources", "images", "energetics", "hypnotist.png");
    private ImageView imgView = new ImageView(new Image(devilKissImagePath.toUri().toString()));
    private String name = "";
    private Hypnosis hypnosis = new Hypnosis();

    private boolean shoot = true;
    private boolean canChangeEnergetic = false;
    private boolean canChooseDevilKiss = false;
    private boolean canChooseElectricity = false;
    private boolean canChooseHypnotist = false;
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
            name = "devilKiss";
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
            name = "devilKiss";
            Game.hud.getDevilKiss().setVisible(true);
            countEnergetics++;
        }
        if (canChooseElectricity) {
            name = "electricity";
            Game.hud.getElectricity().setVisible(true);
            countEnergetics++;
        }

        imgView.setImage(new Image(hypnotistImagePath.toUri().toString()));
        setTranslateX(BLOCK_SIZE * 40);
        setTranslateY(BLOCK_SIZE * 14 - 27);

        getChildren().add(imgView);
        Game.gameRoot.getChildren().add(this);
    }


    public Energetic(boolean devilKiss, boolean electricity, boolean hypnotist) {
        canChooseDevilKiss = devilKiss;
        canChooseElectricity = electricity;
        canChooseHypnotist = hypnotist;
        if (canChooseDevilKiss) {
            name = "devilKiss";
            Game.hud.getDevilKiss().setVisible(true);
            countEnergetics++;
        }
        if (canChooseElectricity) {
            name = "electricity";
            Game.hud.getElectricity().setVisible(true);
            countEnergetics++;
        }
        if (canChooseHypnotist) {
            name = "hypnotist";
            Game.hud.getHypnotist().setVisible(true);
            countEnergetics++;
        }
    }


    public String getName() {
        return name;
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

    public boolean isCanChooseHypnotist() {
        return canChooseHypnotist;
    }

    public void pickUp() {
        if (Game.booker.getBoundsInParent().intersects(getBoundsInParent())) {
            switch (Game.levelNumber) {
                case Level.FIRST_LEVEL:
                    Sounds.audioClipEnergetic.play(Game.menu.voiceSlider.getValue() / 100);
                    name = "devilKiss";
                    countEnergetics++;
                    Game.hud.getDevilKiss().setVisible(true);
                    canChooseDevilKiss = true;
                    break;
                case Level.SECOND_LEVEL:
                    Sounds.audioClipNewEnergetic.play(Game.menu.voiceSlider.getValue() / 100);
                    name = "electricity";
                    countEnergetics++;
                    canChangeEnergetic = true;
                    Game.hud.getElectricity().setVisible(true);
                    canChooseElectricity = true;
                    break;
                case Level.THIRD_LEVEL:
                    Sounds.audioClipNewEnergetic.play(Game.menu.voiceSlider.getValue() / 100);
                    name = "hypnotist";
                    countEnergetics++;
                    canChangeEnergetic = true;
                    Game.hud.getHypnotist().setVisible(true);
                    canChooseHypnotist = true;
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
            imgView.setImage(new Image(hypnotistImagePath.toUri().toString()));
            setTranslateX(BLOCK_SIZE * 40);
            setTranslateY(BLOCK_SIZE * 14 - 27);
        }
        setVisible(true);
    }

    public void changeEnergetic() {
        if (canChangeEnergetic) {
            switch (name) {
                case "devilKiss":
                    if (canChooseElectricity) {
                        name = "electricity";
                        Sounds.changeOnElectricity.play(Game.menu.fxSlider.getValue() / 100);
                        Game.hud.getElectricity().setVisible(true);
                    } else if (canChooseHypnotist) {
                        name = "hypnotist";
                        Sounds.changeOnHypnotist.play(Game.menu.fxSlider.getValue() / 100);
                        Game.hud.getHypnotist().setVisible(true);
                    }
                    break;
                case "electricity":
                    if (canChooseHypnotist) {
                        name = "hypnotist";
                        Sounds.changeOnHypnotist.play(Game.menu.fxSlider.getValue() / 100);
                        Game.hud.getHypnotist().setVisible(true);
                    } else if (canChooseDevilKiss) {
                        name = "devilKiss";
                        Sounds.changeOnDevilKiss.play(Game.menu.fxSlider.getValue() / 100);
                        Game.hud.getElectricity().setVisible(false);
                    }
                    break;
                case "hypnotist":
                    if (canChooseDevilKiss) {
                        name = "devilKiss";
                        Sounds.changeOnDevilKiss.play(Game.menu.fxSlider.getValue() / 100);
                        Game.hud.getElectricity().setVisible(false);
                        Game.hud.getHypnotist().setVisible(false);
                    } else if (canChooseElectricity) {
                        name = "electricity";
                        Sounds.changeOnElectricity.play(Game.menu.fxSlider.getValue() / 100);
                        Game.hud.getHypnotist().setVisible(false);
                    }
            }
            canChangeEnergetic = false;
        }
    }

    public void shoot() {
        switch (name) {
            case "devilKiss":
                fireBalls.add(new FireBall(Game.booker.getTranslateX(), Game.booker.getTranslateY() + 5));
                Sounds.devilKissShoot.play(Game.menu.fxSlider.getValue() / 100);
                break;
            case "electricity":
                if (lightning != null) {
                    Game.gameRoot.getChildren().remove(lightning);
                    lightning = null;
                }
                lightning = new Lightning(Game.booker.getTranslateX(), Game.booker.getTranslateY());
                break;
            case "hypnotist":
                hypnosis.setHypnosis();
                Sounds.hypnotist.play(Game.menu.fxSlider.getValue() / 100);
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
        if (!fireBalls.isEmpty())
            for (FireBall fireBall : fireBalls)
                if (fireBall.update()) {
                    Game.energetic.fireBalls.remove(fireBall);
                    Game.gameRoot.getChildren().remove(fireBall);
                    return;
                }
        if (lightning != null) {
            lightning.update();
            if (lightning.isDelete())
                lightning = null;
        }

        if (hypnosis.isHypnosis())
            hypnosis.update();
    }
}