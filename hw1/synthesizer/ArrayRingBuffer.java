package synthesizer;

import java.util.Iterator;

public class ArrayRingBuffer<T> extends AbstractBoundedQueue<T> {
    /* Index for the next dequeue or peek. */
    private int first;            // index for the next dequeue or peek
    /* Index for the next enqueue. */
    private int last;
    /* Array for storing the buffer data. */
    private T[] rb;

    /**
     * Create a new ArrayRingBuffer with the given capacity.
     */
    public ArrayRingBuffer(int capacity) {
        rb = (T[]) new Object[capacity];
        first = 0;
        last = 0;
        fillCount = 0;
        this.capacity = capacity;
    }
 
    /**
     * Adds x to the end of the ring buffer. If there is no room, then
     * throw new RuntimeException("Ring buffer overflow"). Exceptions
     * covered Monday.
     */
    
    public void enqueue(T x) {
        if (isFull()) {
            throw new RuntimeException("Ring Buffer Overflow");
        }
        fillCount += 1;
        rb[last] = x;
        last += 1;
        if (last == capacity) {
            last = 0;
        }
    }

    /**
     * Dequeue the oldest item in the ring buffer. If the buffer is empty, then
     * throw new RuntimeException("Ring buffer underflow"). Exceptions
     * covered Monday.
     */
    
    public T dequeue() {
        if (isEmpty()) {
            throw new RuntimeException("Ring Buffer Underflow");
        }
        T value = rb[first];
        fillCount -= 1;
        first += 1;
        if (first == capacity) {
            first = 0;
        }
        return value;
    }

    /**
     * Return oldest item, but don't remove it.
     */
    
    public T peek() {
        if (isEmpty()) {
            throw new RuntimeException("Ring Buffer underflow");
        }
        return rb[first];
    }

    // TODO: When you get to part 5, implement the needed code to support iteration.
    @Override
    public Iterator<T> iterator() {
        return new ArrayRingBufferIterator();
    }
    
    private class ArrayRingBufferIterator implements Iterator<T> {
        private int index;
        private int fcs;
        
        public ArrayRingBufferIterator() {
            this.index = (first - 1 + capacity) % capacity;  // 将first的索引减去1，以便于后续循环操作
            this.fcs = fillCount;                           // 表明项目现有数量
        }
        
        public boolean hasNext() {
            return fcs > 0;                          // 只要现有数量大于0，即有next
        }
        
        public T next() {
            if (hasNext()) {
                fcs -= 1;                           // 将项目数量减少，避免重复循环
                index = (index + 1) % capacity;     // 将first索引重归原来，同时开启ring循环
                return rb[index];                   // 将得到的项目值一一返回
            }
            return null;
        }
    }
    
    /**public static void main(String[] args) {
        ArrayRingBuffer<Integer> arb = new ArrayRingBuffer<>(5);
        arb.enqueue(3);
        arb.enqueue(6);
        arb.enqueue(7);
        for (int x : arb) {
            for (int y : arb) {
                System.out.println("x: " + x + ", y: " + y);
            }
        }
    }*/
}
