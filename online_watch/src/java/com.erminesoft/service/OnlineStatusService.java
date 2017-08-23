package com.erminesoft.service;

import java.util.List;

/**
 * {@code OnlineStatusService} is the API to operate with users online status (save and delete).
 */
public interface OnlineStatusService {

    /**
     * Designate that user now become online.
     *
     * @param username username who change his status
     */
    void addOnlineStatus(final String username);

    /**
     * Designate that collection of users now become online.
     *
     * @param usernames collection of usernames who changed they status to online
     */
    void addOnlineStatus(final List<String> usernames);

    /**
     * Designate that user now become offline.
     *
     * @param username username who change his status
     */
    void removeOnlineStatus(final String username);

    /**
     * Designate that collection of users now become offline.
     *
     * @param usernames collection of usernames who changed they status to offline
     */
    void removeOnlineStatus(final List<String> usernames);


    /**
     * Delete all entrances that connect to users online/offline statuses.
     */
    void deleteAllKeys();

    /**
     * This method find all users who are online in a moment it was invoked and
     * designate that collection of users now become online.
     */
    void addOnlineStatusOnStartUp();
}
