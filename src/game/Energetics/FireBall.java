package game.Energetics;

import game.Characters.EnemyBase;
import game.Characters.SpriteAnimation;
import game.Levels.Block;
import game.Game;
import javafx.geometry.Rectangle2D;
import javafx.scene.image.ImageView;
import javafx.util.Duration;

class FireBall extends BaseEnergeticElement {
    private boolean direction;

    FireBall(double x, double y) {
        imgView = new ImageView("file:/../images/energetics/fireBall.png");
        imgView.setViewport( new Rectangle2D(0, 0, 64, 30) );
        animation = new SpriteAnimation(imgView, Duration.seconds(0.5), 8, 8, 0, 0, 64, 30);
        animation.play();

        setTranslateX(x - 24);
        setTranslateY(y);

        if (Game.booker.getScaleX() > 0) {
            direction = true;
            setTranslateX(getTranslateX() + 40);
        } else
            setScaleX(-1);

        getChildren().add(imgView);
        Game.gameRoot.getChildren().add(this);
    }


    public boolean update() {
        if (direction)
            setTranslateX(getTranslateX() + 5);
        else
            setTranslateX(getTranslateX() - 5);

        for (EnemyBase enemy : Game.enemies)
            if (getBoundsInParent().intersects(enemy.getBoundsInParent()))
                enemy.setHP(0);

        for (Block block : Game.blocks)
            if (getBoundsInParent().intersects(block.getBoundsInParent()))
                return true;

        if (Game.levelNumber == 3)
            if (getBoundsInParent().intersects(Game.boss.getBoundsInParent())) {
                Game.boss.setHP(Game.boss.getHP() - Game.weapon.getRpgDamage());
                return true;
            }

        if (getTranslateX() > Game.booker.getTranslateX() + 680
                || getTranslateX() < Game.booker.getTranslateX() - 600)
            return true;

        return false;
    }
}
