package src; 

import java.util.Random; 
import java.util.NoSuchElementException; 

// this class creates a treap (a mix of binary search tree and heap)
public class Treap<K extends Comparable<K>, V> {

    //defines a node in the treap
    private static class Node<K, V> {
        K key; // the key of the node
        V value; // the value associated with the key
        int priority; // random priority for balancing
        Node<K, V> left, right; // left and right child nodes

        Node(K key, V value) {
            this.key = key; // set the key
            this.value = value; // set the value
            this.priority = new Random().nextInt(); // generate a random priority
        }
    }

    private Node<K, V> root; // the root of the treap

    // adds or updates a key-value pair in the treap
    public void put(K key, V value) {
        root = insert(root, key, value); // insert starting from the root
    }

    // recursively inserts a key-value pair and balances the treap
    private Node<K, V> insert(Node<K, V> node, K key, V value) {
        if (node == null) return new Node<>(key, value); // if no node, create one

        int cmp = key.compareTo(node.key); // compare the keys

        if (cmp < 0) { // if the key is smaller, go left
            node.left = insert(node.left, key, value);
            if (node.left.priority > node.priority) node = rotateRight(node); // fix heap
        } else if (cmp > 0) { // if the key is bigger, go right
            node.right = insert(node.right, key, value);
            if (node.right.priority > node.priority) node = rotateLeft(node); // fix heap
        } else {
            node.value = value; // update value if key already exists
        }
        return node; // return the current node
    }

    // gets the value associated with a key
    public V get(K key) {
        Node<K, V> current = root; // start at the root

        while (current != null) {
            int cmp = key.compareTo(current.key); // compare keys
            if (cmp < 0) current = current.left; // go left
            else if (cmp > 0) current = current.right; // go right
            else return current.value; // key found, return value
        }

        throw new NoSuchElementException("key not found: " + key); // key not found
    }

    // removes a key from the treap
    public void remove(K key) {
        root = delete(root, key); // start from the root
    }

    // recursively deletes a key and balances the tree
    private Node<K, V> delete(Node<K, V> node, K key) {
        if (node == null) return null; // base case: not found

        int cmp = key.compareTo(node.key);

        if (cmp < 0) {
            node.left = delete(node.left, key); // go left
        } else if (cmp > 0) {
            node.right = delete(node.right, key); // go right
        } else {
            // key found
            if (node.left == null) return node.right; // only right child
            if (node.right == null) return node.left; // only left child

            // both children exist
            if (node.left.priority > node.right.priority) {
                node = rotateRight(node); // rotate right to move down
                node.right = delete(node.right, key); // continue deleting
            } else {
                node = rotateLeft(node); // rotate left to move down
                node.left = delete(node.left, key); // continue deleting
            }
        }

        return node; // return updated node
    }

    // prints the tree in sorted order (in-order traversal)
    public void inOrderTraversal() {
        inOrderTraversal(root); // call helper method on root
    }

    // helper method for in-order traversal
    private void inOrderTraversal(Node<K, V> node) {
        if (node != null) {
            inOrderTraversal(node.left); // visit left
            System.out.println(node.key + " => " + node.value); // print current
            inOrderTraversal(node.right); // visit right
        }
    }

    // rotates the subtree right and returns the new root
    private Node<K, V> rotateRight(Node<K, V> node) {
        Node<K, V> left = node.left; // save left child
        node.left = left.right; // move right child of left to left of node
        left.right = node; // make node right child of left
        return left; // return new root
    }

    // rotates the subtree left and returns the new root
    private Node<K, V> rotateLeft(Node<K, V> node) {
        Node<K, V> right = node.right; // save right child
        node.right = right.left; // move left child of right to right of node
        right.left = node; // make node left child of right
        return right; // return new root
    }

    // main method to test treap functions
    public static void main(String[] args) {
        Treap<Integer, String> treap = new Treap<>(); // create a new treap instance

        // add some values
        treap.put(50, "fifty");
        treap.put(30, "thirty");
        treap.put(70, "seventy");
        treap.put(20, "twenty");
        treap.put(40, "forty");
        treap.put(60, "sixty");
        treap.put(80, "eighty");

        // print the treap in sorted order
        System.out.println("in-order traversal:");
        treap.inOrderTraversal();

        // test get and remove
        System.out.println("\nsearching for key 40: " + treap.get(40));
        System.out.println("removing key 30...");
        treap.remove(30);
        treap.inOrderTraversal();
    }
}
