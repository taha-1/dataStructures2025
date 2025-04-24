package tree;

import java.util.*;
import java.util.function.Function;

// this class benchmarks sorting algorithms with different input patterns
public class SortBenchmark {

    public static void main(String[] args) {
        int[] sizes = {100, 1000, 5000, 10000}; // input sizes
        String[] patterns = {"Random", "Ascending", "Descending", "NearlySorted"}; // input types

        for (int n : sizes) {
            System.out.printf("\n=== n=%d ===\n", n); // print size
            Integer[] random = generateRandom(n, 42);
            Integer[] asc = Arrays.copyOf(random, n); Arrays.sort(asc);
            Integer[] desc = Arrays.copyOf(asc, n); Arrays.sort(desc, Comparator.reverseOrder());
            Integer[] nearly = Arrays.copyOf(asc, n);
            Random r = new Random(42);
            for (int i = 0; i < n * 0.05; i++) {
                int i1 = r.nextInt(n);
                int i2 = r.nextInt(n);
                int tmp = nearly[i1]; nearly[i1] = nearly[i2]; nearly[i2] = tmp;
            }

            Map<String, Integer[]> dataMap = Map.of(
                "Random", random,
                "Ascending", asc,
                "Descending", desc,
                "NearlySorted", nearly
            );

            for (String pattern : patterns) {
                System.out.printf("-- Pattern: %s --\n", pattern);
                Integer[] data = dataMap.get(pattern);

                benchmark("PQSort", arr -> {
                    PriorityQueue<Integer> pq = new PriorityQueue<>();
                    for (int x : arr) pq.offer(x);
                    Integer[] out = new Integer[arr.length];
                    for (int i = 0; i < out.length; i++) out[i] = pq.poll();
                    return out;
                }, data);

                benchmark("TimSort", arr -> {
                    Integer[] a = arr.clone();
                    Arrays.sort(a);
                    return a;
                }, data);

                benchmark("QuickSort", arr -> {
                    Integer[] a = arr.clone();
                    quickSort(a, 0, a.length - 1);
                    return a;
                }, data);

                benchmark("MergeSort", arr -> {
                    Integer[] a = arr.clone();
                    mergeSort(a, 0, a.length - 1);
                    return a;
                }, data);
            }
        }
    }

    private static void benchmark(String name, Function<Integer[], Integer[]> sorter, Integer[] data) {
        Integer[] arr = data.clone();
        long start = System.nanoTime();
        sorter.apply(arr);
        long time = System.nanoTime() - start;
        System.out.printf("%s: %d µs\n", name, time / 1000);
    }

    private static void quickSort(Integer[] a, int lo, int hi) {
        if (lo < hi) {
            int p = partition(a, lo, hi);
            quickSort(a, lo, p - 1);
            quickSort(a, p + 1, hi);
        }
    }

    private static int partition(Integer[] a, int lo, int hi) {
        int pivot = a[hi];
        int i = lo;
        for (int j = lo; j < hi; j++) {
            if (a[j] <= pivot) {
                int tmp = a[i]; a[i] = a[j]; a[j] = tmp;
                i++;
            }
        }
        int tmp = a[i]; a[i] = a[hi]; a[hi] = tmp;
        return i;
    }

    private static void mergeSort(Integer[] a, int lo, int hi) {
        if (lo < hi) {
            int mid = (lo + hi) / 2;
            mergeSort(a, lo, mid);
            mergeSort(a, mid + 1, hi);
            merge(a, lo, mid, hi);
        }
    }

    private static void merge(Integer[] a, int lo, int mid, int hi) {
        Integer[] tmp = new Integer[hi - lo + 1];
        int i = lo, j = mid + 1, k = 0;
        while (i <= mid && j <= hi) {
            if (a[i] <= a[j]) tmp[k++] = a[i++];
            else tmp[k++] = a[j++];
        }
        while (i <= mid) tmp[k++] = a[i++];
        while (j <= hi) tmp[k++] = a[j++];
        System.arraycopy(tmp, 0, a, lo, tmp.length);
    }

    private static Integer[] generateRandom(int n, int seed) {
        return new Random(seed).ints(1, n * 10)
            .distinct().limit(n)
            .boxed().toArray(Integer[]::new);
    }
}
