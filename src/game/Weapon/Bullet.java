package game.Weapon;

import game.Characters.EnemyBase;
import game.Levels.Block;
import game.Game;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

public class Bullet extends Pane {
    static final int BULLET_SPEED = 15;

    ImageView bullet;

    boolean direction = true;
    boolean delete = false;



    // for subclasses
    Bullet() {

    }

    Bullet(String weaponName) {
        if (Game.booker.getScaleX() < 0) {
            direction = false;
            setScaleX(-1);
        }

        bullet = new ImageView("file:/../images/weapons/normal_bullet.png");

        switch (weaponName) {
            case "pistol":
                setTranslateY(Game.booker.getTranslateY() + 14);
                break;
            case "machine_gun":
                setTranslateY(Game.booker.getTranslateY() + 20);
                break;
        }

        if (direction)
            setTranslateX(Game.booker.getTranslateX() + Game.booker.getWidth());
        else
            setTranslateX(Game.booker.getTranslateX());

        getChildren().add(bullet);
        Game.gameRoot.getChildren().add(this);
    }



    public boolean isDelete() {
        return delete;
    }


    public void update() {
        if (direction)
            setTranslateX(getTranslateX() + BULLET_SPEED);
        else
            setTranslateX(getTranslateX() - BULLET_SPEED);

        if (getTranslateX() > -Game.gameRoot.getLayoutX() + Game.appRoot.getWidth()
                || getTranslateX() < -Game.gameRoot.getLayoutX()) {
            Game.gameRoot.getChildren().remove(this);
            delete = true;
        }

        for (EnemyBase enemy : Game.enemies)
            if (getBoundsInParent().intersects(enemy.getBoundsInParent())) {
                Game.gameRoot.getChildren().remove(this);
                enemy.setHP((short) (enemy.getHP() - Game.weapon.getDamage()));
                enemy.setPlayVoice(true);
                enemy.playHitVoice();
                delete = true;
                return;
            }

        if (Game.boss != null)
            if (getBoundsInParent().intersects(Game.boss.getBoundsInParent())) {
                Game.gameRoot.getChildren().remove(this);
                Game.boss.setHP((short) (Game.boss.getHP() - Game.weapon.getDamage()));
                delete = true;
                return;
            }

        for (Block block : Game.blocks)
            if (getBoundsInParent().intersects(block.getBoundsInParent())) {
                Game.gameRoot.getChildren().remove(this);
                delete = true;
                return;
            }
    }
}