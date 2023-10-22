public class Palindrome {
    public Deque<Character> wordToDeque(String word) {
        Deque<Character> chars = new LinkedListDeque<>();
        for (int i = 0; i < word.length(); i++) {
            chars.addLast(word.charAt(i));
        }
        return chars;
    }
    
    public boolean isPalindrome(String word) {
        Deque<Character> chars = wordToDeque(word);
        return isPalindromehelper(chars);
    }
    
    private boolean isPalindromehelper(Deque<Character> chars) {
        if (chars.size() < 2) {
            return true;
        } else if (chars.removeFirst() != chars.removeLast()) {
            return false;
        }
        return isPalindromehelper(chars);
    }
    
    public boolean isPalindrome(String word, CharacterComparator cc) {
        Deque<Character> chars = wordToDeque(word);
        return isPalindromehelper(chars, cc);
    }
    
    private boolean isPalindromehelper(Deque<Character> chars, CharacterComparator cc) {
        if (chars.size() < 2) {
            return true;
        } else if (!cc.equalChars(chars.removeFirst(), chars.removeLast())) {
            return false;
        }
        return isPalindromehelper(chars, cc);
    }
    
}
