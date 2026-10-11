package com.stateflow.common.core.constant;

/**
 * 微信响应代码常量
 *
 */
public class WxRespCodeConstants {
    // =================== 成功 ====================
    public static final int OK = 0;

    // =================== 用户登录 ====================
    /**
     * code 无效，js_code无效
     */
    public static final int INVALID_CODE = 40029;

    /**
     * 高风险等级用户，小程序登录拦截
     */
    public static final int HIGH_RISK_USER_BLOCKED = 40226;

    /**
     * API 调用太频繁，请稍候再试
     */
    public static final int API_FREQUENCY_LIMIT = 45011;

    /**
     * 系统繁忙，此时请开发者稍候再试
     */
    public static final int SYSTEM_ERROR = -1;

    // ================== 检查登录态 ====================
    /**
     * 无效的签名
     */
    public static final int INVALID_SIGNATURE = 87009;

    // ================== 重置登录态 ====================
    /**
     * 无效的签名方法
     */
    public static final int INVALID_SIG_METHOD = 87008;
}
