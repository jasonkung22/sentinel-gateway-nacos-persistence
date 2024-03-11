package com.alibaba.csp.sentinel.dashboard.rule.nacos.gateway;

import com.alibaba.csp.sentinel.dashboard.config.NacosConfigProperties;
import com.alibaba.csp.sentinel.dashboard.datasource.entity.gateway.ApiDefinitionEntity;
import com.alibaba.csp.sentinel.dashboard.rule.DynamicRulePublisher;
import com.alibaba.csp.sentinel.dashboard.rule.nacos.NacosConfigUtil;
import com.alibaba.csp.sentinel.datasource.Converter;
import com.alibaba.csp.sentinel.util.AssertUtil;
import com.alibaba.nacos.api.config.ConfigService;
import java.util.List;
import javax.annotation.Resource;
import org.springframework.stereotype.Component;

/**
 * @author gyf
 * @date 2024/03/06 10:12
 */
@Component("getWayApiNacosPublisher")
public class GetWayApiNacosPublisher implements DynamicRulePublisher<List<ApiDefinitionEntity>> {

    @Resource
    private ConfigService configService;
    @Resource
    private Converter<List<ApiDefinitionEntity>, String> converter;
    @Resource
    private NacosConfigProperties nacosConfigProperties;

    @Override
    public void publish(String app, List<ApiDefinitionEntity> rules) throws Exception {
        AssertUtil.notEmpty(app, "app name cannot be empty");
        if (rules == null) {
            return;
        }
        configService.publishConfig(app + NacosConfigUtil.GATEWAY_API_DATA_ID_POSTFIX,
                nacosConfigProperties.getGroup(), converter.convert(rules));
    }
}