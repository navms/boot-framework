package io.navms.framework.web.exception;

import io.navms.framework.common.base.domain.Result;
import io.navms.framework.common.base.enums.BaseCode;
import io.navms.framework.common.base.exception.BaseException;
import io.navms.framework.common.base.exception.BusinessException;
import io.navms.framework.common.base.log.LogUtils;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.util.stream.Collectors;

/**
 * 全局异常拦截器
 *
 * @author navms
 */
@Order(100)
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BusinessException.class)
    public Result<Void> handleBusinessException(BusinessException e) {
        LogUtils.error("业务异常：{}", e, e.getMessage());
        return Result.fail(e.getCode(), e.getMessage());
    }

    @ExceptionHandler(BaseException.class)
    public Result<Void> handleBaseException(BaseException e) {
        LogUtils.error("基础异常：{}", e, e.getMessage());
        return Result.fail(e.getCode(), e.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Result<Void> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        LogUtils.error("参数校验异常：{}", e, e.getMessage());
        String message = e.getBindingResult().getFieldErrors().stream()
                .map(FieldError::getDefaultMessage)
                .collect(Collectors.joining(", "));
        return Result.fail(BaseCode.VALIDATE_FAILED.getCode(), message);
    }

    @ExceptionHandler(BindException.class)
    public Result<Void> handleBindException(BindException e) {
        LogUtils.error("参数绑定异常：{}", e, e.getMessage());
        String message = e.getFieldErrors().stream()
                .map(FieldError::getDefaultMessage)
                .collect(Collectors.joining(", "));
        return Result.fail(BaseCode.VALIDATE_FAILED.getCode(), message);
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public Result<Void> handleHttpRequestMethodNotSupported(HttpRequestMethodNotSupportedException e) {
        LogUtils.error("请求方法不支持：{}", e, e.getMessage());
        return Result.fail(BaseCode.METHOD_NOT_ALLOWED);
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Result<Void> handleNoHandlerFoundException(NoHandlerFoundException e) {
        LogUtils.error("404异常：{}", e, e.getMessage());
        return Result.fail(BaseCode.NOT_FOUND);
    }

    @ExceptionHandler(RuntimeException.class)
    public Result<Void> handleRuntimeException(RuntimeException e) {
        LogUtils.error("运行时异常：{}", e, e.getMessage());
        return Result.fail(BaseCode.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(Exception.class)
    public Result<Void> handleException(Exception e) {
        LogUtils.error("系统异常：{}", e, e.getMessage());
        return Result.fail(BaseCode.INTERNAL_SERVER_ERROR);
    }

}
