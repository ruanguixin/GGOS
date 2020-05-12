package com.ggos.game;

import java.io.FileInputStream;
import java.util.Properties;

/**
 * 游戏相关信息的类
 */

public class GameInfo {
    //从配置文件中读取
    private static int levelCount;

    static {
        Properties prop = new Properties();
        try {
            prop.load(new FileInputStream("level/gameinfo"));
            levelCount = Integer.parseInt(prop.getProperty("levelCount"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static int getLevelCount() {
        return levelCount;
    }

    public static void setLevelCount(int levelCount) {
        GameInfo.levelCount = levelCount;
    }
}
