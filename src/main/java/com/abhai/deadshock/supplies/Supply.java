package com.abhai.deadshock.supplies;

import com.abhai.deadshock.Game;
import com.abhai.deadshock.levels.Block;
import com.abhai.deadshock.levels.BlockType;
import com.abhai.deadshock.weapons.WeaponType;
import javafx.geometry.Point2D;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

import java.nio.file.Paths;

public class Supply extends Pane {
    private static final int GRAVITY = 10;
    private final Image ammoImage = new Image(Paths.get("resources", "images", "supply", "ammo.png").toUri().toString());
    private final Image medicineImage = new Image(Paths.get("resources", "images", "supply", "medicine.png").toUri().toString());

    private boolean delete;
    private SupplyType type;
    private final Point2D velocity;
    private final ImageView imageView;

    public Supply() {
        imageView = new ImageView();
        getChildren().add(imageView);

        delete = false;
        velocity = new Point2D(0, GRAVITY);
    }

    private void move() {
        for (int i = 0; i < velocity.getY(); i++) {
            setTranslateY(getTranslateY() + 1);
            for (Block block : Game.level.getBlocks())
                if (getBoundsInParent().intersects(block.getBoundsInParent()) && block.getType() != BlockType.INVISIBLE) {
                    setTranslateY(getTranslateY() - 1);
                    return;
                }
        }
    }

    private void delete() {
        delete = true;
        if (type == SupplyType.AMMO) {
            if (Game.weapon.getType() != WeaponType.RPG)
                Game.weapon.setBullets(Game.weapon.getBullets() + Game.booker.getBulletsForKillingEnemy());
            else
                Game.weapon.setBullets(Game.weapon.getBullets() + Game.booker.getBulletsForKillingEnemy() / 5);
        } else
            Game.booker.addMedicineForKillingEnemy();
        Game.gameRoot.getChildren().remove(this);
    }

    public void init(double x, double y) {
        if (Math.random() < 0.5) {
            type = SupplyType.MEDICINE;
            imageView.setImage(medicineImage);
        } else {
            type = SupplyType.AMMO;
            imageView.setImage(ammoImage);
        }
        delete = false;
        setTranslateX(x);
        setTranslateY(y - 15);
        Game.gameRoot.getChildren().add(this);
    }

    public boolean isDelete() {
        return delete;
    }

    public void update() {
        if (!getBoundsInParent().intersects(Game.booker.getBoundsInParent()))
            move();
        else
            delete();
    }
}