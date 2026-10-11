package com.stateflow.common.security.service;

import com.stateflow.common.core.constant.CacheConstants;
import com.stateflow.common.core.exception.ServiceException;
import com.stateflow.common.core.utils.uuid.UUID;
import com.stateflow.common.redis.service.RedisService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotBlank;
import java.util.HashMap;
import java.util.Map;

/**
 * 注册
 */
@Component
public class RegisterService {
    @Autowired
    private RedisService redisService;

    private static final Logger log = LoggerFactory.getLogger(RegisterService.class);

    public String preregisterUser(
            @NotBlank String openid,
            @NotBlank String unionid,
            @NotBlank String platform
    ) {
        return preregisterUser(openid, unionid, platform, null);
    }

    public String preregisterUser (
            @NotBlank String openid,
            @NotBlank String unionid,
            @NotBlank String platform,
            String refreshToken
    ) {
        String registerCode = UUID.randomUUID().toString(true);
        String registerCodeKey = getRegisterCodeKey(registerCode);
        Map<String, String> registerMap = new HashMap<String, String>() {{
            put(CacheConstants.OPENID, openid);
            put(CacheConstants.UNIONID, unionid);
            put(CacheConstants.PLATFORM, platform);
            put(CacheConstants.REFRESH_TOKEN, refreshToken);
        }};
        redisService.setCacheMap(registerCodeKey, registerMap);
        redisService.expire(registerCodeKey, CacheConstants.MINI_USER_REGIST_EXPIRE);
        return registerCode;
    }

    /**
     * 获取并删除用户注册凭证
     * @param registerCode 注册凭证
     * @return
     */
    public Map<String, String> getPreregisteredUser(@NotBlank String registerCode) {
        String registerCodeKey = getRegisterCodeKey(registerCode);
        Map<String, String> registerMap = redisService.getCacheMap(registerCodeKey);
        if (registerMap == null || registerMap.isEmpty()) {
            log.error("未获取到注册用户信息，或者注册用户信息已过期: {}", registerCode);
            throw new ServiceException("未获取到注册用户信息，或者注册用户信息已过期");
        }
        return registerMap;
    }

    private String getRegisterCodeKey(String registerCode) {
        return CacheConstants.WX_USER_REGIST_KEY + registerCode;
    }
}
