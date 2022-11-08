package com.dev.spring.http.handler;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
@ControllerAdvice(basePackages = "com.dev.spring.rest")
public class RestControllerExceptionHandler extends ResponseEntityExceptionHandler {

}
