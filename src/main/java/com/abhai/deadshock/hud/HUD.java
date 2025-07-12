package com.abhai.deadshock.hud;

import com.abhai.deadshock.Game;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

import java.nio.file.Path;
import java.nio.file.Paths;

import static com.abhai.deadshock.levels.Block.BLOCK_SIZE;

public class HUD extends Pane {
    private Path devilKissImagePath = Paths.get("resources", "images", "hud", "devilKiss.png");
    private Path electricityImagePath = Paths.get("resources", "images", "hud", "electricity.png");
    private Path hypnosisImagePath = Paths.get("resources", "images", "hud", "hypnosis.png");

    private ImageView devilKiss = new ImageView(new Image(devilKissImagePath.toUri().toString()));
    private ImageView electricity = new ImageView(new Image(electricityImagePath.toUri().toString()));
    private ImageView hypnosis = new ImageView(new Image(hypnosisImagePath.toUri().toString()));
    private ImageView money;

    private Text textBullet = new Text();
    private Text textMoney = new Text();

    private Rectangle HP = new Rectangle(100, 12, Color.RED);
    private Rectangle salt = new Rectangle(100, 12, Color.BLUE);


    public HUD() {
        HP.setTranslateX(51);
        HP.setTranslateY(12);

        salt.setTranslateX(40);
        salt.setTranslateY(34);

        Rectangle rect = new Rectangle(110, 30, Color.SKYBLUE);

        rect.setOpacity(0.5);
        rect.setArcHeight(15);
        rect.setArcWidth(15);

        textBullet.setFont(Font.font("Arial", 28));
        textBullet.setFill(Color.WHITE);
        textBullet.setTranslateX(5);
        textBullet.setTranslateY(25);

        Pane bulletPane = new Pane();
        bulletPane.setTranslateX(1165);
        bulletPane.getChildren().addAll(rect, textBullet);

        Path moneyImagePath = Paths.get("resources", "images", "hud", "money.png");
        money = new ImageView(new Image(moneyImagePath.toUri().toString()));
        money.setTranslateX(0);
        money.setTranslateY(BLOCK_SIZE * 15 - BLOCK_SIZE * 1.5);

        textMoney.setFont(Font.font("Arial", FontWeight.BOLD , 22));
        textMoney.setFill(Color.PALEGOLDENROD);
        textMoney.setTranslateX(BLOCK_SIZE - 24);
        textMoney.setTranslateY(money.getTranslateY() + BLOCK_SIZE / 2 + 15);

        devilKiss.setTranslateY(57);
        devilKiss.setVisible(false);

        electricity.setTranslateY(57);
        electricity.setVisible(false);

        hypnosis.setTranslateY(57);
        hypnosis.setVisible(false);

        Path hpImagePath = Paths.get("resources", "images", "hud", "hpAndSalt.png");
        ImageView hp_and_salt = new ImageView(new Image(hpImagePath.toUri().toString()));
        getChildren().addAll(HP, salt, hp_and_salt, devilKiss, electricity, hypnosis, bulletPane, money, textMoney);
        Game.appRoot.getChildren().add(this);
        setVisible(false);
    }



    public Text getTextMoney() {
        return textMoney;
    }

    public ImageView getDevilKiss() {
        return devilKiss;
    }

    public ImageView getElectricity() {
        return electricity;
    }

    public ImageView getHypnosis() {
        return hypnosis;
    }

    public void setMarikLevel() {
        Path moneyImagePath = Paths.get("resources", "images", "hud", "moneyForMarik.png");
        money.setImage(new Image(moneyImagePath.toUri().toString()));
        getChildren().remove(textMoney);
    }

    public void update() {
        HP.setWidth(Game.booker.getHP());
        salt.setWidth(Game.booker.getSalt());

        textBullet.setText( String.valueOf( Game.weapon.getWeaponClip() )  + " / " + String.valueOf(Game.weapon.getBullets()) );
        textMoney.setText( String.valueOf(Game.booker.getMoney()) );

        if (Game.booker.getMoney() > 99)
            textMoney.setTranslateX(BLOCK_SIZE - 24);
        else if (Game.booker.getMoney() > 9)
            textMoney.setTranslateX(BLOCK_SIZE - 17);
        else if (Game.booker.getMoney() < 10)
            textMoney.setTranslateX(BLOCK_SIZE - 12);
    }
}