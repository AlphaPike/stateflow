package com.stateflow.auth.form;

import lombok.Data;

/**
 * 用户登录对象
 * 
 * @author stateflow
 */
@Data
public class LoginBody
{
    /**
     * 用户名
     */
    private String username;

    /**
     * 用户密码
     */
    private String password;

    private String phone;
}
