package com.ggos.game;

import com.ggos.hero.Hero;
import com.ggos.util.Constant;

import java.awt.*;

/**
 * 子弹类
 */
public class Bullet {
    public static final int DEFAULT_SPEED = Hero.DEFAULT_SPEED << 2;

    //子弹半径
    public static final int RADIUS = 2;

    private int x, y;
    private int speed = DEFAULT_SPEED;
    private int dir;
    private int atk;
    private Color color;

    //子弹是否课件
    private boolean visible = true;

    public Bullet(int x, int y, int dir, int atk, Color color) {
        this.x = x;
        this.y = y;
        this.dir = dir;
        this.atk = atk;
        this.color = color;
    }


    public void draw(Graphics g) {
        if (!visible) return;
        logic();
        g.setColor(color);
        g.fillOval(x - RADIUS, y - RADIUS, RADIUS << 1, RADIUS << 1);
    }

    /**
     * 子弹的逻辑
     */

    private void logic() {
        move();
    }

    private void move() {
        switch (dir) {
            case Hero.DIR_UP:
                y -= speed;
                if (y <= 0) visible = false;
                break;
            case Hero.DIR_DOWN:
                y += speed;
                if (y > Constant.FRAME_HEIGHT) visible = false;
                break;
            case Hero.DIR_LEFT:
                x -= speed;
                if (x <= 0) visible = false;
                break;
            case Hero.DIR_RIGHT:
                if (x > Constant.FRAME_WIDTH) visible = false;
                x += speed;
                break;
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

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public int getDir() {
        return dir;
    }

    public void setDir(int dir) {
        this.dir = dir;
    }

    public int getAtk() {
        return atk;
    }

    public void setAtk(int atk) {
        this.atk = atk;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    //给对象池使用，所有属性都是默认值
    public Bullet() {
    }
}
