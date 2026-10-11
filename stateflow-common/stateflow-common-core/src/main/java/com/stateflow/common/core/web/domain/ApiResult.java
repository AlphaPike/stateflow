package com.stateflow.common.core.web.domain;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.stateflow.common.core.constant.HttpStatus;
import com.stateflow.common.core.utils.StringUtils;

/**
 * 统一 API 响应（泛型版本，便于 Knife4j / OpenAPI 解析）
 * <p>
 * JSON 结构与 {@link AjaxResult} 保持一致：{@code {code, msg, data, ...}}。
 * 额外字段通过 {@link #put(String, Object)} 写入，序列化时平铺到顶层，兼容旧用法。
 *
 * @param <T> data 字段类型
 * @author stateflow
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResult<T> implements Serializable
{
    private static final long serialVersionUID = 1L;

    /** 状态码 */
    public static final String CODE_TAG = AjaxResult.CODE_TAG;

    /** 返回内容 */
    public static final String MSG_TAG = AjaxResult.MSG_TAG;

    /** 数据对象 */
    public static final String DATA_TAG = AjaxResult.DATA_TAG;

    private int code;

    private String msg;

    private T data;

    @JsonIgnore
    private Map<String, Object> extra;

    public ApiResult()
    {
    }

    public ApiResult(int code, String msg)
    {
        this.code = code;
        this.msg = msg;
    }

    public ApiResult(int code, String msg, T data)
    {
        this.code = code;
        this.msg = msg;
        if (StringUtils.isNotNull(data))
        {
            this.data = data;
        }
    }

    public static <T> ApiResult<T> success()
    {
        return success("操作成功");
    }

    public static <T> ApiResult<T> success(T data)
    {
        return success("操作成功", data);
    }

    public static <T> ApiResult<T> success(String msg)
    {
        return success(msg, null);
    }

    public static <T> ApiResult<T> success(String msg, T data)
    {
        return new ApiResult<>(HttpStatus.SUCCESS, msg, data);
    }

    public static <T> ApiResult<T> warn(String msg)
    {
        return warn(msg, null);
    }

    public static <T> ApiResult<T> warn(String msg, T data)
    {
        return new ApiResult<>(HttpStatus.WARN, msg, data);
    }

    public static <T> ApiResult<T> error()
    {
        return error("操作失败");
    }

    public static <T> ApiResult<T> error(String msg)
    {
        return error(msg, null);
    }

    public static <T> ApiResult<T> error(String msg, T data)
    {
        return new ApiResult<>(HttpStatus.ERROR, msg, data);
    }

    public static <T> ApiResult<T> error(int code, String msg)
    {
        return new ApiResult<>(code, msg, null);
    }

    public int getCode()
    {
        return code;
    }

    public void setCode(int code)
    {
        this.code = code;
    }

    public String getMsg()
    {
        return msg;
    }

    public void setMsg(String msg)
    {
        this.msg = msg;
    }

    public T getData()
    {
        return data;
    }

    public void setData(T data)
    {
        this.data = data;
    }

    @JsonAnyGetter
    public Map<String, Object> getExtra()
    {
        return extra;
    }

    @JsonAnySetter
    public void setExtraProperty(String key, Object value)
    {
        put(key, value);
    }

    public boolean isSuccess()
    {
        return Objects.equals(HttpStatus.SUCCESS, this.code);
    }

    public boolean isWarn()
    {
        return Objects.equals(HttpStatus.WARN, this.code);
    }

    public boolean isError()
    {
        return Objects.equals(HttpStatus.ERROR, this.code);
    }

    /**
     * 链式写入额外字段（与 AjaxResult.put 行为一致，序列化到 JSON 顶层）
     */
    public ApiResult<T> put(String key, Object value)
    {
        if (extra == null)
        {
            extra = new LinkedHashMap<>();
        }
        extra.put(key, value);
        return this;
    }

    /**
     * 转为旧版 AjaxResult，便于与尚未迁移的代码衔接
     */
    public AjaxResult toAjaxResult()
    {
        AjaxResult ajax = new AjaxResult(code, msg, data);
        if (extra != null)
        {
            extra.forEach(ajax::put);
        }
        return ajax;
    }
}
