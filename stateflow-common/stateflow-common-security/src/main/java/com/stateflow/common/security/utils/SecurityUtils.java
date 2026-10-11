package com.stateflow.common.security.utils;

import javax.servlet.http.HttpServletRequest;

import com.stateflow.common.core.constant.RoleKeyConstant;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import com.stateflow.common.core.constant.SecurityConstants;
import com.stateflow.common.core.constant.TokenConstants;
import com.stateflow.common.core.context.SecurityContextHolder;
import com.stateflow.common.core.utils.ServletUtils;
import com.stateflow.common.core.utils.StringUtils;
import com.stateflow.system.api.model.LoginUser;

import java.util.Set;

/**
 * 权限获取工具类
 * 
 * @author stateflow
 */
public class SecurityUtils
{
    /**
     * 获取用户ID
     */
    public static Long getUserId()
    {
        return SecurityContextHolder.getUserId();
    }

    /**
     * 获取用户名称
     */
    public static String getUsername()
    {
        return SecurityContextHolder.getUserName();
    }

    /**
     * 获取用户key
     */
    public static String getUserKey()
    {
        return SecurityContextHolder.getUserKey();
    }

    /**
     * 获取登录用户信息
     */
    public static LoginUser getLoginUser()
    {
        return SecurityContextHolder.get(SecurityConstants.LOGIN_USER, LoginUser.class);
    }

    /**
     * 获取请求token
     */
    public static String getToken()
    {
        return getToken(ServletUtils.getRequest());
    }

    /**
     * 根据request获取请求token
     */
    public static String getToken(HttpServletRequest request)
    {
        // 从header获取token标识
        String token = request.getHeader(SecurityConstants.AUTHORIZATION_HEADER);
        return replaceTokenPrefix(token);
    }

    /**
     * 裁剪token前缀
     */
    public static String replaceTokenPrefix(String token)
    {
        // 如果前端设置了令牌前缀，则裁剪掉前缀
        if (StringUtils.isNotEmpty(token) && token.startsWith(TokenConstants.PREFIX))
        {
            token = token.replaceFirst(TokenConstants.PREFIX, "");
        }
        return token;
    }

    /**
     * 是否为管理员
     * 
     * @return 结果
     */
    public static boolean isAdmin()
    {
        Set<String> roles = getLoginUser().getRoles();
        return roles.contains(RoleKeyConstant.SUPER_ADMIN);
    }

    public static boolean hasRole (String role) {
        if (StringUtils.isBlank(role)) {
            return false;
        }
        Set<String> roles = getLoginUser().getRoles();
        return roles.contains(role);
    }

    /**
     * 生成BCryptPasswordEncoder密码
     *
     * @param password 密码
     * @return 加密字符串
     */
    public static String encryptPassword(String password)
    {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        return passwordEncoder.encode(password);
    }

    /**
     * 判断密码是否相同
     *
     * @param rawPassword 真实密码
     * @param encodedPassword 加密后字符
     * @return 结果
     */
    public static boolean matchesPassword(String rawPassword, String encodedPassword)
    {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }
}
