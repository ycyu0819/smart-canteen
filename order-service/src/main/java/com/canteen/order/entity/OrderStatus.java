package com.canteen.order.entity;

public enum OrderStatus {
    PLACED, ACCEPTED, PREPARING, WAITING, PICKED_UP, CANCELLED;

    public boolean canTransitionTo(OrderStatus target) {
        return switch (this) {
            case PLACED -> target == ACCEPTED || target == CANCELLED;
            case ACCEPTED -> target == PREPARING || target == CANCELLED;
            case PREPARING -> target == WAITING;
            case WAITING -> target == PICKED_UP;
            default -> false;
        };
    }
}
