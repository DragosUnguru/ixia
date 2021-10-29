package com.some.example.second;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class CustomThread extends Thread {

    private static final String DELIMITER = ";";
    private static final int ERR_FILE_NOT_FOUND = -1;

    private final String pathToFile;
    private volatile long result;

    CustomThread(String pathToFile) {
        this.pathToFile = pathToFile;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void run() {
        // The current solution uses a simple Scanner instance to read entries from one
        // delimiter to another so we can safely assume that there won't be any memory problems
        // when very large files are given.
        // In the eventuality where we knew for sure that the given files weren't so big, we could
        // directly map the files in the memory for blazing fast access / read operations and also pass
        // the whole content of the file to the {@link CustomThread#extractHighestNumber()} method.
        Scanner scan;
        try {
            scan = new Scanner(new File(pathToFile));
            scan.useDelimiter(Pattern.compile(DELIMITER));
        } catch (FileNotFoundException e) {
            System.err.println("Couldn't find file " + pathToFile);

            result = ERR_FILE_NOT_FOUND;
            return;
        }

        while (scan.hasNext()) {
            Integer chunkMaximum = extractHighestNumber(scan.next());
            result = Math.max(result, chunkMaximum);
        }

        scan.close();
    }

    /**
     * Getter returning the computed result of this thread.
     * Result is marked as volatile to make sure the master thread will read the
     * updated value, bypassing the thread-specific cache when writing to it.
     * The reason why {@link CustomThread#result} is of primitive type {@link long}
     * is because it's better to update the value of it by using atomic operations
     * rather than un-atomic reassigning operation of {@link Integer } wrappers.
     *
     * @return the computed result of the current thread.
     */
    public long getResult() {
        return result;
    }

    /**
     * Checks weather or not the thread has thrown an error (the one and only error).
     *
     * @return true if file couldn't be found; false otherwise.
     */
    public boolean returnedError() {
        return result == ERR_FILE_NOT_FOUND;
    }

    /**
     * Given a String full of gibberish, parse and return the maximum number found.
     * Having as {@code haystack} example the value: {@code "kjsfd23rnkjnef34ohi43;3g34;34t34t;ggwse"}
     * the method will return {@code 43}.
     *
     * @param haystack the String to search in
     * @return the highest number found in the haystack.
     */
    private Integer extractHighestNumber(String haystack) {
        List<Integer> numbers = Arrays.stream(haystack.split("[^0-9]"))
                .filter(result -> !result.isEmpty()).map(Integer::parseInt).collect(Collectors.toList());
        return numbers.isEmpty() ? 0 : Collections.max(numbers);
    }
}
