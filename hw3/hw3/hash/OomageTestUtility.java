package hw3.hash;

import java.util.List;

public class OomageTestUtility {
    public static boolean haveNiceHashCodeSpread(List<Oomage> oomages, int M) {
        /*
         * Write a utility function that returns true if the given oomages
         * have hashCodes that would distribute them fairly evenly across
         * M buckets. To do this, convert each oomage's hashcode in the
         * same way as in the visualizer, i.e. (& 0x7FFFFFFF) % M.
         * and ensure that no bucket has fewer than N / 50
         * Oomages and no bucket has more than N / 2.5 Oomages.
         */
        int N = oomages.size();             // oomages的数量
        int[] numInBucket = new int[M];     // 新建整数array
        for (Oomage o : oomages) {          // 遍历oomages中的每一个oomage o
            int bucketNum = (o.hashCode() & 0x7FFFFFFF) % M;    // oomage o在bucket中的index
            numInBucket[bucketNum] += 1;                        // 该bucket的元素数量加一
        }
        for (int num : numInBucket) {       // 遍历每个bucket，判断是否为a nice spread
            if (num <= 1.0 * N / 50 || num >= 1.0 * N / 2.5) {
                return false;
            }
        }
        return true;
    }
}
