package com.ggos.map;

import com.ggos.game.GameFrame;
import com.ggos.game.LevelInfo;
import com.ggos.hero.Hero;
import com.ggos.util.Constant;
import com.ggos.util.MapTilesPool;

import java.awt.*;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * 游戏地图类
 */

public class GameMap {

    public static final int MAP_X = Hero.RADIUS * 3;
    public static final int MAP_Y = Hero.RADIUS * 3 + GameFrame.titleBarH;
    public static final int MAP_WIDTH = Constant.FRAME_WIDTH - Hero.RADIUS * 6;
    public static final int MAP_HEIGHT = Constant.FRAME_HEIGHT - Hero.RADIUS * 8 - GameFrame.titleBarH;

    //地图元素块的容器
    private List<MapTile> tiles = new ArrayList<>();

    //大本营水晶
    private Crystal crystal;

    public GameMap() {}

    /**
     * 初始化地图元素块
     */
    public void initMap(int level) {
        tiles.clear();
        try {
            loadLevel(level);
        } catch (IOException e) {
            e.printStackTrace();
        }


        //随机得到一个地图元素块，添加到容器中来
        /**
        final int COUNT = 20;
        for (int i = 0; i < COUNT; i++) {
            MapTile tile = MapTilesPool.get();
            int x = MyUtil.getRandomNumber(MAP_X, MAP_X + MAP_WIDTH - MapTile.tileW);
            int y = MyUtil.getRandomNumber(MAP_Y, MAP_Y + MAP_HEIGHT - MapTile.tileH);
            int type = MyUtil.getRandomNumber(0, 3);
            //新生成的块与已经存在的块有重叠的部分
            if (isOverlap(tiles, x, y)) {
                i--;
                continue;
            }
            tile.setX(x);
            tile.setY(y);
            tile.setType(type);
            tiles.add(tile);
        }
         */

        //三行地图
        //addRow(MAP_X, MAP_Y, MAP_X + MAP_WIDTH, MapTile.TYPE_ROCK, 0);
        //addRow(MAP_X, MAP_Y + MapTile.tileH * 2, MAP_X + MAP_WIDTH, MapTile.TYPE_GRASS, 0);
        //addRow(MAP_X, MAP_Y + MapTile.tileH * 4, MAP_X + MAP_WIDTH, MapTile.TYPE_HARD_ROCK, 64);

        //初始化大本营
        crystal = new Crystal();
        addCrystal();
    }

    /**
     *
     * @param level
     */
    public void loadLevel(int level) throws IOException {
        //获得关卡信息类的唯一实例对象
        LevelInfo levelInfo = LevelInfo.getInstance();
        levelInfo.setLevel(level);

        Properties prop = new Properties();
        prop.load(new FileInputStream("Level/lv_" + level));

        //将所有的地图信息加载进来
        int enemyCount = Integer.parseInt(prop.getProperty("enemyCount"));
        //设置敌人数量
        levelInfo.setEnemyCount(enemyCount);

        //对敌人类型解析
        String[] enemyType = prop.getProperty("enemyType").split(",");
        int[] type = new int[enemyType.length];
        for (int i = 0; i < type.length; i++) {
            type[i] = Integer.parseInt(enemyType[i]);
        }
        //设置敌人内容
        levelInfo.setEnemyType(type);
        String levelType = prop.getProperty("levelType");
        levelInfo.setLevelType(Integer.parseInt(levelType == null ? "1" : levelType));

        String methodName = prop.getProperty("method");
        int invokeCount = Integer.parseInt(prop.getProperty("invokeCount"));


        String[] params = new String[invokeCount];
        for (int i = 1; i <= invokeCount; i++) {
            params[i - 1] = prop.getProperty("param" + i);
        }

        //使用读取到的参数，调用对应的方法
        invokeMethod(methodName, params);
    }

    //根据方法的名字和参数调用对应的方法

