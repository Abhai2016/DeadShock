package com.abhai.deadshock.world.supplies;

import com.abhai.deadshock.Game;
import com.abhai.deadshock.types.SupplySubType;
import com.abhai.deadshock.types.SupplyType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

import java.nio.file.Paths;
import java.util.Random;

public class Supply extends Pane {
    private static final Random RANDOM = new Random();
    private final Image ammoImage = new Image(Paths.get("resources", "images", "supply", "ammo.png").toUri().toString());
    private final Image saltImage = new Image(Paths.get("resources", "images", "supply", "salt.png").toUri().toString());
    private final Image medicineImage = new Image(Paths.get("resources", "images", "supply", "medicine.png").toUri().toString());

    private boolean delete;
    private SupplySubType subType;

    protected SupplyType type;
    protected final ImageView imageView;

    public Supply() {
        delete = false;
        imageView = new ImageView();
        getChildren().add(imageView);
    }

    protected void move() {

    }

    private void delete() {
        delete = true;
        if (getTranslateY() < Game.SCENE_HEIGHT) {
            if (subType == SupplySubType.SALT)
                Game.getGameWorld().getBooker().addSaltForKillingEnemy();
            else if (subType == SupplySubType.MEDICINE)
                Game.getGameWorld().getBooker().addMedicineForKillingEnemy();
            else
                Game.getGameWorld().getBooker().takeAmmo(subType);
        }
        Game.getGameWorld().getGameRoot().getChildren().remove(this);
    }

    public boolean isDelete() {
        return delete;
    }

    public void init(SupplySubType supplySubType, double x, double y) {
        double random = RANDOM.nextDouble();
        if (random < 0.4) {
            subType = SupplySubType.MEDICINE;
            imageView.setImage(medicineImage);
        } else if (random < 0.7) {
            subType = SupplySubType.SALT;
            imageView.setImage(saltImage);
        } else {
            subType = supplySubType;
            imageView.setImage(ammoImage);
        }
        delete = false;
        setTranslateX(x);
        setTranslateY(y);
        Game.getGameWorld().getGameRoot().getChildren().add(this);
    }

    public void update() {
        if (!getBoundsInParent().intersects(Game.getGameWorld().getBooker().getBoundsInParent()) && getTranslateY() < Game.SCENE_HEIGHT)
            move();
        else
            delete();
    }
}