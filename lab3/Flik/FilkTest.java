import static org.junit.Assert.*;
import org.junit.Test;

public class FilkTest {
    
    @Test
    public void testIsSameNumber() {
        Integer a, b;
        a = 0;
        b = 0;
        for (a = 0, b = 0; a < 500; a++, b++) {
            assertTrue(Flik.isSameNumber(a, b));
        }
    }
}
