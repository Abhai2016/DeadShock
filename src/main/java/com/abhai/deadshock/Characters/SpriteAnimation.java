package com.abhai.deadshock.Characters;

import javafx.animation.Animation;
import javafx.animation.Interpolator;
import javafx.animation.Transition;
import javafx.geometry.Rectangle2D;
import javafx.scene.image.ImageView;
import javafx.util.Duration;

public class SpriteAnimation extends Transition {
    private final ImageView imgView;
    private byte count;
    private byte columns;
    private short offSetX;
    private short offSetY;
    private short width;
    private short height;

    public SpriteAnimation(ImageView imgView, Duration duration, int count,
                    int columns, int offSetX, int offSetY, int width, int height) {
        this.imgView = imgView;
        this.count = (byte)count;
        this.columns = (byte)columns;
        this.offSetX = (short)offSetX;
        this.offSetY = (short)offSetY;
        this.width = (short)width;
        this.height = (short)height;
        setCycleDuration(duration);
        setCycleCount(Animation.INDEFINITE);
        setInterpolator(Interpolator.LINEAR);
        imgView.setViewport( new Rectangle2D(offSetX, offSetY, width, height) );
    }

    @Override
    protected void interpolate(double k) {
        short index = (short)Math.min( Math.floor(k * count), count - 1 );
        int x = (index % columns) * width + offSetX;
        int y = (index / columns) * height + offSetY;
        imgView.setViewport( new Rectangle2D(x, y, width, height) );
    }
}
