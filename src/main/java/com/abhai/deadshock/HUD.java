package com.abhai.deadshock;

import com.abhai.deadshock.Levels.Level;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

public class HUD extends Pane {
    private ImageView devilKiss = new ImageView("file:/../images/hud/devil_kiss.png");
    private ImageView electricity = new ImageView("file:/../images/hud/electricity.png");
    private ImageView hypnotist = new ImageView("file:/../images/hud/hypnotist.png");
    private ImageView money = new ImageView("file:/../images/hud/money.png");

    private Text textBullet = new Text();
    private Text textMoney = new Text();

    private Rectangle HP = new Rectangle(100, 12, Color.RED);
    private Rectangle salt = new Rectangle(100, 12, Color.BLUE);


    HUD() {
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

        money = new ImageView("file:/../images/hud/money.png");
        money.setTranslateX(0);
        money.setTranslateY(Level.BLOCK_SIZE * 15 - Level.BLOCK_SIZE * 1.5);

        textMoney.setFont(Font.font("Arial", FontWeight.BOLD , 22));
        textMoney.setFill(Color.PALEGOLDENROD);
        textMoney.setTranslateX(Level.BLOCK_SIZE - 24);
        textMoney.setTranslateY(money.getTranslateY() + Level.BLOCK_SIZE / 2 + 15);

        devilKiss.setTranslateY(57);
        devilKiss.setVisible(false);

        electricity.setTranslateY(57);
        electricity.setVisible(false);

        hypnotist.setTranslateY(57);
        hypnotist.setVisible(false);

        ImageView hp_and_salt = new ImageView("file:/../images/hud/HP and Salt.png");
        getChildren().addAll(HP, salt, hp_and_salt, devilKiss, electricity, hypnotist, bulletPane, money, textMoney);
        Game.appRoot.getChildren().add(this);
        setVisible(false);
    }



    Text getTextMoney() {
        return textMoney;
    }

    public ImageView getDevilKiss() {
        return devilKiss;
    }

    public ImageView getElectricity() {
        return electricity;
    }

    public ImageView getHypnotist() {
        return hypnotist;
    }


    void setMarikLevel() {
        money.setImage(new Image("file:/../images/hud/moneyForMarik.png"));
        getChildren().remove(textMoney);
    }


    public void update() {
        HP.setWidth(Game.booker.getHP());
        salt.setWidth(Game.booker.getSalt());

        textBullet.setText( String.valueOf( Game.weapon.getWeaponClip() )  + " / " + String.valueOf(Game.weapon.getBullets()) );
        textMoney.setText( String.valueOf(Game.booker.getMoney()) );

        if (Game.booker.getMoney() > 99)
            textMoney.setTranslateX(Level.BLOCK_SIZE - 24);
        else if (Game.booker.getMoney() > 9)
            textMoney.setTranslateX(Level.BLOCK_SIZE - 17);
        else if (Game.booker.getMoney() < 10)
            textMoney.setTranslateX(Level.BLOCK_SIZE - 12);
    }
}