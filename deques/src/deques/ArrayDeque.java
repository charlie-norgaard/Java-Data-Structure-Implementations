package deques;

/** A buggy array implementation of the Deque interface. */
public class ArrayDeque<T> extends AbstractDeque<T> {
    private T[] data;
    private int front;
    private int back;
    private int size;

    @SuppressWarnings("unchecked")
    public ArrayDeque() {
        data = (T[]) new Object[8];
        front = 0;
        back = 1;
        size = 0;
    }

    // used to increase size and index when adding elements
    private static int increment(int i, int length) {
        if (i == length - 1) {
            return 0;
        } else {
            return i + 1;
        }
    }

    // used to decrease size and index when removing elements
    private static int decrement(int i, int length) {
        if (i == 0) {
            return length - 1;
        } else {
            return i - 1;
        }
    }

    // inserts data in front
    // moves front to index - 1
    // adds 1 to size
    public void addFirst(T item) {
        if (size == data.length) {
            resize(data.length * 2);
        }
        data[front] = item;
        front = decrement(front, data.length);
        size += 1;
    }

    // adds data in back
    // moves back to index + 1
    // adds 1 to size
    public void addLast(T item) {
        if (size == data.length) {
            resize(data.length * 2);
        }
        data[back] = item;
        back = increment(back, data.length);
        size += 1;
    }

    // removes element in front of queue
    public T removeFirst() {
        if (size == 0) {
            return null;
        }
        front = increment(front, data.length);
        T result = data[front];
        data[front] = null;
        size -= 1;
        if (needsDownsize()) {
            resize(data.length / 2);
        }
        return result;
    }

    // removes element in back of queue
    public T removeLast() {
        if (size == 0) {
            return null;
        }
        back = decrement(back, data.length);
        T result = data[back];
        data[back] = null;
        size -= 1;
        if (needsDownsize()) {
            resize(data.length / 2);
        }
        return result;
    }

    public T get(int index) {
        if (index >= size) {
            return null;
        } else {
            int place = front + 1 + index;
            return data[place % data.length];
        }
    }

    public String toString() {
        // We use a StringBuilder since it concatenates strings more efficiently
        // than using += in a loop
        StringBuilder output = new StringBuilder();
        if (size >= 0) {
            int counter = 0;
            int i = increment(front, data.length);
            while (counter < size) {
                output.append(data[i]).append(" ");
                i = increment(i, data.length);
                counter += 1;
            }
        }
        return output.toString();
    }

    public int size() {
        return size;
    }

    @SuppressWarnings("unchecked")
    private void resize(int capacity) {
        T[] newData = (T[]) new Object[capacity];
        int i = increment(front, data.length);
        for (int newIndex = 0; newIndex < size; newIndex += 1) {
            if (i > data.length - 1) {
                i = 0;
            }
            newData[newIndex] = data[i];
            i = increment(i, size);
        }
        front = newData.length - 1;
        back = size;
        data = newData;
    }

    // this seems to be a problematic method
    // doesn't seem to use resize correctly
    // why size / data.length < 0.25
    private boolean needsDownsize() {
        // return 3 / 16 < 0.25 && 16 >= 16
        return ((double) size) / data.length < 0.25 && data.length >= 16;
    }
}
