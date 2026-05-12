package com.canteen.queue.service;

import com.canteen.queue.websocket.ScreenWebSocketHandler;
import org.springframework.stereotype.Service;
import java.io.IOException;

@Service
public class ScreenPushService {
    private final ScreenWebSocketHandler handler;

    public ScreenPushService(ScreenWebSocketHandler handler) { this.handler = handler; }

    public void pushCall(Long windowId, Long orderId, String pickupNumber) {
        String msg = String.format(
                "{\"type\":\"NEW_CALL\",\"windowId\":%d,\"orderId\":%d,\"pickupNumber\":\"%s\",\"timestamp\":%d}",
                windowId, orderId, pickupNumber, System.currentTimeMillis());
        try { handler.sendToWindow(windowId, msg); } catch (IOException ignored) {}
    }
}
