package com.abhai.deadshock.utils;

import javafx.animation.Animation;
import javafx.animation.Interpolator;
import javafx.animation.Transition;
import javafx.geometry.Rectangle2D;
import javafx.scene.image.ImageView;
import javafx.util.Duration;

public class SpriteAnimation extends Transition {
    private int offSetY;
    private final int count;
    private final int width;
    private final int height;
    private final int offSetX;
    private final int columns;
    private final ImageView imageView;

    public SpriteAnimation(ImageView imgView, Duration duration, int count, int columns, int offSetX, int offSetY, int width, int height) {
        this.count = count;
        this.width = width;
        imageView = imgView;
        this.height = height;
        this.columns = columns;
        this.offSetX = offSetX;
        this.offSetY = offSetY;
        setCycleDuration(duration);
        setCycleCount(Animation.INDEFINITE);
        setInterpolator(Interpolator.LINEAR);
        imageView.setViewport(new Rectangle2D(offSetX, offSetY, width, height));
    }

    @Override
    protected void interpolate(double k) {
        int index = (int) Math.min(Math.floor(k * count), count - 1);
        int x = (index % columns) * width + offSetX;
        int y = (index / columns) * height + offSetY;
        imageView.setViewport(new Rectangle2D(x, y, width, height));
    }

    public void setOffsetY(int offSetY) {
        this.offSetY = offSetY;
    }
}
