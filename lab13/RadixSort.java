/**
 * Class for doing Radix sort
 *
 * @author qaok
 *
 */
public class RadixSort {
    /**
     * Does LSD radix sort on the passed in array with the following restrictions:
     * The array can only have ASCII Strings (sequence of 1 byte characters)
     * The sorting is stable and non-destructive
     * The Strings can be variable length (all Strings are not constrained to 1 length)
     *
     * @param asciis String[] that needs to be sorted
     *
     * @return String[] the sorted array
     */
    public static String[] sort(String[] asciis) {
        // Implement LSD Sort
        String[] array = new String[asciis.length];      // 复制asciis array
        for (int i = 0; i < asciis.length; i++) {
            array[i] = new String(asciis[i]);
        }
        
        int maxLength = 0;
        for (String str : asciis) {                      // 找出最长的string
            maxLength = Math.max(maxLength, str.length());
        }
        for (int i = maxLength - 1; i >= 0; i--) {        // 按最大的string来确定长度
            sortHelperLSD(array, i);                      // 从个位数开始排起，依次往十位数排，直到排完最长的string的digits
        }
        return array;
    }

    /**
     * LSD helper method that performs a destructive counting sort the array of
     * Strings based off characters at a specific index.
     * @param asciis Input array of Strings
     * @param index The position to sort the Strings on.
     */
    private static void sortHelperLSD(String[] asciis, int index) {
        // Optional LSD helper method for required LSD radix sort
        int R = 256;
        int[] count = new int[R];
        int[] starts = new int[R];
        int[] indexs = new int[asciis.length];
        
        // 获得asciis array中的每一个string在index处的ascii值
        for (int i = 0; i < asciis.length; i++) {
            int asciiNums;
            try {
                //System.out.println(asciis[i].charAt(index));
                asciiNums = (int) asciis[i].charAt(index);
            } catch (StringIndexOutOfBoundsException e) {
                asciiNums = 0;
            }
            // 此处按照原来在asciis中每个string的顺序放入indexs array中
            indexs[i] = asciiNums;
            count[asciiNums]++;      // counts中相同的index则数值加一
        }
        
        /**for (int i = 0; i < starts.length - 1; i++) {  // 调整starts的index
            starts[i + 1] = starts[i] + count[i];
        }*/
        int pos = 0;
        for (int i = 0; i < R; i++) {
            starts[i] = pos;
            pos += count[i];
        }
        String[] sortedAsciis = new String[asciis.length];
        for (int i = 0; i < asciis.length; i++) {      // 此处沿用CountingSort方法
            int item = indexs[i];
            int place = starts[item];
            sortedAsciis[place] = asciis[i];
            starts[item]++;
        }
        
        for (int i = 0; i < sortedAsciis.length; i++) {  // 放入signature中的“asciis array”中
            asciis[i] = sortedAsciis[i];
        }
        
        return;
    }
    
    public static void main(String[] args) {

        String[] strings = new String[]{"35006", "112", "9040", "294", "9209", "8200", "394", "8210"};
        strings = sort(strings);
        for (String string : strings) {
            System.out.println(string);
        }
        
    }

    /**
     * MSD radix sort helper function that recursively calls itself to achieve the sorted array.
     * Destructive method that changes the passed in array, asciis.
     *
     * @param asciis String[] to be sorted
     * @param start int for where to start sorting in this method (includes String at start)
     * @param end int for where to end sorting in this method (does not include String at end)
     * @param index the index of the character the method is currently sorting on
     *
     **/
    private static void sortHelperMSD(String[] asciis, int start, int end, int index) {
        // Optional MSD helper method for optional MSD radix sort
        return;
    }
}
