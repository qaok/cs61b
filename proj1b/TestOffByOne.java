import org.junit.Test;
import static org.junit.Assert.*;

public class TestOffByOne {
    
    // You must use this CharacterComparator and not instantiate
    // new ones, or the autograder might be upset.
    static CharacterComparator offByOne = new OffByOne();

    // Your tests go here.
    @Test
    public void testequalChars() {
        assertTrue(offByOne.equalChars('a', 'b'));
        assertTrue(offByOne.equalChars('r', 'q'));
        assertTrue(offByOne.equalChars('A', 'B'));
        assertTrue(offByOne.equalChars('3', '2'));
        assertTrue(offByOne.equalChars('&', '%'));
        
        assertFalse(offByOne.equalChars('a', 'A'));
        assertFalse(offByOne.equalChars('B', 'b'));
        assertFalse(offByOne.equalChars('A', 'C'));
        assertFalse(offByOne.equalChars('a', 'e'));
        assertFalse(offByOne.equalChars('z', 'a'));
        assertFalse(offByOne.equalChars('a', 'a'));
    }
}
