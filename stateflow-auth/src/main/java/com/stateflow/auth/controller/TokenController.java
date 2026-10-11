package com.stateflow.auth.controller;

import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import com.stateflow.auth.form.LoginBody;
import com.stateflow.auth.service.SysLoginService;
import com.stateflow.common.core.domain.R;
import com.stateflow.common.core.utils.JwtUtils;
import com.stateflow.common.core.utils.StringUtils;
import com.stateflow.common.security.auth.AuthUtil;
import com.stateflow.common.security.service.TokenService;
import com.stateflow.common.security.utils.SecurityUtils;
import com.stateflow.system.api.model.LoginUser;

/**
 * Password login and JWT refresh. Third-party identity providers are intentionally absent.
 */
@RestController
public class TokenController
{
    @Autowired
    private TokenService tokenService;

    @Autowired
    private SysLoginService sysLoginService;

    @PostMapping("login")
    public R<?> login(@RequestBody LoginBody form)
    {
        LoginUser userInfo = sysLoginService.login(form.getPhone(), form.getPassword());
        return R.ok(tokenService.createToken(userInfo));
    }

    @DeleteMapping("logout")
    public R<?> logout(HttpServletRequest request)
    {
        String token = SecurityUtils.getToken(request);
        if (StringUtils.isNotEmpty(token))
        {
            String username = JwtUtils.getUserName(token);
            AuthUtil.logoutByToken(token);
            sysLoginService.logout(username);
        }
        return R.ok();
    }

    @PostMapping("refresh")
    public R<?> refresh(HttpServletRequest request)
    {
        LoginUser loginUser = tokenService.getLoginUser(request);
        if (loginUser != null)
        {
            tokenService.refreshToken(loginUser);
        }
        return R.ok();
    }
}
