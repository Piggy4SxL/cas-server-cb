package com.oc.entity;

import lombok.Data;

/**
 * LDAP 连接信息
 *
 * @author SxL
 * @since 1.0.0
 * Created on 1/16/2019 5:35 PM.
 */
@Data
public class LdapInfo {

    private String url;

    private String base;

    private String username;

    private String password;
}
