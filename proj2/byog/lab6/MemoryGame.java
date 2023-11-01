package byog.lab6;



import java.awt.Color;
import java.awt.Font;
import java.util.Random;

public class MemoryGame {
    private int width;
    private int height;
    private int round;
    private Random rand;
    private boolean gameOver;
    private boolean playerTurn;
    private static final char[] CHARACTERS = "abcdefghijklmnopqrstuvwxyz".toCharArray();
    private static final String[] ENCOURAGEMENT = {"You can do this!", "I believe in you!",
                                                   "You got this!", "You're a star!", "Go Bears!",
                                                   "Too easy for you!", "Wow, so impressive!"};

    public static void main(String[] args) {      // 交互输入回合数
        if (args.length < 1) {
            System.out.println("Please enter a seed");
            return;
        }

        long seed = Long.parseLong(args[0]);
        MemoryGame game = new MemoryGame(40, 40, seed);
        game.startGame();
    }

    public MemoryGame(int width, int height, long seed) {
        /* Sets up StdDraw so that it has a width by height grid of 16 by 16 squares as its canvas
         * Also sets up the scale so the top left is (0,0) and the bottom right is (width, height)
         */
        this.width = width;
        this.height = height;
        StdDraw.setCanvasSize(this.width * 16, this.height * 16);
        Font font = new Font("Monaco", Font.BOLD, 30);
        StdDraw.setFont(font);
        StdDraw.setXscale(0, this.width);
        StdDraw.setYscale(0, this.height);
        StdDraw.clear(Color.BLACK);
        StdDraw.enableDoubleBuffering();

        //TODO: Initialize random number generator
        rand = new Random(seed);
    }

    public String generateRandomString(int n) {       // 随机生成挑战文字
        //TODO: Generate random string of letters of length n
        StringBuilder sb = new StringBuilder();
        while (sb.length() < n) {
            sb.append(CHARACTERS[rand.nextInt(CHARACTERS.length)]);
        }
        return sb.toString();
    }

    public void drawFrame(String s) {
        //TODO: Take the string and display it in the center of the screen
        //TODO: If game is not over, display relevant game information at the top of the screen
        int midWidth = width / 2;
        int midHeight = height / 2;
        
        StdDraw.clear();                // 将画布清除为白色
        StdDraw.clear(Color.black);     // 将画布清除为指定颜色，此处为黑色
        
        // Draw the GUI
        if (!gameOver) {
            Font smallFont = new Font("Monaco", Font.BOLD, 20);
            StdDraw.setFont(smallFont);
            StdDraw.textLeft(1, height - 1, "Round: " + round);
            StdDraw.text(midWidth, height - 1, playerTurn ? "Type!" : "Watch!");
            StdDraw.textRight(width - 1, height - 1, ENCOURAGEMENT[round % ENCOURAGEMENT.length]);
            StdDraw.line(0, height - 2, width, height - 2);
        }
        
        // Draw the actual text
        Font bigFont = new Font("Monaco", Font.BOLD, 30);   // 设置字体、大小、样式
        StdDraw.setFont(bigFont);                  // 设置字体
        StdDraw.setPenColor(Color.white);          // 设置笔颜色
        StdDraw.text(midWidth, midHeight, s);      // 使用文本对图形进行注释
        StdDraw.show();                            // 展示
    }

    public void flashSequence(String letters) {   // 文字出现的时长及间隔时长
        //TODO: Display each character in letters, making sure to blank the screen between letters
        for (int i = 0; i < letters.length(); i++) {
            drawFrame(letters.substring(i, i + 1));
            StdDraw.pause(1000);
            drawFrame(" ");
            StdDraw.pause(500);
        }
    }

    public String solicitNCharsInput(int n) {     // 识别来自玩家的输入字母数量及返回玩家输入的字母
        //TODO: Read n letters of player input
        String input = "";
        drawFrame(input);
        
        while (input.length() < n) {
            if (!StdDraw.hasNextKeyTyped()) {
                continue;
            }
            char key = StdDraw.nextKeyTyped();
            input += String.valueOf(key);
            drawFrame(input);
        }
        StdDraw.pause(500);
        return input;
    }

    public void startGame() {
        //TODO: Set any relevant variables before the game starts
        gameOver = false;
        playerTurn = false;
        round = 1;
        
        //TODO: Establish Game loop
        while (!gameOver) {
            playerTurn = false;
            drawFrame("Round " + round + "! Good luck!");   // 上方出现的回合、鼓励语等其他信息
            StdDraw.pause(1500);
            
            String roundString = generateRandomString(round);
            flashSequence(roundString);
            
            playerTurn = true;
            String userInput = solicitNCharsInput(round);
            
            if (!userInput.equals(roundString)) {             // 判断输入文字是否与预期文字相同
                gameOver = true;
                drawFrame("Game Over! Final level: " + round);
            } else {
                drawFrame("Correct, well done!");
                StdDraw.pause(1500);
                round += 1;
            }
        }
    }

}
