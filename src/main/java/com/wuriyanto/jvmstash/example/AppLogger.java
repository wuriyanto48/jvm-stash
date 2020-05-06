package com.wuriyanto.jvmstash.example;

import com.wuriyanto.jvmstash.Stash;
import com.wuriyanto.jvmstash.StashException;
import com.wuriyanto.jvmstash.StashLogHandler;

import java.io.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AppLogger {

    private static final Logger LOGGER = Logger.getLogger(AppLogger.class.getName());

    public static void main(String[] args) throws StashException {

        InputStream keyStore = null;

        try {
            keyStore = new FileInputStream("/Users/wuriyanto/Documents/java-work/jvm-stash/src/main/resources/server.p12");
        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.exit(1);
        }

        Stash stash = new Stash.Builder()
                .setHost("localhost")
                .setPort(5000)
                .setSecure(true)
                .setKeyStoreIs(keyStore)
                .setKeyStorePassword("damn12345")
                .build();

        try {
            stash.connect();
        } catch (StashException e) {
            System.out.println(e.getMessage());
            System.exit(1);
        }

        // Set Handler with StashLogHandler
        LOGGER.addHandler(new StashLogHandler(stash));

        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

        while (true) {
            try {
                String action = reader.readLine();
                if (action.equals("exit")) {
                    break;
                }

                // Use standar log that uses StashLogHandler
                LOGGER.log(Level.INFO, action);

            } catch (IOException e) {
                System.out.println(e.getMessage());
                break;
            }
        }

        try {
            stash.close();
        } catch (IOException e) {
            System.out.println(e.getMessage());
            System.exit(1);
        }
    }
}
