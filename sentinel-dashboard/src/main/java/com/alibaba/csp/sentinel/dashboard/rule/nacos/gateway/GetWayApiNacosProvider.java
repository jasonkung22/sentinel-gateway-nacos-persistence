package com.alibaba.csp.sentinel.dashboard.rule.nacos.gateway;

import com.alibaba.csp.sentinel.dashboard.config.NacosConfigProperties;
import com.alibaba.csp.sentinel.dashboard.datasource.entity.gateway.ApiDefinitionEntity;
import com.alibaba.csp.sentinel.dashboard.rule.DynamicRuleProvider;
import com.alibaba.csp.sentinel.dashboard.rule.nacos.NacosConfigUtil;
import com.alibaba.csp.sentinel.datasource.Converter;
import com.alibaba.csp.sentinel.util.StringUtil;
import com.alibaba.nacos.api.config.ConfigService;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Resource;
import org.springframework.stereotype.Component;

/**
 * @author gyf
 * @date 2024/03/06 10:12
 */
@Component("getWayApiNacosProvider")
public class GetWayApiNacosProvider implements DynamicRuleProvider<List<ApiDefinitionEntity>> {
    @Resource
    private ConfigService configService;
    @Resource
    private Converter<String , List<ApiDefinitionEntity>> converter;
    @Resource
    private NacosConfigProperties nacosConfigProperties;
    @Override
    public List<ApiDefinitionEntity> getRules(String appName) throws Exception {
        String rules = configService.getConfig(appName+ NacosConfigUtil.GATEWAY_API_DATA_ID_POSTFIX
                ,nacosConfigProperties.getGroup(),3000);
        if(StringUtil.isEmpty(rules)){
            return new ArrayList<>();
        }
        return converter.convert(rules);
    }
}
