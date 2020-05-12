package com.ggos.util;

import com.ggos.hero.EnemySoldier;
import com.ggos.hero.Hero;

import java.util.ArrayList;
import java.util.List;

/**
 * 敌人士兵对象池
 */

public class HeroesPool {
    public static final int DEFAULT_POOL_SIZE = 20;
    public static final int DEFAULT_POOL_MAX = 20;

    //用于保存所有死亡的容器
    private static List<Hero> pool = new ArrayList<>();
    //在类加载的时候创建20个死亡对象添加的容器中
    static {
        for (int i = 0; i < DEFAULT_POOL_SIZE; i++) {
            pool.add(new EnemySoldier());
        }
    }

    /**
     * 从pool中获取一个死亡对象
     * @return
     */

    public static Hero get() {
        Hero hero = null;
        if (pool.size() == 0) {
            hero = new EnemySoldier();
        } else {
            hero = pool.remove(0);
        }
        return hero;
    }
    //死亡被销毁的时候，归还到池塘中来
    public static void theReturn(Hero hero) {
        //池塘中的死亡个数到达最大值
        if (pool.size() == DEFAULT_POOL_MAX) {
            return;
        }
        pool.add(hero);
    }
}
