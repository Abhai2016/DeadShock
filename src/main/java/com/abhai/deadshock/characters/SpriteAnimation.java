package com.abhai.deadshock.characters;

import javafx.animation.Animation;
import javafx.animation.Interpolator;
import javafx.animation.Transition;
import javafx.geometry.Rectangle2D;
import javafx.scene.image.ImageView;
import javafx.util.Duration;

public class SpriteAnimation extends Transition {
    private final ImageView imgView;
    private int count;
    private int columns;
    private int offSetX;
    private int offSetY;
    private int width;
    private int height;

    public SpriteAnimation(ImageView imgView, Duration duration, int count,
                    int columns, int offSetX, int offSetY, int width, int height) {
        this.imgView = imgView;
        this.count = count;
        this.columns = columns;
        this.offSetX = offSetX;
        this.offSetY = offSetY;
        this.width = width;
        this.height = height;
        setCycleDuration(duration);
        setCycleCount(Animation.INDEFINITE);
        setInterpolator(Interpolator.LINEAR);
        imgView.setViewport( new Rectangle2D(offSetX, offSetY, width, height) );
    }

    @Override
    protected void interpolate(double k) {
        int index = (int) Math.min( Math.floor(k * count), count - 1 );
        int x = (index % columns) * width + offSetX;
        int y = (index / columns) * height + offSetY;
        imgView.setViewport( new Rectangle2D(x, y, width, height) );
    }
}
