package com.abhai.deadshock.weapons.bullets;

import com.abhai.deadshock.DifficultyLevel;
import com.abhai.deadshock.Game;
import com.abhai.deadshock.characters.enemies.Enemy;
import com.abhai.deadshock.characters.enemies.EnemyType;
import com.abhai.deadshock.levels.Block;
import com.abhai.deadshock.levels.BlockType;
import com.abhai.deadshock.utils.Sounds;
import com.abhai.deadshock.utils.SpriteAnimation;
import com.abhai.deadshock.weapons.WeaponType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.util.Duration;

import java.nio.file.Paths;

public class Bullet extends Pane {
    private static final int RPG_SPEED = 10;
    private static final int BULLET_SPEED = 15;
    private static final Image RPG_IMAGE = new Image(Paths.get("resources", "images", "weapons", "rpgBullet.png").toUri().toString());
    protected static final Image BULLET_IMAGE = new Image(Paths.get("resources", "images", "weapons", "bullet.png").toUri().toString());

    private WeaponType type;
    private boolean exploding;
    private boolean bossDamagedByRPG;
    private final ImageView explosion;
    private final SpriteAnimation explosionAnimation;

    protected int speed;
    protected int damage;
    protected boolean delete;
    protected boolean direction;
    protected final ImageView bullet;

    public Bullet() {
        bullet = new ImageView();
        getChildren().add(bullet);

        explosion = new ImageView(new Image(Paths.get("resources", "images", "weapons", "explosion.png").toUri().toString()));
        explosion.setFitWidth(128);
        explosion.setFitHeight(128);
        explosionAnimation = new SpriteAnimation(explosion, Duration.seconds(1), 16, 4, 0, 0, 128, 128);
        explosionAnimation.setCycleCount(1);
    }

    private void createExplosion() {
        exploding = true;
        getChildren().remove(bullet);
        getChildren().add(explosion);
        explosionAnimation.play();

        Sounds.rpgExplosion.play(Game.menu.getFxSlider().getValue() / 100);
        explosion.setTranslateX(getTranslateX() - explosion.getFitWidth() / 2);
        explosion.setTranslateY(getTranslateY() - explosion.getFitHeight() / 2);
        explosionAnimation.setOnFinished(event -> {
            delete = true;
            explosionAnimation.stop();
            getChildren().add(bullet);
            getChildren().remove(explosion);
            Game.gameRoot.getChildren().remove(this);
        });
    }

    public void init(WeaponType type) {
        delete = false;
        this.type = type;
        direction = true;
        exploding = false;
        bossDamagedByRPG = false;

        if (Game.booker.getScaleX() < 0) {
            direction = false;
            setScaleX(-1);
        }

        if (direction)
            setTranslateX(Game.booker.getTranslateX() + Game.booker.getWidth());
        else
            setTranslateX(Game.booker.getTranslateX());

        switch (this.type) {
            case WeaponType.PISTOL -> {
                speed = BULLET_SPEED;
                bullet.setImage(BULLET_IMAGE);
                setDifficultyLevelForBullet();
                setTranslateY(Game.booker.getTranslateY() + 14);
            }
            case WeaponType.MACHINE_GUN -> {
                speed = BULLET_SPEED;
                bullet.setImage(BULLET_IMAGE);
                setDifficultyLevelForBullet();
                setTranslateY(Game.booker.getTranslateY() + 20);
            }
            case WeaponType.RPG -> {
                speed = RPG_SPEED;
                bullet.setImage(RPG_IMAGE);
                setDifficultyLevelForRpg();
                setTranslateY(Game.booker.getTranslateY() + 10);
            }
        }
        Game.gameRoot.getChildren().add(this);
    }

    private void intersectsWithEnemies() {
        if (exploding) {
            for (Enemy enemy : Game.enemies)
                if (explosion.getBoundsInParent().intersects(enemy.getBoundsInParent()))
                    if (enemy.getType() != EnemyType.BOSS)
                        enemy.setHP(enemy.getHP() - Game.weapon.getRpgDamage());
                    else if (!bossDamagedByRPG) {
                        enemy.setHP(enemy.getHP() - Game.weapon.getRpgDamage());
                        bossDamagedByRPG = true;
                        return;
                    }
        } else {
            for (Enemy enemy : Game.enemies)
                if (getBoundsInParent().intersects(enemy.getBoundsInParent())) {
                    enemy.playHitVoice();
                    if (type == WeaponType.RPG)
                        createExplosion();
                    else {
                        delete = true;
                        Game.gameRoot.getChildren().remove(this);
                        enemy.setHP((enemy.getHP() - Game.weapon.getDamage()));
                    }
                    return;
                }
        }
    }

    protected void intersectsWithWorld() {
        if (getTranslateX() > -Game.gameRoot.getLayoutX() + Game.appRoot.getWidth() || getTranslateX() < -Game.gameRoot.getLayoutX()) {
            delete = true;
            Game.gameRoot.getChildren().remove(this);
            return;
        }

        for (Block block : Game.level.getBlocks())
            if (getBoundsInParent().intersects(block.getBoundsInParent()) && block.getType() != BlockType.INVISIBLE) {
                if (type == WeaponType.RPG)
                    createExplosion();
                else {
                    delete = true;
                    Game.gameRoot.getChildren().remove(this);
                }
                return;
            }
    }

    public boolean isDelete() {
        return delete;
    }

    private void setDifficultyLevelForRpg() {
        switch (Game.difficultyLevel) {
            case DifficultyLevel.MARIK -> damage = 300;
            case DifficultyLevel.EASY -> damage = 250;
            case DifficultyLevel.MEDIUM -> damage = 200;
            case DifficultyLevel.HARD, DifficultyLevel.HARDCORE -> damage = 150;
        }
    }

    protected void setDifficultyLevelForBullet() {
        switch (Game.difficultyLevel) {
            case DifficultyLevel.MARIK -> damage = 30;
            case DifficultyLevel.EASY -> damage = 20;
            case DifficultyLevel.MEDIUM -> damage = 15;
            case DifficultyLevel.HARD -> damage = 10;
            case DifficultyLevel.HARDCORE -> damage = 8;
        }
    }

    public void update() {
        if (exploding) {
            if (explosion.getBoundsInParent().intersects(Game.booker.getBoundsInParent()))
                Game.booker.setHP(Game.booker.getHP() - Game.weapon.getRpgDamage());
            intersectsWithEnemies();
        } else {
            if (direction)
                setTranslateX(getTranslateX() + speed);
            else
                setTranslateX(getTranslateX() - speed);
            intersectsWithWorld();
            intersectsWithEnemies();
        }
    }
}