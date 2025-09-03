package com.abhai.deadshock.weapons.bullets;

import com.abhai.deadshock.menus.DifficultyLevel;
import com.abhai.deadshock.Game;
import com.abhai.deadshock.characters.enemies.Enemy;
import com.abhai.deadshock.utils.Sounds;
import com.abhai.deadshock.weapons.WeaponType;

public class EnemyBullet extends Bullet {
    private int damage;

    public EnemyBullet() {
    }

    public void setDifficultyLevel() {
        switch (Game.difficultyLevel) {
            case DifficultyLevel.MARIK -> damage = 2;
            case DifficultyLevel.EASY -> damage = 3;
            case DifficultyLevel.MEDIUM -> damage = 5;
            case DifficultyLevel.HARD -> damage = 7;
            case DifficultyLevel.HARDCORE -> damage = 10;
        }
    }

    private void intersectsWithCharacters() {
        if (getBoundsInParent().intersects(Game.booker.getBoundsInParent())) {
            Game.booker.setHP(Game.booker.getHP() - damage);
            switch ((int) (Math.random() * 3)) {
                case 0 -> Sounds.bookerHit.play(Game.menu.getVoiceSlider().getValue() / 100);
                case 1 -> Sounds.bookerHit2.play(Game.menu.getVoiceSlider().getValue() / 100);
                case 2 -> Sounds.bookerHit3.play(Game.menu.getVoiceSlider().getValue() / 100);
            }
            delete = true;
            Game.gameRoot.getChildren().remove(this);
        }

        for (Enemy enemy : Game.enemies)
            if (getBoundsInParent().intersects(enemy.getBoundsInParent())) {
                if (Game.difficultyLevel == DifficultyLevel.MARIK || Game.difficultyLevel == DifficultyLevel.EASY)
                    enemy.setHP(enemy.getHP() - damage);
                delete = true;
                Game.gameRoot.getChildren().remove(this);
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