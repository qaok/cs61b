package byog.Core;

import byog.TileEngine.TERenderer;
import byog.TileEngine.TETile;

public class MAPTEST {
    private static final int WIDTH = 80;
    private static final int HEIGHT = 80;
    public static void main(String[] args) {
        TERenderer ter = new TERenderer();
        ter.initialize(WIDTH, HEIGHT);
        TETile[][] world = GenerateWorld.Generate(WIDTH,HEIGHT, 1955);
        ter.renderFrame(world);
    }
}
