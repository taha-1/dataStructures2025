package tree;

import java.util.*;
import java.util.function.Function; //for the interface

//  benchmarks five sorting algorithms using different input patterns
public class SortBenchmark {

    public static void main(String[] args) {
        int[] sizes = {100, 1000, 5000, 10000}; // array sizes to test
        String[] patterns = {"Random", "Ascending", "Descending", "NearlySorted"}; // input data types

        for (int n : sizes) {
            System.out.printf("\n=== n=%d ===\n", n); // print size heading
            Integer[] random = generateRandom(n, 42); // generate random data
            Integer[] asc = Arrays.copyOf(random, n); Arrays.sort(asc); // sorted ascending
            Integer[] desc = Arrays.copyOf(asc, n); Arrays.sort(desc, Comparator.reverseOrder()); // sorted descending
            Integer[] nearly = Arrays.copyOf(asc, n); // copy for nearly sorted
            Random r = new Random(42); // new random for swapping
            for (int i = 0; i < n * 0.05; i++) { // swap 5% of the elements
                int i1 = r.nextInt(n);
                int i2 = r.nextInt(n);
                int tmp = nearly[i1]; nearly[i1] = nearly[i2]; nearly[i2] = tmp;
            }

            // store all input patterns in a map
            Map<String, Integer[]> dataMap = Map.of(
                    "Random", random,
                    "Ascending", asc,
                    "Descending", desc,
                    "NearlySorted", nearly
            );

            for (String pattern : patterns) {
                System.out.printf("-- Pattern: %s --\n", pattern); // show pattern being tested
                Integer[] data = dataMap.get(pattern); // get the data array

                // pqsort using priority queue
                benchmark("PQSort", arr -> {
                    PriorityQueue<Integer> pq = new PriorityQueue<>();
                    for (int x : arr) pq.offer(x); // insert into pq
                    Integer[] out = new Integer[arr.length]; // output array
                    for (int i = 0; i < out.length; i++) out[i] = pq.poll(); // remove smallest
                    return out;
                }, data);

                // timsort using java built-in sort
                benchmark("TimSort", arr -> {
                    Integer[] a = arr.clone(); // clone array
                    Arrays.sort(a); // use java sort
                    return a;
                }, data);

                // quicksort
                benchmark("QuickSort", arr -> {
                    Integer[] a = arr.clone(); // clone array
                    quickSort(a, 0, a.length - 1); // sort with quicksort
                    return a;
                }, data);

                // mergesort
                benchmark("MergeSort", arr -> {
                    Integer[] a = arr.clone(); // clone array
                    mergeSort(a, 0, a.length - 1); // sort with mergesort
                    return a;
                }, data);
            }
        }
    }

    // measure how long a sorting function takes
    private static void benchmark(String name, Function<Integer[], Integer[]> sorter, Integer[] data) {
        Integer[] arr = data.clone(); // copy the array
        long start = System.nanoTime(); // start timer
        sorter.apply(arr); // sort the array
        long time = System.nanoTime() - start; // end timer
        System.out.printf("%s: %d µs\n", name, time / 1000); // print time in microseconds
    }

    // quicksort algorithm
    private static void quickSort(Integer[] a, int lo, int hi) {
        if (lo < hi) {
            int p = partition(a, lo, hi); // find pivot
            quickSort(a, lo, p - 1); // sort left
            quickSort(a, p + 1, hi); // sort right
        }
    }

    // helper for quicksort: partition step
    private static int partition(Integer[] a, int lo, int hi) {
        int pivot = a[hi]; // pivot element
        int i = lo;
        for (int j = lo; j < hi; j++) {
            if (a[j] <= pivot) {
                int tmp = a[i]; a[i] = a[j]; a[j] = tmp; // swap
                i++;
            }
        }
        int tmp = a[i]; a[i] = a[hi]; a[hi] = tmp; // final pivot swap
        return i;
    }

    // mergesort algorithm
    private static void mergeSort(Integer[] a, int lo, int hi) {
        if (lo < hi) {
            int mid = (lo + hi) / 2; // find middle
            mergeSort(a, lo, mid); // sort left half
            mergeSort(a, mid + 1, hi); // sort right half
            merge(a, lo, mid, hi); // merge both halves
        }
    }

    // merge step for mergesort
    private static void merge(Integer[] a, int lo, int mid, int hi) {
        Integer[] tmp = new Integer[hi - lo + 1]; // temporary array
        int i = lo, j = mid + 1, k = 0;
        while (i <= mid && j <= hi) { // merge both halves
            if (a[i] <= a[j]) tmp[k++] = a[i++];
            else tmp[k++] = a[j++];
        }
        while (i <= mid) tmp[k++] = a[i++]; // copy left
        while (j <= hi) tmp[k++] = a[j++]; // copy right
        System.arraycopy(tmp, 0, a, lo, tmp.length); // copy back to original
    }

    // generate a random array of unique integers
    private static Integer[] generateRandom(int n, int seed) {
        return new Random(seed).ints(1, n * 10)
                .distinct().limit(n)
                .boxed().toArray(Integer[]::new); // boxed to Integer[]
    }
}