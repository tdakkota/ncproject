package org.tdakkota.ncproject.resources;

import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.jboss.logging.Logger;
import org.tdakkota.ncproject.entities.IncidentEvent;
import org.tdakkota.ncproject.misc.IncidentEventEncoder;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@ApplicationScoped
@ServerEndpoint(value = "/ws", encoders = {IncidentEventEncoder.class}, decoders = {IncidentEventEncoder.class})
public class EventResource {
    private Map<String, Session> sessions = new ConcurrentHashMap<>();

    @Inject
    Logger log;

    @OnOpen
    public void onOpen(Session session) {
        log.info("got new subscriber: " + session.getId());
        sessions.put(session.getId(), session);
    }

    @OnClose
    public void onClose(Session session) {
        log.info("subscriber disconnected: " + session.getId());
        deleteSubscriber(session.getId());
    }

    @OnError
    public void onError(Session session, Throwable throwable) {
        log.info("subscriber disconnected: " + session.getId(), throwable);
        deleteSubscriber(session.getId());
    }

    private void deleteSubscriber(String id) {
        sessions.remove(id);
    }

    @OnMessage
    public void onMessage(String message) {
        // do nothing, channel is read only
    }

    @Incoming("incidents")
    public void busSubscriber(IncidentEvent event) {
        broadcast(event);
    }

    private void broadcast(IncidentEvent event) {
        log.info("broadcast event: " + event.toString() + ", sessions: " + sessions.size());

        sessions.values().forEach(s -> {
            s.getAsyncRemote().sendObject(event, result -> {
                if (result.getException() != null) {
                    log.info("failed to send message", result.getException());
                }
            });
        });
    }
}
