package byog.Core;

import byog.TileEngine.TERenderer;
import byog.TileEngine.TETile;
import byog.TileEngine.Tileset;
import edu.princeton.cs.introcs.StdDraw;
import java.util.Random;

import java.awt.Color;  // 导入颜色
import java.awt.Font;   // 导入字体
import java.io.*;       // TODO:没搞懂啥意思

public class Game {
    transient TERenderer ter = new TERenderer();
    private static final long serialVersionUID = 123123123123123L;
    /* Feel free to change the width and height. */
    public static final int WIDTH = 80;
    public static final int HEIGHT = 80;
    private static long seed;
    private static Random randomSeed;
    
    private static final int MID_WIDTH = WIDTH / 2;
    private static final int MID_HEIGHT = HEIGHT / 2;
    private static final int QUA_HEIGHT = MID_HEIGHT / 2;
    private TETile[][] world;
    private boolean gameOver;
    private Position playerPos;
    private Position doorPos;
    private TETile hidden = Tileset.FlOOR1;
    /**
     * Method used for playing a fresh game. The game should start from the main menu.
     */
    public void playWithKeyboard() {
        //draw main menu
        drawMainMenu();
        //user type command
        while (true) {
            if (StdDraw.hasNextKeyTyped()) {
                char cmd = StdDraw.nextKeyTyped();
                // N: prompt user to enter seed
                if (cmd == 'N') {
                    promptSeed();
                    world = GenerateWorld.Generate(WIDTH, HEIGHT, seed);
                    initializePosition();
                    break;
                } else if (cmd == 'L') {
                    // L: load game from saved file
                    loadGame();
                    break;
                } else if (cmd == 'Q') {
                    // Q: quit game
                    System.exit(0);
                }
            }
        }
        ter.initialize(WIDTH, HEIGHT + 5);
        while (true) {
            if (world != null) {
                ter.renderFrame(world);
                interact();
                mouseHover();
            }
        }
    }

