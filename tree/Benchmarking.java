package tree;

import java.util.*;

// this class benchmarks insert time for treap and treemap
public class Benchmarking {

    // main method where the benchmarking happens
    public static void main(String[] args) {
        int[] sizes = {100, 1000, 5000, 10000}; // different sizes to test

        for (int size : sizes) { // loop through each size
            System.out.println("\n==== SIZE: " + size + " ===="); // print current size

            Integer[] randomData = generateRandomArray(size); // make random array

            // benchmark treap
            System.out.println("-- Treap insert --");
            Treap<Integer, String> treap = new Treap<>(); // create a new treap
            long start = System.nanoTime(); // start timer
            for (int i : randomData) {
                treap.put(i, "value"); // insert each key
            }
            long end = System.nanoTime(); // end timer
            System.out.println("Treap time: " + (end - start) / 1000 + " µs"); // print time in microseconds

            //java built in
            System.out.println("-- TreeMap insert --");
            TreeMap<Integer, String> treeMap = new TreeMap<>(); // create a new treemap
            start = System.nanoTime(); // start timer
            for (int i : randomData) {
                treeMap.put(i, "value"); // insert each key
            }
            end = System.nanoTime(); // end timer
            System.out.println("TreeMap time: " + (end - start) / 1000 + " µs"); // print time in microseconds
        }
    }

    // method to generate a random array of integers
    public static Integer[] generateRandomArray(int size) {
        Random rand = new Random(); // new random generator
        Integer[] arr = new Integer[size]; // make empty array
        for (int i = 0; i < size; i++) {
            arr[i] = rand.nextInt(size * 10); // fill it with random numbers
        }
        return arr; // return the array
    }
}
