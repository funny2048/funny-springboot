package com.funny.framework.web.handler;

import com.funny.framework.core.enums.BasicErrorCode;
import com.funny.framework.core.result.CommonResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * 全局异常处理类
 * @author funny2048
 */
@ControllerAdvice
public class GlobalExceptionAdvice {
    private static final Logger LOGGER = LoggerFactory.getLogger(GlobalExceptionAdvice.class);

    private static final CommonResult ERROR = CommonResult.buildFailure(BasicErrorCode.SERVER_ERROR.getCode(),
            BasicErrorCode.SERVER_ERROR.getMessage());

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public CommonResult globalException(Exception ex) {
        LOGGER.error("GlobalControllerAdvice中捕获全局异常: {}", ex.getMessage(), ex);
        return ERROR;
    }
}