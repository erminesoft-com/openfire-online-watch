package com.erminesoft.service;

import org.jivesoftware.util.JiveGlobals;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.Pipeline;

import java.io.IOException;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * This class responsible for implements methods to work with Redis and it settings also to read some properties
 * from database that Openfire use for it own needs.
 *
 * @see OnlineStatusService
 * @see RedisSettings
 * @see OpenfirePropertiesForRedis
 */
public final class RedisService implements OnlineStatusService, RedisSettings, OpenfirePropertiesForRedis {

    private static final Logger logger = LoggerFactory.getLogger(RedisService.class);

    private static final String ASTERISK = "*";

    private static RedisService instance;

    private final RedisConfig config = new RedisConfig();

    private RedisService() {
    }

    public static RedisService getInstance() {
        if (instance == null) {
            instance = new RedisService();
        }
        return instance;
    }

    @Override
    public void addOnlineStatus(String username) {
        logger.info("Invoke addOnlineStatus(), username = {}, pool = {}", username, config.receiveJedisPool());
        Jedis jedis = config.receiveJedisPool().getResource();
        jedis.hset(getAppropriateKeyForRedis(username), username, String.valueOf(new Date().getTime()));
        jedis.close();
    }

    @Override
    public void addOnlineStatus(List<String> usernames) {
        logger.info("Invoke addOnlineStatus(), usernames = {}, pool = {}", Arrays.toString(usernames.toArray()));
        Jedis jedis = config.receiveJedisPool().getResource();
        Pipeline p = jedis.pipelined();
        try {
            for (String username : usernames) {
                p.hset(getAppropriateKeyForRedis(username), username, String.valueOf(new Date().getTime()));
            }
            p.sync();
            p.close();
            jedis.close();
        } catch (IOException e) {
            logger.info("cannot close pipeline {}, message : {}", e, e.getMessage());
        }
    }

    @Override
    public void removeOnlineStatus(String username) {
        Jedis jedis = config.receiveJedisPool().getResource();
        jedis.hdel(getAppropriateKeyForRedis(username), username);
        jedis.close();
    }

    @Override
    public void removeOnlineStatus(List<String> username) {
        Jedis jedis = config.receiveJedisPool().getResource();
        Set<String> keys = jedis.keys(getAppropriateKeyForRedis(ASTERISK));
        jedis.del(keys.toArray(new String[keys.size()]));
        jedis.close();
    }

    @Override
    public void deleteAllKeys() {
        Jedis jedis = config.receiveJedisPool().getResource();
        Set<String> keys = jedis.keys(getAppropriateKeyForRedis(ASTERISK));
        jedis.del(keys.toArray(new String[keys.size()]));
        jedis.close();
    }

    @Override
    public void addOnlineStatusOnStartUp() {
        OnStartUpWorker worker = new OnStartUpWorker();
        Thread thread = new Thread(worker);
        thread.setDaemon(true);
        thread.start();
    }

    @Override
    public String getDatasourceUrl() {
        return config.getRedisUrl();
    }

    @Override
    public void setDatasourceUrl(String url) {
        config.setJedisServerUrl(url);
    }

    @Override
    public int getDatasourcePort() {
        return config.getRedisPort();
    }

    @Override
    public void setDatasourcePort(int port) {
        config.setJedisServerPort(port);
    }

    @Override
    public String getNamespaceInDatasource() {
        return config.getNamespaceInRedis();
    }

    @Override
    public void seNamespaceIntDatasource(String namespace) {
        config.setNamespaceInRedis(namespace);
    }

    @Override
    public String getKeyForRedisHost() {
        return config.getRedisUrlKey();
    }

    @Override
    public String getKeyForRedisNamespace() {
        return config.getNamespaceInRedisKey();
    }

    @Override
    public String getKeyForRedisPort() {
        return config.getRedisPortKey();
    }

    @Override
    public void removeAllConnections() {
        config.removeJedis();
    }

    private String getAppropriateKeyForRedis(final String username) {
        return buildRedisKey(config.getNamespaceInRedis(), username);
    }

    private String buildRedisKey(final String basicPart, final String username) {
        return new StringBuilder().append(basicPart).append(username.charAt(0)).toString();
    }

    /*
    This class represents settings to communicate with Redis.
    Word "ersto" in properties names is just "ERminesoft STatus Online" -> "ersto".
     */
    class RedisConfig {
        private final static String REDIS_NAMESPACE_KEY = "ersto.onlineservice.namespace";
        private final static String REDIS_NAMESPACE_DEFAULT_VALUE = "ersto_users_online_";
        private final static String REDIS_URL_KEY = "ersto.onlineservice.url";
        private final static String REDIS_URL_DEFAULT_VALUE = "localhost";
        private final static String REDIS_PORT_KEY = "ersto.onlineservice.port";
        private final static int REDIS_PORT_DEFAULT_VALUE = 6979;
        private final Logger logger = LoggerFactory.getLogger(RedisConfig.class);
        private String namespaceInRedis;
        private String jedisServerUrl;
        private int jedisServerPort;

        private JedisPool pool = null;


        private RedisConfig() {

            this.namespaceInRedis = JiveGlobals.getProperty(REDIS_NAMESPACE_KEY, REDIS_NAMESPACE_DEFAULT_VALUE);
            this.jedisServerUrl = JiveGlobals.getProperty(REDIS_URL_KEY, REDIS_URL_DEFAULT_VALUE);
            this.jedisServerPort = Integer.valueOf(JiveGlobals.getProperty(REDIS_PORT_KEY, String.valueOf(REDIS_PORT_DEFAULT_VALUE)));

            logger.info("Initialize pool, settings url : {}, port : {}", jedisServerUrl, jedisServerPort);
            pool = new JedisPool(new JedisPoolConfig(), jedisServerUrl, jedisServerPort);
            logger.info("Initialized pool = {}", pool);

        }

        public JedisPool receiveJedisPool() {
            return this.pool;
        }

        public void removeJedis() {
            if (this.pool != null) {
                this.pool = null;
            }
        }

        public String getRedisUrlKey() {
            return REDIS_URL_KEY;
        }

        public String getRedisPortKey() {
            return REDIS_PORT_KEY;
        }

        public String getNamespaceInRedisKey() {
            return REDIS_NAMESPACE_KEY;
        }

        public String getRedisUrl() {
            return this.jedisServerUrl;
        }

        public int getRedisPort() {
            return this.jedisServerPort;
        }

        public String getNamespaceInRedis() {
            return this.namespaceInRedis;
        }

        public void setNamespaceInRedis(String namespace) {
            JiveGlobals.setProperty(REDIS_NAMESPACE_KEY, namespace);
            this.namespaceInRedis = namespace;
        }

        public void setJedisServerUrl(String url) {
            JiveGlobals.setProperty(REDIS_URL_KEY, url);
            this.jedisServerUrl = url;
        }

        public void setJedisServerPort(int port) {
            JiveGlobals.setProperty(REDIS_PORT_KEY, String.valueOf(port));
            this.jedisServerPort = port;
        }
    }
}
