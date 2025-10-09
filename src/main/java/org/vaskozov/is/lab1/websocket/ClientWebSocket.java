package org.vaskozov.is.lab1.websocket;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.json.bind.Jsonb;
import jakarta.json.bind.JsonbBuilder;
import jakarta.json.bind.JsonbConfig;
import jakarta.websocket.OnClose;
import jakarta.websocket.OnMessage;
import jakarta.websocket.OnOpen;
import jakarta.websocket.Session;
import jakarta.websocket.server.ServerEndpoint;
import org.vaskozov.is.lab1.bean.Person;
import org.vaskozov.is.lab1.lib.BroadcastMessage;
import org.vaskozov.is.lab1.lib.PersonState;

import java.util.HashSet;
import java.util.Set;

@ApplicationScoped
@ServerEndpoint("/ws/subscribe")
public class ClientWebSocket {
    private static final JsonbConfig JSONB_CONFIG = new JsonbConfig();
    private static final Jsonb JSONB = JsonbBuilder.create(JSONB_CONFIG);

    private final Set<Session> sessions = new HashSet<>();

    @OnOpen
    public void onOpen(Session session) {
        synchronized (sessions) {
            sessions.add(session);
        }
        System.out.println("New session: " + session.getId());
    }

    @OnMessage
    public void onMessage(String message, Session session) {
        System.out.println("Received message: " + message + " from: " + session.getId());
    }

    @OnClose
    public void onClose(Session session) {
        synchronized (sessions) {
            sessions.remove(session);
        }
        System.out.println("Closed session: " + session.getId());
    }

    public void broadcastPersonDeleted(Person person) {
        String personAsJson = JSONB.toJson(new BroadcastMessage(PersonState.DELETED, person));

        synchronized (sessions) {
            for (Session session : sessions) {
                sendPersonToClient(personAsJson, session);
            }
        }
    }

    public void broadcastPersonUpdate(Person person) {
        String personAsJson = JSONB.toJson(new BroadcastMessage(PersonState.UPDATED, person));

        synchronized (sessions) {
            for (Session session : sessions) {
                sendPersonToClient(personAsJson, session);
            }
        }
    }

    private void sendPersonToClient(String personAsJson, Session session) {
        if (session.isOpen()) {
            try {
                session.getBasicRemote().sendText(personAsJson);
            } catch (Exception exception) {
                System.out.println("Exception: " + exception);
                exception.getStackTrace();
            }
        } else {
            System.out.println("Not conntected");
        }
    }
}
