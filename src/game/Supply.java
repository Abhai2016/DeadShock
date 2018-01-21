package game;

import game.Weapon.Weapon;
import javafx.animation.RotateTransition;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.util.Duration;

public class Supply extends Pane {
    private ImageView medicine = new ImageView(new Image("file:/../images/supply/medicine.png"));
    private ImageView ammo = new ImageView(new Image("file:/../images/supply/ammo.png"));

    private RotateTransition rt = new RotateTransition(Duration.seconds(1), ammo);

    private String supply;
    private String enemyName;

    private boolean delete = false;



    public Supply(int value, double x, double y) {
        switch (value) {
            case 0:
                supply = "medicine";
                getChildren().add(medicine);
                break;
            case 1:
                supply = "ammo";
                getChildren().add(ammo);
                break;
        }
        setTranslateX(x);
        setTranslateY(y - 15);
    }


    public Supply(double x, double y, String name) {
        enemyName = name;

        supply = "ammo";
        getChildren().add(ammo);

        setTranslateX(x);
        setTranslateY(y);
        Game.gameRoot.getChildren().add(this);

        rt.setByAngle(360);
        rt.play();
    }


    public String getSupply() {
        return supply;
    }


    public ImageView getImageSupply() {
        if (supply.equals("medicine"))
            return medicine;
        else
            return ammo;
    }


    public boolean isDelete() {
        return delete;
    }


    public void update() {
        if (getBoundsInParent().intersects(Game.booker.getBoundsInParent()))
            switch (enemyName) {
                case "comstock":
                    if (Game.weapon.getName().equals("pistol"))
                        Game.weapon.setBullets(Game.weapon.getBullets() + Game.booker.getBulletCount());
                    else
                        Weapon.WeaponData.pistolBullets += Game.booker.getBulletCount();
                    Game.gameRoot.getChildren().remove(this);
                    delete = true;
                    break;

                case "red_eye":
                    if (Game.weapon.getName().equals("machine_gun"))
                        Game.weapon.setBullets(Game.weapon.getBullets() + Game.booker.getBulletCount());
                    else
                        Weapon.WeaponData.machineGunBullets += Game.booker.getBulletCount();
                    Game.gameRoot.getChildren().remove(this);
                    delete = true;
                    break;
            } else {
                rt.setOnFinished( event -> rt.play() );

                if (getTranslateX() < Game.booker.getTranslateX())
                    setTranslateX(getTranslateX() + Game.booker.getCHARACTER_SPEED() * 1.5);
                if (getTranslateX() > Game.booker.getTranslateX())
                    setTranslateX(getTranslateX() - Game.booker.getCHARACTER_SPEED() * 1.5);

                if (getTranslateY() < Game.booker.getTranslateY())
                    setTranslateY(getTranslateY() + Game.booker.getCHARACTER_SPEED() * 1.5);
                if (getTranslateY() > Game.booker.getTranslateY())
                    setTranslateY(getTranslateY() - Game.booker.getCHARACTER_SPEED() * 1.5);
            }

    }
}
