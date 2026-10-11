package com.stateflow.system.api.model;

import java.io.Serializable;
import java.util.Set;
import com.stateflow.system.api.domain.SysUser;
import lombok.Data;

/**
 * 用户信息
 *
 * @author stateflow
 */
@Data
public class LoginUser implements Serializable
{
    private static final long serialVersionUID = 1L;

    /**
     * 用户唯一标识
     */
    private String token;

    /**
     * 用户名id
     */
    private Long userid;

    /**
     * 用户名
     */
    private String username;

    /**
     * 登录时间
     */
    private Long loginTime;

    /**
     * 过期时间
     */
    private Long expireTime;

    /**
     * 登录IP地址
     */
    private String ipaddr;

    /**
     * 权限列表
     */
    private Set<String> permissions;

    /**
     * 角色列表
     */
    private Set<String> roles;

    /**
     * 用户信息
     */
    private SysUser sysUser;

    /**
     * 刷新token
     */
    private String refreshToken;

    /**
     * 切换用户前最初登录账号的 JWT。有值表示当前处于切换用户状态。
     * 多次切换时始终指向最初登录用户，不会改成中间被切换的用户。
     */
    private String originAccessToken;

    /**
     * 最初登录账号的用户ID
     */
    private Long originUserId;

    /**
     * 最初登录账号的用户名
     */
    private String originUsername;

    /**
     * 本次切换前正在使用的用户ID。多次切换时更新为刚被切走的用户。
     */
    private Long previousUserId;

    /**
     * 本次切换前正在使用的用户名。多次切换时更新为刚被切走的用户。
     */
    private String previousUsername;
}
