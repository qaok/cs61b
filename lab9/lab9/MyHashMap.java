package lab9;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 *  A hash table-backed Map implementation. Provides amortized constant time
 *  access to elements via get(), remove(), and put() in the best case.
 *
 *  @author qaok
 */
public class MyHashMap<K, V> implements Map61B<K, V> {

    private static final int DEFAULT_SIZE = 16;
    private static final double MAX_LF = 0.75;

    private ArrayMap<K, V>[] buckets;
    private int size;

    private int loadFactor() {
        return size / buckets.length;
    }

    public MyHashMap() {
        buckets = new ArrayMap[DEFAULT_SIZE];
        this.clear();
    }

    /* Removes all of the mappings from this map. */
    @Override
    public void clear() {
        this.size = 0;
        for (int i = 0; i < this.buckets.length; i += 1) {
            this.buckets[i] = new ArrayMap<>();
        }
    }

    /** Computes the hash function of the given key. Consists of
     *  computing the hashcode, followed by modding by the number of buckets.
     *  To handle negative numbers properly, uses floorMod instead of %.
     */
    private int hash(K key) {       // 获得key的hashcode，之后再获得该hashcode在整个hashmap中的index
        if (key == null) {
            return 0;
        }

        int numBuckets = buckets.length;
        return Math.floorMod(key.hashCode(), numBuckets);
    }

    /* Returns the value to which the specified key is mapped, or null if this
     * map contains no mapping for the key.
     */
    @Override
    public V get(K key) {
        int keyHash = hash(key);
        return buckets[keyHash].get(key);
    }
    
    private void resize() {
        ArrayMap<K, V>[] newbuckets = new ArrayMap[buckets.length * 2];
        for (int i = 0; i < newbuckets.length; i += 1) {
            newbuckets[i] = new ArrayMap<>();
        }
        for (K key : keySet()) {
            int keyHash = hash(key);     // 获得key在buckets中的index
            newbuckets[Math.floorMod(key.hashCode(), 2 * buckets.length)].put(key, buckets[keyHash].get(key));
        }
        /**
         * newbuckets[Math.floorMod(key.hashCode(), 2 * buckets.length)]
         * 此处的Math.floorMod(key.hashCode(), 2 * buckets.length)主要是
         * 获得key在newbuckets中的index，
         * put(key, buckets[keyHash].get(key))是将原来的key——value配对重新放入
         * newbuckets中
         */
        buckets = newbuckets;
    }

    /* Associates the specified value with the specified key in this map. */
    @Override
    public void put(K key, V value) {
        if (loadFactor() > MAX_LF) {
            resize();
        }
        int keyHash = hash(key);                    // 获得key在hashmap中的index
        if (!buckets[keyHash].containsKey(key)) {   // 根据index查找，如果hashmap中没有这个key，则size+1
            size += 1;
        }
        buckets[keyHash].put(key, value);
    }

    /* Returns the number of key-value mappings in this map. */
    @Override
    public int size() {
        return size;
    }

    //////////////// EVERYTHING BELOW THIS LINE IS OPTIONAL ////////////////

    /* Returns a Set view of the keys contained in this map. */
    @Override
    public Set<K> keySet() {         // 主要就是返回map中的key
        Set<K> keys = new HashSet<>();
        for (int i = 0; i < buckets.length; i += 1) {
            Set<K> keySetBucket = buckets[i].keySet();
            keys.addAll(keySetBucket);
        }
        return keys;
    }

    /* Removes the mapping for the specified key from this map if exists.
     * Not required for this lab. If you don't implement this, throw an
     * UnsupportedOperationException. */
    @Override
    public V remove(K key) {
        int keyHash = hash(key);                  // 得到key的index
        if (buckets[keyHash].containsKey(key)) {  // size-1
            size -= 1;
        }
        return buckets[keyHash].remove(key);      // 借用arraymap的类删除
    }

    /* Removes the entry for the specified key only if it is currently mapped to
     * the specified value. Not required for this lab. If you don't implement this,
     * throw an UnsupportedOperationException.*/
    @Override
    public V remove(K key, V value) {             // 同上
        int keyHash = hash(key);
        if (buckets[keyHash].get(key) == value) {
            size -= 1;
        }
        return buckets[keyHash].remove(key, value);
    }

    @Override
    public Iterator<K> iterator() {
        return keySet().iterator();
    }
}
