package com.oc.auth.exception;

import javax.security.auth.login.AccountExpiredException;

/**
 * 验证码错误异常类
 *
 * @author SxL
 * @since 1.0.0
 * Created on 1/17/2019 1:34 PM.
 */
public class CodeException extends AccountExpiredException {
    public CodeException() {
    }

    public CodeException(String msg) {
        super(msg);
    }
}
