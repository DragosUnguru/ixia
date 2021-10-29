package com.some.example.second;


/**
 * Enum describing the possible approaches implemented of the given #2 problem.
 */
public enum SolvingMethod {
    CONVENTIONAL("Conventional method using Threads."),
    EXECUTOR("Fixed thread pool executor method using Callables.");

    private String description;

    SolvingMethod(String description) {
        this.description = description;
    }
}
