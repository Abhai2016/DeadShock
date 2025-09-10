package com.abhai.deadshock.weapons.bullets;

import com.abhai.deadshock.Game;
import com.abhai.deadshock.characters.enemies.Enemy;
import com.abhai.deadshock.types.DifficultyType;
import com.abhai.deadshock.types.WeaponType;
import com.abhai.deadshock.utils.GameMedia;

public class EnemyBullet extends Bullet {
    private int damage;

    public EnemyBullet() {}

    public void setDifficultyType() {
        switch (Game.getGameWorld().getDifficultyType()) {
            case DifficultyType.MARIK -> damage = 2;
            case DifficultyType.EASY -> damage = 3;
            case DifficultyType.MEDIUM -> damage = 5;
            case DifficultyType.HARD -> damage = 7;
            case DifficultyType.HARDCORE -> damage = 10;
        }
    }

    private void intersectsWithCharacters() {
        if (getBoundsInParent().intersects(Game.getGameWorld().getBooker().getBoundsInParent())) {
            Game.getGameWorld().getBooker().setHp(Game.getGameWorld().getBooker().getHp() - damage);
            switch ((int) (Math.random() * 3)) {
                case 0 -> GameMedia.BOOKER_HIT.play(Game.getGameWorld().getMenu().getVoiceSlider().getValue() / 100);
                case 1 -> GameMedia.BOOKER_HIT_2.play(Game.getGameWorld().getMenu().getVoiceSlider().getValue() / 100);
                case 2 -> GameMedia.BOOKER_HIT_3.play(Game.getGameWorld().getMenu().getVoiceSlider().getValue() / 100);
            }
            delete = true;
            Game.getGameWorld().getGameRoot().getChildren().remove(this);
            return;
        }

        for (Enemy enemy : Game.getGameWorld().getEnemies())
            if (getBoundsInParent().intersects(enemy.getBoundsInParent())) {
                if (Game.getGameWorld().getDifficultyType() == DifficultyType.MARIK || Game.getGameWorld().getDifficultyType() == DifficultyType.EASY)
                    enemy.setHP(enemy.getHP() - damage);
                delete = true;
                Game.getGameWorld().getGameRoot().getChildren().remove(this);
                return;
            }
    }

    public void init(double scaleX, double x, double y) {
        super.init(WeaponType.PISTOL);
        if (scaleX > 0) {
            direction = true;
            setTranslateX(x + 67);
        } else {
            setTranslateX(x - 5);
            direction = false;
        }
        setTranslateY(y + 9);
    }

    @Override
    public void update() {
        if (direction)
            setTranslateX(getTranslateX() + speed);
        else
            setTranslateX(getTranslateX() - speed);

        intersectsWithWorld();
        intersectsWithCharacters();
    }
}