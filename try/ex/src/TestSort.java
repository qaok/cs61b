improt org.junit.Assert;
import org.junit.Test;
public class TestSort {
    public static void testSort() {
        String[] input = {"cows", "dwell", "above", "clouds"};
        String[] expected = {"above", "cows", "clouds", "dwell"};
        Sort.sort(input);

        org.junit.Assert.assertArrayEquals(expected, input);
    }

    public static void main(String[] args) {
        testSort();
    }
}
