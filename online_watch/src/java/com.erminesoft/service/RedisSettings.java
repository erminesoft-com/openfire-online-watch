package com.erminesoft.service;

/**
 * {@code RedisSettings} is the API to operate with Redis via some Java library.
 */
public interface RedisSettings {

    /**
     * Receive the Redis` host.
     *
     * @return e.g. "localhost"
     */
    String getDatasourceUrl();

    /**
     * Setup the Redis` host.
     *
     * @param url the host that should be used to make connection with Redis
     */
    void setDatasourceUrl(String url);

    /**
     * Receive the port value that Redis use.
     *
     * @return the {@code int} value that Redis use
     */
    int getDatasourcePort();

    /**
     * Setup the Redis` port.
     *
     * @param port the port that should be used to make connection with Redis
     */
    void setDatasourcePort(int port);

    /**
     * Receive namespace that will be used in Redis as key or (! important) a part of key.
     *
     * @return the basic namespace for keys in redis or it may be a key
     */
    String getNamespaceInDatasource();

    /**
     * Setup namespace that will be used in Redis as key or (! important) a part of key.
     *
     * @param namespace the basic namespace for keys in redis or it may be a key
     */
    void seNamespaceIntDatasource(String namespace);

    /**
     * Remove all connections to Redis.
     */
    void removeAllConnections();
}
