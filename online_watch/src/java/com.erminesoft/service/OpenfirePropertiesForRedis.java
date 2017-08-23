package com.erminesoft.service;

/**
 * {@code OpenfirePropertiesForRedis} is the API to operate with Openfire`s properties stored in the database.
 */
public interface OpenfirePropertiesForRedis {

    /**
     * Receive Redis` host property name from ofProperty table.
     *
     * @return host that Redis used
     */
    String getKeyForRedisHost();

    /**
     * Receive Redis` namespace property name from ofProperty table.
     *
     * @return namespace that used to make keys in Redis
     */
    String getKeyForRedisNamespace();

    /**
     * Receive Redis` port property name from ofProperty table.
     *
     * @return port that Redis used
     */
    String getKeyForRedisPort();
}
