package com.ggos.map;

import com.ggos.game.Bullet;
import com.ggos.util.BulletsPool;
import com.ggos.util.MyUtil;

import java.awt.*;
import java.util.List;

/**
 * 地图元素块
 */

public class MapTile {
    public static final int TYPE_ROCK = 0;
    public static final int TYPE_CRYSTAL = 3;
    public static final int TYPE_GRASS = 2;
    public static final int TYPE_HARD_ROCK = 1;


    private static Image[] tileImg;
    public static int tileW = 64; //TODO
    public static int radiusW = tileW >> 1;
    public static int tileH = 64;
    public static int radiusH = tileH >> 1;

    private int type = TYPE_HARD_ROCK;

    public int hp = DEFAULT_HP;
    private static final int DEFAULT_HP = 10000;

    static {
        tileImg = new Image[4];
        tileImg[TYPE_ROCK] = MyUtil.createImage("res/rock.png");
        tileImg[TYPE_CRYSTAL] = MyUtil.createImage("res/crystal.png");
        tileImg[TYPE_GRASS] = MyUtil.createImage("res/grass.png");
        tileImg[TYPE_HARD_ROCK] = MyUtil.createImage("res/hard_rock.png");


        if (tileW <= 0) {
            tileW = tileImg[TYPE_ROCK].getWidth(null);
            tileH = tileImg[TYPE_ROCK].getHeight(null);
            }
    }
    //图片资源的左上角
    private int x, y;
    private boolean visible = true;

    //判断当前地图块是否为水晶

    public MapTile(int x, int y) {
        this.x = x;
        this.y = y;
        if (tileW <= 0) {
            tileW = tileImg[TYPE_ROCK].getWidth(null);
            tileH = tileImg[TYPE_ROCK].getHeight(null);
        }
    }

    public MapTile() {
    }

    public void draw(Graphics g) {
        if (!visible) return;
        if (tileW <= 0) {
            tileW = tileImg[type].getWidth(null);
            tileH = tileImg[type].getHeight(null);
        }
        g.drawImage(tileImg[type], x, y, null);

        if (isCrystal()) {
            new BloodBar().draw(g);
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

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }


    /**
     * 地图块和若干个子弹是否有碰撞
     * @param bullets
     * @return
     */
    public boolean isCollideBullet(List<Bullet> bullets) {
        if (!visible || type == TYPE_GRASS) return false;
        for (int i = 0; i < bullets.size(); i++) {
            Bullet bullet = bullets.get(i);
            int bulletX = bullet.getX();
            int bulletY = bullet.getY();
            boolean collide = MyUtil.isCollide(x + radiusW, y + radiusH, radiusW, bulletX, bulletY);
            if (collide) {
                //子弹的销毁
                bullet.setVisible(false);
                BulletsPool.theReturn(bullet);
                return true;
            }
        }
        return false;
    }


    //判断当前的地图块是否为水晶
    public boolean isCrystal() {
        return type == TYPE_CRYSTAL;
    }

    public void crystalHurt(Bullet bullet) {
        //TODO
        int atk = bullet.getAtk();
        hp -= atk;
        if (hp < 0) {
            hp = 0;
            MyUtil.delaySecondsToOver(1000);
        }
    }

    class BloodBar {
        public static final int BAR_LENGTH = 64;
        public static final int BAR_HEIGHT = 5;

        public void draw(Graphics g) {
            //填充底色
            g.setColor(Color.YELLOW);
            g.fillRect(x, y - BAR_HEIGHT * 2, BAR_LENGTH, BAR_HEIGHT);
            //当前血量
            g.setColor(Color.BLUE);
            g.fillRect(x, y - BAR_HEIGHT * 2, hp * BAR_LENGTH / DEFAULT_HP, BAR_HEIGHT);

            //边框
            g.setColor(Color.WHITE);
            g.drawRect(x, y - BAR_HEIGHT * 2, BAR_LENGTH, BAR_HEIGHT);
        }
    }
}