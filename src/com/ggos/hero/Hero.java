package com.ggos.hero;

import com.ggos.game.Bullet;
import com.ggos.game.Die;
import com.ggos.game.GameFrame;
import com.ggos.map.MapTile;
import com.ggos.util.*;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 英雄类
 */

public abstract class Hero {

    public static final int DIR_UP = 0;
    public static final int DIR_DOWN = 1;
    public static final int DIR_LEFT = 2;
    public static final int DIR_RIGHT = 3;

    //半径
    public static final int RADIUS = 16;
    //默认速度 每帧 30ms
    public static final int DEFAULT_SPEED = 4;

    //英雄的状态
    public static final int STATE_STAND = 0;
    public static final int STATE_MOVE = 1;
    public static final int STATE_DIE = 2;
    //英雄的初始生命
    public static final int DEFAULT_HP = 500;
    public int maxHP = DEFAULT_HP;


    private int x, y;
    private int hp = DEFAULT_HP;
    private String name;
    private int atk;
    public static final int ATK_MAX = 100;
    public static final int ATK_MIN = 90;
    private int speed = DEFAULT_SPEED;
    private int dir;
    private int state = STATE_STAND;
    private Color color;
    private boolean isEnemy = false;

    private BloodBar bar = new BloodBar();

    //子弹
    private List<Bullet> bullets = new ArrayList<>();
    //使用容器来保存当前英雄下所有的死亡效果
    private List<Die> dies = new ArrayList<>();


    public Hero(int x, int y, int dir) {
        this.x = x;
        this.y = y;
        this.dir = dir;
        initHero();
    }

    public Hero() {
        initHero();
    }

