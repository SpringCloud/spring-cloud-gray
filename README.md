## Spring Cloud版本控制和灰度发布


在我们使用spring mvc单体架构时，我们可以通过uri，或者请求头做多版本路由，虽然同一个功能需要维护多个版本的接口，但是对于系统而言，不会因为新增一个接口版本而影响到老用户。当我们使用spring cloud构建微服务平台时，也希望能做到这一点，然而spring cloud并没有提供这个功能。

在spring cloud的微服务体系中，大多是使用eureka做为注册中心，ribbon做为负载均衡，hystrix做为断路器。但是在国内网络中却鲜少关于spring-cloud的接口多版本控制的开源项目，而在国内，spring cloud做为越来越被创业公司认同的微服务框架，多版本控制的需求也越来越明显，于是就有了开发多版本控制和灰度管理的想法。




#### 不足
没有界面化， 仅仅只有接口列表。


#### 扩展思考
gray目前仅仅只支持spring cloud eureka， 但是在spring cloud中，eureka只是做为其中一个注册中心， 如果要做spring cloud的灰度管理， 就还需要兼容其他的注册中心， 比如zookeeper, consul, nacos等。

