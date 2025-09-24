package com.abhai.deadshock.world.supplies;

import com.abhai.deadshock.Game;
import com.abhai.deadshock.characters.Character;
import com.abhai.deadshock.types.SupplyType;
import javafx.animation.Animation;
import javafx.animation.RotateTransition;
import javafx.util.Duration;

public class ElizabethSupply extends Supply {

    public ElizabethSupply() {
        type = SupplyType.ELIZABETH;
        RotateTransition rotateAnimation = new RotateTransition(Duration.seconds(1), imageView);
        rotateAnimation.setCycleCount(Animation.INDEFINITE);
        rotateAnimation.play();
    }

    @Override
    protected void move() {
        if (getTranslateX() < Game.getGameWorld().getBooker().getTranslateX())
            setTranslateX(getTranslateX() + Character.SPEED * 1.5);
        if (getTranslateX() > Game.getGameWorld().getBooker().getTranslateX())
            setTranslateX(getTranslateX() - Character.SPEED * 1.5);
        if (getTranslateY() < Game.getGameWorld().getBooker().getTranslateY())
            setTranslateY(getTranslateY() + Character.SPEED * 1.5);
        if (getTranslateY() > Game.getGameWorld().getBooker().getTranslateY())
            setTranslateY(getTranslateY() - Character.SPEED * 1.5);
    }
}
