package com.stateflow.common.core.exception.user;

import com.stateflow.common.core.exception.base.BaseException;

/**
 * 用户信息异常类
 * 
 * @author stateflow
 */
public class UserException extends BaseException
{
    private static final long serialVersionUID = 1L;

    public UserException(String code, Object[] args)
    {
        super("user", code, args, null);
    }
}
