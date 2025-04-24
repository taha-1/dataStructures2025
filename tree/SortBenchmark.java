package tree;

import java.util.*;
import java.util.function.Function;

// this class benchmarks sorting algorithms with different input patterns
public class SortBenchmark {


    public static void main(String[] args) {
        int[] sizes = {100, 1000}; // test sizes
        String[] patterns = {"Random", "Ascending", "Descending"}; // test input patterns

        for (int n : sizes) { // loop through sizes
            System.out.printf("\n=== n=%d ===\n", n); // show current size
            Integer[] random = generateRandom(n, 42); // random data
            Integer[] asc = Arrays.copyOf(random, n); // copy for sorted
            Arrays.sort(asc); // sort ascending
            Integer[] desc = Arrays.copyOf(asc, n); // copy for descending
            Arrays.sort(desc, Comparator.reverseOrder()); // sort descending

            // map input patterns to data arrays
            Map<String, Integer[]> dataMap = Map.of(
                    "Random", random,
                    "Ascending", asc,
                    "Descending", desc
            );

            for (String pattern : patterns) { // loop through each pattern
                System.out.printf("-- Pattern: %s --\n", pattern);
                Integer[] data = dataMap.get(pattern); // get the array for that pattern

                // priority queue sort (acts like heap sort)
                benchmark("PQSort", arr -> {
                    PriorityQueue<Integer> pq = new PriorityQueue<>();
                    for (int x : arr) pq.offer(x); // insert all
                    Integer[] out = new Integer[arr.length]; // output array
                    for (int i = 0; i < out.length; i++) out[i] = pq.poll(); // extract min
                    return out;
                }, data);

                // java built-in sort (tim sort)
                benchmark("JavaSort", arr -> {
                    Integer[] a = arr.clone(); // make a copy
                    Arrays.sort(a); // sort using java
                    return a;
                }, data);
            }
        }
    }

    // method to benchmark a sorting function
    private static void benchmark(String name, Function<Integer[], Integer[]> sorter, Integer[] data) {
        Integer[] arr = data.clone(); // clone the input
        long start = System.nanoTime(); // start timer
        sorter.apply(arr); // run the sort
        long time = System.nanoTime() - start; // calculate time
        System.out.printf("%s: %d µs\n", name, time / 1000); // print in microseconds
    }

    // generate a random array of unique integers
    private static Integer[] generateRandom(int n, int seed) {
        return new Random(seed).ints(1, n * 10) // create stream of ints
                .distinct().limit(n)            // make them unique and limit to size
                .boxed().toArray(Integer[]::new); // box to Integer[] and return
    }
}