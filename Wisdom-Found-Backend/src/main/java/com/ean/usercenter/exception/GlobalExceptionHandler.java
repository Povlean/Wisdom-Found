package com.ean.usercenter.exception;

import com.ean.usercenter.common.BaseResponse;
import com.ean.usercenter.common.ErrorCode;
import com.ean.usercenter.common.ResultUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @description:TODO
 * @author:Povlean
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BusinessException.class)
    public BaseResponse businessExceptionHandler(BusinessException e) {
        log.error("businessException:" + e.getMessage(), e);
        return ResultUtils.error(e.getCode(), e.getMessage(), e.getDescription());
    }

    @ExceptionHandler(RuntimeException.class)
    public BaseResponse runtimeExceptionHandler(RuntimeException e) {
        log.error("runtimeException:" + e.getMessage() ,e);
        return ResultUtils.error(ErrorCode.SYSTEM_ERROR, e.getMessage(), "后端代码异常");
    }

}
