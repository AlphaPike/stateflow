package com.stateflow.system.api.factory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;
import com.stateflow.common.core.domain.R;
import com.stateflow.system.api.RemoteUserService;
import com.stateflow.system.api.domain.SysUser;
import com.stateflow.system.api.model.LoginUser;

@Component
public class RemoteUserFallbackFactory implements FallbackFactory<RemoteUserService>
{
    private static final Logger log = LoggerFactory.getLogger(RemoteUserFallbackFactory.class);

    @Override
    public RemoteUserService create(Throwable throwable)
    {
        log.error("user service call failed: {}", throwable.getMessage());
        return new RemoteUserService()
        {
            @Override
            public R<LoginUser> getUserInfoPhone(String phone, String source)
            {
                return R.fail("failed to load user: " + throwable.getMessage());
            }

            @Override
            public R<Boolean> recordUserLogin(SysUser sysUser, String source)
            {
                return R.fail("failed to record login: " + throwable.getMessage());
            }
        };
    }
}