    private void invokeMethod(String name, String[] params) {
        for (String param : params) {
            String[] split = param.split(",");
            int[] arr = new int[split.length];
            for (int i = 0; i < split.length; i++) {
                arr[i] = Integer.parseInt(split[i]);
            }
            //块之间的间隔为地图块宽度的倍数
            final int DIS = MapTile.tileW;
            switch(name) {
                case "addRow":
                    addRow(MAP_X + arr[0] * DIS, MAP_Y + GameFrame.titleBarH + arr[1] * DIS,
                            MAP_X + MAP_WIDTH - arr[2] * DIS, arr[3], DIS * arr[4]);
                    break;
                case "addCol":
                    addCol(MAP_X + arr[0] * DIS, MAP_Y + GameFrame.titleBarH + arr[1] * DIS,
                            MAP_Y + GameFrame.titleBarH + MAP_HEIGHT - arr[2] * DIS, arr[3], DIS * arr[4]);
                    break;
                case "addRect":
                    addRect(MAP_X + arr[0] * DIS, MAP_Y + GameFrame.titleBarH+ arr[1] * DIS,
                            MAP_X + MAP_WIDTH - arr[2] * DIS,
                            MAP_Y + GameFrame.titleBarH + MAP_HEIGHT - arr[2] * DIS, arr[3], DIS * arr[4]);
                    break;
            }
        }
    }


    private void addCrystal() {
        tiles.addAll(crystal.getTiles());
    }

    /**
     * 判断某一个点是否和tiles集合中所有的块有重叠的部分
     * @param tiles
     * @param x
     * @param y
     * @return
     */
    private boolean isOverlap(List<MapTile> tiles, int x, int y) {
        for (MapTile tile : tiles) {
            int tileX = tile.getX();
            int tileY = tile.getY();
            if (Math.abs(tileX - x) < MapTile.tileW  &&
                    Math.abs(tileY - y) < MapTile.tileH) {
                return true;
            }
        }
        return false;
    }

    public void drawBack(Graphics g) {
        for (int i = 0; i < tiles.size(); i++) {
            MapTile tile = tiles.get(i);
            if (tile.getType() != MapTile.TYPE_GRASS) tile.draw(g);
        }
    }

    public void drawCover (Graphics g) {
        for (int i = 0; i < tiles.size(); i++) {
            MapTile tile = tiles.get(i);
            if (tile.getType() == MapTile.TYPE_GRASS) tile.draw(g);
        }
    }

    public List<MapTile> getTiles() {
        return tiles;
    }

    public void setTiles(List<MapTile> tiles) {
        this.tiles = tiles;
    }

    public void clearDestroyedTile() {
        for (int i = 0; i < tiles.size(); i++) {
            MapTile tile = tiles.get(i);
            if (!tile.isVisible()) {
                MapTile remove = tiles.remove(i);
                //设置地图块重新可见（下一关用）
                remove.setVisible(true);
                //返回地图块对象池
                MapTilesPool.theReturn(remove);
            }

        }
    }

    /**
     * 往地图中添加一行指定类型的地图块
     * @param startX 起始x坐标
     * @param startY 起始y坐标
     * @param endX 结束x坐标
     * @param type 地图块类型
     * @param DIS 地图块的间隔是块的宽度，则是连续的
     */
    public void addRow(int startX, int startY, int endX, int type, final int DIS) {
        int count = (endX - startX) / (MapTile.tileW + DIS);
        for (int i = 0; i < count; i++) {
            MapTile tile = MapTilesPool.get();
            tile.setType(type);
            tile.setX(startX + i * (MapTile.tileW + DIS));
            tile.setY(startY);
            tiles.add(tile);
        }
    }

    /**
     * 往地图中添加一列指定类型的地图块
     * @param startX
     * @param startY
     * @param endY
     * @param type
     * @param DIS
     */
    public void addCol(int startX, int startY, int endY, int type, final int DIS) {
        int count = (endY - startY) / (MapTile.tileH + DIS);
        for (int i = 0; i < count; i++) {
            MapTile tile = MapTilesPool.get();
            tile.setType(type);
            tile.setX(startX);
            tile.setY(startY + i * (MapTile.tileH + DIS));
            tiles.add(tile);
        }
    }

    /**
     * 对指定的矩形区域添加地图块
     * @param startX
     * @param startY
     * @param endX
     * @param endY
     * @param type
     * @param DIS
     */
    public void addRect(int startX, int startY, int endX, int endY, int type, final int DIS ) {
        int rows = (endY - startY) / (MapTile.tileH + DIS);
        for (int i = 0; i < rows; i++) {
            addRow(startX, startY + i * (MapTile.tileH + DIS), endX, type, DIS);
        }

        //int cols = (endX - startX) / (MapTile.tileW + DIS);


    }
}
