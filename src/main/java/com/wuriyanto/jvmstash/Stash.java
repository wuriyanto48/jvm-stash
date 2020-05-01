package com.wuriyanto.jvmstash;

import javax.net.ssl.*;
import java.io.*;
import java.net.Socket;
import java.security.*;
import java.security.cert.CertificateException;
import java.util.logging.Level;
import java.util.logging.Logger;

/*
 * Wuriyanto 2020.
 * Stash class, represent OutputStream writer
 * this class will override all write method from OutputStream to write data into logstash
 */
public class Stash extends OutputStream {

    private static final Logger LOGGER = Logger.getLogger(Stash.class.getName());

    // represent Carriage Return and Line Feed in ASCII code
    private static final byte[] CRLF = new byte[]{13, 10};

    // represent socket client
    private Socket socket;

    // represent io writer
    private BufferedWriter writer;

    // represent io reader
    // not yet useful right now,
    // but in future maybe we need read the reply from server
    private BufferedReader reader;

    private Boolean closed;
    private Builder builder;

    private Stash(Builder builder) {
        this.builder = builder;

    }

    public void connect() throws StashException {
        try {
            this.socket = new Socket(this.builder.host, this.builder.port);

            if (this.builder.secure) {
                LOGGER.log(Level.INFO, "connection secure");

                KeyStore ks = KeyStore.getInstance("PKCS12");
                InputStream keyIn = this.builder.keyStoreIs;
                ks.load(keyIn, this.builder.keyStorePassword.toCharArray());

                KeyManagerFactory kmf = KeyManagerFactory.getInstance("SunX509");
                ks.load(keyIn, this.builder.keyStorePassword.toCharArray());

                TrustManagerFactory tmf = TrustManagerFactory.getInstance("SunX509");
                tmf.init(ks);

                // Create a SSLSocketFactory that allows for self signed certs
                SSLContext ctx = SSLContext.getInstance("TLS");
                ctx.init(kmf.getKeyManagers(), tmf.getTrustManagers(), null);

                SSLSocketFactory ssf = ctx.getSocketFactory();
                SSLSocket sslSocket = (SSLSocket) ssf.createSocket(this.builder.host, this.builder.port);

                // start handshake
                sslSocket.startHandshake();
                this.socket = sslSocket;
                // replace socket with sslsocket

            }
        } catch (IOException e) {
            LOGGER.log(Level.WARNING, e.getMessage());
            throw new StashException(e.getMessage());
        } catch (CertificateException e) {
            LOGGER.log(Level.WARNING, e.getMessage());
            throw new StashException("certificate error " + e.getMessage());
        } catch (NoSuchAlgorithmException e) {
            LOGGER.log(Level.WARNING, e.getMessage());
            throw new StashException("algorithm error " + e.getMessage());
        } catch (KeyStoreException e) {
            LOGGER.log(Level.WARNING, e.getMessage());
            throw new StashException("keystore error " + e.getMessage());
        } catch (KeyManagementException e) {
            LOGGER.log(Level.WARNING, e.getMessage());
            throw new StashException("key management error " + e.getMessage());
        }

        try {
            this.writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            this.reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } catch (IOException e) {
            LOGGER.log(Level.WARNING, e.getMessage());
            throw new StashException(e.getMessage());
        }
    }

    @Override
    public void write(int b) throws IOException {

    }

    @Override
    public void write(byte[] b) throws IOException {
        //super.write(b);
    }

    @Override
    public void close() throws IOException {
        this.socket.close();
        this.writer.close();
        this.reader.close();
        //super.close();
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
