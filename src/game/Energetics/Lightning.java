package game.Energetics;

import game.Characters.EnemyBase;
import game.Characters.SpriteAnimation;
import game.Game;
import game.Sounds;
import javafx.geometry.Rectangle2D;
import javafx.scene.image.ImageView;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

import java.io.File;

class Lightning extends BaseEnergeticElement {
    private MediaPlayer mediaPlayerLightning = new MediaPlayer(
            new Media(new File("file:/../sounds/fx/energetics/lightning.mp3").toURI().toString()));
    private boolean delete = false;

    Lightning(double x, double y) {
        imgView = new ImageView("file:/../images/energetics/lightning.png");
        imgView.setViewport( new Rectangle2D(0, 0, 172, 63) );
        animation = new SpriteAnimation(imgView, Duration.seconds(0.5), 10, 1, 0, 0, 172, 63);
        animation.play();

        if (Game.booker.getScaleX() > 0)
            setTranslateX(x + Game.booker.getImgView().getFitWidth() - 5);
        else {
            setScaleX(-1);
            setTranslateX(Game.booker.getTranslateX() - 170);
        }

        setTranslateY(y - 10);

        mediaPlayerLightning.setVolume(Game.menu.fxSlider.getValue() / 100);
        mediaPlayerLightning.play();
        mediaPlayerLightning.setOnEndOfMedia( () -> {
            delete = true;
            Game.gameRoot.getChildren().remove(this);
        });

        getChildren().add(imgView);
        Game.gameRoot.getChildren().add(this);
    }


    boolean isDelete() {
        return delete;
    }


    private void move() {
        if (Game.booker.getScaleX() > 0) {
            setTranslateX(Game.booker.getTranslateX() + Game.booker.getImgView().getFitWidth() - 5);
            setScaleX(1);
        }
        else {
            setScaleX(-1);
            setTranslateX(Game.booker.getTranslateX() - 170);
        }

        setTranslateY(Game.booker.getTranslateY() - 10);
    }


    public boolean update() {
        for (EnemyBase enemy : Game.enemies)
            if (getBoundsInParent().intersects(enemy.getBoundsInParent())) {
                enemy.setHP(0);
                Sounds.killByLightning.play(Game.menu.fxSlider.getValue() / 100);
                return true;
            }

        if (Game.levelNumber == 3)
            if (getBoundsInParent().intersects(Game.boss.getBoundsInParent())) {
                Game.boss.setHP(Game.boss.getHP() - Game.weapon.getDamage() / 2);
            }
        move();
        return false;
    }
}
