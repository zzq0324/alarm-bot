package cn.zzq0324.alarm.bot.core.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

/**
 * description: GlobalExceptionHandler <br>
 * date: 2022/2/28 10:22 下午 <br>
 * author: zzq0324 <br>
 * version: 1.0 <br>
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    public static final Map<String,Object> DEFAULT_RESPONSE = new HashMap<String,Object>(){{
        put("code",HttpStatus.INTERNAL_SERVER_ERROR.value());
        put("message",HttpStatus.INTERNAL_SERVER_ERROR.toString());
    }};

    @ResponseBody
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Map handleException(Exception e){
        log.error("exception occur.",e);

        return DEFAULT_RESPONSE;
    }
}
