package com.canteen.queue.websocket;

import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class ScreenWebSocketHandler extends TextWebSocketHandler {
    private final Map<Long, WebSocketSession> windowSessions = new ConcurrentHashMap<>();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        String path = session.getUri().getPath();
        Long windowId = extractWindowId(path);
        if (windowId != null) {
            windowSessions.put(windowId, session);
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        windowSessions.values().remove(session);
    }

    public void sendToWindow(Long windowId, String message) throws IOException {
        WebSocketSession session = windowSessions.get(windowId);
        if (session != null && session.isOpen()) {
            session.sendMessage(new TextMessage(message));
        }
    }

    private Long extractWindowId(String path) {
        try {
            String[] parts = path.split("/");
            return Long.valueOf(parts[parts.length - 1]);
        } catch (Exception e) {
            return null;
        }
    }
}
