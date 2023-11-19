import edu.princeton.cs.algs4.Queue;
import org.junit.Test;

public class MergeSort {
    /**
     * Removes and returns the smallest item that is in q1 or q2.
     *
     * The method assumes that both q1 and q2 are in sorted order, with the smallest item first. At
     * most one of q1 or q2 can be empty (but both cannot be empty).
     *
     * @param   q1  A Queue in sorted order from least to greatest.
     * @param   q2  A Queue in sorted order from least to greatest.
     * @return      The smallest item that is in q1 or q2.
     */
    private static <Item extends Comparable> Item getMin(
            Queue<Item> q1, Queue<Item> q2) {
        if (q1.isEmpty()) {
            return q2.dequeue();
        } else if (q2.isEmpty()) {
            return q1.dequeue();
        } else {
            // Peek at the minimum item in each queue (which will be at the front, since the
            // queues are sorted) to determine which is smaller.
            Comparable q1Min = q1.peek();
            Comparable q2Min = q2.peek();
            if (q1Min.compareTo(q2Min) <= 0) {
                // Make sure to call dequeue, so that the minimum item gets removed.
                return q1.dequeue();
            } else {
                return q2.dequeue();
            }
        }
    }

    /** Returns a queue of queues that each contain one item from items. */
    private static <Item extends Comparable> Queue<Queue<Item>>
            makeSingleItemQueues(Queue<Item> items) {
        // Your code here!
        Queue<Queue<Item>> itemsi = new Queue<>();       // 新建一个元素为Queue<Item>的queue
        for (Item item: items) {
            Queue<Item> siq = new Queue<>();
            siq.enqueue(item);
            itemsi.enqueue(siq);
        }
        return itemsi;
    }

    /**
     * Returns a new queue that contains the items in q1 and q2 in sorted order.
     *
     * This method should take time linear in the total number of items in q1 and q2.  After
     * running this method, q1 and q2 will be empty, and all of their items will be in the
     * returned queue.
     *
     * @param   q1  A Queue in sorted order from least to greatest.
     * @param   q2  A Queue in sorted order from least to greatest.
     * @return      A Queue containing all of the q1 and q2 in sorted order, from least to
     *              greatest.
     *
     */
    private static <Item extends Comparable> Queue<Item> mergeSortedQueues(
            Queue<Item> q1, Queue<Item> q2) {
        // Your code here!
        Queue<Item> newQueue = new Queue<>();
        while (!q1.isEmpty() || !q2.isEmpty()) {  // 任意一个queue不为空即可
            Item curitem = getMin(q1, q2);
            newQueue.enqueue(curitem);
        }
        return newQueue;
    }

    /** Returns a Queue that contains the given items sorted from least to greatest. */
    public static <Item extends Comparable> Queue<Item> mergeSort(
            Queue<Item> items) {
        // Your code here!
        if (items.size() <= 1) {  //元素过少
            return items;
        }
        Queue<Item> left = new Queue<>();
        Queue<Item> right = new Queue<>();
        int halfLength = items.size() / 2;
        // 此处也可以选择makeSingleItemQueues方法
        for (Item item: items) {
            if (halfLength > 0) {
                left.enqueue(item);
            } else {
                right.enqueue(item);
            }
            halfLength -= 1;
        }
        Queue<Item> leftSorted = mergeSort(left);    // 继续分区排序
        Queue<Item> rightSorted = mergeSort(right);  // 同上
        Queue<Item> newitems = mergeSortedQueues(leftSorted, rightSorted);  // 合并排序
        
        return newitems;
    }
    
    @Test
    public static void main(String[] args) {
        Queue<String> students = new Queue<String>();
        students.enqueue("Alice");
        students.enqueue("Vanessa");
        students.enqueue("Ethan");
        students.enqueue("Bthan");
        System.out.println(students);
        System.out.println(MergeSort.mergeSort(students));
    }
}
