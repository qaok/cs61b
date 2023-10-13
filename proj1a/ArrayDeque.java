public class ArrayDeque<T> {
    private T[] items;
    private int size;

    public ArrayDeque() {
        items = (T[]) new Object[8];
        size = 0;
    }

    private void resize(int capacity) {
        T[]a = (T[]) new Object[capacity];
        System.arraycopy(items, 0, a, 0, size);
        items = a;       
    }

    public void addFirst(T item) {
        if (size == items.length) {
            resize(size * 4);
        }
        T[] a = items;
        System.arraycopy(items, 0, a, 1, size);
        items = a;
        items[0] = item;
        size += 1;
    }

    public void addLast(T item) {
        if (size == items.length) {
            resize(size * 4);
        }
        items[size] = item;
        size += 1;
    }

    public boolean isEmpty() {
        if (size == 0) {
            return true;
        }
        return false;
    }

    public void printDeque() {
        T[] ptr = items;
        int i = size;
        while (i < size) {
            System.out.print(ptr[i]);
            System.out.print(" ");
            i += 1;
        }
        System.out.println(" ");
    }

    private T getFirst() {
        if (size == 0) {
            return null;
        }
        return items[0];
    }

    private T getLast() {
        if (size == 0) {
            return null;
        }
        return items[size - 1];
    }

    public T get(int index) {
        if (index < 0 || index >= size) {
            return null;
        }
        return items[index];
    }

    public int size() {
        return size;
    }

    public T removeFirst() {
        T x = getFirst();
        items[0] = null;
        size -= 1;
        return x;
    }

    public T removeLast() {
        T x = getLast();
        items[size - 1] = null;
        size -= 1;
        return x;
    }


}
