package com.oc.auth.exception;

import javax.security.auth.login.AccountExpiredException;

/**
 * 密码错误异常类
 *
 * @author SxL
 * @since 1.0.0
 * Created on 1/17/2019 1:33 PM.
 */
public class PasswordException extends AccountExpiredException {
    public PasswordException() {
    }

    public PasswordException(String msg) {
        super(msg);
    }
}
