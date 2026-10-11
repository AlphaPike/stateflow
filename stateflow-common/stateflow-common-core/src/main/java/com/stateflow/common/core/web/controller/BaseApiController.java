package com.stateflow.common.core.web.controller;

import com.stateflow.common.core.web.domain.ApiResult;

/**
 * 带泛型 API 响应的 Controller 基类，便于 Knife4j 解析返回值。
 * <p>
 * 子类接口应声明 {@link ApiResult} 返回类型，并通过本类提供的 {@link #ok()}、{@link #ok(Object)}、
 * {@link #fail(String)}、{@link #toApiResult(int)} 等方法构造响应。
 * 父类 {@link BaseController} 的 {@code success/error/warn/toAjax} 仍保留（返回 {@link com.stateflow.common.core.web.domain.AjaxResult}），
 * 因 Java 泛型擦除无法以相同签名重写为 {@link ApiResult}。
 */
public class BaseApiController extends BaseController
{
    public ApiResult<Void> ok()
    {
        return ApiResult.success();
    }

    public ApiResult<Void> ok(String message)
    {
        return ApiResult.success(message);
    }

    public <T> ApiResult<T> ok(T data)
    {
        return ApiResult.success(data);
    }

    public ApiResult<Void> fail()
    {
        return ApiResult.error();
    }

    public ApiResult<Void> fail(String message)
    {
        return ApiResult.error(message);
    }

    public ApiResult<Void> warnMsg(String message)
    {
        return ApiResult.warn(message);
    }

    protected ApiResult<Void> toApiResult(int rows)
    {
        return rows > 0 ? ApiResult.success() : ApiResult.error();
    }

    protected ApiResult<Void> toApiResult(boolean result)
    {
        return result ? ApiResult.success() : ApiResult.error();
    }
}
