package byog.Core;

import java.io.Serializable; //序列化，只是一个空接口，无需任何方法。
public class Position implements Serializable {
    public int x;
    public int y;
    
    private static final long serialVersionUID = 123123123123124L;
    
    public Position(int x, int y) {
        this.x = x;
        this.y = y;
    }
}
