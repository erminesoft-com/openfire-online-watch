package com.erminesoft.service;

import org.jivesoftware.openfire.SessionManager;
import org.jivesoftware.openfire.XMPPServer;
import org.jivesoftware.openfire.session.ClientSession;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Main task executed by this class is : when plugin starts it should determine all users who are now online
 * and save their status to Redis.
 */
class OnStartUpWorker implements Runnable {

    private OnlineStatusService service = RedisService.getInstance();

    @Override
    public void run() {
        List<String> usernames = receiveAllOnlineUsersNames();
        service.addOnlineStatus(usernames);
    }

    private List<String> receiveAllOnlineUsersNames() {
        SessionManager sessionManager = XMPPServer.getInstance().getSessionManager();
        Collection<ClientSession> sessions = sessionManager.getSessions();

        List<String> usersOnline = new ArrayList<String>(sessions.size());

        for (ClientSession session : sessions) {
            usersOnline.add(session.getAddress().getNode());
        }
        return usersOnline;
    }
}
