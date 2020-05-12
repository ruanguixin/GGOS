package com.ggos.hero;

import com.ggos.util.MyUtil;

import java.awt.*;

public class MyHero extends Hero {

    private static Image[] heroImg;

    static {
        heroImg = new Image[4];
        heroImg[0] = MyUtil.createImage("res/FukaUp.Png");
        heroImg[1] = MyUtil.createImage("res/FukaDown.Png");
        heroImg[2] = MyUtil.createImage("res/FukaLeft.Png");
        heroImg[3] = MyUtil.createImage("res/FukaRight.Png");
    }
    public MyHero(int x, int y, int dir) {
        super(x, y, dir);
    }

    @Override
    public void drawImgHero(Graphics g) {
        g.drawImage(heroImg[getDir()], getX() - RADIUS, getY() - RADIUS, null);
    }

    @Override
    public void draw(Graphics g) {
        super.draw(g);
        drawName(g);
    }


}
