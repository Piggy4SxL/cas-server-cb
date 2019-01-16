package com.oc.auth.config;

import com.oc.auth.handler.CustomMongoAuthenticationHandler;
import com.oc.entity.LdapInfo;
import org.apereo.cas.authentication.AuthenticationEventExecutionPlan;
import org.apereo.cas.authentication.AuthenticationEventExecutionPlanConfigurer;
import org.apereo.cas.authentication.AuthenticationHandler;
import org.apereo.cas.authentication.principal.DefaultPrincipalFactory;
import org.apereo.cas.services.ServicesManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;

/**
 * @author wyj
 * @date 2018/11/22
 */
public class CustomAuthenticationConfiguration implements AuthenticationEventExecutionPlanConfigurer {

    @Autowired
    @Qualifier("servicesManager")
    private ServicesManager servicesManager;

    @Value("${ldap.url}")
    private String url;

    @Value("${ldap.base}")
    private String baseDN;

    @Value("${ldap.username}")
    private String root;

    @Value("${ldap.password}")
    private String password;
    
    @Bean
    public AuthenticationHandler myAuthenticationHandler() {

        LdapInfo ldapInfo = new LdapInfo();
        ldapInfo.setUrl(url);
        ldapInfo.setBase(baseDN);
        ldapInfo.setUsername(root);
        ldapInfo.setPassword(password);

        return new CustomMongoAuthenticationHandler(CustomMongoAuthenticationHandler.class.getName(),
                servicesManager, new DefaultPrincipalFactory(), 1, ldapInfo);
    }

    @Override
    public void configureAuthenticationExecutionPlan(final AuthenticationEventExecutionPlan plan) {
        plan.registerAuthenticationHandler(myAuthenticationHandler());
    }
}
