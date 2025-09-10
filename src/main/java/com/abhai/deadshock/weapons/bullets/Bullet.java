package com.abhai.deadshock.weapons.bullets;

import com.abhai.deadshock.Game;
import com.abhai.deadshock.characters.enemies.Enemy;
import com.abhai.deadshock.types.BlockType;
import com.abhai.deadshock.types.EnemyType;
import com.abhai.deadshock.types.WeaponType;
import com.abhai.deadshock.utils.GameMedia;
import com.abhai.deadshock.utils.SpriteAnimation;
import com.abhai.deadshock.world.levels.Block;
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

        GameMedia.RPG_EXPLOSION.play(Game.getGameWorld().getMenu().getFxSlider().getValue() / 100);
        setTranslateX(getTranslateX() - explosionImageView.getFitWidth() / 2);
        setTranslateY(getTranslateY() - explosionImageView.getFitHeight() / 2);
        explosionAnimation.setOnFinished(event -> {
            delete = true;
            explosionAnimation.stop();
            getChildren().add(bulletImageView);
            getChildren().remove(explosionImageView);
            Game.getGameWorld().getAppRoot().getChildren().remove(this);
        });
    }

    public void init(WeaponType type) {
        delete = false;
        this.type = type;
        direction = true;
        exploding = false;
        bossDamagedByRPG = false;

        if (Game.getGameWorld().getBooker().getScaleX() < 0) {
            direction = false;
            setScaleX(-1);
        }

        if (direction)
            setTranslateX(Game.getGameWorld().getBooker().getTranslateX() + Game.getGameWorld().getBooker().getWidth());
        else
            setTranslateX(Game.getGameWorld().getBooker().getTranslateX());

        switch (this.type) {
            case WeaponType.PISTOL -> {
                speed = BULLET_SPEED;
                bulletImageView.setImage(BULLET_IMAGE);
                setTranslateY(Game.getGameWorld().getBooker().getTranslateY() + 14);
            }
            case WeaponType.MACHINE_GUN -> {
                speed = BULLET_SPEED;
                bulletImageView.setImage(BULLET_IMAGE);
                setTranslateY(Game.getGameWorld().getBooker().getTranslateY() + 20);
            }
            case WeaponType.RPG -> {
                speed = RPG_SPEED;
                bulletImageView.setImage(RPG_IMAGE);
                setTranslateY(Game.getGameWorld().getBooker().getTranslateY() + 10);
            }
        }
        Game.getGameWorld().getGameRoot().getChildren().add(this);
    }

    private void intersectsWithEnemies() {
        for (Enemy enemy : Game.getGameWorld().getEnemies())
            if (getBoundsInParent().intersects(enemy.getBoundsInParent())) {
                if (exploding) {
                    if (enemy.getType() != EnemyType.BOSS)
                        enemy.setHP(enemy.getHP() - Game.getGameWorld().getBooker().getWeapon().getRpgDamage());
                    else if (!bossDamagedByRPG) {
                        enemy.setHP(enemy.getHP() - Game.getGameWorld().getBooker().getWeapon().getRpgDamage());
                        bossDamagedByRPG = true;
                    }
                } else {
                    enemy.playHitVoice();
                    if (type == WeaponType.RPG)
                        createExplosion();
                    else {
                        delete = true;
                        Game.getGameWorld().getGameRoot().getChildren().remove(this);
                        enemy.setHP((enemy.getHP() - Game.getGameWorld().getBooker().getWeapon().getBulletDamage()));
                    }
                }
                return;
            }
    }

    protected void intersectsWithWorld() {
        if (getTranslateX() > -Game.getGameWorld().getGameRoot().getLayoutX() + Game.SCENE_WIDTH || getTranslateX() < -Game.getGameWorld().getGameRoot().getLayoutX()) {
            delete = true;
            Game.getGameWorld().getGameRoot().getChildren().remove(this);
            return;
        }

        for (Block block : Game.getGameWorld().getLevel().getBlocks())
            if (getBoundsInParent().intersects(block.getBoundsInParent()) && block.getType() != BlockType.INVISIBLE) {
                if (type == WeaponType.RPG)
                    createExplosion();
                else {
                    delete = true;
                    Game.getGameWorld().getGameRoot().getChildren().remove(this);
                }
                return;
            }
    }

    public boolean isDelete() {
        return delete;
    }

    public void update() {
        if (exploding) {
            if (getBoundsInParent().intersects(Game.getGameWorld().getBooker().getBoundsInParent()))
                Game.getGameWorld().getBooker().setHp(Game.getGameWorld().getBooker().getHp() - Game.getGameWorld().getBooker().getWeapon().getRpgDamage());
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