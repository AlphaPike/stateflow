package com.stateflow.common.core.constant;

/**
 * 缓存常量信息
 * 
 * @author stateflow
 */
public class CacheConstants
{
    /**
     * 缓存有效期，默认720（分钟）
     */
    public final static long EXPIRATION = 720;

    /**
     * 缓存刷新时间，默认120（分钟）
     */
    public final static long REFRESH_TIME = 120;

    /**
     * 密码最大错误次数
     */
    public final static int PASSWORD_MAX_RETRY_COUNT = 5;

    /**
     * 密码锁定时间，默认10（分钟）
     */
    public final static long PASSWORD_LOCK_TIME = 10;

    /**
     * 权限缓存前缀
     */
    public final static String LOGIN_TOKEN_KEY = "login_tokens:";

    /**
     * 验证码 redis key
     */
    public static final String CAPTCHA_CODE_KEY = "captcha_codes:";

    /**
     * 参数管理 cache key
     */
    public static final String SYS_CONFIG_KEY = "sys_config:";

    /**
     * 字典管理 cache key
     */
    public static final String SYS_DICT_KEY = "sys_dict:";

    /**
     * 登录账户密码错误次数 redis key
     */
    public static final String PWD_ERR_CNT_KEY = "pwd_err_cnt:";

    /**
     * 登录IP黑名单 cache key
     */
    public static final String SYS_LOGIN_BLACKIPLIST = SYS_CONFIG_KEY + "sys.login.blackIPList";

    /**
     * 微信用户注册 redis key
     */
    public static final String WX_USER_REGIST_KEY = "user_regist_params:";

    public static final String UNIONID = "unionid";

    public static final String OPENID = "openid";

    public static final String PLATFORM = "platform";
    public static final String WX_ACCESS_TOKEN = "access_token:wxmini";
    public static final String LARK_TENANT_ACCESS_TOKEN = "access_token:lark_tenant";
    public static final long MINI_USER_REGIST_EXPIRE = 5 * 60;
    
    /**
     * 微信支付证书序列号 redis key
     */
    public static final String WX_CERT_SERIAL_NO = "wx_cert_serial_no";
    
    /**
     * 微信支付商户私钥 redis key
     */
    public static final String WX_MERCHANT_PRIVATE_KEY = "wx_merchant_private_key";

    /**
     * 微信支付商户公钥 redis key
     */
    public static final String WX_MERCHANT_PUBLIC_KEY = "wx_merchant_public_key";
    
    /**
     * 证书和私钥缓存过期时间（30天）
     */
    public static final long WX_CERT_EXPIRE = 30 * 24 * 60 * 60;

    public static final String PROJECT_PUBLIC_CACHE = "cache:public:project:";

    public static final String PROJECT_SKU_PUBLIC_CACHE = "cache:project:sku:";
    public static final String OUT_USER_ID = "out_user_id";
    public static final String REFRESH_TOKEN = "refresh_token";

    /**
     * API Key 缓存前缀
     */
    public static final String API_KEY_CACHE_KEY = "api_keys:";

    /**
     * API Key 缓存有效期（分钟）
     */
    public static final long API_KEY_CACHE_EXPIRE_MINUTES = 60;
}
