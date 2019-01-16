package com.oc.auth.handler;

import cn.sxl.utils.encrypt.SshaUtils;
import cn.sxl.utils.ldap.LdapUtils;
import cn.sxl.utils.otp.OtpUtils;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.oc.auth.credential.CusLoginUserInfoEntity;
import com.oc.entity.LdapInfo;
import com.oc.entity.User;
import com.oc.repository.UserRepository;
import org.apereo.cas.authentication.Credential;
import org.apereo.cas.authentication.HandlerResult;
import org.apereo.cas.authentication.MessageDescriptor;
import org.apereo.cas.authentication.UsernamePasswordCredential;
import org.apereo.cas.authentication.handler.support.AbstractPreAndPostProcessingAuthenticationHandler;
import org.apereo.cas.authentication.principal.PrincipalFactory;
import org.apereo.cas.services.ServicesManager;

import javax.security.auth.login.AccountException;
import java.security.GeneralSecurityException;
import java.util.HashMap;
import java.util.List;

/**
 * 自定义验证
 *
 * @author SxL
 * @since 1.0.0
 * Created on 1/16/2019 4:35 PM.
 */
public class CustomMongoAuthenticationHandler extends AbstractPreAndPostProcessingAuthenticationHandler {

    private LdapInfo ldapInfo;

    public CustomMongoAuthenticationHandler(String name, ServicesManager servicesManager, PrincipalFactory principalFactory,
                                            Integer order, LdapInfo ldapInfo) {
        super(name, servicesManager, principalFactory, order);
        this.ldapInfo = ldapInfo;
    }

    @Override
    public boolean supports(Credential credential) {
        System.out.println("被调用了");
        return credential instanceof UsernamePasswordCredential;
    }

    /**
     * 验证码，自定义验证， 验证用户名密码是否正确
     */
    @Override
    protected HandlerResult doAuthentication(Credential credential) throws GeneralSecurityException {

        final CusLoginUserInfoEntity myCredential = (CusLoginUserInfoEntity) credential;
        //自定义其他校验
        LdapUtils ldap = new LdapUtils(ldapInfo.getUrl(), ldapInfo.getBase(), ldapInfo.getUsername(), ldapInfo.getPassword(), null, null);
        UserRepository userRepository = new UserRepository(ldap);

        String cn = myCredential.getUsername();
        String password = myCredential.getPassword();
        String code = myCredential.getCode();

        User user = userRepository.findByCn(cn);

        if (!verifyPassword(user.getPassword(), password)) {
            throw new AccountException("密码错误！");
        }

        if (!verifyCode(user.getSt(), code)) {
            throw new AccountException("动态验证码错误！");
        }

        HashMap<String, Object> principleAttributes = Maps.newHashMap();
        principleAttributes.put("cn", user.getCn());
        principleAttributes.put("givenName", user.getGivenName());

        final List<MessageDescriptor> list = Lists.newArrayList();

        return createHandlerResult(myCredential,
                this.principalFactory.createPrincipal(cn, principleAttributes), list);
    }

    private boolean verifyPassword(List<String> passwordList, String correctPassword) {
        boolean passBy = false;

        for (String password : passwordList) {
            if (password.contains("{SSHA}")) {
                passBy = SshaUtils.verifyPassword(password, correctPassword);
            } else {
                passBy = correctPassword.equals(password);
            }

            if (passBy) {
                break;
            }
        }

        return passBy;
    }

    private boolean verifyCode(String st, String code) {
        long t = System.currentTimeMillis();
        OtpUtils otpUtils = new OtpUtils();
        otpUtils.setWindowSize(5);

        return otpUtils.check_code(st, Long.parseLong(code), t);
    }
}