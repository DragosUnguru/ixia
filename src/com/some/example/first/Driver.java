package com.some.example.first;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Driver {

    /**
     * Main driver method defining the currently running tests.
     *
     * @param args command line arguments
     */
    public static void main(String[] args) {
        final int QUERIES_START = 1;
        final int QUERIES_END = 10000;

        List<Integer> queries = IntStream.rangeClosed(QUERIES_START, QUERIES_END)
                .boxed().collect(Collectors.toList());
        Map<Integer, String> mappings = new HashMap<>();

        mappings.put(7, "mere");
        mappings.put(8, "pere");
        mappings.put(3, "capsuni");
        mappings.put(21, "gutui");

        solve(queries, mappings);
    }

    /**
     * Solves and prints to stdout the divisibility problem.
     *
     * @param queries list of queries to resolve
     * @param mapping mapping of divisibility factors and the desired string
     */
    private static void solve(List<Integer> queries, Map<Integer, String> mapping) {
        System.out.println(queries.stream().map(query ->
                getResult(getDivisors(query), mapping, query.toString())).collect(Collectors.toList()));
    }

    /**
     * Takes advantage of the fact that we can find the divisors in pairs of two.
     * To maintain the descending order of the found pairs we're making use of a
     * maximum-heap structure. Therefore, we get all the divisors in a descending order
     * in a final complexity of O(sqrt(n)log(n)), much smaller than O(n).
     *
     * @param number integer whose divisors are computed.
     * @return an {@link Iterable} implementation that provides the divisors in the correct prioritized order.
     */
    private static Iterable<Integer> getDivisors(Integer number) {
        // Max Heap.
        PriorityQueue<Integer> priorityQueue = new PriorityQueue<>(Collections.reverseOrder());

        // The number itself is a valid divisor.
        priorityQueue.add(number);

        for (int i = (int) Math.sqrt(number); i > 1; --i) {

            if (number % i == 0) {
                if (number / i != i) {
                    // Don't add duplicates. For example for 25 there would be the (5, 5) pair.
                    priorityQueue.add(number / i);
                }

                priorityQueue.add(i);
            }
        }

        return priorityQueue;
    }

    /**
     * Util helper method that solves the problem for one given query.
     *
     * @param divisors  list of divisors of the given query in the correct prioritized order
     * @param mapping   the divisibility to output String mapping
     * @param orDefault the fallback value to print when no match was found
     * @return String representing the solution for the given query.
     */
    private static String getResult(Iterable<Integer> divisors, Map<Integer, String> mapping, String orDefault) {
        for (Integer divisor : divisors) {
            if (mapping.containsKey(divisor)) {
                return mapping.get(divisor);
            }
        }

        return orDefault;
    }
}
