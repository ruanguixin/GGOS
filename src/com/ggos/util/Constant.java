package com.ggos.util;

import java.awt.*;

//游戏中的常量都在该类中维护，方便后期的管理
public class Constant {

    /**
     *游戏窗口相关
     */
    public static final String GAME_TITLE = "Gun Gale Online Scuffle";

    public static final int FRAME_WIDTH = 800;
    public static final int FRAME_HEIGHT = 600;


    //动态获取当前系统屏幕的宽和高
    public static final int SCREEN_W = Toolkit.getDefaultToolkit().getScreenSize().width;
    public static final int SCREEN_H = Toolkit.getDefaultToolkit().getScreenSize().height;

    public static final int FRAME_X = SCREEN_W - FRAME_WIDTH>>1; //居中
    public static final int FRAME_Y = SCREEN_H - FRAME_HEIGHT>>1;

    /**
     * 游戏菜单相关
     */
    public static final int STATE_MENU = 0;
    public static final int STATE_HELP = 1;
    public static final int STATE_ABOUT = 2;
    public static final int STATE_RUN = 3;
    public static final int STATE_OVER = 4;
    public static final int STATE_WIN = 5;

    public static final String[] MENUS = {
            "START GAME",
            "CONTINUE",
            "HELP",
            "ABOUT",
            "EXIT GAME"
    };

    public static final String OVER_STR0 = "PRESS ESC TO EXIT GAME";
    public static final String OVER_STR1 = "PRESS ENTER TO MENU";

    public static final String ABOUT_STR = "PRESS ESC TO BACK";

    public static final String WIN_STR = "CONGRATULATIONS!";

    //菜单字体设置
    public static final Font GAME_FONT = new Font("Arial", Font.BOLD, 24);
    public static final Font SMALL_FONT = new Font("Arial", Font.BOLD, 12);


    public static final int REPAINT_INTERVAL = 30;

    //最大敌人数量
    public static final int ENEMY_MAX_COUNT = 10;
    public static final int ENEMY_BORN_INTERVAL = 5000;

    public static final int ENEMY_AI_INTERVAL = 3000;
    public static final double ENEMY_FIRE_PERCENT = 0.03;

}
