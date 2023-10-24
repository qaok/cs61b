import static org.junit.Assert.*;
import org.junit.Test;

public class TestArrayDequeGold {
    
    @Test
    public void testStudentArrayDeque() {
        StudentArrayDeque<Integer> actual = new StudentArrayDeque<>();
        for (int i = 0; i < 10; i += 1) {
            double numberBetweenZeroAndOne = StdRandom.uniform();
            if (numberBetweenZeroAndOne < 0.5) {
                actual.addLast(i);
            } else {
                actual.addFirst(i);
            }
        }
        Integer actual1 = actual.removeLast();
        Integer actual2 = actual.removeFirst();
        Integer actual3 = actual.removeFirst();
        
        ArrayDequeSolution<Integer> expected = new ArrayDequeSolution<>();
        for (int i = 0; i < 10; i += 1) {
            double numberBetweenZeroAndOne = StdRandom.uniform();
            if (numberBetweenZeroAndOne < 0.5) {
                expected.addLast(i);
            } else {
                expected.addFirst(i);
            }
        }
        Integer expected1 = expected.removeLast();
        Integer expected2 = expected.removeFirst();
        Integer expected3 = expected.removeFirst();
        
        

        assertEquals("Oh noooo!\nThis is bad:\n   Random number " + actual1
                        + " not equal to " + expected1 + "!",
                expected1, actual1);
        assertEquals("Oh noooo!\nThis is bad:\n   Random number " + actual2
                        + " not equal to " + expected2 + "!",
                expected2, actual2);
        assertEquals("Oh noooo!\nThis is bad:\n   Random number " + actual3
                        + " not equal to " + expected3 + "!",
                expected3, actual3);
    }
}
