## SpringCloudGray 部署
Spring Cloud Gray 分为管控端，和客户端，管控端需要部署java管理服务和web操作界面。而客户端则简单很多，我们分别介绍下部署方式。
### 管控端
#### java管理服务

**1. 添加jar包依赖**
maven工程可以在pom.xml中添加配置
``` xml
<dependency>
    <groupId>cn.springcloud.gray</groupId>
    <artifactId>spring-cloud-starter-gray-server</artifactId>
    <version>A.1.1.2</version>
</dependency>
```

**2. 添加application参数**
在application.yaml中添加spring boot启动时需要的参数，以及eureka、数据库、jpa的配置等。
还有灰度管理的配置。
``` yaml
server:
  port: 20202
spring:
  application:
    name: gray-server
  #数据库配置
  datasource:
    driver-class-name: com.mysql.jdbc.Driver
    url: jdbc:mysql://localhost:3306/gray_server01?charset=utf8mb4&useSSL=false
    username: root
    password: root
  # JPA 相关配置
  jpa:
    database-platform: org.hibernate.dialect.MySQL5InnoDBDialect
    show-sql: true
    generate-ddl: true
    hibernate:
      ddl-auto: update
# eureka
eureka:
  client:
    register-with-eureka: true
    fetch-registry: true
    serviceUrl:
      defaultZone: http://localhost:20001/eureka/
#灰度管理
gray:
  server:
    evictionIntervalTimerInMs: 30000
    instance:
      normalInstanceStatus: STARTING,UP,OUT_OF_SERVICE
      eviction:
        enabled: true
        evictionIntervalTimerInMs: 86400000
        evictionInstanceStatus: DOWN,UNKNOWN
        lastUpdateDateExpireDays: 1
```
要了解参数的相关信息，请移步**配置参数**查看

**3. 创建Application类**
创建Application类，启动spring boot应用，灰度的管控端需要在Application类添加一个注解@EnableGrayServer，这样spring boot启动时，才会去加载gray server的相关配置类。
``` java
import cn.springcloud.gray.server.EnableGrayServer;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableGrayServer
@EnableDiscoveryClient
public class GrayServerApplication {

    private static final org.slf4j.Logger log = LoggerFactory.getLogger(GrayServerApplication.class);


    public static void main(String[] args) throws UnknownHostException {
new SpringApplicationBuilder(GrayServerApplication.class).web(true).run(args);
    }
}
```

到此，灰度管控端的工程就完成了，可以运行Application类启动spring boot。假如服务访问路径是 http://localhost:20202 , 可以通过http://localhost:20202/swagger-ui.html 访问swagger文档。

#### web操作界面

web操作界的源码在spring-cloud-gray-webui工程中，在源码根目录中运行npm run dev，可以运行开发环境的代码。
```
$ npm run dev
```
可以在.env.development文件中修改灰度管理服务的访问地址

```
ENV = 'development'
VUE_APP_BASE_API = 'http://localhost:20202'
VUE_CLI_BABEL_TRANSPILE_MODULES = true
```

如果要编译测试环境和生产环境的发布文件，可以参考下列列表

环境 | 运行命令 | 配置文件
--- | --- | ---
开发环境 | npm run dev | .env.development
测试环境 | npm run build:stage | .env.staging
生产环境 | npm run build:prod | .env.production

编译后的文件都在根目录的dist文件夹下


