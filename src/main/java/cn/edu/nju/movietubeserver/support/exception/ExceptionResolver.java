package cn.edu.nju.movietubeserver.support.exception;

import cn.edu.nju.movietubeserver.constant.ErrorMessage;
import cn.edu.nju.movietubeserver.support.response.RestApiResponse;
import cn.edu.nju.movietubeserver.support.response.RestApiResponseUtil;
import java.sql.SQLException;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

/**
 * @author dc
 * @date 2019/12/20 21:16
 */
@RestControllerAdvice
@Slf4j
public class ExceptionResolver
{

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ConstraintViolationException.class)
    public RestApiResponse<Void> validatorException(final ConstraintViolationException e)
    {
        log.error("验证实体异常 => {}", e.getMessage());
        log.error("堆栈信息 => ", e);
        final String message =
            e.getConstraintViolations().stream().map(ConstraintViolation::getMessage).collect(Collectors.joining(","));
        return RestApiResponseUtil.createErrorResponse(HttpStatus.BAD_REQUEST, message);
    }

    @ResponseStatus(HttpStatus.OK)
    @ExceptionHandler({ServiceException.class, ServletException.class})
    public RestApiResponse<Void> serviceException(final Throwable e)
    {
        log.error("服务异常 => {}", e.getMessage());
        log.error("堆栈信息 => ", e);
        return RestApiResponseUtil.createErrorResponse(HttpStatus.BAD_REQUEST, e.getMessage());
    }

    @ResponseStatus(HttpStatus.OK)
    @ExceptionHandler({DBException.class, SQLException.class, DataAccessException.class})
    public RestApiResponse<Void> databaseException(final Throwable e)
    {
        log.error("数据库操作异常 => {}", e.getMessage());
        log.error("堆栈信息 => ", e);
        return RestApiResponseUtil.createErrorResponse(HttpStatus.BAD_REQUEST,
            Optional.ofNullable(e.getMessage()).orElse(ErrorMessage.DATABASE_ERROR_MESSAGE));
    }

    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler({BadCredentialsException.class, AuthenticationException.class})
    public RestApiResponse<Void> authException(final Throwable e)
    {
        log.error("身份验证异常 => {}", e.getMessage());
        log.error("堆栈信息 => ", e);
        return RestApiResponseUtil.createErrorResponse(HttpStatus.UNAUTHORIZED,
            ErrorMessage.DEFAULT_UNAUTHORIZED_MESSAGE);
    }

    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ExceptionHandler({AccessDeniedException.class})
    public RestApiResponse<Void> userException(final Throwable e)
    {
        log.error("用户异常 => {}", e.getMessage());
        log.error("堆栈信息 => ", e);
        return RestApiResponseUtil.createErrorResponse(HttpStatus.BAD_REQUEST, e.getMessage());
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(NoHandlerFoundException.class)
    public RestApiResponse<Void> apiNotFound(final Throwable e, final HttpServletRequest request)
    {
        log.error("API 不存在 => {}", e.getMessage());
        log.error("堆栈信息 => ", e);
        return RestApiResponseUtil.createErrorResponse(HttpStatus.BAD_REQUEST,
            "API [" + request.getRequestURI() + "] not existed");
    }

    @ResponseStatus(HttpStatus.OK)
    @ExceptionHandler(UsernameNotFoundException.class)
    public RestApiResponse<Void> userNotFound(final Throwable e)
    {
        log.error("用户不存在 => {}", e.getMessage());
        log.error("堆栈信息 => ", e);
        return RestApiResponseUtil.createErrorResponse(HttpStatus.BAD_REQUEST, e.getMessage());
    }

    @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public RestApiResponse<Void> methodNotSupport(final Throwable e)
    {
        log.error("方法异常 => {}", e.getMessage());
        log.error("堆栈信息 => ", e);
        return RestApiResponseUtil.createErrorResponse(HttpStatus.METHOD_NOT_ALLOWED,
            ErrorMessage.DEFAULT_METHOD_NOT_ALLOWED_MESSAGE);
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    public RestApiResponse<Void> globalException(final HttpServletRequest request, final Throwable e)
    {
        log.error("全局异常 => {}", e.getMessage());
        log.error("堆栈信息 => ", e);
        return RestApiResponseUtil.createErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR,
            String.format("API [ %s ] internal server error => %s", request.getRequestURI(), e.getMessage()));
    }
}
