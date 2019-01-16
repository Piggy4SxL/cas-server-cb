package com.oc.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author wyj
 * @date 2018/11/22
 */
@Data
public class User implements Serializable {
    private String cn;

    private String st;

    private String givenName;

    private List<String> password;
}
