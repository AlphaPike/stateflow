package com.stateflow.gateway.service;

import java.io.IOException;
import com.stateflow.common.core.exception.CaptchaException;
import com.stateflow.common.core.web.domain.AjaxResult;

/**
 * 验证码处理
 *
 * @author stateflow
 */
public interface ValidateCodeService
{
    /**
     * 生成验证码
     */
    public AjaxResult createCaptcha() throws IOException, CaptchaException;

    /**
     * 校验验证码
     */
    public void checkCaptcha(String key, String value) throws CaptchaException;
}
