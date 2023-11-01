package byog.Core;

public class Room implements Comparable<Room> {   // 继承了comparable的泛型，并将泛型改为Room。
    private Position p;
    private int width;
    private int height;
    
    public Room(Position P, int Width, int Height) {  // 构建新房间
        p = P;
        width = Width;
        height = Height;
    }
    
    public boolean overlap(Room room) {     // 判断两个房间是否重叠
        for (int i = p.x; i < p.x + width; i++) {
            for (int j = p.y; j < p.y + height; j++) {
                for (int k = room.p.x; k < room.p.x + room.width; k++) {
                    for (int l = room.p.y; l < room.p.y + room.height; l++) {
                        if (i == k && j == l) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }
    
    @Override
    public int compareTo(Room r) {
        return this.p.x - r.p.x;
    }
    
    public Position getP() {
        return this.p;
    }
    
    public int getWidth() {
        return this.width;
    }
    
    public int getHeight() {
        return this.height;
    }
}
