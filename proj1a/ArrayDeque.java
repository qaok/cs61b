public class ArrayDeque<T> {
    private T[] items;
    private int size;
    private int first;
    private int last;
    private int capacity;

    public ArrayDeque() {
        capacity = 8;
        size = 0;
        first = 4;
        last = 5;
        items = (T[]) new Object[capacity];
    }


    private int updatefirst(int i) {
        return (i - 1 + capacity) % capacity;
    }

    private int updatelast(int i) {
        return (i + 1 + capacity) % capacity;
    }

    private void resize(int x) {
        capacity = x;
        T[] a = (T[]) new Object[x];
        for (int i = first + 1, j = 0; j < size; i++, j++) {
            if (i >= capacity) {
                i = 0;
            }
            a[j] = items[i];
        }
        last = size;
        first = capacity - 1;
        items = a;       
    }

    public void addFirst(T item) {
        if (size >= items.length) {
            resize(capacity * 2);
        }
        items[first] = item;
        first = updatefirst(first);
        size += 1;

    }

    public void addLast(T item) {
        if (size >= items.length) {
            resize(capacity * 2);
        }
        items[last] = item;
        last = updatelast(last);
        size += 1;
    }

    public boolean isEmpty() {
        if (size == 0) {
            return true;
        }
        return false;
    }

    public void printDeque() {
        for (int i = first + 1; i != last; i++) {
            if (i >= capacity) {
                i = 0;
            }
            System.out.println(items[i]);
        }
    }

    private T getFirst() {
        if (size == 0) {
            return null;
        }
        first = updatefirst(first);
        return items[first];
    }

    private T getLast() {
        if (size == 0) {
            return null;
        }
        last = updatelast(last);
        return items[last];
    }

    public T get(int index) {
        if (index < 0 || index >= size) {
            return null;
        }
        return items[(first + index) % capacity];
    }

    public int size() {
        return size;
    }

    public T removeFirst() {
        T x = getFirst();
        first = updatelast(first);
        items[first] = null;
        size -= 1;
        if (size < capacity / 4 && capacity > 8) {
            resize(capacity / 2);
        }
        return x;
    }

    public T removeLast() {
        T x = getLast();
        last = updatefirst(last);
        items[last] = null;
        size -= 1;
        if (size < capacity / 4 && capacity > 8) {
            resize(capacity / 2);
        }
        return x;
    }

}
