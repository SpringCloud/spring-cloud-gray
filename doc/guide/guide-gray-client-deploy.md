## SpringCloudGray 部署
Spring Cloud Gray 分为管控端，和客户端，管控端需要部署java管理服务和web操作界面。而客户端则简单很多，我们分别介绍下部署方式。

### 客户端
#### 服务消费方添加jar包依赖
maven工程在pom.xml中添加依赖
```xml
<dependency>
    <groupId>cn.springcloud.gray</groupId>
    <artifactId>spring-cloud-starter-gray-client</artifactId>
    <version>A.1.1.2</version>
</dependency>
<dependency>
    <groupId>cn.springcloud.gray</groupId>
    <artifactId>spring-cloud-gray-plugin-openfeign</artifactId>
    <version>A.1.1.2</version>
</dependency>

```

#### zuul(网关)添加jar包依赖
maven工程在pom.xml中添加依赖
```xml
<dependency>
    <groupId>cn.springcloud.gray</groupId>
    <artifactId>spring-cloud-starter-gray-client</artifactId>
    <version>A.1.1.2</version>
</dependency>
<dependency>
    <groupId>cn.springcloud.gray</groupId>
    <artifactId>spring-cloud-gray-plugin-zuul</artifactId>
    <version>A.1.1.2</version>
</dependency>
```

#### spring cloud gateway(网关)添加jar包依赖
maven工程在pom.xml中添加依赖
```xml
<dependency>
    <groupId>cn.springcloud.gray</groupId>
    <artifactId>spring-cloud-starter-gray-client</artifactId>
    <version>B.0.0.2</version>
    <exclusions>
        <exclusion>
            <artifactId>spring-cloud-gray-plugin-webmvc</artifactId>
            <groupId>cn.springcloud.gray</groupId>
        </exclusion>
        <exclusion>
            <groupId>cn.springcloud.gray</groupId>
            <artifactId>spring-cloud-gray-plugin-eureka</artifactId>
        </exclusion>
    </exclusions>
</dependency>

<dependency>
    <groupId>cn.springcloud.gray</groupId>
    <artifactId>spring-cloud-gray-plugin-gateway</artifactId>
    <version>B.0.0.2</version>
</dependency>
```


