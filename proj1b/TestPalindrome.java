import org.junit.Test;
import static org.junit.Assert.*;

public class TestPalindrome {
    // You must use this palindrome, and not instantiate
    // new Palindromes, or the autograder might be upset.
    static Palindrome palindrome = new Palindrome();
    static CharacterComparator cc;

    @Test
    public void testWordToDeque() {
        Deque d = palindrome.wordToDeque("persiflage");
        String actual = "";
        for (int i = 0; i < "persiflage".length(); i++) {
            actual += d.removeFirst();
        }
        assertEquals("persiflage", actual);
    }
    
    @Test
    public void testisPalindrome() {
        assertFalse(palindrome.isPalindrome("cat"));
        assertTrue(palindrome.isPalindrome("a"));
        assertTrue(palindrome.isPalindrome("racecar"));
        assertTrue(palindrome.isPalindrome("shannahs"));
        assertFalse(palindrome.isPalindrome("interesting"));
        assertFalse(palindrome.isPalindrome("Racecar"));
    }
    
    @Test
    public void testisPalindromes() {
        assertFalse(palindrome.isPalindrome("cat", cc));
        assertTrue(palindrome.isPalindrome("flake", cc));
        assertTrue(palindrome.isPalindrome("arqb", cc));
        assertFalse(palindrome.isPalindrome("aaaa", cc));
        assertFalse(palindrome.isPalindrome("azab", cc));
    }
}
