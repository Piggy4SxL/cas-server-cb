package com.oc.auth.credential;

import org.apereo.cas.authentication.RememberMeUsernamePasswordCredential;

/**
 * 添加验证码
 *
 * @author SxL
 * @since 1.0.0
 * Created on 1/16/2019 4:22 PM.
 */
public class CusLoginUserInfoEntity extends RememberMeUsernamePasswordCredential {
    private static final long serialVersionUID = 1L;

    private String code;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public static long getSerialversionuid() {
        return serialVersionUID;
    }
}
