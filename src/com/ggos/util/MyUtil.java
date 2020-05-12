package com.ggos.util;

import com.ggos.game.GameFrame;

import java.awt.*;

/**
 * 工具类
 */


public class MyUtil {
    public MyUtil() {
    }

    /**
     * 得到指定区间的随机数
     * @param min
     * @param max
     * @return 随机数
     */
    public static final int getRandomNumber (int min, int max) {
        return (int) (Math.random()*(max - min) + min);
    }

    /**
     *
     * @return
     */

    public static final Color getRandomColor() {
        int red = getRandomNumber(0, 256);
        int blue = getRandomNumber(0, 256);
        int green = getRandomNumber(0, 256);
        return new Color(red, green, blue);//得到随机的颜色
    }

    /**
     * 判断一个点是否在矩形内部
     * @param rectX 矩形的中心点的X坐标
     * @param rectY 矩形的中心店的Y坐标
     * @param radius 矩形（正方形）的边长的一半
     * @param pointX 点的X坐标
     * @param pointY 点的Y坐标
     * @return 在内部返回true，否则返回false
     */

    public static final boolean isCollide(int rectX, int rectY, int radius, int pointX, int pointY) {
        int disX = Math.abs(rectX - pointX);
        int disY = Math.abs(rectY - pointY);
        if (disX <= radius && disY <= radius) return true;
        return false;
    }

    /**
     *
     * @param path
     * @return
     */
    public static final Image createImage(String path) {
        return Toolkit.getDefaultToolkit().createImage(path);
    }



    private static final String[] NAMES = {
            "FUKAZIROH", "LLENN"
    };

    private static final String[] MODIFY = {
            "PINK DEVIL"
    };

    public static final String getRandomName() {
        return //MODIFY[getRandomNumber(0, MODIFY.length)] + " " +
                NAMES[getRandomNumber(0, NAMES.length)];
    }

    public static void delaySecondsToOver(int millisSecond) {
        new Thread() {
            @Override
            public void run() {
                try {
                    Thread.sleep(millisSecond);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                GameFrame.setGameState(Constant.STATE_OVER);
            }
        }.start();
    }

}
