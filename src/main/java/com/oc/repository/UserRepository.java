package com.oc.repository;

import cn.sxl.utils.ldap.LdapUtils;
import com.google.common.collect.Lists;
import com.oc.entity.User;

import javax.naming.NamingEnumeration;
import javax.naming.directory.Attributes;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;
import java.util.List;

/**
 * LDAP 操作
 *
 * @author SxL
 * @since 1.0.0
 * Created on 1/16/2019 1:45 PM.
 */
public class UserRepository {

    public UserRepository(LdapUtils ldap) {
        this.ldap = ldap;
    }

    private LdapUtils ldap;

    public User findByCn(String cn) {
        ldap.connect();
        User user = new User();
        List<String> passwordList = Lists.newArrayList();
        try {
            SearchControls constraints = new SearchControls();
            constraints.setSearchScope(SearchControls.SUBTREE_SCOPE);
            constraints.setReturningObjFlag(true);
            constraints.setReturningAttributes(null);

            NamingEnumeration<SearchResult> en = ldap.ctx.search("", "cn=" + cn, constraints);

            if (en == null || !en.hasMoreElements()) {
                System.out.println("未找到该用户");
            }
            // maybe more than one element
            while (en != null && en.hasMoreElements()) {
                Object obj = en.nextElement();
                if (obj != null) {
                    SearchResult si = (SearchResult) obj;
                    Attributes attributes = si.getAttributes();
                    user.setSt(attributes.get("st").get().toString());
                    user.setGivenName(attributes.get("givenName").get().toString());
                    NamingEnumeration<?> userPassword = attributes.get("userPassword").getAll();

                    while (userPassword != null && userPassword.hasMore()) {
                        String password = new String((byte[]) userPassword.next(), "utf-8");
                        passwordList.add(password);
                    }

                    user.setPassword(passwordList);
                } else {
                    System.out.println((Object) null);
                }
            }
        } catch (Exception e) {
            System.out.println("查找用户时产生异常。");
            e.printStackTrace();
            ldap.closeContext();
        }

        ldap.closeContext();
        return user;
    }
}
