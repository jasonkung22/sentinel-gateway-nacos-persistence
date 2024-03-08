# dashboard源码修改记录：

1、拷贝test模块中的代码，去掉nacos-datasource依赖<scope/>

2、将网关规则id生成逻辑从自增改为雪花算法；并引入hutool工具类

3、修复id为Long类型转到前端精度丢失的问题

4、修改网关规则加载到内存逻辑，第一次app注册才将nacos配置加载内存中

# 客户端配置：

### 添加配置

application.yml

```yaml
spring:
  cloud:
    sentinel:
      enable: true
      filter:
        enabled: false
      eager: true #立即加载
      transport:
        dashboard: localhost:9999
      datasource:
        gw-flow:
          nacos:
            group-id: sentinel_group
            namespace: sentinel
            data-id: ${spring.application.name}-sentinel-gateway-flow-rules # 在修改的sentinel 源码中定义的规则名
            server-addr: 192.168.0.7:8848
            username: admin
            password: 111111
            data-type: json
            rule-type: gw-flow
        gw-api-group:
          nacos:
            group-id: sentinel_group
            namespace: sentinel
            data-id: ${spring.application.name}-sentinel-gateway-api-rules # 在修改的sentinel 源码中定义的规则名
            server-addr: 192.168.0.7:8848
            username: admin
            password: 111111
            data-type: json
            rule-type: gw-api-group
```

### 引入依赖

pom.xml

```xml
		<dependency>
            <groupId>com.alibaba.cloud</groupId>
            <artifactId>spring-cloud-alibaba-sentinel-gateway</artifactId>
        </dependency>

        <dependency>
            <groupId>com.alibaba.cloud</groupId>
            <artifactId>spring-cloud-starter-alibaba-sentinel</artifactId>
        </dependency>

        <dependency>
            <groupId>com.alibaba.csp</groupId>
            <artifactId>sentinel-datasource-nacos</artifactId>
        </dependency>
```

