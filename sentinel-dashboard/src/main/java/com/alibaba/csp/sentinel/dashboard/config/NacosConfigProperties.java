package com.alibaba.csp.sentinel.dashboard.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * nacos配置
 * @author gyf
 * @date 2024/03/11 16:14
 */
@Component
@ConfigurationProperties(prefix = "sentinel.nacos.config")
public class NacosConfigProperties {

    /**
     * nacos config server address.
     */
    private String serverAddress;

    /**
     * namespace, separation configuration of different environments.
     */
    private String namespace;

    /**
     * the nacos authentication username.
     */
    private String username;

    /**
     * the nacos authentication password.
     */
    private String password;

    /**
     * nacos config group, group is config data meta info.
     */
    private String group = "sentinel_group";

    public String getServerAddress() {
        return serverAddress;
    }

    public void setServerAddress(String serverAddress) {
        this.serverAddress = serverAddress;
    }

    public String getNamespace() {
        return namespace;
    }

    public void setNamespace(String namespace) {
        this.namespace = namespace;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }
}
