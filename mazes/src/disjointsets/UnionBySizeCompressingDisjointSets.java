package disjointsets;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * A quick-union-by-size data structure with path compression.
 * @see DisjointSets for more documentation.
 */
public class UnionBySizeCompressingDisjointSets<T> implements DisjointSets<T> {
    // Do NOT rename or delete this field. We will be inspecting it directly in our private tests.
    List<Integer> pointers;

    /*
    However, feel free to add more fields and private helper methods. You will probably need to
    add one or two more fields in order to successfully implement this class.
    */
    //private List<Integer> set;  // stores our stuff
    private int size;
    private final HashMap<T, Integer> indexOfElement;


    public UnionBySizeCompressingDisjointSets() {
        this.pointers = new ArrayList<>();
        this.indexOfElement = new HashMap<>();
    }

    // needs to be tested, okay start
    @Override
    public void makeSet(T item) {
        if (indexOfElement.containsKey(item)) {
            throw new IllegalArgumentException();
        }
        int id = -1;
        pointers.add(id);
        indexOfElement.put(item, this.size);
        this.size++;
    }

    @Override
    public int findSet(T item) {
        if (!indexOfElement.containsKey(item)) {
            throw new IllegalArgumentException(); // make private method
        }
        int index = indexOfElement.get(item);
        int value = this.pointers.get(index);

        while (value >= 0) {
            index = value;
            value = this.pointers.get(index);
        }
        int root = index;

        index = indexOfElement.get(item);
        value = this.pointers.get(index);
        while (value >= 0) {
            this.pointers.set(index, root);
            index = value;
            value = this.pointers.get(index);
        }
        return root;
    }

    @Override
    public boolean union(T item1, T item2) {
        if (!indexOfElement.containsKey(item1) || !indexOfElement.containsKey(item2)) {
            throw new IllegalArgumentException();
        }

        if (findSet(item1) == findSet(item2)) {
            return false;
        } else {
            // magnitude of the following give the weight of a set containing item1 or item2
            int item1idx = findSet(item1);
            int item2idx = findSet(item2);
            int item1SetSize = pointers.get(item1idx);
            int item2SetSize = pointers.get(item2idx);

            if (item1SetSize < item2SetSize) {  // set of item1 has more in set, absorbs item2
                // update item2's set representative to item1's
                //System.out.println("before else " + set.get(indexOfElement.get(item2)));
                this.pointers.set(item2idx, item1idx);
                // update size after union
                this.pointers.set(item1idx, item1SetSize+item2SetSize);
            } else {
                // update item1's set representative to item2's
                //System.out.println("after else " + set.get(indexOfElement.get(item2)));
                pointers.set(item1idx, item2idx);
                // update size after union
                pointers.set(item2idx, item2SetSize+item1SetSize);
            }
            return true;
        }
    }
}
