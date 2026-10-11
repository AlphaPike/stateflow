package com.stateflow.file.config;

import com.stateflow.common.core.domain.R;
import com.stateflow.common.core.exception.InnerAuthException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 文件服务异常处理
 */
@RestControllerAdvice
public class FileExceptionHandler {

    @ExceptionHandler(InnerAuthException.class)
    public R<Void> handleInnerAuthException(InnerAuthException e) {
        return R.fail(e.getMessage());
    }
}