    /**
     * Method used for autograding and testing the game code. The input string will be a series
     * of characters (for example, "n123sswwdasdassadwas", "n123sss:q", "lwww". The game should
     * behave exactly as if the user typed these characters into the game after playing
     * playWithKeyboard. If the string ends in ":q", the same world should be returned as if the
     * string did not end with q. For example "n123sss" and "n123sss:q" should return the same
     * world. However, the behavior is slightly different. After playing with "n123sss:q", the game
     * should save, and thus if we then called playWithInputString with the string "l", we'd expect
     * to get the exact same world back again, since this corresponds to loading the saved game.
     * @param input the input string to feed to your program
     * @return the 2D TETile[][] representing the state of the world
     */
    public TETile[][] playWithInputString(String input) {
        // Fill out this method to run the game using the input passed in,
        // and return a 2D tile representation of the world that would have been
        // drawn if the same inputs had been given to playWithKeyboard().
        input = input.toUpperCase();                // 把输入的字母全部变成大写字母
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < input.length(); i++) {
            char cmd = input.charAt(i);
            if (cmd == 'N') {                      // 如果字母是N，那么开始读取种子
                //read seed
                while (input.charAt(i) != 'S') {   // 在没识别到S前，把种子数读取出来
                    if (Character.isDigit(input.charAt(i))) {
                        sb.append(input.charAt(i));
                    }
                    i += 1;
                }
                seed = Long.parseLong(sb.toString());   // 此处的种子数即为读取出来的数量
                world = GenerateWorld.Generate(WIDTH, HEIGHT, seed);   // 生成世界地图
                initializePosition();                   // 初始化位置
            } else if (cmd == 'L') {                    // 如果字母是L，加载游戏
                loadGame();
            } else if (cmd == 'W' || cmd == 'S' || cmd == 'A' || cmd == 'D') {   // 移动角色
                move(cmd);
            } else if (cmd == ':' && input.charAt(i + 1) == 'Q') {   // 结束游戏并保存
                i += 1;
                saveGame();
            }
        }
        return world;
    }
    
    
    // 画出主菜单
    private static void drawMainMenu() {
        StdDraw.setCanvasSize(WIDTH * 16, HEIGHT * 16);   // 将画布像素设置为宽的16倍*高的16倍像素
        StdDraw.setXscale(0, WIDTH);            // 将 x 刻度设置为指定范围
        StdDraw.setYscale(0, HEIGHT);           // 将 y 刻度设置为指定范围
        StdDraw.clear(Color.BLACK);             // 将屏幕清除为指定颜色
        StdDraw.enableDoubleBuffering();        // 启用双缓冲
        StdDraw.clear(Color.BLACK);
        StdDraw.setPenColor(Color.WHITE);       // 将笔颜色设置为指定颜色
        Font font = new Font("Monaco", Font.BOLD, 80);   // 设置字体的大小、样式
        StdDraw.setFont(font);                  // 设置为指定字体
        StdDraw.text(MID_WIDTH, QUA_HEIGHT + MID_HEIGHT, "CS61B: THE GAME");
        font = new Font("Monaco", Font.BOLD, 50);
        StdDraw.setFont(font);
        // 画出一众文本
        StdDraw.text(MID_WIDTH, MID_HEIGHT, "New Game (N)");
        StdDraw.text(MID_WIDTH, MID_HEIGHT - 3, "Load Game (L)");
        StdDraw.text(MID_WIDTH, MID_HEIGHT - 6, "Quit (Q)");
        StdDraw.show();
    }
    
    
    // 加载游戏
    private void loadGame() {
        File f = new File("./game.txt");     // TODO:没有这个文件
        if (f.exists()) {
            try {
                FileInputStream fs = new FileInputStream(f);
                ObjectInputStream os = new ObjectInputStream(fs);
                Game loadGame = (Game) os.readObject();
                os.close();
                
                //load saved game state
                this.seed = loadGame.seed;
                this.world = loadGame.world;
                this.gameOver = loadGame.gameOver;
                this.playerPos = loadGame.playerPos;
                this.doorPos = loadGame.doorPos;
                this.hidden = loadGame.hidden;
            } catch (FileNotFoundException e) {
                System.out.println("file not found");
                System.exit(0);
            } catch (IOException e) {
                System.out.println(e);
                System.exit(0);
            } catch (ClassNotFoundException e) {
                System.out.println("class not found");
                System.exit(0);
            }
        } else {
            //no save found, program exits
            System.exit(0);
        }
    }
    
    
    // 保存文件
    private void saveGame() {
        File f = new File("./game.txt");
        try {
            f.createNewFile();
            FileOutputStream fs = new FileOutputStream(f);
            ObjectOutputStream os = new ObjectOutputStream(fs);
            os.writeObject(this);
            os.close();
        } catch (FileNotFoundException e) {
            System.out.println("file not found");
            System.exit(0);
        } catch (IOException e) {
            System.out.println(e);
            System.exit(0);
        }
    }
    
    private void quit(char cmd) {
        if (cmd == ':') {
            while (true) {
                if (StdDraw.hasNextKeyTyped() && StdDraw.nextKeyTyped() == 'Q') {
                    // :Q: save and quit game
                    saveGame();
                    System.exit(0);
                }
            }
        }
    }
    
    /**
     * Interact with world: WSAD or Enter
     */
    private void interact() {
        if (StdDraw.hasNextKeyTyped()) {
            char cmd = StdDraw.nextKeyTyped();
            quit(cmd);
            move(cmd);
        }
    }
    
    /**
     * Character move
     */
    private void move(char c) {
        int x = (c == 'A' ? -1 : (c == 'D' ? 1 : 0));
        int y = (c == 'S' ? -1 : (c == 'W' ? 1 : 0));
        if (!world[playerPos.x + x][playerPos.y + y].equals(Tileset.WALL)
                && !world[playerPos.x + x][playerPos.y + y].equals(Tileset.LOCKED_DOOR)) {
            world[playerPos.x][playerPos.y] = hidden;
            playerPos.x += x;
            playerPos.y += y;
            hidden = world[playerPos.x][playerPos.y];
            world[playerPos.x][playerPos.y] = Tileset.PLAYER;
        }
    }
    
    /**
     * draw and user should be prompted to enter a seed
     */
    private void promptSeed() {
        StringBuilder sb = new StringBuilder();
        while (true) {
            if (StdDraw.hasNextKeyTyped()) {
                char c = StdDraw.nextKeyTyped();
                if (c == 'S') {
                    seed = Long.parseLong(sb.toString());
                    break;
                }
                sb.append(c);
            }
            drawSeedFrame(sb.toString());
        }
    }
    
    /**
     * display description when mouse hovers on tiles
     */
    private void mouseHover() {   // TODO:没搞懂
    
    }
    
    // 种子交互输入时显示的文字
    private void drawSeedFrame(String s) {
        StdDraw.clear(Color.BLACK);
        StdDraw.setPenColor(Color.WHITE);
        Font font = new Font("Monaco", Font.BOLD, 60);
        StdDraw.setFont(font);
        StdDraw.text(MID_WIDTH, QUA_HEIGHT + MID_HEIGHT, "Please enter your seed:");
        StdDraw.text(MID_WIDTH, QUA_HEIGHT + MID_HEIGHT - 5, s);
        StdDraw.show();
    }
    
    
    
    // 初始化角色和大门的位置
    private void initializePosition() {
        for (int i = 0; i < WIDTH; i++) {
            for (int j = 0; j < HEIGHT; j++) {
                if (world[i][j].equals(Tileset.PLAYER)) {
                    playerPos = new Position(i, j);
                }
                if (world[i][j].equals(Tileset.UNLOCKED_DOOR)) {
                    doorPos = new Position(i, j);
                }
            }
        }
    }
}
