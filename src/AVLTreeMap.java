package src;

// class for an avl tree map that keeps key-value pairs balanced
public class AVLTreeMap<K extends Comparable<K>, V> {

    private static class Node<K, V> {
        K key;
        V value;
        int height;
        Node<K, V> left, right;

        Node(K key, V value) {
            this.key = key;
            this.value = value;
            this.height = 1;
        }
    }

    private Node<K, V> root;

    private int height(Node<K, V> node) {
        return node == null ? 0 : node.height;
    }

    private void updateHeight(Node<K, V> node) {
        node.height = 1 + Math.max(height(node.left), height(node.right));
    }

    private int balanceFactor(Node<K, V> node) {
        return height(node.left) - height(node.right);
    }

    private Node<K, V> rotateRight(Node<K, V> y) {
        Node<K, V> x = y.left;
        Node<K, V> T2 = x.right;

        x.right = y;
        y.left = T2;

        updateHeight(y);
        updateHeight(x);
        return x;
    }

    private Node<K, V> rotateLeft(Node<K, V> x) {
        Node<K, V> y = x.right;
        Node<K, V> T2 = y.left;

        y.left = x;
        x.right = T2;

        updateHeight(x);
        updateHeight(y);
        return y;
    }

    public void put(K key, V value) {
        root = insert(root, key, value);
    }

    private Node<K, V> insert(Node<K, V> node, K key, V value) {
        if (node == null) return new Node<>(key, value);

        int cmp = key.compareTo(node.key);
        if (cmp < 0) {
            node.left = insert(node.left, key, value);
        } else if (cmp > 0) {
            node.right = insert(node.right, key, value);
        } else {
            node.value = value;
            return node;
        }

        updateHeight(node);

        int balance = balanceFactor(node);

        if (balance > 1 && key.compareTo(node.left.key) < 0)
            return rotateRight(node);
        if (balance < -1 && key.compareTo(node.right.key) > 0)
            return rotateLeft(node);
        if (balance > 1 && key.compareTo(node.left.key) > 0) {
            node.left = rotateLeft(node.left);
            return rotateRight(node);
        }
        if (balance < -1 && key.compareTo(node.right.key) < 0) {
            node.right = rotateRight(node.right);
            return rotateLeft(node);
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
        throw new RuntimeException("key not found: " + key);
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

    public static void main(String[] args) {
        AVLTreeMap<Integer, String> tree = new AVLTreeMap<>();
        tree.put(50, "fifty");
        tree.put(30, "thirty");
        tree.put(70, "seventy");
        tree.put(20, "twenty");
        tree.put(40, "forty");
        tree.put(60, "sixty");
        tree.put(80, "eighty");

        System.out.println("in-order traversal:");
        tree.inOrderTraversal();

        System.out.println("\ngetting key 40: " + tree.get(40));
    }
}
