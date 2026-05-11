package com.canteen.common.response;

public enum ResultCode {
    PHONE_ALREADY_EXISTS(10001, "手机号已注册"),
    BAD_CREDENTIALS(10002, "用户名或密码错误"),
    TOKEN_EXPIRED(10003, "Token已过期，请重新登录"),
    USER_DISABLED(10004, "用户已被禁用"),
    DISH_NOT_FOUND(20001, "菜品不存在"),
    STOCK_INSUFFICIENT(20002, "库存不足"),
    DISH_OFF_SHELF(20003, "菜品已下架"),
    NOT_IN_SELLING_SLOT(20004, "不在售卖时段"),
    ORDER_NOT_FOUND(30001, "订单不存在"),
    INVALID_STATUS_TRANSITION(30002, "订单状态不允许此操作"),
    PICKUP_CODE_INVALID(30003, "取餐码无效"),
    PICKUP_CODE_USED(40001, "取餐码已使用"),
    QUEUE_EMPTY(40002, "排队队列为空"),
    RATE_LIMIT(50001, "请求频率超限"),
    UNAUTHORIZED(50002, "未登录或Token无效");

    private final int code;
    private final String message;

    ResultCode(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() { return code; }
    public String getMessage() { return message; }
}
