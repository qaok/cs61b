package lab9;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * Implementation of interface Map61B with BST as core data structure.
 *
 * @author qaok
 */
public class BSTMap<K extends Comparable<K>, V> implements Map61B<K, V> {

    private class Node {
        /* (K, V) pair stored in this Node. */
        private K key;
        private V value;

        /* Children of this Node. */
        private Node left;
        private Node right;

        private Node(K k, V v) {
            key = k;
            value = v;
        }
    }

    private Node root;  /* Root node of the tree. */
    private int size; /* The number of key-value pairs in the tree */
    private V deletedValue;

    /* Creates an empty BSTMap. */
    public BSTMap() {
        this.clear();
    }

    /* Removes all of the mappings from this map. */
    @Override
    public void clear() {
        root = null;
        size = 0;
    }

    /** Returns the value mapped to by KEY in the subtree rooted in P.
     *  or null if this map contains no mapping for the key.
     */
    private V getHelper(K key, Node p) {
        if (p == null) {
            return null;
        }
        if (p.key.compareTo(key) == 0) {
            return p.value;
        }
        if (p.key.compareTo(key) > 0) {
            return getHelper(key, p.right);
        } else {
            return getHelper(key, p.left);
        }
    }

    /** Returns the value to which the specified key is mapped, or null if this
     *  map contains no mapping for the key.
     */
    @Override
    public V get(K key) {
        return getHelper(key, root);
    }

    /** Returns a BSTMap rooted in p with (KEY, VALUE) added as a key-value mapping.
      * Or if p is null, it returns a one node BSTMap containing (KEY, VALUE).
     */
    private Node putHelper(K key, V value, Node p) {
        if (containsKey(key)) {
            return p;
        }
        if (p == null) {
            p = new Node(key, value);
            size += 1;
        }
        if (p.key.compareTo(key) == 0) {
            p.key = key;
            p.value = value;
            return p;
        } else if (p.key.compareTo(key) < 0) {
            p.left = putHelper(key, value, p.left);
            return p;
        } else {
            p.right = putHelper(key, value, p.right);
            return p;
        }
    }

    /** Inserts the key KEY
     *  If it is already present, updates value to be VALUE.
     */
    @Override
    public void put(K key, V value) {
        root = putHelper(key, value, root);
    }

    /* Returns the number of key-value mappings in this map. */
    @Override
    public int size() {
        return size;
    }

    //////////////// EVERYTHING BELOW THIS LINE IS OPTIONAL ////////////////
    private void keySetHelper(Set<K> keys, Node p) {    // 遍历左边树和右边树
        if (p == null) {
            return;
        }
        keys.add(p.key);
        keySetHelper(keys, p.left);
        keySetHelper(keys, p.right);
    }

    /* Returns a Set view of the keys contained in this map. */
    @Override
    public Set<K> keySet() {
        Set<K> keys = new HashSet<>();   // 新建一个hash集合
        keySetHelper(keys, root);
        return keys;
    }
    
    /** Removes KEY from the tree if present
     *  returns VALUE removed,
     *  null on failed removal.
     */
    private Node findmin(Node x) {  // 找到左边子树中最大的子树
        if (x.right == null) {
            return x;
        } else {
            return findmin(x.right);
        }
    }
    
    private Node deleteMin(Node x) { // 删除左边子树中最大的子树
        if (x.right == null) {
            return x.left;
        }
        x.right = deleteMin(x.right);
        return x;
    }
    
    private Node removeHelper(K key, Node p) {
        if (get(key) == null) {
            return null;
        }
        int cmp = p.key.compareTo(key);
        if (cmp > 0) {
            p.right = removeHelper(key, p.right);
        } else if (cmp < 0) {
            p.left = removeHelper(key, p.left);
        } else {
            deletedValue = p.value;
            if (p.left == null) {
                return p.right;
            }
            if (p.right == null) {
                return p.left;
            }
            Node x = p;                   // 新建x，复制p
            p = findmin(x.left);          // 将p设为x左边子树中最大的node
            p.left = deleteMin(x.left);   // 重新连接左边子树
            p.right = x.right;            // 重新连接右边子树
        }
        return p;
    }
    
    @Override
    public V remove(K key) {
        if (key == null) {
            throw new IllegalArgumentException();
        }
        root = removeHelper(key, root);
        V returnValue = deletedValue;
        if (returnValue != null) {
            size -= 1;
        }
        deletedValue = null;
        return returnValue;
        
    }

    /** Removes the key-value entry for the specified key only if it is
     *  currently mapped to the specified value.  Returns the VALUE removed,
     *  null on failed removal.
     **/
    @Override
    public V remove(K key, V value) {
        if (get(key) != value) {
            return null;
        }
        return remove(key);
    }

    @Override
    public Iterator<K> iterator() {
        return keySet().iterator();
    }
    
    public static void main(String[] args) {
        BSTMap<String, Integer> bstmap = new BSTMap<>();
        bstmap.put("hello", 5);
        bstmap.put("cat", 10);
        bstmap.put("fish", 22);
        bstmap.put("zebra", 90);
    }
}
