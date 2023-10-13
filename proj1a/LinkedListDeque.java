public class LinkedListDeque<T> {
    private class Deque<T> {
        private Deque<T> prev;
        private T item;
        private Deque<T> next;

        private Deque(Deque<T> p, T i, Deque<T> n) {
            prev = p;
            item = i;
            next = n;
        }
    }

    private Deque<T> sentFront;
    private Deque<T> sentBack;
    private int size;



    public LinkedListDeque() {
        sentFront = new Deque(null, (T) new Object(), null);
        sentBack = sentFront;
        size = 0;
    }

    public void addFirst(T item) {
        Deque<T> newdeque = new Deque(sentFront, item, sentFront.next);
        sentFront.next = newdeque;
        size += 1;
    }

    public void addLast(T item) {
        Deque<T> newdeque = new Deque(sentBack.prev, item, sentBack);
        sentBack.prev = newdeque;
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
        Deque<T> ptr = sentFront;
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
        Deque<T> removedeque = sentFront.next;
        sentFront.next = removedeque.next;
        size -= 1;
        return removedeque.item;
    }

    public T removeLast() {
        if (size == 0) {
            return null;
        }
        Deque<T> removedeque = sentBack.prev;
        sentBack.prev = removedeque.prev;
        size -= 1;
        return removedeque.item;
    }

    public T get(int index) {
        if (size <= index || size == 0) {
            return null;
        }
        Deque<T> ptr = sentFront.next;
        int a = 0;
        while (a < index) {
            a = a + 1;
            ptr = ptr.next;
        }
        return ptr.item;
    }

    private T helper(int index, Deque<T> newdeque) {
        if (index == 0) {
            return newdeque.item;
        }
        return helper(index - 1, newdeque.next);
    }

    public T getRecursive(int index) {
        if (index >= size || size == 0) {
            return null;
        }
        return helper(index, sentFront.next);
    }

}
