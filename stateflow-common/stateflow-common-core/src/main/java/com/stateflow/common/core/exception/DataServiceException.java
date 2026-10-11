package com.stateflow.common.core.exception;

/**
 * 数据库数据异常
 * 
 * @author stateflow
 */
public final class DataServiceException extends RuntimeException
{
    private static final long serialVersionUID = 1L;

    /**
     * 错误码
     */
    private Integer code;

    /**
     * 错误提示
     */
    private String message;

    /**
     * 错误明细，内部调试错误
     *
     */
    private String detailMessage;

    /**
     * 空构造方法，避免反序列化问题
     */
    public DataServiceException()
    {
    }

    public DataServiceException(String message)
    {
        this.message = message;
    }

    public DataServiceException(String message, Integer code)
    {
        this.message = message;
        this.code = code;
    }

    public String getDetailMessage()
    {
        return detailMessage;
    }

    @Override
    public String getMessage()
    {
        return message;
    }

    public Integer getCode()
    {
        return code;
    }

    public DataServiceException setMessage(String message)
    {
        this.message = message;
        return this;
    }

    public DataServiceException setDetailMessage(String detailMessage)
    {
        this.detailMessage = detailMessage;
        return this;
    }
}