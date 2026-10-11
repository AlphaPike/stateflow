package com.stateflow.auth.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.stateflow.common.core.constant.CacheConstants;
import com.stateflow.common.core.constant.Constants;
import com.stateflow.common.core.constant.SecurityConstants;
import com.stateflow.common.core.constant.UserConstants;
import com.stateflow.common.core.domain.R;
import com.stateflow.common.core.enums.UserStatus;
import com.stateflow.common.core.exception.ServiceException;
import com.stateflow.common.core.text.Convert;
import com.stateflow.common.core.utils.DateUtils;
import com.stateflow.common.core.utils.StringUtils;
import com.stateflow.common.core.utils.ip.IpUtils;
import com.stateflow.common.redis.service.RedisService;
import com.stateflow.common.security.utils.SecurityUtils;
import com.stateflow.system.api.RemoteUserService;
import com.stateflow.system.api.domain.SysUser;
import com.stateflow.system.api.model.LoginUser;

/**
 * Password login with retry limiting and an IP block list stored in Redis.
 */
@Component
public class SysLoginService
{
    @Autowired
    private RemoteUserService remoteUserService;

    @Autowired
    private SysPasswordService passwordService;

    @Autowired
    private SysRecordLogService recordLogService;

    @Autowired
    private RedisService redisService;

    public LoginUser login(String phone, String password)
    {
        if (StringUtils.isAnyBlank(phone, password))
        {
            recordLogService.recordLogininfor(phone, Constants.LOGIN_FAIL, "phone and password are required");
            throw new ServiceException("phone and password are required");
        }
        if (password.length() < UserConstants.PASSWORD_MIN_LENGTH
                || password.length() > UserConstants.PASSWORD_MAX_LENGTH)
        {
            recordLogService.recordLogininfor(phone, Constants.LOGIN_FAIL, "password length is invalid");
            throw new ServiceException("password length is invalid");
        }
        if (phone.length() != 11)
        {
            recordLogService.recordLogininfor(phone, Constants.LOGIN_FAIL, "phone length is invalid");
            throw new ServiceException("phone length is invalid");
        }
        String blackStr = Convert.toStr(redisService.getCacheObject(CacheConstants.SYS_LOGIN_BLACKIPLIST));
        if (IpUtils.isMatchedIp(blackStr, IpUtils.getIpAddr()))
        {
            recordLogService.recordLogininfor(phone, Constants.LOGIN_FAIL, "ip is blocked");
            throw new ServiceException("ip is blocked");
        }
        R<LoginUser> userResult = remoteUserService.getUserInfoPhone(phone, SecurityConstants.INNER);
        LoginUser userInfo = checkUserRespStatus(userResult);
        SysUser user = userResult.getData().getSysUser();
        passwordService.validate(user, password);
        recordLoginInfo(userInfo);
        return userInfo;
    }

    public void logout(String loginName)
    {
        recordLogService.recordLogininfor(loginName, Constants.LOGOUT, "logout");
    }

    private void recordLoginInfo(LoginUser loginUser)
    {
        SysUser user = loginUser.getSysUser();
        recordLogService.recordLogininfor(loginUser.getUsername(), Constants.LOGIN_SUCCESS, "login success");
        SysUser patch = new SysUser();
        patch.setUserId(user.getUserId());
        patch.setLoginIp(IpUtils.getIpAddr());
        patch.setLoginDate(DateUtils.getNowDate());
        remoteUserService.recordUserLogin(patch, SecurityConstants.INNER);
    }

    private LoginUser checkUserRespStatus(R<LoginUser> userResult)
    {
        if (userResult == null || R.FAIL == userResult.getCode())
        {
            throw new ServiceException(userResult == null ? "user service unavailable" : userResult.getMsg());
        }
        LoginUser userInfo = userResult.getData();
        SysUser user = userInfo.getSysUser();
        String username = userInfo.getUsername();
        if (UserStatus.DELETED.getCode().equals(user.getDelFlag()))
        {
            recordLogService.recordLogininfor(username, Constants.LOGIN_FAIL, "account deleted");
            throw new ServiceException("account deleted: " + username);
        }
        if (UserStatus.DISABLE.getCode().equals(user.getStatus()))
        {
            recordLogService.recordLogininfor(username, Constants.LOGIN_FAIL, "account disabled");
            throw new ServiceException("account disabled: " + username);
        }
        return userInfo;
    }
}
