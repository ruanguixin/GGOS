package com.ggos.map;

import com.ggos.util.Constant;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 本家水晶
 */

public class Crystal {
    public static final int CRYSTAL_X = Constant.FRAME_WIDTH - MapTile.tileW >> 1;
    public static final int CRYSTAL_Y = Constant.FRAME_HEIGHT - MapTile.tileH;
    //设置水晶地图块集合，为添加保护罩做准备
    private List<MapTile> tiles = new ArrayList<>();
    public Crystal() {

        tiles.add(new MapTile(CRYSTAL_X, CRYSTAL_Y));
        //设置水晶地图块的类型
        tiles.get(0).setType(MapTile.TYPE_CRYSTAL);
    }

    public void draw(Graphics g) {
        for (MapTile tile : tiles) {
            tile.draw(g);
        }
    }

    public List<MapTile> getTiles() {
        return tiles;
    }

    public void setTiles(List<MapTile> tiles) {
        this.tiles = tiles;
    }
}
