package maps;

import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;

/**
 * @see AbstractIterableMap
 * @see Map
 */
public class ChainedHashMap<K, V> extends AbstractIterableMap<K, V> {
    private static final double DEFAULT_RESIZING_LOAD_FACTOR_THRESHOLD = 1;
    private static final int DEFAULT_INITIAL_CHAIN_COUNT = 10;
    private static final int DEFAULT_INITIAL_CHAIN_CAPACITY = 8;  // just to match default capacity of ArrayMap

    /*
    Warning:
    You may not rename this field or change its type.
    We will be inspecting it in our secret tests.
     */
    AbstractIterableMap<K, V>[] chains;
    private final double lambda;
    private final int chainCount;
    private final int chainCap;
    private int size;   // number of key-value pairs
    // You're encouraged to add extra fields (and helper methods) though!

    /**
     * Constructs a new ChainedHashMap with default resizing load factor threshold,
     * default initial chain count, and default initial chain capacity.
     */
    public ChainedHashMap() {
        this(DEFAULT_RESIZING_LOAD_FACTOR_THRESHOLD, DEFAULT_INITIAL_CHAIN_COUNT, DEFAULT_INITIAL_CHAIN_CAPACITY);
    }

    /**
     * Constructs a new ChainedHashMap with the given parameters.
     *
     * @param resizingLoadFactorThreshold the load factor threshold for resizing. When the load factor
     *                                    exceeds this value, the hash table resizes. Must be > 0.
     * @param initialChainCount the initial number of chains for your hash table. Must be > 0.
     * @param chainInitialCapacity the initial capacity of each ArrayMap chain created by the map.
     *                             Must be > 0.
     */
    public ChainedHashMap(double resizingLoadFactorThreshold, int initialChainCount, int chainInitialCapacity) {
        this.lambda = resizingLoadFactorThreshold;
        this.chainCount = initialChainCount;
        this.chainCap = chainInitialCapacity;
        this.chains = createArrayOfChains(chainCount);
    }

    /**
     * This method will return a new, empty array of the given size that can contain
     * {@code AbstractIterableMap<K, V>} objects.
     *
     * Note that each element in the array will initially be null.
     *
     * Note: You do not need to modify this method.
     * @see ArrayMap createArrayOfEntries method for more background on why we need this method
     */
    @SuppressWarnings("unchecked")
    private AbstractIterableMap<K, V>[] createArrayOfChains(int arraySize) {
        return (AbstractIterableMap<K, V>[]) new AbstractIterableMap[arraySize];
    }

    /**
     * Returns a new chain.
     *
     * This method will be overridden by the grader so that your ChainedHashMap implementation
     * is graded using our solution ArrayMaps.
     *
     * Note: You do not need to modify this method.
     */
    protected AbstractIterableMap<K, V> createChain(int initialSize) {
        return new ArrayMap<>(initialSize);
    }

    // NOTE: may be good. need to double-check
    @Override
    public V get(Object key) {
        if (this.containsKey(key)) {
            //System.out.println("get: " + chains[keyIndex].get(key));
            return chains[hashValue(key)].get(key);
        }
        return null;
    }

    private int hashValue(Object key) {
        if (key == null) {
            return 0;
        } else {
            int hashCode = key.hashCode();
            return Math.abs(hashCode % this.chainCount);
        }
    }

    @Override
    public V put(K key, V value) {
        V result;
        if (this.chains[hashValue(key)] == null) {
            this.chains[hashValue(key)] = createChain(this.chainCap);
            this.size++;
            return this.chains[hashValue(key)].put(key, value);
            //return null;
        }
        result = this.chains[hashValue(key)].put(key, value);
        this.size++;
        // perform resizing if needed
        if ((double) this.size / this.chainCount >= lambda) {
            //System.out.println("DEBIG: Reize");
            resize();
        }
        return result;
    }

    // Resize helper method
    private void resize() {
        int newChainCount = 2 * this.chainCount;
        AbstractIterableMap<K, V>[] newChain = createArrayOfChains(newChainCount);
        for (int i = 0; i < this.chainCount; i++) {
            if (this.chains[i] != null) {
                for (Entry<K, V> entry : this.chains[i]) {
                    int newHash = hashValue(entry.getKey());
                    if (newChain[newHash] == null) {
                        newChain[newHash] = createChain(this.chainCap);
                    }
                    newChain[newHash].put(entry.getKey(), entry.getValue());
                }
            }
        }
        this.chains = newChain;
    }


    // NOTE: Should be good, need to double-check
    @Override
    public V remove(Object key) {
        if (this.containsKey(key)) {
            this.size--;
            return chains[hashValue(key)].remove(key);
        }
        return null;
    }

    // NOTE: should be good, need to double-check
    @Override
    public void clear() {
        for (int i = 0; i < chainCount; i++) {
            chains[i] = null;
        }
    }

    // NOTE Should be good, need to double-check
    @Override
    public boolean containsKey(Object key) {
        if (chains[hashValue(key)] != null) {
            return (chains[hashValue(key)].containsKey(key));
        }
        return false;
    }

    @Override
    // returns the total number of key value pairs we have
    public int size() {
        return this.size;
    }

    @Override
    public Iterator<Map.Entry<K, V>> iterator() {
        // Note: you won't need to change this method (unless you add more constructor parameters)
        return new ChainedHashMapIterator<>(this.chains);
    }

    /*
    See the assignment webpage for tips and restrictions on implementing this iterator.
     */
    private static class ChainedHashMapIterator<K, V> implements Iterator<Map.Entry<K, V>> {
        private final AbstractIterableMap<K, V>[] chains;
        private int curIndex;
        private final int chainCount;

        public ChainedHashMapIterator(AbstractIterableMap<K, V>[] chains) {
            this.chains = chains;
            this.chainCount = chains.length;
            //this.curIndex = 0;
        }

        @Override
        public boolean hasNext() {
            for (int i = curIndex; i < chainCount; i++) {
                if (chains[i] != null) {
                    return true;
                }
                curIndex++;
            }
            return false;
        }

        @Override
        public Map.Entry<K, V> next() {
            if (this.hasNext()) {
                Iterator<Map.Entry<K, V>> iterArrayMap = this.chains[curIndex].iterator();
                if (iterArrayMap.hasNext()) {
                    curIndex++;
                    return iterArrayMap.next();
                }
                curIndex++;
                return this.next();
            } else {
                throw new NoSuchElementException();
            }
        }
    }
}

