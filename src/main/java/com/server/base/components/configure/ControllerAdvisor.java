package com.server.base.components.configure;

import com.server.base.components.exceptions.CommonException;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.SignatureException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

@Slf4j
@RestControllerAdvice
public class ControllerAdvisor{

    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(value = {CommonException.class})
    public ResponseEntity internalServerExceptionHandler(CommonException exception){
        exception.printStackTrace();
        return new ResponseEntity(exception.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    @ExceptionHandler(value = {NoHandlerFoundException.class})
    public ResponseEntity noHandlerExceptionHandler() {
        return new ResponseEntity("잘못된 요청입니다.", HttpStatus.NOT_FOUND);
    }

    @ResponseStatus(value = HttpStatus.FORBIDDEN)
    @ExceptionHandler(value = {MalformedJwtException.class, UnsupportedJwtException.class, SignatureException.class , IllegalArgumentException.class})
    public ResponseEntity illegalTokenExceptionHandler() {
        return new ResponseEntity("올바르지 않은 토큰입니다.", HttpStatus.FORBIDDEN);
    }

    @ResponseStatus(value = HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(value = {ExpiredJwtException.class})
    public ResponseEntity invalidTokenExceptionHandler() {
        return new ResponseEntity("접근 권한이 없습니다.", HttpStatus.UNAUTHORIZED);
    }


}
