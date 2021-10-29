package com.some.example.second;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class Driver {

    private static final String[] FILE_PATHS = {
            "C:\\Users\\ungur\\Documents\\projs\\playground\\resources\\second\\input1.txt",
            "C:\\Users\\ungur\\Documents\\projs\\playground\\resources\\second\\input2.txt",
            "C:\\Users\\ungur\\Documents\\projs\\playground\\resources\\second\\input3.txt",
            "C:\\Users\\ungur\\Documents\\projs\\playground\\resources\\second\\input4.txt" };

    /**
     * Main driver method defining the currently running tests.
     *
     * @param args command line arguments
     */
    public static void main(String[] args) {
        final SolvingMethod method = SolvingMethod.CONVENTIONAL;

        if (method.equals(SolvingMethod.EXECUTOR)) {
            solveUsingExecutorPool();
        } else {
            solveUsingConventionalThreads();
        }
    }

    /**
     * Solves the exercise using the conventional threads approach, making use of
     * {@link CustomThread} implementation.
     */
    private static void solveUsingConventionalThreads() {
        List<CustomThread> myThreads = new ArrayList<>();

        // Construct custom threads.
        for (String filePath : FILE_PATHS) {
            myThreads.add(new CustomThread(filePath));
        }

        // Run threads.
        for (CustomThread thread : myThreads) {
            thread.start();
        }

        // Wait for threads to finish.
        for (CustomThread thread : myThreads) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                // Simulate log.
                System.err.println("Thread " + thread.getName() + " couldn't be joined.");
            }
        }

        // Compute result of each thread.
        long result = myThreads.stream().mapToLong(CustomThread::getResult).sum();
        System.out.println(result);
    }

    /**
     * Solves the exercise using the conventional threads approach, making use of
     * {@link CustomCallable} implementation.
     */
    private static void solveUsingExecutorPool() {
        final int NUM_THREADS = FILE_PATHS.length;
        ExecutorService executor = Executors.newFixedThreadPool(NUM_THREADS);
        List<Future<Integer>> results = new ArrayList<>();

        // Submit threads execution.
        for (String filePath : FILE_PATHS) {
            results.add(executor.submit(new CustomCallable(filePath)));
        }
        executor.shutdown();

        // Compute result of each thread.
        long sum = results.stream().mapToLong(result -> {
            try {
                return result.get().longValue();
            } catch (InterruptedException | ExecutionException e) {
                // Simulate log.
                System.err.println("Thread failed.");
                return 0;
            }
        }).sum();
        System.out.println(sum);
    }
}
