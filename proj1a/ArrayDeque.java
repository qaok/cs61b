public class ArrayDeque<T> {
    private T[] items;
    private int size;
    private int first;
    private int last;

    public ArrayDeque() {
        size = 0;
        first = 4;
        last = 5;
        items = (T[]) new Object[8];
    }


    private int updatefirst(int i) {
        return (i - 1 + items.length) % items.length;
    }

    private int updatelast(int i) {
        return (i + 1 + items.length) % items.length;
    }

    private void resize(int x) {
        T[] a = (T[]) new Object[x];
        for (int i = first + 1, j = 0; j < size; i++, j++) {
            if (i >= items.length) {
                i = 0;
            }
            a[j] = items[i];
        }
        last = size;
        first = x - 1;
        items = a;       
    }

    public void addFirst(T item) {
        if (size >= items.length) {
            resize(items.length * 2);
        }
        items[first] = item;
        first = updatefirst(first);
        size += 1;

    }

    public void addLast(T item) {
        if (size >= items.length) {
            resize(items.length * 2);
        }
        items[last] = item;
        last = updatelast(last);
        size += 1;
    }

    public boolean isEmpty() {
        if (size <= 0) {
            return true;
        }
        return false;
    }

    public void printDeque() {
        for (int i = first + 1; i != last; i++) {
            if (i >= items.length) {
                i = 0;
            }
            System.out.println(items[i]);
        }
    }

    /**
    private T getFirst() {
        if (size <= 0) {
            return null;
        }
        int index = first;
        index = updatelast(index);
        return items[index];
    }

    private T getLast() {
        if (size <= 0) {
            return null;
        }
        int index = last;
        index = updatefirst(index);
        return items[index];
    }
    */

    public T get(int index) {
        if (index < 0 || index > size) {
            return null;
        }
        return items[(first + index + 1) % items.length];
    }

    public int size() {
        return size;
    }

    public T removeFirst() {
        if (size <= 0) {
            return null;
        }
        first = updatelast(first);   //go to the next position.
        T x = items[first];
        items[first] = null;
        size -= 1;
        if (size < items.length / 4 && items.length > 8) {
            resize(items.length / 2);
        }
        return x;
    }

    public T removeLast() {
        if (size <= 0) {
            return null;
        }
        last = updatefirst(last);
        T x = items[last];
        items[last] = null;
        size -= 1;
        if (size < items.length / 4 && items.length > 8) {
            resize(items.length / 2);
        }
        return x;
    }

}
