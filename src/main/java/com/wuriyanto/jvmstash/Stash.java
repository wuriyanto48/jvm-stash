package com.wuriyanto.jvmstash;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.logging.Logger;

/*
 * Wuriyanto 2020.
 * Stash class, represent OutputStream writer
 * this class will override all write method from OutputStream to write data into logstash
 */
public class Stash extends OutputStream {

    private static final Logger LOGGER = Logger.getLogger(Stash.class.getName());

    private Socket socket;
    private Boolean closed;

    private Stash(Builder builder) {

    }

    @Override
    public void write(int b) throws IOException {

    }

    @Override
    public void write(byte[] b) throws IOException {
        super.write(b);
    }

    @Override
    public void close() throws IOException {
        super.close();
    }

    public static class Builder {
        private String host;
        private Integer port;
        private Boolean secure;
        private InputStream keyStoreIs;
        private String keyStorePassword;

        public Builder setHost(String host) {
            this.host = host;
            return this;
        }

        public Builder setPort(Integer port) {
            this.port = port;
            return this;
        }

        public Builder setSecure(Boolean secure) {
            this.secure = secure;
            return this;
        }

        public Builder setKeyStoreIs(InputStream keyStoreIs) {
            this.keyStoreIs = keyStoreIs;
            return this;
        }

        public Builder setKeyStorePassword(String keyStorePassword) {
            this.keyStorePassword = keyStorePassword;
            return this;
        }

        public Stash build() {
            return new Stash(this);
        }
    }
}
