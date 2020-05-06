package com.wuriyanto.jvmstash;

import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DefaultStashSocketFactory implements StashSocketFactory {

    private static final Logger LOGGER = Logger.getLogger(DefaultStashSocketFactory.class.getName());

    private String host;
    private Integer port;
    private Boolean secure;
    private Integer connectionTimeout;
    private Integer readTimeout;

    private SSLSocketFactory sslSocketFactory;

    public DefaultStashSocketFactory(String host, Integer port, Boolean secure,
                                     Integer connectionTimeout, Integer readTimeout,
                                     SSLSocketFactory sslSocketFactory) {
        this.host = host;
        this.port = port;
        this.secure = secure;
        this.connectionTimeout = connectionTimeout;
        this.readTimeout = readTimeout;
        this.sslSocketFactory = sslSocketFactory;

    }

    @Override
    public Socket create() throws IOException {
        Socket socket = null;
        try {
            socket = new Socket();
            socket.setKeepAlive(true);
            socket.setTcpNoDelay(true);

            SocketAddress socketAddress = new InetSocketAddress(getHost(), getPort());

            socket.connect(socketAddress, getConnectionTimeout());
            socket.setSoTimeout(getReadTimeout());

            if (this.secure) {
                if (null == sslSocketFactory) {
                    sslSocketFactory = (SSLSocketFactory) SSLSocketFactory.getDefault();
                }

                SSLSocket sslSocket = (SSLSocket) sslSocketFactory.createSocket(socket, getHost(), getPort(), true);

                // start handshake
                sslSocket.startHandshake();

                socket = sslSocket;
            }

            return socket;

        } catch (Exception e) {
            LOGGER.log(Level.WARNING, e.getMessage());
            if (socket != null) {
                socket.close();
            }
        }
        return socket;
    }

    @Override
    public String getHost() {
        return host;
    }

    @Override
    public void setHost(String host) {
        this.host = host;
    }

    @Override
    public Integer getPort() {
        return port;
    }

    @Override
    public void setPort(Integer port) {
        this.port = port;
    }

    @Override
    public Integer getConnectionTimeout() {
        return connectionTimeout;
    }

    @Override
    public void setConnectionTimeout(Integer connectionTimeout) {
        this.connectionTimeout = connectionTimeout;
    }

    @Override
    public Integer getReadTimeout() {
        return readTimeout;
    }

    @Override
    public void setReadTimeout(Integer readTimeout) {
        this.readTimeout = readTimeout;
    }
}
