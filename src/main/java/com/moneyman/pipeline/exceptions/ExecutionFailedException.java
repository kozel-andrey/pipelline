package com.moneyman.pipeline.exceptions;

public class ExecutionFailedException extends RuntimeException {


    public ExecutionFailedException(String name) {
        super("Failed execution of task: " + name);
    }
}
