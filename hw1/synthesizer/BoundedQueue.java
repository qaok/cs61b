package synthesizer;
public interface BoundedQueue<T> extends Iterable<T> {
    int capacity();     // return size of the buffer                        项目容量
    int fillCount();    // return number of items currently in the buffer   项目数量
    void enqueue(T x);  // add item x to the end                            在尾部增加项目
    T dequeue();        // delete and return item from the front            删除头个值并返回该值
    T peek();           // return (but do not delete) item from the front   返回头个值
    default boolean isEmpty() {   // is the buffer empty (fillCount equals zero)?
        return fillCount() == 0;
    }
    default boolean isFull() {   // is the buffer full (fillCount is same as capacity)?
        return fillCount() == capacity();
    }
}
