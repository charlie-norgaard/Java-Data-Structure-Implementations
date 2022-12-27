package deques;

/**
 * @see Deque
 */
public class LinkedDeque<T> extends AbstractDeque<T> {
    private int size;
    // IMPORTANT: Do not rename these fields or change their visibility.
    // We access these during grading to test your code.
    Node<T> front;
    Node<T> back;
    // Feel free to add any additional fields you may need, though.
    Node<T> data;

    public LinkedDeque() {
        size = 0;
        front = new Node<>(null);
        back = new Node<>(null);
        data = null;
        front.next = back;
        back.prev = front;
        front.prev = null;
        back.next = null;
    }

    public void addFirst(T item) {
        size += 1;
        data = new Node<>(item);
        data.next = front.next;
        front.next = data;
        data.prev = front;
        front.next.next.prev = data;
    }

    public void addLast(T item) {
        size += 1;
        data = new Node<>(item);
        data.prev = back.prev;
        data.next = back;
        back.prev = data;
        back.prev.prev.next = data;
    }

    public T removeFirst() {
        if (size == 0) {
            return null;
        }
        size -= 1;
        T result = front.next.value;
        Node<T> secondFront = front.next.next;
        front.next.prev = null;
        front.next.next = null;
        front.next = secondFront;
        secondFront.prev = front;
        return result;
    }

    public T removeLast() {
        if (size == 0) {
            return null;
        }
        size -= 1;
        T result = back.prev.value;
        Node<T> secondBack = back.prev.prev;
        back.prev.next = null;
        back.prev.prev = null;
        back.prev = secondBack;
        secondBack.next = back;
        return result;
    }

    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return size() == 0;
    }

    public T get(int index) {
        T result = null;
        if ((index > size) || (index < 0)) {
            return null;
        }
        if (index <= size / 2) {
            result = getFromFront(index);
        } else if (index >= size / 2) {
            result = getFromBack(index);
        }
        return result;
    }

    private T getFromFront(int index) {
        Node<T> currNode = front;
        int currCount = -1;
        while (currCount != index) {
            currNode = currNode.next;
            currCount++;
        }
        return currNode.value;
    }

    private T getFromBack(int index) {
        Node<T> currNode = back;
        int currCount = size;
        while (currCount != index) {
            currNode = currNode.prev;
            currCount--;
        }
        return currNode.value;
    }
}
