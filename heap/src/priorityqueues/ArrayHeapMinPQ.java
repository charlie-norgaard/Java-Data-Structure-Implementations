package priorityqueues;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * @see ExtrinsicMinPQ
 */
public class ArrayHeapMinPQ<T> implements ExtrinsicMinPQ<T> {
    // IMPORTANT: Do not rename these fields or change their visibility.
    // We access these during grading to test your code.
    static final int START_INDEX = 0;
    List<PriorityNode<T>> items;
    private final HashMap<T, Integer> itemIndex;  // tracks items and where they are stored
    private int size;

    public ArrayHeapMinPQ() {
        this.items = new ArrayList<>();
        this.itemIndex = new HashMap<>();
    }

    // Here's a method stub that may be useful. Feel free to change or remove it, if you wish.
    // You'll probably want to add more helper methods like this one to make your code easier to read.
    /**
     * A helper method for swapping the items at two indices of the array heap.
     */
    private void swap(int a, int b) {
        PriorityNode<T> tempNode = this.items.get(a);
        this.items.set(a, this.items.get(b));
        this.items.set(b, tempNode);

        // since we have swapped indices, we need to update
        // the HashMap to resolve this change
        this.itemIndex.put(this.items.get(b).getItem(), b);
        this.itemIndex.put(this.items.get(a).getItem(), a);
    }

    @Override
    // takes item and priority of the item and adds it
    // to the PQ
    public void add(T item, double priority) {
        if (this.contains(item)) {
            throw new IllegalArgumentException();
        }
        PriorityNode<T> toAdd = new PriorityNode<>(item, priority);
        if (this.size != 0) {
            this.items.add(this.size, toAdd);
            this.itemIndex.put(item, this.size);
            percolateUp(this.size);
        } else {
            this.items.add(START_INDEX, toAdd);
            this.itemIndex.put(item, START_INDEX);
        }
        this.size++;
    }

    // private method for percolating up when adding a new element or
    // if need be when a priority has been changed
    private void percolateUp(int index) {
        int parentIndex;
        double addedPriority = this.items.get(index).getPriority();
        while (index > START_INDEX) {
            parentIndex = (index-1)/2;
            double parentPriority = this.items.get(parentIndex).getPriority();
            if (parentPriority >= addedPriority) {
                swap(index, parentIndex);
            } else {
                break;
            }
            index = parentIndex;
        }
    }

    @Override
    public boolean contains(T item) {
        return this.itemIndex.containsKey(item);
    }

    @Override
    public T peekMin() {
        if (size() == 0) {
            throw new NoSuchElementException("PQ is empty");
        }
        return this.items.get(START_INDEX).getItem();
    }

    @Override
    public T removeMin() {
        if (this.size() == 0) {
            throw new NoSuchElementException("PQ is empty");
        }
        T min = this.items.get(START_INDEX).getItem();
        // this.items.remove(START_INDEX);
        swap(START_INDEX, this.size - 1);
        this.itemIndex.remove(min);
        this.items.remove(this.size - 1);
        this.size--;
        percolateDown(START_INDEX);
        return min;
    }

    // private method for percolating Down the tree. Used for when
    // removing a tree or if need be, when a priority has been changed
    private void percolateDown(int index) {
        while (2 * index + 2 <= this.size) {
            int nextIndex;
            if (2 * index + 2 >= this.size) {
                // this is where right child does not exist
                nextIndex = 2 * index + 1;
                double leftPriority = this.items.get(nextIndex).getPriority();
                if (lessThan(leftPriority, this.items.get(index).getPriority())) {
                    swap(index, nextIndex);
                } else {
                    break;
                }
            } else {
                // right child exists
                double leftPriority = this.items.get(2 * index + 1).getPriority();
                double rightPriority = this.items.get(2 * index + 2).getPriority();
                double childPriority;
                if (lessThan(leftPriority, rightPriority)) {
                    childPriority = leftPriority;
                    nextIndex = 2 * index + 1;
                } else {
                    childPriority = rightPriority;
                    nextIndex = 2 * index + 2;
                }
                if (lessThan(childPriority, this.items.get(index).getPriority())) {
                    swap(index, nextIndex);
                } else {
                    break;
                }
            }
            index = nextIndex;
        }
    }

    // private method to check if a priority I is less than
    // priority j
    private boolean lessThan(double i, double j) {
        return i < j;
    }

    @Override
    public void changePriority(T item, double priority) {
        if (this.contains(item)) {
            int indexOfItem = this.itemIndex.get(item);
            this.items.get(indexOfItem).setPriority(priority);
            // restore invariant
            percolateDown(indexOfItem);
            percolateUp(indexOfItem);
        } else {
            throw new NoSuchElementException("PQ does not contain " + item);
        }
    }

    @Override
    public int size() {
        return this.size;
    }
}
