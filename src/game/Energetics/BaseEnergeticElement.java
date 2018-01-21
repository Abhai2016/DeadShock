package game.Energetics;


import game.Characters.SpriteAnimation;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

class BaseEnergeticElement extends Pane {
    ImageView imgView;
    SpriteAnimation animation;


    BaseEnergeticElement() {

    }


    public boolean update() {
        return false;
    }
}
