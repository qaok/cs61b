package byog.lab5;
import org.junit.Test;
import static org.junit.Assert.*;

import byog.Core.Position;

import byog.TileEngine.TERenderer;
import byog.TileEngine.TETile;
import byog.TileEngine.Tileset;

import java.util.Random;

/**
 * Draws a world consisting of hexagonal regions.
 */
public class HexWorld {
    private static final int WIDTH = 100;
    private static final int HEIGHT = 60;
    private static final long SEED = 2873123;
    private static final Random RANDOM = new Random(SEED);
    
    
    /**
     * Computes the width of row i for a size s hexagon.
     * @param s The size of the hex.               //s为六边形的边长
     * @param i The row number where i = 0 is the bottom row.
     * @return
     */
    public static int hexRowWidth(int s, int i) {
        int effectiveI = i;
        if (i >= s) {
            effectiveI = 2 * s - 1 - effectiveI;
        }
        
        return s + 2 * effectiveI;
    }
    
    /**
     * Computesrelative x coordinate of the leftmost tile in the ith
     * row of a hexagon, assuming that the bottom row has an x-coordinate
     * of zero. For example, if s = 3, and i = 2, this function
     * returns -2, because the row 2 up from the bottom starts 2 to the left
     * of the start position, e.g.
     *   xxxx
     *  xxxxxx
     * xxxxxxxx
     * xxxxxxxx <-- i = 2, starts 2 spots to the left of the bottom of the hex
     *  xxxxxx
     *   xxxx
     *
     * @param s size of the hexagon
     * @param i row num of the hexagon, where i = 0 is the bottom
     * @return
     */
    public static int hexRowOffset(int s, int i) {
        int effectiveI = i;
        if (i >= s) {
            effectiveI = 2 * s - 1 - effectiveI;
        }
        return -effectiveI;
    }
    
    /** Adds a row of the same tile.
     * @param world the world to draw on
     * @param p the leftmost position of the row
     * @param width the number of tiles wide to draw
     * @param t the tile to draw
     */
    public static void addRow(TETile[][] world, Position p, int width, TETile t) {
        for (int xi = 0; xi < width; xi += 1) {
            int xCoord = p.x + xi;
            int yCoord = p.y;
            world[xCoord][yCoord] = TETile.colorVariant(t, 32, 32, 32, RANDOM);
        }
    }
    
    /**
     * Adds a hexagon to the world.
     * @param world the world to draw on
     * @param p the bottom left coordinate of the hexagon
     * @param s the size of the hexagon
     * @param t the tile to draw
     */
    public static void addHexagon(TETile[][] world, Position p, int s, TETile t) {
        
        if (s < 2) {
            throw new IllegalArgumentException("Hexagon must be at least size 2.");
        }
        
        // hexagons have 2*s rows. this code iterates up from the bottom row,
        // which we call row 0.
        for (int yi = 0; yi < 2 * s; yi += 1) {
            int thisRowY = p.y + yi;
            
            int xRowStart = p.x + hexRowOffset(s, yi);
            Position rowStartP = new Position(xRowStart, thisRowY);
            
            int rowWidth = hexRowWidth(s, yi);
            
            addRow(world, rowStartP, rowWidth, t);
            
        }
    }
    
    public static void main(String[] args) {
        TERenderer teRenderer = new TERenderer();
        teRenderer.initialize(WIDTH, HEIGHT);
        TETile[][] world = new TETile[WIDTH][HEIGHT];
        for (int x = 0; x < WIDTH; x += 1) {
            for (int y = 0; y < HEIGHT; y += 1) {
                world[x][y] = Tileset.NOTHING;
            }
        }
        Position position = new Position(5, 15);   //开始位置
        HexWorld.addHexagon(world, position, 2, Tileset.FLOWER);
        HexWorld.addHexagon(world, new Position(12, 13), 3, Tileset.FlOOR1);
        HexWorld.addHexagon(world, new Position(22, 11), 4, Tileset.WATER);
        HexWorld.addHexagon(world, new Position(35, 9), 5, Tileset.GRASS);
        
        HexWorld.addHexagon(world, new Position(6, 19), 5, Tileset.FlOOR1);
        HexWorld.addHexagon(world, new Position(15, 19), 5, Tileset.FlOOR2);
        HexWorld.addHexagon(world, new Position(28, 19), 5, Tileset.FlOOR2);
        HexWorld.addHexagon(world, new Position(41, 19), 5, Tileset.FlOOR1);
        HexWorld.addHexagon(world, new Position(54, 19), 5, Tileset.FlOOR3);
        
        HexWorld.addHexagon(world, new Position(45, 29), 5, Tileset.SAND);
        HexWorld.addHexagon(world, new Position(50, 29), 5, Tileset.GRASS);
        HexWorld.addHexagon(world, new Position(55, 29), 5, Tileset.GRASS);
        HexWorld.addHexagon(world, new Position(60, 29), 5, Tileset.SAND);
        
        
        
        
        teRenderer.renderFrame(world);
    }
}
