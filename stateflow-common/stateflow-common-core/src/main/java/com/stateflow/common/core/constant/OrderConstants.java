package com.stateflow.common.core.constant;

public class OrderConstants {
    public static final String WEIXIN_PAYMENT = "WEIXIN_PAYMENT";



    /**
     * 押金现金支付
     */
    public static final String DEPOSIT_CASH_PAYMENT = "1";

    /**
     * 第三方免押（微信免押 / 支付宝免押）
     */
    public static final String DEPOSIT_FREE = "2";

    /**
     * 功能开关：开启
     */
    public static final String SWITCH_ON = "1";

    /**
     * 功能开关：关闭
     */
    public static final String SWITCH_OFF = "0";

    /**
     * 第三方免押：已创建，待用户确认
     */
    public static final String DEPOSIT_FREE_STATE_CREATED = "CREATED";

    /**
     * 第三方免押：进行中（用户已确认免押）
     */
    public static final String DEPOSIT_FREE_STATE_DOING = "DOING";

    /**
     * 第三方免押：已完成
     */
    public static final String DEPOSIT_FREE_STATE_DONE = "DONE";

    /**
     * 第三方免押：已取消
     */
    public static final String DEPOSIT_FREE_STATE_REVOKED = "REVOKED";

    /**
     * 第三方免押：已失效
     */
    public static final String DEPOSIT_FREE_STATE_EXPIRED = "EXPIRED";
    /**
     * 退款进行中
     */
    public static final String REFUND_PROCESSING = "REFUND_PROCESSING";
    /**
     * 退款失败
     */
    public static final String REFUND_RESULT_FAILED = "REFUND_RESULT_FAILED";
    public static final String REFUND_SUCCESS = "refund_success";

    /**
     * 类目上架
     */
    public static final String ITEM_ACTIVE = "0";

    /**
     * 类目下架
     */
    public static final String ITEM_INACTIVE = "1";

    /**
     * 测试订单
     */
    public static final String ORDER_STATE_TEST = "test";

    /**
     * 正式订单
     */
    public static final String ORDER_STATE_PROD = "prod";

    public static final String SERVICE_UNREFUNDED = "0";

    public static final String SERVICE_REFUNDED = "1";

    public static final String DEPOSIT_UNREFUNDED = "0";

    public static final String DEPOSIT_REFUNDED = "1";

    public static final String REFUND_LOCK_PREFIX = "refund_lock:";

    public static final long REFUND_LOCK_EXPIRE = 5 * 60L; // 5分钟
}
