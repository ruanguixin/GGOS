package com.ggos.util;

import com.ggos.map.MapTile;

import java.util.ArrayList;
import java.util.List;

public class MapTilesPool {
    public static final int DEFAULT_POOL_SIZE = 50;
    public static final int DEFAULT_POOL_MAX = 70;

    //用于保存所有死亡的容器
    private static List<MapTile> pool = new ArrayList<>();
    //在类加载的时候创建20个死亡对象添加的容器中
    static {
        for (int i = 0; i < DEFAULT_POOL_SIZE; i++) {
            pool.add(new MapTile());
        }
    }

    /**
     * 从pool中获取一个死亡对象
     * @return
     */

    public static MapTile get() {
        MapTile tile = null;
        if (pool.size() == 0) {
            tile = new MapTile();
        } else {
            tile = pool.remove(0);
        }
        return tile;
    }
    //死亡被销毁的时候，归还到池塘中来
    public static void theReturn(MapTile tile) {
        //池塘中的死亡个数到达最大值
        if (pool.size() == DEFAULT_POOL_MAX) {
            return;
        }
        pool.add(tile);
    }
}
