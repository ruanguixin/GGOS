package com.ggos.game;

import com.ggos.util.MyUtil;

import java.awt.*;

/**
 * 用来控制死亡效果的类
 */

public class Die {
    public static final int DIE_FRAME_COUNT = 12;

    //导入资源
    private static Image[] img;

    //死亡效果的宽度和高度
    private static int dieWidth;
    private static int dieHeight;

    static {
        img = new Image[DIE_FRAME_COUNT / 3];
        for (int i = 0; i < img.length; i++) {
            img[i] = MyUtil.createImage("res/Bleed_" + i + ".png");
        }
    }


    //死亡效果属性
    private int x, y;
    //当前播放的帧的下标[0-11]
    private int index;

    private boolean visible = true;

    public Die() {
    }

    public Die(int x, int y) {
        this.x = x;
        this.y = y;
        index = 0;
    }


    public void draw(Graphics g) {
        //爆炸效果图片的宽高获取
        if (dieHeight <= 0) {
            dieWidth = img[0].getWidth(null) >> 1;
            dieHeight = img[0].getHeight(null) >> 1;
        }

        if (!visible) return;
        g.drawImage(img[index / 3], x - dieWidth, y - dieHeight ,null);
        index++;
        if (index >= DIE_FRAME_COUNT) {
            visible = false;
        }
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }
}
