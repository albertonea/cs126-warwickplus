package structures.data;


import structures.data.interfaces.List;
import structures.data.interfaces.Set;

import java.util.Random;

public class SetSkipList<K extends Comparable<? super K>, V> {
    private final Random random = new Random();
    private Node<K, V> head;
    private int height = 1;

    public SetSkipList() {
        EndNode<K, V> tail = new EndNode<>();
        head = new StartNode<>();
        head.setNext(tail);
        tail.setPrev(head);
    }

    public List<V> get(K key) {
        Node<K, V> node = findNode(key);
        if (node == null) {
            return null;
        }
        return node.getBottom().getValues().toList();
    }

    public List<V> getRange(K from, K to) {
        if (from.compareTo(to) > 0) {
            return null;
        }

        Node<K, V> tmp = findNodeOrElseAfter(from);
        if (tmp == null) return null;
        tmp = tmp.getBottom();

        List<V> merged = new LinkedList<>();
        while (!tmp.isEnd() && !tmp.keyGreaterThan(to)) {
            // Expose inner LinkedList from your Set implementation
            var values = tmp.getValues();
            if (values != null) {
                merged.addAll(values);
            }
            tmp = tmp.getNext();
        }

        return merged;
    }


    public void put(K key, V value) {
        Node<K, V> existingNode = findNode(key);
        if (existingNode != null) {
            existingNode.getBottom().add(value);
            return;
        }

        int newNodeHeight = randomHeight();
        adjustHeightIfNeeded(newNodeHeight);

        Node<K, V> current = this.head;
        Node<K, V> lastInserted = null;
        int currentLevel = height;

        while (current != null && currentLevel > 0) {
            Node<K, V> next = current.getNext();

            if (next.isEnd() || next.keyGreaterThan(key)) {
                if (currentLevel <= newNodeHeight) {
                    Node<K, V> nodeToInsert = new DataNode<>(key);
                    spliceBetween(current, next, nodeToInsert);

                    if (lastInserted != null) {
                        lastInserted.setBelow(nodeToInsert);
                        nodeToInsert.setAbove(lastInserted);
                    }
                    lastInserted = nodeToInsert;
                }
                current = current.getBelow();
                currentLevel--;
            } else {
                current = next;
            }
        }

        if (lastInserted != null) {
            lastInserted.add(value);
        }
    }

    public boolean remove(K key, V value) {
        Node<K, V> node = findNode(key);
        if (node == null) {
            return false;
        }

        Node<K, V> bottom = node.getBottom();
        Set<V> values = bottom.getValues();
        values.remove(value);
        if (values.isEmpty()) {
            nodeEmpty(node);
        }
        return true;
    }


    private void nodeEmpty(Node<K, V> node) {
        var tmpNode = node;
        while (tmpNode != null) {
            var endNode = tmpNode.getNext();
            var startNode = tmpNode.getPrev();
            startNode.setNext(endNode);
            endNode.setPrev(startNode);
            tmpNode = tmpNode.getBelow();
        }

        while (height > 1 && this.head.getNext().isEnd()) {
            this.head = this.head.getBelow();
            this.head.setAbove(null);
            this.head.getNext().setAbove(null);
            height--;
        }
    }

    private Node<K, V> findNode(K key) {
        Node<K, V> current = this.head;

        while (current != null) {
            Node<K, V> next = current.getNext();
            if (next.isEnd() || next.keyGreaterThan(key)) {
                current = current.getBelow();
            } else if (next.keyEquals(key)) {
                return next;
            } else {
                current = next;
            }
        }
        return null;
    }

    private Node<K, V> findNodeOrElseAfter(K key) {
        Node<K, V> current = this.head;

        // next is end drop below if we can or return null
        // next is greater drop below if we can

        while (current != null) {
            Node<K, V> next = current.getNext();
            if (next.isEnd()) {
                current = current.getBelow();
            } else if (next.keyGreaterThan(key)) {
                if (current.getBelow() != null) {
                    current = current.getBelow();
                } else {
                    return next;
                }
            } else if (next.keyEquals(key)) {
                return next;
            } else {
                current = next;
            }
        }
        return null;
    }

    private int randomHeight() {
        int h = 1;
        while (coinFlip()) {
            h++;
        }
        return h;
    }

    private boolean coinFlip() {
        return random.nextFloat() < 0.5f;
    }

    private void adjustHeightIfNeeded(int newNodeHeight) {
        if (newNodeHeight <= height) {
            return;
        }
        int extraLevels = newNodeHeight - height;

        Node<K, V> head = this.head;
        Node<K, V> tail = head.getNext();

        Node<K, V> currentHead = head;
        Node<K, V> currentTail = tail;

        for (int i = 0; i < extraLevels; i++) {
            StartNode<K, V> newHead = new StartNode<>();
            EndNode<K, V> newTail = new EndNode<>();

            newHead.setNext(newTail);
            newTail.setPrev(newHead);

            newHead.setBelow(currentHead);
            newTail.setBelow(currentTail);

            currentHead.setAbove(newHead);
            currentTail.setAbove(newTail);

            currentHead = newHead;
            currentTail = newTail;

            this.head = newHead;
        }

        height += extraLevels;
    }

    private void spliceBetween(Node<K, V> left, Node<K, V> right, Node<K, V> node) {
        node.setPrev(left);
        node.setNext(right);
        left.setNext(node);
        right.setPrev(node);
    }

    private static abstract class Node<K extends Comparable<? super K>, V> {
        private Node<K, V> next;
        private Node<K, V> prev;
        private Node<K, V> above;
        private Node<K, V> below;
        protected K key;
        final Set<V> values = new LinkedListSet<>();

        public Set<V> getValues() {
            return values;
        }

        public void add(V v) {
            values.add(v);
        }

        public Node<K, V> getNext() {
            return next;
        }
        public void setNext(Node<K, V> next) {
            this.next = next;
        }
        public Node<K, V> getPrev() {
            return prev;
        }
        public void setPrev(Node<K, V> prev) {
            this.prev = prev;
        }
        public Node<K, V> getAbove() {
            return above;
        }
        public void setAbove(Node<K, V> above) {
            this.above = above;
        }
        public Node<K, V> getBelow() {
            return below;
        }

        public Node<K, V> getBottom() {
            Node<K, V> tmp = this;

            while (tmp.getBelow() != null) {
                tmp = tmp.getBelow();
            }
            return tmp;
        }

        public void setBelow(Node<K, V> below) {
            this.below = below;
        }

        public boolean isStart() {
            return false;
        }
        public boolean isEnd() {
            return false;
        }

        public boolean keyGreaterThan(K otherKey) {
            if (key == null) {
                return false;
            }
            return key.compareTo(otherKey) > 0;
        }

        public boolean keyEquals(K otherKey) {
            if (key == null || otherKey == null) {
                return false;
            }
            return key.compareTo(otherKey) == 0;
        }
    }

    private static final class StartNode<K extends Comparable<? super K>, V> extends Node<K, V> {
        @Override
        public boolean isStart() {
            return true;
        }
    }

    private static final class EndNode<K extends Comparable<? super K>, V> extends Node<K, V> {
        @Override
        public boolean isEnd() {
            return true;
        }
    }

    private static final class DataNode<K extends Comparable<? super K>, V> extends Node<K, V> {
        DataNode(K key) {
            this.key = key;
        }
    }
}
