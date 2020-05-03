package com.wuriyanto.jvmstash;

import java.io.IOException;
import java.io.OutputStream;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

public class StashLogHandler extends Handler {

    private static final Logger LOGGER = Logger.getLogger(StashLogHandler.class.getName());

    private final OutputStream out;

    public StashLogHandler(OutputStream out) {
        this.out = out;
    }

    @Override
    public void publish(LogRecord record) {
        String json = JsonUtil.dataToJson(record);
        try {
            out.write(json.getBytes());
        } catch (IOException e) {
            LOGGER.log(Level.WARNING, e.getMessage());
        }
    }

    @Override
    public void flush() {

    }

    @Override
    public void close() throws SecurityException {

    }
}
