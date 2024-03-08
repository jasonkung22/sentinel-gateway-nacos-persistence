/*
 * Copyright 1999-2018 Alibaba Group Holding Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.alibaba.csp.sentinel.dashboard.discovery;

import com.alibaba.csp.sentinel.dashboard.datasource.entity.gateway.ApiDefinitionEntity;
import com.alibaba.csp.sentinel.dashboard.datasource.entity.gateway.GatewayFlowRuleEntity;
import com.alibaba.csp.sentinel.dashboard.repository.gateway.InMemApiDefinitionStore;
import com.alibaba.csp.sentinel.dashboard.repository.gateway.InMemGatewayFlowRuleStore;
import com.alibaba.csp.sentinel.dashboard.rule.DynamicRuleProvider;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import javax.annotation.PostConstruct;

import com.alibaba.csp.sentinel.util.StringUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

@Component
public class AppManagement implements MachineDiscovery {

    private final Logger logger = LoggerFactory.getLogger(AppManagement.class);

    @Autowired
    private ApplicationContext context;

    @Autowired
    @Qualifier("gateWayFlowRulesNacosProvider")
    private DynamicRuleProvider<List<GatewayFlowRuleEntity>> flowRuleProvider;

    @Autowired
    private InMemGatewayFlowRuleStore flowRuleRepository;


    @Autowired
    @Qualifier("getWayApiNacosProvider")
    private DynamicRuleProvider<List<ApiDefinitionEntity>> apiProvider;

    @Autowired
    private InMemApiDefinitionStore apiRepository;

    private MachineDiscovery machineDiscovery;

    @PostConstruct
    public void init() {
        machineDiscovery = context.getBean(SimpleMachineDiscovery.class);
    }

    @Override
    public Set<AppInfo> getBriefApps() {
        return machineDiscovery.getBriefApps();
    }

    @Override
    public long addMachine(MachineInfo machineInfo) {
        // 是网关并且是第一次注册
        String app = machineInfo.getApp();
        if (isGateway(machineInfo.getAppType()) && getDetailApp(app) == null) {
            pullGatewayNacosApi(app);
            pullGatewayNacosFlowRules(app);
        }
        return machineDiscovery.addMachine(machineInfo);
    }

    @Override
    public boolean removeMachine(String app, String ip, int port) {
        return machineDiscovery.removeMachine(app, ip, port);
    }

    @Override
    public List<String> getAppNames() {
        return machineDiscovery.getAppNames();
    }

    @Override
    public AppInfo getDetailApp(String app) {
        return machineDiscovery.getDetailApp(app);
    }

    @Override
    public void removeApp(String app) {
        machineDiscovery.removeApp(app);
    }

    public boolean isValidMachineOfApp(String app, String ip) {
        if (StringUtil.isEmpty(app)) {
            return false;
        }
        return Optional.ofNullable(getDetailApp(app))
                .flatMap(a -> a.getMachine(ip))
                .isPresent();
    }

    private boolean isGateway(Integer appType) {
        return appType == 1;
    }

    public void pullGatewayNacosFlowRules(String app) {
        try {
            List<GatewayFlowRuleEntity> rules = flowRuleProvider.getRules(app);
            flowRuleRepository.saveAll(rules);
        } catch (Exception e) {
            logger.error("pull nacos flow rules error, app:{}", app, e);
        }
    }

    public void pullGatewayNacosApi(String app) {
        try {
            List<ApiDefinitionEntity> api = apiProvider.getRules(app);
            apiRepository.saveAll(api);
        } catch (Exception e) {
            logger.error("pull nacos api error, app:{}", app, e);
        }
    }

}
