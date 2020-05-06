package com.wuriyanto.jvmstash.example;

import com.wuriyanto.jvmstash.JsonUtil;
import com.wuriyanto.jvmstash.Stash;
import com.wuriyanto.jvmstash.StashException;

import java.io.*;
import java.util.Date;
import java.util.UUID;
import java.util.logging.*;

/**
 * example using jvm-stash
 */
public class App {

    private static final Logger LOGGER = Logger.getLogger(App.class.getName());

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

        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

        while(true) {
            try {
                String action = reader.readLine();
                if (action.equals("exit")) {
                    break;
                }

                Event event = new Event();
                event.setId(UUID.randomUUID().toString());
                event.setActionName(action);
                event.setEndpoint("http://my.dev/"+action);
                event.setTime(new Date());
                String json = JsonUtil.dataToJson(event);

                stash.write(json.getBytes());

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

    static class Event {

        private String id;
        private String actionName;
        private String endpoint;
        private Date time;


        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getActionName() {
            return actionName;
        }

        public void setActionName(String actionName) {
            this.actionName = actionName;
        }

        public String getEndpoint() {
            return endpoint;
        }

        public void setEndpoint(String endpoint) {
            this.endpoint = endpoint;
        }

        public Date getTime() {
            return time;
        }

        public void setTime(Date time) {
            this.time = time;
        }
    }

}
