package com.stateflow.system.api;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import com.stateflow.common.core.constant.SecurityConstants;
import com.stateflow.common.core.constant.ServiceNameConstants;
import com.stateflow.common.core.domain.R;
import com.stateflow.system.api.domain.SysUser;
import com.stateflow.system.api.factory.RemoteUserFallbackFactory;
import com.stateflow.system.api.model.LoginUser;

/**
 * User lookup used by the auth service.
 */
@FeignClient(contextId = "remoteUserService", value = ServiceNameConstants.SYSTEM_SERVICE, fallbackFactory = RemoteUserFallbackFactory.class)
public interface RemoteUserService
{
    @GetMapping("/user/info-phone/{phone}")
    R<LoginUser> getUserInfoPhone(@PathVariable("phone") String phone, @RequestHeader(SecurityConstants.FROM_SOURCE) String source);

    @PutMapping("/user/recordlogin")
    R<Boolean> recordUserLogin(@RequestBody SysUser sysUser, @RequestHeader(SecurityConstants.FROM_SOURCE) String source);
}
