package com.ggos.util;

import com.ggos.game.Die;

import java.util.ArrayList;
import java.util.List;

public class DiesPool {
    public static final int DEFAULT_POOL_SIZE = 10;
    public static final int DEFAULT_POOL_MAX = 20;

    //用于保存所有死亡的容器
    private static List<Die> pool = new ArrayList<>();
    //在类加载的时候创建10个死亡对象添加的容器中
    static {
        for (int i = 0; i < DEFAULT_POOL_SIZE; i++) {
            pool.add(new Die());
        }
    }

    /**
     * 从pool中获取一个死亡对象
     * @return
     */

    public static Die get() {
        Die die = null;
        if (pool.size() == 0) {
            die = new Die();
        } else {
            die = pool.remove(0);
        }
        return die;
    }
    //死亡被销毁的时候，归还到池塘中来
    public static void theReturn(Die die) {
        //池塘中的子弹个数到达最大值
        if (pool.size() == DEFAULT_POOL_MAX) {
            return;
        }
        pool.add(die);
    }
}
