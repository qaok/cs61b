import org.junit.Test;
import static org.junit.Assert.*;

public class TestOffByN {
    static CharacterComparator offByN;
    
    @Test
    public void testOffByN() {
        offByN = new OffByN(5);
        assertTrue(offByN.equalChars('a','f'));
        assertTrue(offByN.equalChars('f','a'));
        assertFalse(offByN.equalChars('f', 'h'));
    }
}
