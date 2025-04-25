package src;

import org.junit.Test;

import static org.junit.jupiter.api.Assertions.*; // imports assertion methods

// class to test the treap implementation
public class TreapTest {

    // test inserting values and getting them back
    @Test
    public void testInsertAndGet() {
        Treap<Integer, String> treap = new Treap<>(); // create new treap
        treap.put(10, "ten"); // insert key 10
        treap.put(20, "twenty"); // insert key 20
        assertEquals("ten", treap.get(10)); // check key 10 returns "ten"
        assertEquals("twenty", treap.get(20)); // check key 20 returns "twenty"
    }

    // test updating a key that already exists
    @Test
    public void testUpdate() {
        Treap<Integer, String> treap = new Treap<>(); // create new treap
        treap.put(10, "ten"); // insert key 10
        treap.put(10, "TEN"); // update key 10 with new value
        assertEquals("TEN", treap.get(10)); // should return updated value
    }

    // test removing a key and expecting error when trying to get it
    @Test
    public void testRemove() {
        Treap<Integer, String> treap = new Treap<>(); // create new treap
        treap.put(5, "five"); // insert key 5
        treap.remove(5); // remove key 5
        assertThrows(RuntimeException.class, () -> treap.get(5)); // should throw error
    }

    // test printing keys in order (manually verify output)
    @Test
    public void testInOrderPrint() {
        Treap<Integer, String> treap = new Treap<>(); // create new treap
        treap.put(3, "three"); // insert key 3
        treap.put(1, "one"); // insert key 1
        treap.put(2, "two"); // insert key 2
        treap.inOrderTraversal(); // should print keys in sorted order: 1, 2, 3
    }
}
