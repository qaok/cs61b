public class LinkedListDeque<T> {
    private class Deque {
        private Deque prev;
        private T item;
        private Deque next;

        private Deque(Deque p, T i, Deque n) {
            prev = p;
            item = i;
            next = n;
            if (p != null) {
                p.next = this;
            }
            if (n != null) {
                n.prev = this;
            }
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
        Deque removedeque = sentFront.next;
        if (removedeque.next == sentBack) {
            sentFront = sentBack;
        }
        sentFront.next = removedeque.next;
        size -= 1;
        return removedeque.item;
    }

    public T removeLast() {
        if (size == 0) {
            return null;
        }
        Deque removedeque = sentBack.prev;
        sentBack.prev = removedeque.prev;
        size -= 1;
        return removedeque.item;
    }

    public T get(int index) {
        if (index < 0 || (size <= index)) {
            return null;
        }
        Deque ptr = sentFront.next;
        int a = 0;
        while (a < index) {
            a = a + 1;
            ptr = ptr.next;
        }
        return ptr.item;
    }

    public T getRecursive(int index) {
        return helper(index, sentFront.next);
    }

    private T helper(int index, Deque newdeque) {
        if (index >= size || index < 0) {
            return null;
        }
        if (index == 0) {
            return newdeque.item;
        }
        return helper(index - 1, newdeque.next);
    }
}
