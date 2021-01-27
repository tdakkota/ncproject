package org.tdakkota.ncproject.resources;

import io.quarkus.test.common.http.TestHTTPResource;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;
import org.tdakkota.ncproject.entities.Incident;
import org.tdakkota.ncproject.entities.IncidentEvent;
import org.tdakkota.ncproject.misc.IncidentEventEncoder;
import org.tdakkota.ncproject.services.IncidentEventEmitter;

import javax.inject.Inject;
import javax.websocket.*;
import java.net.URI;
import java.util.Optional;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;

@QuarkusTest
public class EventResourceTest {
    private static final LinkedBlockingDeque<IncidentEvent> MESSAGES = new LinkedBlockingDeque<>();
    private static final Object openNotify = new Object();

    // Queue poll timeout in seconds.
    private final int POLL_TIMEOUT = 10;

    @TestHTTPResource("/ws")
    URI uri;

    @Inject
    IncidentEventEmitter emitter;


    @Test
    public void testWebsocket() throws Exception {
        try (Session session = ContainerProvider.getWebSocketContainer().connectToServer(Client.class, uri)) {
            synchronized (openNotify) {
                openNotify.wait(POLL_TIMEOUT * 1000);
            }

            // Send event via emitter.
            Incident i = new Incident();
            i.setId(10L);
            emitter.opened(i);

            // Wait for notify from WS endpoint.
            IncidentEvent e = Optional.ofNullable(MESSAGES.poll(POLL_TIMEOUT, TimeUnit.SECONDS)).orElseThrow();
            assertEquals(i.getId(), e.getId());
            assertEquals(IncidentEvent.EventType.OPENED, e.getType());
        }
    }

    @ClientEndpoint(encoders = {IncidentEventEncoder.class}, decoders = {IncidentEventEncoder.class})
    public static class Client {

        @OnOpen
        public void open(Session session) {
            // Send a message to indicate that we are ready,
            // as the message handler may not be registered immediately after this callback.
            session.getAsyncRemote().sendText("_ready_");
            synchronized (openNotify) {
                openNotify.notifyAll();
            }
        }

        @OnClose
        public void close(Session session, CloseReason reason) {
            Optional.ofNullable(reason).ifPresent(r -> System.out.println(r.getReasonPhrase()));
        }


        @OnError
        public void error(Session session, Throwable throwable) {
            Optional.ofNullable(throwable).ifPresent(Throwable::printStackTrace);
        }

        @OnMessage
        void message(IncidentEvent e) {
            MESSAGES.add(e);
        }

    }
}
