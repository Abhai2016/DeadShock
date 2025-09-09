package com.abhai.deadshock.hud;

import com.abhai.deadshock.Game;
import com.abhai.deadshock.types.DifficultyType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

import java.nio.file.Paths;

public class HUD extends Pane {
    private static final int BULLET_PANE_ARC = 15;
    private static final int RECTANGLE_WIDTH = 100;
    private static final int RECTANGLE_HEIGHT = 12;
    private static final int HP_TEXT_OFFSET_X = 51;
    private static final int HP_TEXT_OFFSET_Y = 12;
    private static final int SALT_TEXT_OFFSET_X = 40;
    private static final int SALT_TEXT_OFFSET_Y = 34;
    private static final int ENERGETIC_OFFSET_Y = 57;
    private static final int MONEY_TEXT_OFFSET_X = 24;
    private static final int MONEY_TEXT_OFFSET_Y = 38;
    private static final int BULLET_TEXT_OFFSET_X = 5;
    private static final int BULLET_TEXT_OFFSET_Y = 25;

    private ImageView money;
    private ImageView hypnosis;
    private ImageView devilKiss;
    private ImageView hpAndSalt;
    private ImageView electricity;

    private Text moneyText;
    private Text bulletText;
    private Pane bulletPane;

    private Rectangle HP;
    private Rectangle salt;

    private Image moneyForMarikImage;
    private Image moneyForOtherDifficultyLevelsImage;

    public HUD() {
        initializeMoneyPane();
        initializeBulletsPane();
        initializeHpAndSaltPane();
        initializeEnergeticsPane();

        Game.getAppRoot().getChildren().add(this);
        getChildren().addAll(HP, salt, hpAndSalt, devilKiss, electricity, hypnosis, bulletPane, money, moneyText);
    }

    public void setDifficultyLevel() {
        if (Game.getGameWorld().getDifficultyType() == DifficultyType.MARIK) {
            getChildren().remove(moneyText);
            money.setImage(moneyForMarikImage);
        } else if (!getChildren().contains(moneyText)) {
            getChildren().add(moneyText);
            money.setImage(moneyForOtherDifficultyLevelsImage);
        }
    }

    private void initializeMoneyPane() {
        moneyForMarikImage = new Image(Paths.get("resources", "images", "hud", "moneyForMarik.png").toUri().toString());
        moneyForOtherDifficultyLevelsImage = new Image(Paths.get("resources", "images", "hud", "money.png").toUri().toString());
        money = new ImageView(moneyForOtherDifficultyLevelsImage);
        money.setTranslateY(Game.SCENE_HEIGHT - money.getImage().getHeight());

        moneyText = new Text();
        moneyText.setFill(Color.PALEGOLDENROD);
        moneyText.setTranslateX(MONEY_TEXT_OFFSET_X);
        moneyText.setFont(Font.font("Arial", FontWeight.BOLD, 22));
        moneyText.setTranslateY(money.getTranslateY() + MONEY_TEXT_OFFSET_Y);
    }

    private void initializeBulletsPane() {
        Rectangle rect = new Rectangle(RECTANGLE_WIDTH, 30, Color.SKYBLUE);
        rect.setOpacity(0.5);
        rect.setArcWidth(BULLET_PANE_ARC);
        rect.setArcHeight(BULLET_PANE_ARC);

        bulletText = new Text();
        bulletText.setTranslateX(BULLET_TEXT_OFFSET_X);
        bulletText.setTranslateY(BULLET_TEXT_OFFSET_Y);
        bulletText.setFill(Color.WHITE);
        bulletText.setFont(Font.font("Arial", 24));

        bulletPane = new Pane();
        bulletPane.getChildren().addAll(rect, bulletText);
        bulletPane.setTranslateX(Game.SCENE_WIDTH - rect.getWidth());
    }

    public void updateMoneyTextPosition() {
        if (Game.getGameWorld().getBooker().getMoney() > 99)
            moneyText.setTranslateX(MONEY_TEXT_OFFSET_X);
        else if (Game.getGameWorld().getBooker().getMoney() > 9)
            moneyText.setTranslateX(MONEY_TEXT_OFFSET_X + 7);
        else if (Game.getGameWorld().getBooker().getMoney() < 10)
            moneyText.setTranslateX(MONEY_TEXT_OFFSET_X + 12);
    }

    private void initializeHpAndSaltPane() {
        HP = new Rectangle(RECTANGLE_WIDTH, RECTANGLE_HEIGHT, Color.RED);
        HP.setTranslateX(HP_TEXT_OFFSET_X);
        HP.setTranslateY(HP_TEXT_OFFSET_Y);

        salt = new Rectangle(RECTANGLE_WIDTH, RECTANGLE_HEIGHT, Color.BLUE);
        salt.setTranslateX(SALT_TEXT_OFFSET_X);
        salt.setTranslateY(SALT_TEXT_OFFSET_Y);

        hpAndSalt = new ImageView(new Image(Paths.get("resources",
                "images", "hud", "hpAndSalt.png").toUri().toString()));
    }

    private void initializeEnergeticsPane() {
        devilKiss = new ImageView(new Image(Paths.get("resources",
                "images", "hud", "devilKiss.png").toUri().toString()));
        devilKiss.setTranslateY(ENERGETIC_OFFSET_Y);
        devilKiss.setVisible(false);

        electricity = new ImageView(new Image(Paths.get("resources",
                "images", "hud", "electricity.png").toUri().toString()));
        electricity.setTranslateY(ENERGETIC_OFFSET_Y);
        electricity.setVisible(false);

        hypnosis = new ImageView(new Image(Paths.get("resources",
                "images", "hud", "hypnosis.png").toUri().toString()));
        hypnosis.setTranslateY(ENERGETIC_OFFSET_Y);
        hypnosis.setVisible(false);
    }

    public ImageView getHypnosis() {
        return hypnosis;
    }

    public ImageView getDevilKiss() {
        return devilKiss;
    }

    public ImageView getElectricity() {
        return electricity;
    }

    public void update() {
        HP.setWidth(Game.getGameWorld().getBooker().getHp());
        salt.setWidth(Game.getGameWorld().getBooker().getSalt());

        moneyText.setText(String.valueOf(Game.getGameWorld().getBooker().getMoney()));
        bulletText.setText(Game.getGameWorld().getBooker().getWeapon().getCurrentClip() +
                " / " + Game.getGameWorld().getBooker().getWeapon().getCurrentBullets());
    }
}