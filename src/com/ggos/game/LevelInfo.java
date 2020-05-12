package com.ggos.game;

import com.ggos.util.MyUtil;

/**
 * 用来管理当前关卡的信息
 * 单例设计模式：如果一个类之需要该类只有唯一的实例
 */
public class LevelInfo {
    //构造方法私有化
    private LevelInfo() {}

    //定义静态的本类型的变量来指向唯一的实例
    private static LevelInfo instance;

    private int levelType;

    //懒汉模式的单例，第一次使用该实例的时候创建唯一的实例
    //所有的访问该类的唯一实例都是通过该方法
    //该方法具有安全隐患，多线程的情况下可以能会创建多个实例
    public static LevelInfo getInstance(){
        if (instance == null) {
            instance = new LevelInfo();
        }
        return instance;
    }
    //关卡编号
    private int level;
    //关卡敌人的数量
    private int enemyCount;
    //通关要求时长，-1意味着不限时
    private int crossTime = -1;
    //敌人类型信息
    private int[] enemyType;


    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getEnemyCount() {
        return enemyCount;
    }

    public void setEnemyCount(int enemyCount) {
        this.enemyCount = enemyCount;
    }

    public int getCrossTime() {
        return crossTime;
    }

    public void setCrossTime(int crossTime) {
        this.crossTime = crossTime;
    }

    public int[] getEnemyType() {
        return enemyType;
    }

    public void setEnemyType(int[] enemyType) {
        this.enemyType = enemyType;
    }

    public int getLevelType() {
        return levelType <= 0 ? 1 : levelType;
    }

    public void setLevelType(int levelType) {
        this.levelType = levelType;
    }

    public int getRandomEnemyType() {
        int index = MyUtil.getRandomNumber(0, enemyType.length);
        return enemyType[index];
    }
}
