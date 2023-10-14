public class LinkedListDeque<T> {
    private class Deque {
        private Deque prev;
        private T item;
        private Deque next;

        private Deque(Deque p, T i, Deque n) {
            prev = p;
            item = i;
            next = n;
            if (n != null) {
                n.prev = this;
            }
            if (p != null) {
                p.next = this;
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
        Deque newdeque = new Deque(sentFront, item, sentFront.next);
        size += 1;
        if (size == 1) {
            sentBack = newdeque;
        }
    }

    public void addLast(T item) {
        Deque newdeque = new Deque(sentBack, item, null);
        sentBack = newdeque;
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
        sentFront.next = removedeque.next;
        if (removedeque == sentBack) {
            sentBack = sentFront;
        } else {
            removedeque.next.prev = sentFront;
        }
        size -= 1;
        return removedeque.item;
    }

    public T removeLast() {
        if (size == 0) {
            return null;
        }
        Deque removedeque = sentBack;
        sentBack = removedeque.prev;
        sentBack.next = null;
        size -= 1;
        return removedeque.item;
    }

    public T get(int index) {
        if (size <= index || size == 0) {
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

    private T helper(int index, Deque nowdeque) {
        if (index == 0) {
            return nowdeque.item;
        }
        if (index >= size || size == 0) {
            return null;
        }
        return helper(index - 1, nowdeque.next);
    }

    public T getRecursive (int index) {
        return helper(index, sentFront.next);
    }
}
