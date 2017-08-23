package com.erminesoft;

import com.erminesoft.listener.OnlineStatusListener;
import com.erminesoft.service.OnlineStatusService;
import com.erminesoft.service.OpenfirePropertiesForRedis;
import com.erminesoft.service.RedisService;
import com.erminesoft.service.RedisSettings;
import org.jivesoftware.openfire.container.Plugin;
import org.jivesoftware.openfire.container.PluginManager;
import org.jivesoftware.openfire.user.PresenceEventDispatcher;
import org.jivesoftware.util.JiveGlobals;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

/**
 * Openfire "Online Watch" plugin.
 * Serve information about online/offline status with Redis.
 */
public class OnlineWatch implements Plugin {

    private static final Logger logger = LoggerFactory.getLogger(OnlineWatch.class);

    private OnlineStatusListener statusListener = new OnlineStatusListener();

    private RedisSettings redisSettings = RedisService.getInstance();
    private OpenfirePropertiesForRedis openfireSettingsForRedis = RedisService.getInstance();
    private OnlineStatusService service = RedisService.getInstance();

    @Override
    public void initializePlugin(PluginManager manager, File pluginDirectory) {
        PresenceEventDispatcher.addListener(statusListener);
        service.addOnlineStatusOnStartUp();
    }

    @Override
    public void destroyPlugin() {
        PresenceEventDispatcher.removeListener(statusListener);
        service.deleteAllKeys();
        redisSettings.removeAllConnections();
    }

    public String getDatasourceUrl() {
        return redisSettings.getDatasourceUrl();
    }

    public void setDatasourceUrl(String anotherUrl) {
        logger.info("Online_Watch plugin : set new Host : {}", anotherUrl);
        JiveGlobals.setProperty(openfireSettingsForRedis.getKeyForRedisHost(), anotherUrl);
        redisSettings.setDatasourceUrl(anotherUrl);
    }

    public String getDatasourcePort() {
        return String.valueOf(redisSettings.getDatasourcePort());
    }

    public void setDatasourcePort(String anotherPort) {
        logger.info("Online_Watch plugin : set new Port : {}", anotherPort);
        JiveGlobals.setProperty(openfireSettingsForRedis.getKeyForRedisPort(), anotherPort);
        redisSettings.setDatasourcePort(Integer.valueOf(anotherPort));
    }

    public String getCacheNamespace() {
        return redisSettings.getNamespaceInDatasource();
    }

    public void setCacheNamespace(String anotherNamespace) {
        logger.info("Online_Watch plugin : set new Namespace : {}", anotherNamespace);
        JiveGlobals.setProperty(openfireSettingsForRedis.getKeyForRedisNamespace(), anotherNamespace);
        redisSettings.seNamespaceIntDatasource(anotherNamespace);
    }
}
