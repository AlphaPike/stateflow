package com.stateflow.common.core.enums;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

@AllArgsConstructor
public enum RefundType {
    USER_REFUND("USER_REFUND", "用户主动退款"),
    AFTER_SALES_REFUND("AFTER_SALES_REFUND", "售后退款"),
    NO_VERIFY_AUTO_REFUND("NO_VERIFY_AUTO_REFUND", "未核销订单自动退款"),
    DEPOSIT_REFUND("DEPOSIT_REFUND", "押金退款");


    @Getter
    private final String refundType;
    @Getter
    private final String refundReason;


}
