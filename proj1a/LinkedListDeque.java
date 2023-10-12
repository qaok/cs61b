public class LinkedListDeque<T> {
    private class Deque {
        public Deque prev;
        public T item;
        public Deque next;

        public Deque(Deque p, T i, Deque n) {
            prev = p;
            item = i;
            next = n;
        }
    } 

    private Deque sentFront;
    private Deque sentBack;
    private int size;


    public LinkedListDeque() {
        sentFront = new Deque(null, (T) new Object(), null);
        sentBack = sentFront;
        size = 0;
    }

    public void addFirst(T item) {
        sentFront.next = new Deque(sentFront, item, sentBack);
        size += 1;
    }

    public void addLast(T item) {
        sentBack.prev = new Deque(sentFront, item, sentBack);
        size += 1;
    }

    public boolean isEmpty() {
        if (size == 0) {
            return true;
        }
        return false;
    }

    public int size() {
        return size;
    }

    public void printDeque() {
        Deque ptr = sentFront;
        while (ptr.next != null) {
            ptr = ptr.next;
            System.out.print(ptr.item);
            System.out.print(" ");
        }
        System.out.println(" ");
    }

    public T removeFirst() {
        if (size == 0) {
            return null;
        }
        Deque removedeque = sentFront.prev;
        size -= 1;
        return removedeque.item;
    }

    public T removeLast() {
        if (size == 0) {
            return null;
        }
        Deque removedeque = sentBack;
        sentBack = removedeque.next;
        sentBack.next = null;
        size -= 1;
        return removedeque.item;
    }

    public T get(int index) {
        Deque p = sentFront;
        if (index < 0 || (size <= index)) {
            return null;
        }
        Deque ptr = sentFront.prev;
            int a = 0;
            while (a < index) {
                a = a + 1;
                ptr = ptr.prev;
            }
            return ptr.item;
    }

    public T getRecursize(int index) {
        return helper(index, sentFront.prev);
    }

    private T helper(int index, Deque newdeque) {
        if (index >= size || index < 0) {
            return null;
        }
        if (index == 0) {
            return newdeque.item;
        }
        return helper(index - 1, newdeque.prev);
    }
}
