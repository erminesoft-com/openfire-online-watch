package com.erminesoft.listener;

import com.erminesoft.service.OnlineStatusService;
import com.erminesoft.service.RedisService;
import org.jivesoftware.openfire.session.ClientSession;
import org.jivesoftware.openfire.user.PresenceEventListener;
import org.jivesoftware.openfire.user.UserNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xmpp.packet.JID;
import org.xmpp.packet.Presence;

/**
 * This class responsible for adding/removing information about users statuses.
 */
public class OnlineStatusListener implements PresenceEventListener {

    private static final Logger logger = LoggerFactory.getLogger(OnlineStatusListener.class);

    private static OnlineStatusService service = RedisService.getInstance();

    @Override
    public void availableSession(ClientSession session, Presence presence) {
        try {
            service.addOnlineStatus(session.getUsername());
        } catch (UserNotFoundException e) {
            logger.warn("Not authenticated session");
        }
    }

    @Override
    public void unavailableSession(ClientSession session, Presence presence) {
        try {
            service.removeOnlineStatus(session.getUsername());
        } catch (UserNotFoundException e) {
            logger.warn("Not authenticated session");
        }
    }

    @Override
    public void presenceChanged(ClientSession session, Presence presence) {

    }

    @Override
    public void subscribedToPresence(JID subscriberJID, JID authorizerJID) {

    }

    @Override
    public void unsubscribedToPresence(JID unsubscriberJID, JID recipientJID) {

    }
}
