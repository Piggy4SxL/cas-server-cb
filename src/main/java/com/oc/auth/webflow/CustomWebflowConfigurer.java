package com.oc.auth.webflow;

import com.oc.auth.credential.CusLoginUserInfoEntity;
import org.apereo.cas.configuration.CasConfigurationProperties;
import org.apereo.cas.web.flow.CasWebflowConstants;
import org.apereo.cas.web.flow.configurer.AbstractCasWebflowConfigurer;
import org.springframework.context.ApplicationContext;
import org.springframework.webflow.definition.registry.FlowDefinitionRegistry;
import org.springframework.webflow.engine.Flow;
import org.springframework.webflow.engine.ViewState;
import org.springframework.webflow.engine.builder.BinderConfiguration;
import org.springframework.webflow.engine.builder.support.FlowBuilderServices;

/**
 * 把自定义的登录对象绑定到页面上
 *
 * @author SxL
 * @since 1.0.0
 * Created on 1/16/2019 4:26 PM.
 */
public class CustomWebflowConfigurer extends AbstractCasWebflowConfigurer {

    public CustomWebflowConfigurer(FlowBuilderServices flowBuilderServices, FlowDefinitionRegistry loginFlowDefinitionRegistry, ApplicationContext applicationContext, CasConfigurationProperties casProperties) {
        super(flowBuilderServices, loginFlowDefinitionRegistry, applicationContext, casProperties);
    }

    @Override
    protected void doInitialize() {
        final Flow loginFlow = getLoginFlow();
        // 重写绑定自定义credential
        createFlowVariable(loginFlow, CasWebflowConstants.VAR_ID_CREDENTIAL, CusLoginUserInfoEntity.class);
        // 获取登录页
        final ViewState state = (ViewState) loginFlow.getState(CasWebflowConstants.STATE_ID_VIEW_LOGIN_FORM);
        // 获取参数绑定对象
        final BinderConfiguration cfg = getViewStateBinderConfiguration(state);
        // 由于用户名以及密码已经绑定，所以只需对新加系统参数绑定即可
        // 参数1 ：字段名
        // 参数2 ：转换器
        // 参数3 ：是否必须的字段
        cfg.addBinding(new BinderConfiguration.Binding("code", null, true));
    }
}
