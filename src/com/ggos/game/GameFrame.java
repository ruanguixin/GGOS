package com.ggos.game;

import com.ggos.hero.EnemySoldier;
import com.ggos.hero.Hero;
import com.ggos.hero.MyHero;
import com.ggos.map.GameMap;
import com.ggos.util.MusicUtil;
import com.ggos.util.MyUtil;

import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import static com.ggos.util.Constant.*;
/**
 * 游戏主窗口；
 */

public class GameFrame extends Frame implements Runnable {
    //第一次使用的时候加载
    private Image overImg = null;
    private Image startImg = null;

    //双缓冲-1：定义一张和屏幕大小一样的图片
    private BufferedImage bufImg = new BufferedImage(FRAME_WIDTH, FRAME_HEIGHT,
            BufferedImage.TYPE_4BYTE_ABGR);

    //游戏状态
    private static int gameState;

    //菜单指向
    private static int menuIndex;

    //标题栏的高度
    public static int titleBarH;

    //定义英雄对象
    private static Hero myHero;

    private static List<Hero> enemies = new ArrayList<>();

    //本关卡产生了多少个敌人
    private static int bornEnemyCount;
    public static int killEnemyCount;

    //定义地图相关的内容
    private static GameMap gameMap = new GameMap();;



    /**
    * 对窗口进行初始化
    */
    public GameFrame() {
        initFrame();

        initEventListener();
        //启动用于刷新窗口
        new Thread(this).start();
    }
    
    //对游戏进行初始化
    private void initGame() {
        gameState = STATE_MENU;
        
    }

    //属性进行初始化
    private void initFrame() {
        //设置标题
        setTitle(GAME_TITLE);

        //设置窗口大小
        setSize(FRAME_WIDTH, FRAME_HEIGHT);

        //设置窗口左上角坐标
        setLocation(FRAME_X, FRAME_Y);

        //固定窗口大小
        setResizable(false);

        //设置窗口可见
        setVisible(true);

        //求标题栏的高度
        titleBarH = getInsets().top;

    }
    


    /**
     * 是Frame类的方法继承下来的内容
     * 该方法负责了所有的回执的内容，所有需要在屏幕中显示的内容，都要在该方法内调用，该方法不能主动调用
     * 必须通过调用repaint()去回调该方法
     * @param g1 画笔对象，系统提供的
     */
    @Override
    public void update(Graphics g1) {
        //双缓冲-2：得到图片的画笔
        Graphics g = bufImg.getGraphics();

        //双缓冲-3：使用图片画笔绘制
        g.setFont(GAME_FONT);
        switch(gameState) {
            case STATE_MENU:
                drawMenu(g);
                break;
            case STATE_HELP:
                drawHelp(g);
                break;
            case STATE_ABOUT:
                drawAbout(g);
                break;
            case STATE_RUN:
                drawRun(g);
                break;
            case STATE_OVER:
                drawOver(g);
                break;
            case STATE_WIN:
                drawWin(g);
                break;
        }

        //双缓冲-4：使用系统画笔将图片绘制到frame上来
        g1.drawImage(bufImg, 0 ,0 , null);
    }

    /**
     * 绘制游戏结束的方法
     * @param g
     */
    private void drawOver(Graphics g) {
        //保证只加载一次
        if (overImg == null) {
            overImg = MyUtil.createImage("res/game_over.png");
        }

        int imgW = overImg.getWidth(null);
        int imgH = overImg.getHeight(null);

        g.drawImage(overImg, FRAME_WIDTH - imgW >> 1, FRAME_HEIGHT - imgH >> 1, null);

        //添加按键的提示信息
        g.setColor(Color.WHITE);
        g.setFont(SMALL_FONT);
        g.drawString(OVER_STR0, 10, FRAME_HEIGHT - 20);
        g.drawString(OVER_STR1, FRAME_WIDTH - 150, FRAME_HEIGHT - 20);

    }

    private void drawWin(Graphics g) {
        if (overImg == null) {
            overImg = MyUtil.createImage("res/start.png");
        }

        int imgW = overImg.getWidth(null);
        int imgH = overImg.getHeight(null);

        g.drawImage(overImg, FRAME_WIDTH - imgW >> 1, FRAME_HEIGHT - imgH >> 1, null);

        //添加按键的提示信息
        g.setColor(Color.BLACK);
        g.setFont(GAME_FONT);
        g.drawString(WIN_STR, FRAME_WIDTH / 2 - 120, FRAME_HEIGHT / 2 - 100);

        g.setFont(SMALL_FONT);
        g.drawString(OVER_STR0, 10, FRAME_HEIGHT - 20);
        g.drawString(OVER_STR1, FRAME_WIDTH - 150, FRAME_HEIGHT - 20);
    }

    private void drawRun(Graphics g) {
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, FRAME_WIDTH, FRAME_HEIGHT);

        //绘制地图
        gameMap.drawBack(g);

        drawEnemies(g);
        myHero.draw(g);

        gameMap.drawCover(g);

        drawDies(g);

        //子弹和英雄碰撞的方法
        bulletCollideHero();

