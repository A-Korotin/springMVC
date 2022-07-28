package se.ifmo.korotin.springmvc;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Component
public class WebSocketHandler extends TextWebSocketHandler {
    private final List<WebSocketSession> sessionList = new ArrayList<>();

    @NonNull
    private final AircraftRepository repository;

    public List<WebSocketSession> getSessionList() {
        return sessionList;
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws IOException {
        sessionList.add(session);
        System.out.println("Connection established from " + session + " @ " + Instant.now());
    }

    @Override
    protected void handleTextMessage(WebSocketSession session,
                                     TextMessage message) throws Exception {
        try {
            System.out.println("Message received: '" + message + "', from " + session);

            for (WebSocketSession sessionInList: sessionList) {
                if(sessionInList != session) {
                    sessionInList.sendMessage(message);
                    System.out.println("--> Sending message '" + message + "' to " +sessionInList);
                }
            }
        } catch (Exception e) {
            System.out.println("Exception handling message: " +
                    e.getLocalizedMessage());
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session,
                                      CloseStatus status) throws Exception {
        sessionList.remove(session);
        System.out.println("Connection closed by " + session + " @ " + Instant.now());
    }
}