    private void initHero() {
        color = MyUtil.getRandomColor();
        name = MyUtil.getRandomName();
        atk = MyUtil.getRandomNumber(ATK_MIN, ATK_MAX);
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

    public int getHp() {
        return hp;
    }

    public void setHp(int hp) {
        this.hp = hp;
    }

    public int getAtk() {
        return atk;
    }

    public void setAtk(int atk) {
        this.atk = atk;
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

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public List getBullets() {
        return bullets;
    }

    public void setBullets(List bullets) {
        this.bullets = bullets;
    }

    public boolean isEnemy() {
        return isEnemy;
    }

    public void setEnemy(boolean enemy) {
        isEnemy = enemy;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getMaxHP() {
        return maxHP;
    }

    public void setMaxHP(int maxHP) {
        this.maxHP = maxHP;
    }

    /**
     * 绘制英雄
     *
     * @param g
     */
    public void draw(Graphics g) {
        logic();

        drawImgHero(g);

        drawBullets(g);

        bar.draw(g);
    }

    public void drawName(Graphics g) {
        g.setColor(Color.PINK);
        g.setFont(Constant.SMALL_FONT);
        g.drawString(name, x - RADIUS, y - 30);
    }

    /**
     * 使用图片的方式去绘制
     *
     * @param g
     */

    public abstract void drawImgHero(Graphics g);

    /**
     * 使用系统的方式去绘制
     *
     * @param g
     */

    private void drawHero(Graphics g) {
        g.setColor(color);
        //绘制英雄代表圆
        g.fillOval(x - RADIUS, y - RADIUS, RADIUS << 1, RADIUS << 1);
        int endX = x;
        int endY = y;
        switch (dir) {
            case DIR_UP:
                endY = y - RADIUS * 2;
                break;
            case DIR_DOWN:
                endY = y + RADIUS * 2;
                break;
            case DIR_LEFT:
                endX = x - RADIUS * 2;
                break;
            case DIR_RIGHT:
                endX = x + RADIUS * 2;
                break;
        }
        g.drawLine(x, y, endX, endY);
    }

    //英雄的逻辑处理
    private void logic() {
        switch (state) {
            case STATE_STAND:
                break;
            case STATE_MOVE:
                move();
                break;
            case STATE_DIE:
                break;
        }
    }

    //英雄移动的功能
    private int oldX = -1, oldY = -1;
    private void move() {
        oldX = x;
        oldY = y;
        switch (dir) {
            case DIR_UP:
                y -= speed;
                if (y < RADIUS + GameFrame.titleBarH) {
                    y = RADIUS + GameFrame.titleBarH;
                }
                break;
            case DIR_DOWN:
                y += speed;
                if (y > Constant.FRAME_HEIGHT - RADIUS) {
                    y = Constant.FRAME_HEIGHT - RADIUS;
                }
                break;
            case DIR_LEFT:
                x -= speed;
                if (x < RADIUS) {
                    x = RADIUS;
                }
                break;
            case DIR_RIGHT:
                x += speed;
                if (x > Constant.FRAME_WIDTH - RADIUS) {
                    x = Constant.FRAME_WIDTH - RADIUS;
                }
                break;
        }
    }

    /**
     * 英雄开火的方法
     * 创建一个子弹对象，子弹对象的属性信息通过英雄的信息获得
     * 然后将创建的子弹添加到英雄管理的容器中
     */
    //上一次射击时间
    private long fireTime;
    //子弹发射间隔
    public static final int FIRE_INTERVAL = 200;

    public void fire() {
        if (System.currentTimeMillis() - fireTime >= FIRE_INTERVAL) {
            int bulletX = x;
            int bulletY = y;
            switch (dir) {
                case DIR_UP:
                    bulletY -= RADIUS;
                    break;
                case DIR_DOWN:
                    bulletY += RADIUS;
                    break;
                case DIR_LEFT:
                    bulletX -= RADIUS;
                    break;
                case DIR_RIGHT:
                    bulletX += RADIUS;
                    break;
            }

            //从对象池中获取子弹对象
            Bullet bullet = BulletsPool.get();
            //设置子弹属性
            bullet.setX(bulletX);
            bullet.setY(bulletY);
            bullet.setDir(dir);
            bullet.setAtk(atk);
            bullet.setColor(Color.ORANGE);
            bullet.setVisible(true);

            bullets.add(bullet);

            //射击后，记录此次发射时间
            fireTime = System.currentTimeMillis();
            MusicUtil.playBomb();
        }

    }

    /**
     * 将当前英雄的发射的所有子弹绘制出来
     *
     * @param g
     */
    private void drawBullets(Graphics g) {
        for (Bullet bullet : bullets) {
            bullet.draw(g);
        }
        //遍历所有的子弹，将不可见的子弹移除，并还原回对象池
        for (int i = 0; i < bullets.size(); i++) {
            Bullet bullet = bullets.get(i);
            if (!bullet.isVisible()) {
                Bullet remove = bullets.remove(i);
                i--;
                BulletsPool.theReturn(remove);
            }
        }
    }

    /**
     * 英雄销毁时处理所有的子弹
     */

    public void bulletsReturn() {
        for (Bullet bullet : bullets) {
            BulletsPool.theReturn(bullet);
        }
        bullets.clear();
    }


    //hero和子弹碰撞的方法

    public void collideBullets(List<Bullet> bullets) {
        //遍历所有的子弹，一次和当前英雄进行碰撞检测
        for (Bullet bullet : bullets) {
            int bulletX = bullet.getX();
            int bulletY = bullet.getY();

            //子弹和英雄碰上了
            if (MyUtil.isCollide(x, y, RADIUS, bullet.getX(), bullet.getY())) {
                //子弹消失
                bullet.setVisible(false);
                //英雄受到伤害
                hurt(bullet);
                //添加击中效果
                addDie(x, y);
            }
        }
    }

    public void addDie(int x, int y) {
        //添加死亡效果
        Die die = DiesPool.get();
        die.setX(x);
        die.setY(y);
        die.setVisible(true);
        die.setIndex(0);
        dies.add(die);
    }

    //英雄受到伤害
    private void hurt(Bullet bullet) {
        //TODO
        int atk = bullet.getAtk();
        hp -= atk;
        if (hp < 0) {
            hp = 0;
            disappear();
        }
    }

    private void disappear() {
        if (isEnemy) {
            GameFrame.killEnemyCount++;
            //敌人被消灭，归还对象池
            HeroesPool.theReturn(this);
            //本关是否结束
            if (GameFrame.isCrossLevel()) {
                //判断游戏是 否通关
                if (GameFrame.isLastLevel()) {
                    //通关了
                    GameFrame.setGameState(Constant.STATE_WIN);
                    MusicUtil.playWin();
                } else{
                    //TODO 进入下一关
                    GameFrame.nextLevel();
                }
            }
        } else {
            MyUtil.delaySecondsToOver(1000);
        }

    }

    public boolean isDisappear() {
        return hp <= 0;
    }

    /**绘制当前英雄所有死亡效果
     * 绘制所有死亡的效果
     * @param g
     */
    public void drawDies(Graphics g) {
        for (Die die : dies) {
            die.draw(g);
        }

        //将不可见的死亡效果删除，还回对象池

        for (int i = 0; i < dies.size(); i++) {
            Die die = dies.get(i);
            if (!die.isVisible()) {
                Die remove = dies.remove(i);
                DiesPool.theReturn(remove);
                i--;
            }
        }
    }

    //内部类来表示英雄的血条
    class BloodBar {
        public static final int BAR_LENGTH = 32;
        public static final int BAR_HEIGHT = 5;

        public void draw(Graphics g) {
            //填充底色
            g.setColor(Color.YELLOW);
            g.fillRect(x - RADIUS, y - RADIUS - BAR_HEIGHT * 2, BAR_LENGTH, BAR_HEIGHT);
            //当前血量
            g.setColor(Color.RED);
            g.fillRect(x - RADIUS, y - RADIUS - BAR_HEIGHT * 2, hp * BAR_LENGTH / maxHP, BAR_HEIGHT);

            //边框
            g.setColor(Color.WHITE);
            g.drawRect(x - RADIUS, y - RADIUS - BAR_HEIGHT * 2, BAR_LENGTH, BAR_HEIGHT);
        }
    }


    public void bulletCollideMapTile(List<MapTile> tiles) {
        //英雄的子弹和地图块的碰撞
        for (int i = 0; i < tiles.size(); i++) {
            MapTile tile = tiles.get(i);
            if (tile.isCollideBullet(bullets)) {
                //添加击中效果
                addDie(tile.getX() + MapTile.radiusW, tile.getY() + MapTile.radiusH);
                if (tile.isCrystal()) {
                    //当水晶被击毁后，过一秒钟后切换到游戏结束画面
                    for (Bullet bullet : bullets) {
                        tile.crystalHurt(bullet);
                    }
                } else if (tile.getType() == MapTile.TYPE_HARD_ROCK){
                    continue;
                } else {
                    //设置地图块销毁
                    tile.setVisible(false);
                    //归还对象池
                    //MapTilesPool.theReturn(tile);
                }
                /*
                if (tile.isCrystal()) {
                    delaySecondsToOver(1000);
                }

                 */
            }
        }
    }


    /**
     * 从tile中提取8个点来判断这些点是否有任何一个点和当前英雄有碰撞
     * @param tiles
     * @return
     */
    public boolean isCollideTile(List<MapTile> tiles) {
        for (MapTile tile : tiles) {
            if (!tile.isVisible() || tile.getType() == MapTile.TYPE_GRASS) continue;
            //第一个点，左上点-1
            int tileX = tile.getX();
            int tileY = tile.getY();
            boolean collide = MyUtil.isCollide(x, y, RADIUS, tileX, tileY);
            //如果碰上了就直接返回
            if (collide) return true;

            //中上点-2
            tileX += MapTile.radiusW;
            collide = MyUtil.isCollide(x, y, RADIUS, tileX, tileY);

            if (collide) return true;

            //右上点-3
            tileX += MapTile.radiusW;
            collide = MyUtil.isCollide(x, y, RADIUS, tileX, tileY);

            if (collide) return true;

            //右中点-4
            tileY += MapTile.radiusH;
            collide = MyUtil.isCollide(x, y, RADIUS, tileX, tileY);

            if (collide) return true;

            //右下点-5
            tileY += MapTile.radiusH;
            collide = MyUtil.isCollide(x, y, RADIUS, tileX, tileY);

            if (collide) return true;

            //中下点-6
            tileX -= MapTile.radiusW;
            collide = MyUtil.isCollide(x, y, RADIUS, tileX, tileY);

            if (collide) return true;

            //左下点-7
            tileX -= MapTile.radiusW;
            collide = MyUtil.isCollide(x, y, RADIUS, tileX, tileY);

            if (collide) return true;

            //左中点-8
            tileY -= MapTile.radiusH;
            collide = MyUtil.isCollide(x, y, RADIUS, tileX, tileY);

            if (collide) return true;
        }
        return false;
    }

    //英雄回退的方法
    public void back() {
        x = oldX;
        y = oldY;
    }
}
