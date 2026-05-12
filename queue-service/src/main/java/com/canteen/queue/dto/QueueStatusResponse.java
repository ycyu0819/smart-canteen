package com.canteen.queue.dto;

public class QueueStatusResponse {
    private String currentCall;
    private Object waitingList;
    private int waitingCount;

    public QueueStatusResponse(String currentCall, Object waitingList, int waitingCount) {
        this.currentCall = currentCall;
        this.waitingList = waitingList;
        this.waitingCount = waitingCount;
    }

    public String getCurrentCall() { return currentCall; }
    public Object getWaitingList() { return waitingList; }
    public int getWaitingCount() { return waitingCount; }
}
