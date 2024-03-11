package com.alibaba.csp.sentinel.dashboard.rule.nacos.gateway;

import com.alibaba.csp.sentinel.dashboard.config.NacosConfigProperties;
import com.alibaba.csp.sentinel.dashboard.datasource.entity.gateway.GatewayFlowRuleEntity;
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
 * @date 2024/03/06 10:11
 */
@Component("gateWayFlowRulesNacosPunlisher")
public class GateWayFlowRulesNacosPunlisher implements DynamicRulePublisher<List<GatewayFlowRuleEntity>> {

    @Resource
    private ConfigService configService;
    @Resource
    private Converter<List<GatewayFlowRuleEntity>, String> converter;
    @Resource
    private NacosConfigProperties nacosConfigProperties;


    @Override
    public void publish(String app, List<GatewayFlowRuleEntity> rules) throws Exception {
        AssertUtil.notEmpty(app, "app name cannot be empty");
        if (rules == null) {
            return;
        }
        configService.publishConfig(app + NacosConfigUtil.GATEWAY_FLOW_DATA_ID_POSTFIX,
                nacosConfigProperties.getGroup(), converter.convert(rules));
    }
}
