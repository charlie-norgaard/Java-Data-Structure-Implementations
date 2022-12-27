package maps;

import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Objects;

/**
 * @see AbstractIterableMap
 * @see Map
 */
public class ArrayMap<K, V> extends AbstractIterableMap<K, V> {
    private static final int DEFAULT_INITIAL_CAPACITY = 8;
    /*
    Warning:
    You may not rename this field or change its type.
    We will be inspecting it in our secret tests.
     */
    SimpleEntry<K, V>[] entries;
    private int size;
    // You may add extra fields or helper methods though!

    /**
     * Constructs a new ArrayMap with default initial capacity.
     */
    public ArrayMap() {
        this(DEFAULT_INITIAL_CAPACITY);
    }

    /**
     * Constructs a new ArrayMap with the given initial capacity (i.e., the initial
     * size of the internal array).
     *
     * @param initialCapacity the initial capacity of the ArrayMap. Must be > 0.
     */
    public ArrayMap(int initialCapacity) throws IllegalArgumentException {
        if (initialCapacity < 0) {
            throw new IllegalArgumentException("The initial capacity must be > 0");
        }
        this.entries = this.createArrayOfEntries(initialCapacity);
    }

    /**
     * This method will return a new, empty array of the given size that can contain
     * {@code Entry<K, V>} objects.
     *
     * Note that each element in the array will initially be null.
     *
     * Note: You do not need to modify this method.
     */
    @SuppressWarnings("unchecked")
    private SimpleEntry<K, V>[] createArrayOfEntries(int arraySize) {
        /*
        It turns out that creating arrays of generic objects in Java is complicated due to something
        known as "type erasure."

        We've given you this helper method to help simplify this part of your assignment. Use this
        helper method as appropriate when implementing the rest of this class.

        You are not required to understand how this method works, what type erasure is, or how
        arrays and generics interact.
        */
        return (SimpleEntry<K, V>[]) (new SimpleEntry[arraySize]);
    }

    // helper method for finding index of certain key
    private int getIndexOf(Object givenKey) {
        int result = -1;
        for (int i = 0; i < size; i++) {
            K currKey = entries[i].getKey();

            if (Objects.equals(currKey, givenKey)) {
                result = i;
            }
        }
        return result;
    }

    // takes in an Object key and if the key is contained
    // in the current map, the corresponding value for this
    // key in the map is returned. returns null if key is not
    // contained in the map.
    @Override
    public V get(Object key) {
        if (!containsKey(key)) {
            return null;
        }
        int index = this.getIndexOf(key);
        return entries[index].getValue();
    }

    // takes a key and value and inserts the (key,value) pair
    // in the map. If the key already exists in the map, the
    // existing value for this key is replaced with a new value
    // 'value', and the old value is returned. returns null if
    // key was not already in the map. put() also performs a resize
    // if the map array is full prior to adding the new (key,value) pair.
    @Override
    public V put(K key, V value) {
        V currValue = null;
        if (this.containsKey(key)) {
            currValue = this.entries[getIndexOf(key)].getValue();
            this.entries[getIndexOf(key)] = new SimpleEntry<>(key, value);
        } else {
            if (this.size() == this.entries.length) {
                resize();
            }
            this.entries[this.size()] = new SimpleEntry<>(key, value);
            entries[size] = new SimpleEntry<>(key, value);
            size++;
        }
        return currValue;
    }

    // helper used to resize the array which contains our map
    private void resize() {
        SimpleEntry<K, V>[] newEntries = createArrayOfEntries(this.size() + DEFAULT_INITIAL_CAPACITY);
        for (int i = 0; i < size; i++) {
            newEntries[i] = this.entries[i];
        }
        this.entries = newEntries;
    }

    // takes an Object key and if the key is contained in the current
    // map, removes the key and is corresponding value from the map, and
    // the value for this key is returned.
    // Otherwise, does nothing and returns null
    @Override
    public V remove(Object key) {
        int index = getIndexOf(key);
        if (index == -1) {
            return null;
        }
        V currValue = null;
        if (this.containsKey(key)) {
            currValue = this.entries[index].getValue();
            this.entries[index] = this.entries[size - 1];
            this.size--;
        }
        return currValue;
    }

    // Empties the map when called
    @Override
    public void clear() {
        for (int i = 0; i < size; i++) {
            this.entries[i] = null;
        }
    }

    // Takes an Object key, and returns true if the key
    // is contained in the current map, false otherwise.
    @Override
    public boolean containsKey(Object key) {
        boolean result = false;
        for (int i = 0; i < size; i++) {
            if (Objects.equals(key, this.entries[i].getKey())) {
                result = true;
                break;
            }
        }
        return result;
    }

    // returns the number of (key,value) pairs
    // in the current map
    @Override
    public int size() {
        return this.size;
    }

    @Override
    public Iterator<Map.Entry<K, V>> iterator() {
        // Note: You may or may not need to change this method, depending on whether you
        // add any parameters to the ArrayMapIterator constructor.
        return new ArrayMapIterator<>(this.entries, this.size);
    }

    // returns an iterator for the current map. gets all the (key,value) mappings
    // for the current map
    private static class ArrayMapIterator<K, V> implements Iterator<Map.Entry<K, V>> {
        private final SimpleEntry<K, V>[] entries;
        // You may add more fields and constructor parameters
        private int curIndex;  // indicator of where we are
        private final int size;

        // ArrayMapIterator with SimpleEntry entries and int size
        public ArrayMapIterator(SimpleEntry<K, V>[] entries, int size) {
            this.entries = entries;
            this.size = size;
        }

        // returns true if the map has more (key,value)
        // pairs as we go through each pair. false otherwise
        @Override
        public boolean hasNext() {
            return (curIndex <= size-1);
        }

        // returns the next entry in the current map
        // throws a NoSuchElementException() if we
        // have gone through all the (key,value) pairs
        @Override
        public Map.Entry<K, V> next() {
            if (this.hasNext()) {
                Entry<K, V> current = new SimpleEntry<>(this.entries[curIndex].getKey(),
                    this.entries[curIndex].getValue());
                curIndex++;
                return current;
            } else {
                throw new NoSuchElementException();
            }
        }
    }
}
