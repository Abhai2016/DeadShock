package com.abhai.deadshock.weapons.bullets;

import com.abhai.deadshock.Game;
import com.abhai.deadshock.characters.enemies.Enemy;
import com.abhai.deadshock.characters.enemies.EnemyType;
import com.abhai.deadshock.utils.Sounds;
import com.abhai.deadshock.utils.SpriteAnimation;
import com.abhai.deadshock.weapons.WeaponType;
import com.abhai.deadshock.world.levels.Block;
import com.abhai.deadshock.world.levels.BlockType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.util.Duration;

import java.nio.file.Paths;

public class Bullet extends Pane {
    private static final int RPG_SPEED = 10;
    private static final int BULLET_SPEED = 15;
    private static final int EXPLOSION_SIZE = 128;
    private static final Image RPG_IMAGE = new Image(Paths.get("resources", "images", "weapons", "rpgBullet.png").toUri().toString());
    private static final Image BULLET_IMAGE = new Image(Paths.get("resources", "images", "weapons", "bullet.png").toUri().toString());

    private WeaponType type;
    private boolean exploding;
    private boolean bossDamagedByRPG;
    private final ImageView bulletImageView;
    private final ImageView explosionImageView;
    private final SpriteAnimation explosionAnimation;

    protected int speed;
    protected boolean delete;
    protected boolean direction;

    public Bullet() {
        bulletImageView = new ImageView(BULLET_IMAGE);
        getChildren().add(bulletImageView);

        explosionImageView = new ImageView(new Image(Paths.get("resources", "images", "weapons", "explosion.png").toUri().toString()));
        explosionImageView.setFitWidth(EXPLOSION_SIZE);
        explosionImageView.setFitHeight(EXPLOSION_SIZE);
        explosionAnimation = new SpriteAnimation(explosionImageView, Duration.seconds(1), 16, 4, 0, 0, EXPLOSION_SIZE, EXPLOSION_SIZE);
        explosionAnimation.setCycleCount(1);
    }

    private void createExplosion() {
        exploding = true;
        getChildren().remove(bulletImageView);
        getChildren().add(explosionImageView);
        explosionAnimation.play();

        Sounds.rpgExplosion.play(Game.menu.getFxSlider().getValue() / 100);
        setTranslateX(getTranslateX() - explosionImageView.getFitWidth() / 2);
        setTranslateY(getTranslateY() - explosionImageView.getFitHeight() / 2);
        explosionAnimation.setOnFinished(event -> {
            delete = true;
            explosionAnimation.stop();
            getChildren().add(bulletImageView);
            getChildren().remove(explosionImageView);
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
                bulletImageView.setImage(BULLET_IMAGE);
                setTranslateY(Game.booker.getTranslateY() + 14);
            }
            case WeaponType.MACHINE_GUN -> {
                speed = BULLET_SPEED;
                bulletImageView.setImage(BULLET_IMAGE);
                setTranslateY(Game.booker.getTranslateY() + 20);
            }
            case WeaponType.RPG -> {
                speed = RPG_SPEED;
                bulletImageView.setImage(RPG_IMAGE);
                setTranslateY(Game.booker.getTranslateY() + 10);
            }
        }
        Game.gameRoot.getChildren().add(this);
    }

    private void intersectsWithEnemies() {
        for (Enemy enemy : Game.enemies)
            if (getBoundsInParent().intersects(enemy.getBoundsInParent())) {
                if (exploding) {
                    if (enemy.getType() != EnemyType.BOSS)
                        enemy.setHP(enemy.getHP() - Game.booker.getWeapon().getRpgDamage());
                    else if (!bossDamagedByRPG) {
                        enemy.setHP(enemy.getHP() - Game.booker.getWeapon().getRpgDamage());
                        bossDamagedByRPG = true;
                    }
                } else {
                    enemy.playHitVoice();
                    if (type == WeaponType.RPG)
                        createExplosion();
                    else {
                        delete = true;
                        Game.gameRoot.getChildren().remove(this);
                        enemy.setHP((enemy.getHP() - Game.booker.getWeapon().getBulletDamage()));
                    }
                }
                return;
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

    public void update() {
        if (exploding) {
            if (getBoundsInParent().intersects(Game.booker.getBoundsInParent()))
                Game.booker.setHP(Game.booker.getHP() - Game.booker.getWeapon().getRpgDamage());
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