package tree;

import java.util.Random;
import java.util.NoSuchElementException;

public class Treap<K extends Comparable<K>, V> {

    private static class Node<K, V> {
        K key;
        V value;
        int priority;
        Node<K, V> left, right;

        Node(K key, V value) {
            this.key = key;
            this.value = value;
            this.priority = new Random().nextInt();
        }
    }

    private Node<K, V> root;

    public void put(K key, V value) {
        root = insert(root, key, value);
    }

    private Node<K, V> insert(Node<K, V> node, K key, V value) {
        if (node == null) return new Node<>(key, value);
        int cmp = key.compareTo(node.key);
        if (cmp < 0) {
            node.left = insert(node.left, key, value);
            if (node.left.priority > node.priority) node = rotateRight(node);
        } else if (cmp > 0) {
            node.right = insert(node.right, key, value);
            if (node.right.priority > node.priority) node = rotateLeft(node);
        } else {
            node.value = value;
        }
        return node;
    }

    public V get(K key) {
        Node<K, V> current = root;
        while (current != null) {
            int cmp = key.compareTo(current.key);
            if (cmp < 0) current = current.left;
            else if (cmp > 0) current = current.right;
            else return current.value;
        }
        throw new NoSuchElementException("key not found: " + key);
    }

    public void remove(K key) {
        root = delete(root, key);
    }

    private Node<K, V> delete(Node<K, V> node, K key) {
        if (node == null) return null;
        int cmp = key.compareTo(node.key);
        if (cmp < 0) node.left = delete(node.left, key);
        else if (cmp > 0) node.right = delete(node.right, key);
        else {
            if (node.left == null) return node.right;
            if (node.right == null) return node.left;
            if (node.left.priority > node.right.priority) {
                node = rotateRight(node);
                node.right = delete(node.right, key);
            } else {
                node = rotateLeft(node);
                node.left = delete(node.left, key);
            }
        }
        return node;
    }

    public void inOrderTraversal() {
        inOrderTraversal(root);
    }

    private void inOrderTraversal(Node<K, V> node) {
        if (node != null) {
            inOrderTraversal(node.left);
            System.out.println(node.key + " => " + node.value);
            inOrderTraversal(node.right);
        }
    }

    private Node<K, V> rotateRight(Node<K, V> node) {
        Node<K, V> left = node.left;
        node.left = left.right;
        left.right = node;
        return left;
    }

    private Node<K, V> rotateLeft(Node<K, V> node) {
        Node<K, V> right = node.right;
        node.right = right.left;
        right.left = node;
        return right;
    }

    public static void main(String[] args) {
        Treap<Integer, String> treap = new Treap<>();
        treap.put(50, "fifty");
        treap.put(30, "thirty");
        treap.put(70, "seventy");
        treap.put(20, "twenty");
        treap.put(40, "forty");
        treap.put(60, "sixty");
        treap.put(80, "eighty");

        System.out.println("in-order traversal:");
        treap.inOrderTraversal();

        System.out.println("\nsearching for key 40: " + treap.get(40));
        System.out.println("removing key 30...");
        treap.remove(30);
        treap.inOrderTraversal();
    }
}
