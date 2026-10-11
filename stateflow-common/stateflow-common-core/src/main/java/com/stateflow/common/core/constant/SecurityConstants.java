package com.stateflow.common.core.constant;

/**
 * 权限相关通用常量
 * 
 * @author stateflow
 */
public class SecurityConstants
{
    /**
     * 用户ID字段
     */
    public static final String DETAILS_USER_ID = "user_id";

    /**
     * 用户名字段
     */
    public static final String DETAILS_USERNAME = "username";

    /**
     * 授权信息字段
     */
    public static final String AUTHORIZATION_HEADER = "Authorization";

    /**
     * 请求来源
     */
    public static final String FROM_SOURCE = "from-source";

    /**
     * 内部请求
     */
    public static final String INNER = "inner";

    /**
     * 用户标识
     */
    public static final String USER_KEY = "user_key";

    /**
     * 登录用户
     */
    public static final String LOGIN_USER = "login_user";

    /**
     * 角色权限
     */
    public static final String ROLE_PERMISSION = "role_permission";

    /**
     * 请求标识
     */
    public static final String IDEMPOTENT_HEADER = "Idempotent";

    /**
     * API Key 请求头
     */
    public static final String API_KEY_HEADER = "X-Api-Key";

    /**
     * API Key ID 透传头
     */
    public static final String API_KEY_ID = "api_key_id";

    /**
     * 网关 API Key 鉴权通过标记
     */
    public static final String API_KEY_AUTH = "api_key_auth";
}
