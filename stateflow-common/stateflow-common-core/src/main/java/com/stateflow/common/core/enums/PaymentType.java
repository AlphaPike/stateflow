package com.stateflow.common.core.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum PaymentType {
    WX_PAY("WEIXIN_PAYMENT", "微信支付"),
    ALIPAY("ALIPAY_PAYMENT", "支付宝支付"),
    /** 微信免押（微信支付分） */
    WX_DEPOSIT_FREE("WEIXIN_DEPOSIT_FREE", "微信信用免押"),
    /** 支付宝免押（预留） */
    ALIPAY_DEPOSIT_FREE("ALIPAY_DEPOSIT_FREE", "支付宝免押");

    final String code;
    final String desc;

    /**
     * 根据代码获取状态
     */
    public static PaymentType fromCode(String code) {
        if (code == null) {
            return null;
        }
        for (PaymentType state : values()) {
            if (state.code.equals(code)) {
                return state;
            }
        }
        return null;
    }

    public static String toDesc(String code) {
        PaymentType paymentType = fromCode(code);
        if (paymentType == null) {
            return null;
        }
        return paymentType.getDesc();
    }

    public boolean isDepositFree() {
        return this == WX_DEPOSIT_FREE || this == ALIPAY_DEPOSIT_FREE;
    }

    public boolean isWeixin() {
        return this == WX_PAY || this == WX_DEPOSIT_FREE;
    }

    public boolean isAlipay() {
        return this == ALIPAY || this == ALIPAY_DEPOSIT_FREE;
    }

    /** 免押失败转付押金时对应的普通支付方式 */
    public PaymentType toCashPayment() {
        if (this == WX_DEPOSIT_FREE) {
            return WX_PAY;
        }
        if (this == ALIPAY_DEPOSIT_FREE) {
            return ALIPAY;
        }
        return this;
    }

    public static boolean isDepositFree(String code) {
        PaymentType type = fromCode(code);
        return type != null && type.isDepositFree();
    }
}
