package com.ggos.hero;

import com.ggos.game.GameFrame;
import com.ggos.game.LevelInfo;
import com.ggos.util.Constant;
import com.ggos.util.HeroesPool;
import com.ggos.util.MyUtil;

import java.awt.*;

/**
 * 敌人士兵类
 */

public class EnemySoldier extends Hero {
    public static final int TYPE_M = 0;
    public static final int Type_A = 1;
    private int type = TYPE_M;


    private EnemySoldier(int x, int y, int dir) {
        super(x, y, dir);

        aiTime = System.currentTimeMillis();
        type = MyUtil.getRandomNumber(0, 2);
    }

    public EnemySoldier() {
        aiTime = System.currentTimeMillis();
        type = MyUtil.getRandomNumber(0, 2);
    }

    private static Image[] mImg;
    private static Image[] aImg;
    //记录5秒开始的时间
    private long aiTime;

    //静态代码块对它进行初始化
    static {
        mImg = new Image[4];
        mImg[0] = MyUtil.createImage("res/MUp.Png");
        mImg[1] = MyUtil.createImage("res/MDown.Png");
        mImg[2] = MyUtil.createImage("res/MLeft.Png");
        mImg[3] = MyUtil.createImage("res/MRight.Png");

        aImg = new Image[4];
        aImg[0] = MyUtil.createImage("res/PitahuiUp.Png");
        aImg[1] = MyUtil.createImage("res/PitahuiDown.Png");
        aImg[2] = MyUtil.createImage("res/PitahuiLeft.Png");
        aImg[3] = MyUtil.createImage("res/PitahuiRight.Png");
    }


    @Override
    public void drawImgHero(Graphics g) {
        ai();
        g.drawImage(type == TYPE_M ? mImg[getDir()] : aImg[getDir()],
                getX() - RADIUS, getY() - RADIUS, null);
    }

    //用于创建一个敌人
    public static Hero createEnemy() {
        int x = MyUtil.getRandomNumber(0, 2) == 0 ? RADIUS :
                Constant.FRAME_WIDTH - RADIUS;
        int y = GameFrame.titleBarH + RADIUS;
        int dir = DIR_DOWN;
//        Hero enemy = new EnemySoldier(x, y, dir);
        EnemySoldier enemy = (EnemySoldier)HeroesPool.get();
        enemy.setX(x);
        enemy.setY(y);
        enemy.setDir(dir);
        enemy.setEnemy(true);
        //TODO
        enemy.setState(STATE_MOVE);
        //根据游戏的难度设置敌人的血量
        int maxHp = Hero.DEFAULT_HP * LevelInfo.getInstance().getLevelType();
        enemy.setHp(maxHp);
        enemy.setMaxHP(maxHp);
        //通过关卡信息中的敌人来设置当前出生的敌人的类型
        int enemyType = LevelInfo.getInstance().getRandomEnemyType();
        enemy.setType(enemyType);

        return enemy;
    }
    //敌人士兵的AI
    private void ai() {
        if (System.currentTimeMillis() - aiTime > Constant.ENEMY_AI_INTERVAL) {
            //间隔5秒随机一个状态
            setDir(MyUtil.getRandomNumber(DIR_UP, DIR_RIGHT + 1));
            setState(MyUtil.getRandomNumber(0, 2) == 0 ? STATE_STAND : STATE_MOVE);
            aiTime = System.currentTimeMillis();
        }
        //比较小的概率去开火
        if (Math.random() < Constant.ENEMY_FIRE_PERCENT) {
            fire();
        }
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
