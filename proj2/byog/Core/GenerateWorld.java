package byog.Core;

import byog.TileEngine.TERenderer;
import byog.TileEngine.TETile;
import byog.TileEngine.Tileset;

import java.util.Random;
import java.util.ArrayList;
import java.util.Collections;


public class GenerateWorld {
    private static long seed;
    private static int worldWidth;
    private static int worldHeight;
    private static Random rand;
    private static TETile[][] world;
    private static ArrayList<Room> roomList;
    
    public static TETile[][] Generate(int Width, int Height, long Seed) {
        seed = Seed;
        worldWidth = Width;
        worldHeight = Height;
        rand = new Random(seed);
        world = new TETile[worldWidth][worldHeight];
        roomList = new ArrayList<>();
        fillNothing(world);
        
        int count = RandomUtils.uniform(rand, 40, 50);     // TODO:随机生成什么东西，暂时还没搞明白
        for (int i = 0; i < count; i += 1) {                     // 随机生成房间的宽和高，然后利用drawRoom画出来
            Position p = new Position(rand.nextInt(100), rand.nextInt(100));
            int roomWidth = RandomUtils.uniform(rand, 2, 10);   // TODO:随机生成什么东西，暂时还没搞明白
            int roomHeight = RandomUtils.uniform(rand, 2, 10);  // TODO:随机生成什么东西，暂时还没搞明白
            drawRoom(world, p, roomWidth, roomHeight);
        }
        
        Collections.sort(roomList);     // TODO：这应该是收集生成的房间数组，但是暂时不能肯定
        for (int i = 0; i < roomList.size() - 1; i++) {  // TODO：这里没搞明白
            drawLHallway(world, randomPointInRoom(roomList.get(i)), randomPointInRoom(roomList.get(i + 1)));
        }
        
        //此处生成角色和大门
        Position p = randomPointInRoom(roomList.get(0));
        world[p.x][p.y] = Tileset.PLAYER;
        p = randomPointInWall(roomList.get(0));
        world[p.x][p.y] = Tileset.LOCKED_DOOR;
        
        return world;
    }
    
    // 画房间的类
    private static void drawRoom(TETile[][] tiles, Position p, int Width, int Height) {
        int outerWidth = Width + 2;
        int outerHeight = Height + 2;
        Room room = new Room(p, outerWidth, outerHeight);
        boolean val = true;
       
        for (int i = 0; i < roomList.size(); i++) {
            if (room.overlap(roomList.get(i))) {      // 如果房间重叠了，val就为假
                val = false;
                break;
            }
        }
        
        // 如果房间边界超出了世界边界，那val值也为false
        if (p.x + outerWidth > worldWidth || p.y + outerHeight > worldHeight) {
            val = false;
        }
        if (val) {        // 只有val值为真才能进行此循环
            TETile tile;
            for (int i = p.x; i < p.x + outerWidth; i++) {    // 画出房间的墙和地板
                for (int j = p.y; j < p.y + outerHeight; j++) {
                    if (i == p.x || i == p.x + outerWidth - 1 || j == p.y || j == p.y + outerHeight - 1) {
                        tile = Tileset.WALL;
                    } else {
                        tile = Tileset.FlOOR2;
                    }
                    tiles[i][j] = tile;
                }
            }
            roomList.add(room);        // room list数组里加入一所房间
        }
    }
    
    // 画房间为矩形的类
    private static void drawRectangle(TETile[][] tiles, TETile tile, Position p, int width, int height) {
        for (int i = p.x; i < p.x + width; i++) {
            for (int j = p.y; j < p.y + height; j++) {
                tiles[i][j] = tile;    // 到这一步时，并未将矩形房间进行填充，因为tile还没确定Tileset
            }
        }
    }
    
    // 画水平线
    private static void drawHorizontalLine(TETile[][] tiles, TETile tile, Position p, int width) {
        for (int i = p.x; i < p.x + width + 1; i++) {
            if (tiles[i][p.y] != Tileset.FLOOR) {
                tiles[i][p.y] = tile;   // 此步时，同上
            }
        }
    }
    
    // 画垂直线
    private static void drawVerticalLine(TETile[][] tiles, TETile tile, Position p, int height) {
        for (int i = p.y; i < p.y + height; i++) {
            if (tiles[p.x][i] != Tileset.FLOOR) {
                tiles[p.x][i] = tile;   // 此步时，同上
            }
        }
    }
    
    // 画L线
    private static void drawLLine(TETile[][] tiles, TETile tile, Position p1, Position p2) {
        int width = p2.x - p1.x;
        int height = Math.abs(p1.y - p2.y);
        if (p1.y < p2.y) {      // 这里利用了前面所写的类
            drawHorizontalLine(tiles, tile, p1, width);
            drawVerticalLine(tiles, tile, new Position(p2.x, p1.y), height);
        } else if (p1.y > p2.y) {
            drawHorizontalLine(tiles, tile, p1, width);
            drawVerticalLine(tiles, tile, p2, height);
        } else {
            drawHorizontalLine(tiles, tile, p1, width);
        }
    }
    
    // 画L形走廊
    private static void drawLHallway(TETile[][] tiles, Position p1, Position p2) {
        TETile t = Tileset.FlOOR2;      // 这里开始确定了TETile的填充了
        drawLLine(tiles, t, p1, p2);
        t = Tileset.WALL;               // 画墙
        Position p3, p4, p5, p6;
        if (p1.y < p2.y) {
            p3 = new Position(p1.x, p1.y + 1);
            p5 = new Position(p1.x, p1.y - 1);
        } else {
            p3 = new Position(p1.x, p1.y - 1);
            p5 = new Position(p1.x, p1.y + 1);
        }
        p4 = new Position(p2.x - 1, p2.y);
        p6 = new Position(p2.x + 1, p2.y);
        drawLLine(tiles, t, p3, p4);     // 画地板
        drawLLine(tiles, t, p5, p6);     // 画地板
    }
    
    // 随机生成房间的位置
    private static Position randomPointInRoom(Room r) {
        Position rPos = r.getP();
        int randX = RandomUtils.uniform(rand, rPos.x + 1, rPos.x + r.getWidth() - 2);
        int randY = RandomUtils.uniform(rand, rPos.y + 1, rPos.y + r.getHeight() - 2);
        return new Position(randX, randY);
    }
    
    // 随机生成墙的位置
    private static Position randomPointInWall(Room r) {
        Position rPos = r.getP();
        int randX = 0;
        int randY = 0;
        while (!world[randX][randY].equals(Tileset.WALL)) {
            randX = RandomUtils.uniform(rand, rPos.x, rPos.x + r.getWidth() - 1);
            randY = RandomUtils.uniform(rand, rPos.y, rPos.y + r.getHeight() - 1);
        }
        return new Position(randX, randY);
    }
    
    
    private static void fillNothing(TETile[][] world) {
        for (int x = 0; x < worldWidth; x += 1) {           // 初始化世界
            for (int y = 0; y < worldHeight; y += 1) {
                world[x][y] = Tileset.NOTHING;
            }
        }
    }
}
