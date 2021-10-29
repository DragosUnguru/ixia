package com.some.example.second;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.Callable;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class CustomCallable implements Callable<Integer> {


    private static final String DELIMITER = ";";
    private static final int ERR_FILE_NOT_FOUND = -1;

    private final String pathToFile;

    CustomCallable(String pathToFile) {
        this.pathToFile = pathToFile;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Integer call() {
        // The current solution uses a simple Scanner instance to read entries from one
        // delimiter to another so we can safely assume that there won't be any memory problems
        // when very large files are given.
        // In the eventuality where we knew for sure that the given files weren't so big, we could
        // directly map the files in the memory for blazing fast access / read operations and also pass
        // the whole content of the file to the {@link CustomThread#extractHighestNumber()} method.
        Integer result = 0;
        Scanner scan;
        try {
            scan = new Scanner(new File(pathToFile));
            scan.useDelimiter(Pattern.compile(DELIMITER));
        } catch (FileNotFoundException e) {
            System.err.println("Couldn't find file " + pathToFile);

            return ERR_FILE_NOT_FOUND;
        }

        while (scan.hasNext()) {
            Integer chunkMaximum = extractHighestNumber(scan.next());
            result = Math.max(result, chunkMaximum);
        }

        scan.close();
        return result;
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