        //子弹和所有地图块的碰撞
        bulletCollideMapTile();
    }


    private void drawAbout(Graphics g) {
        if (startImg == null) {
            startImg = MyUtil.createImage("res/start.png");
        }
        g.drawImage(startImg, 0, 0, null);
        g.setColor(Color.BLACK);
        g.setFont(SMALL_FONT);
        g.drawString(ABOUT_STR, 10, FRAME_HEIGHT - 20);
    }

    private void drawHelp(Graphics g) {
    }

    /**
     * 绘制菜单状态下的内容
     * @param g 画笔对象，系统提供的
     */
    private void drawMenu(Graphics g) {
        //绘制黑色的背景
        g.setColor(Color.BLACK);
        g.fillRect(0,0,FRAME_WIDTH, FRAME_HEIGHT);
        
        final int STR_WIDTH = 160;
        int x = FRAME_WIDTH - STR_WIDTH >> 1;
        int y = FRAME_HEIGHT / 3;
        final int DIS = 30;
        
        g.setColor(Color.WHITE);
        for (int i = 0; i < MENUS.length; i++) {
            if (i == menuIndex) {
                g.setColor(Color.YELLOW); //选中的菜单选项颜色设为黄色
            } else {
                g.setColor(Color.WHITE);
            }
            g.drawString(MENUS[i], x, y + DIS * i);
        }
    }


    /**
     * 初始化窗口的监听事件
     */
    private void initEventListener() {
        //注册监听事件
        addWindowListener(new WindowAdapter() {
            //点击关闭按钮时，方法会被自动调用
            @Override
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });
        //添加按键监听事件
        addKeyListener(new KeyAdapter() {
            //按键被按下的时候被回调的内容
            @Override
            public void keyPressed(KeyEvent e) {
                //被按下键的键值
                int keyCode = e.getKeyCode();
                switch(gameState) {
                    case STATE_MENU:
                        keyEventMenu(keyCode);
                        break;
                    case STATE_HELP:
                        keyEventHelp(keyCode);
                        break;
                    case STATE_ABOUT:
                        keyEventAbout(keyCode);
                        break;
                    case STATE_RUN:
                        keyEventRun(keyCode);
                        break;
                    case STATE_OVER:
                        keyEventOver(keyCode);
                        break;
                    case STATE_WIN:
                        keyEventWin(keyCode);
                        break;
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {

                int keyCode = e.getKeyCode();
                if (gameState == STATE_RUN) {
                    keyReleasedEventRun(keyCode);
                }
            }
        });
    }
    //游戏通关的按键处理
    private void keyEventWin(int keyCode) {
        keyEventOver(keyCode);
    }

    private void keyReleasedEventRun(int keyCode) {
        switch (keyCode) {
            case KeyEvent.VK_UP:
            case KeyEvent.VK_W:
            case KeyEvent.VK_DOWN:
            case KeyEvent.VK_S:
            case KeyEvent.VK_LEFT:
            case KeyEvent.VK_A:
            case KeyEvent.VK_RIGHT:
            case KeyEvent.VK_D:
                myHero.setState(Hero.STATE_STAND);
                break;
        }
    }

    /**
     * 游戏结束的按键处理
     * @param keyCode TODO
     */
    private void keyEventOver(int keyCode) {

        if (keyCode == KeyEvent.VK_ESCAPE) {
            System.exit(0);
        } else if (keyCode == KeyEvent.VK_ENTER) {
            setGameState(STATE_MENU);
            //游戏操作需要关闭，某些属性需要重置
            resetGame();
        }
    }
    //重置游戏
    private void resetGame() {
        killEnemyCount = 0;
        menuIndex = 0;
        //子弹回收
        myHero.bulletsReturn();
        //销毁己方英雄
        myHero = null;
        //清空敌人
        for (int i = 0; i < enemies.size(); i++) {
            Hero enemy = enemies.get(i);
            enemy.bulletsReturn();
        }
        enemies.clear();
        //清空地图资源
        gameMap = null;
    }

    //游戏中的按键处理
    private void keyEventRun(int keyCode) {
        switch (keyCode) {
            case KeyEvent.VK_UP:
            case KeyEvent.VK_W:
                myHero.setDir(Hero.DIR_UP);
                myHero.setState(Hero.STATE_MOVE);
                break;
            case KeyEvent.VK_DOWN:
            case KeyEvent.VK_S:
                myHero.setDir(Hero.DIR_DOWN);
                myHero.setState(Hero.STATE_MOVE);
                break;
            case KeyEvent.VK_LEFT:
            case KeyEvent.VK_A:
                myHero.setDir(Hero.DIR_LEFT);
                myHero.setState(Hero.STATE_MOVE);
                break;
            case KeyEvent.VK_RIGHT:
            case KeyEvent.VK_D:
                myHero.setDir(Hero.DIR_RIGHT);
                myHero.setState(Hero.STATE_MOVE);
                break;

            case KeyEvent.VK_SPACE:
            case KeyEvent.VK_H:
                myHero.fire();
                break;
        }
    }

    private void keyEventAbout(int keyCode) {
        if (keyCode == KeyEvent.VK_ESCAPE) {
            setGameState(STATE_MENU);
        }
    }

    private void keyEventHelp(int keyCode) {
    }
    //菜单状态下键的处理
    private void keyEventMenu(int keyCode) {
        switch (keyCode) {
            case KeyEvent.VK_UP:
            case KeyEvent.VK_W:
                menuIndex--;
                if (menuIndex < 0) {
                    menuIndex = MENUS.length - 1;
                }
                break;
            case KeyEvent.VK_DOWN:
            case KeyEvent.VK_S:
                menuIndex++;
                if (menuIndex > MENUS.length - 1) {
                    menuIndex = 0;
                }
                break;
            case KeyEvent.VK_ENTER:
                //TODO
                switch (menuIndex) {
                    case 0:
                        startGame(1);
                        break;
                    case 3:
                        setGameState(STATE_ABOUT);
                        break;
                    case 4:
                        System.exit(0);
                        break;
            }
        }
    }


    /**
     * 开始新游戏的方法
     * @param level
     */

    private static void startGame(int level) {
        enemies.clear();
        if (gameMap == null) {
            gameMap = new GameMap();
        }
        gameMap.initMap(level);
        MusicUtil.playStart();
        killEnemyCount = 0;
        bornEnemyCount = 0;
        gameState = STATE_RUN;
        //创建英雄对象，敌人的士兵对象
        myHero = new MyHero(FRAME_WIDTH / 3, FRAME_HEIGHT - Hero.RADIUS, Hero.DIR_UP);

        //使用一个单独的线程用于控制生产敌人
        new Thread() {
            @Override
            public void run() {
                while (true) {
                    if (LevelInfo.getInstance().getEnemyCount() > bornEnemyCount &&
                            enemies.size() < ENEMY_MAX_COUNT) {
                        Hero enemy = EnemySoldier.createEnemy();
                        enemies.add(enemy);
                        bornEnemyCount++;
                    }
                    try {
                        Thread.sleep(ENEMY_BORN_INTERVAL);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    //只有在run状态下才创造敌人
                    if (gameState != STATE_RUN) {
                        break;
                    }
                }
            }
        }.start();
    }

    //如果死亡，从容器中移除
    private void drawEnemies(Graphics g) {
        for (int i = 0; i < enemies.size(); i++) {
            Hero enemy = enemies.get(i);
            if (enemy.isDisappear()) {
                enemies.remove(i);
                i--;
                continue;
            }
            enemy.draw(g);
        }
    }

    @Override
    public void run() {
        while(true) {
            repaint();
            try {
                Thread.sleep(REPAINT_INTERVAL);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }


    //敌人子弹和己方英雄的碰撞
    //己方英雄的子弹和所有敌人的碰撞

    private void bulletCollideHero() {
        //敌人子弹和己方英雄的碰撞
        for (int i = 0; i < enemies.size(); i++) {
            Hero enemy = enemies.get(i);
            enemy.collideBullets(myHero.getBullets());
        }
        //己方英雄的子弹和所有敌人的碰撞
        for (int i = 0; i < enemies.size(); i++) {
            Hero enemy = enemies.get(i);
            myHero.collideBullets(enemy.getBullets());
        }
    }

    //子弹和地图块的碰撞
    private void bulletCollideMapTile() {
        //己方英雄的子弹和地图块的碰撞
        myHero.bulletCollideMapTile(gameMap.getTiles());
        //所有敌人和地图块的碰撞
        for (int i = 0; i < enemies.size(); i++) {
            Hero enemy = enemies.get(i);
            enemy.bulletCollideMapTile(gameMap.getTiles());
        }
        //英雄和地图块的碰撞
        if (myHero.isCollideTile(gameMap.getTiles())) {
            myHero.back();
        }

        for (int i = 0; i < enemies.size(); i++) {
            Hero enemy = enemies.get(i);
            if (enemy.isCollideTile(gameMap.getTiles())) {
                enemy.back();
            }
        }

        //清理所有被销毁的地图块
        gameMap.clearDestroyedTile();
    }


    private void drawDies(Graphics g) {
        for (int i = 0; i < enemies.size(); i++) {
            Hero enemy = enemies.get(i);
            enemy.drawDies(g);
        }
        myHero.drawDies(g);
    }


    //获取游戏状态
    public static int getGameState() {
        return gameState;
    }

    public static void setGameState(int gameState) {
        GameFrame.gameState = gameState;
    }

    /**
     * 游戏是否通关
     * @return
     */
    public static boolean isLastLevel() {
        //当前关卡和总关卡一致
        int currentLevel = LevelInfo.getInstance().getLevel();
        int levelCount = GameInfo.getLevelCount();
        return currentLevel == levelCount;
    }

    /**
     * 判断是否过关
     * @return
     */

    public static boolean isCrossLevel() {
        //消灭的敌人数量和关卡敌人的数量一致
        return killEnemyCount == LevelInfo.getInstance().getEnemyCount();
    }

    public static void nextLevel() {
        startGame(LevelInfo.getInstance().getLevel() + 1);

    }
}
