package com.wuriyanto.jvmstash;

import java.io.IOException;
import java.net.Socket;

public interface StashSocketFactory {

    Socket create() throws IOException;

    String getHost();

    void setHost(String host);

    Integer getPort();

    void setPort(Integer port);

    Integer getConnectionTimeout();

    void setConnectionTimeout(Integer connectionTimeout);

    Integer getReadTimeout();

    void setReadTimeout(Integer readTimeout);
}
