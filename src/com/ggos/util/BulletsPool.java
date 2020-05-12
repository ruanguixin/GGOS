package com.ggos.util;

import com.ggos.game.Bullet;

import java.util.ArrayList;
import java.util.List;

/**
 * 子弹对象池类
 */

public class BulletsPool {
    public static final int DEFAULT_POOL_SIZE = 200;
    public static final int DEFAULT_POOL_MAX = 300;

    //用于保存所有子弹的容器
    private static List<Bullet> pool = new ArrayList<>();
    //在类加载的时候创建200个子弹对象添加的容器中
    static {
        for (int i = 0; i < DEFAULT_POOL_SIZE; i++) {
            pool.add(new Bullet());
        }
    }

    /**
     * 从pool中获取一个子弹对象
     * @return
     */

    public static Bullet get() {
        Bullet bullet = null;
        if (pool.size() == 0) {
            bullet = new Bullet();
        } else {
            bullet = pool.remove(0);
        }
        return bullet;
    }
    //子弹被销毁的时候，归还到池塘中来
    public static void theReturn(Bullet bullet) {
        //池塘中的子弹个数到达最大值
        if (pool.size() == DEFAULT_POOL_MAX) {
            return;
        }
        pool.add(bullet);
    }
}
