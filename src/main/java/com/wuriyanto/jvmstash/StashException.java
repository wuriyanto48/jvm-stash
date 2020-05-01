package com.wuriyanto.jvmstash;

public class StashException extends Exception {

    public StashException(String message) {
        super("stash error : " + message);
    }
}
