## Spring Cloud版本控制和灰度发布


在我们使用spring mvc单体架构时，我们可以通过uri，或者请求头做多版本路由，虽然同一个功能需要维护多个版本的接口，但是对于系统而言，不会因为新增一个接口版本而影响到老用户。当我们使用spring cloud构建微服务平台时，也希望能做到这一点，然而spring cloud并没有提供这个功能。

在spring cloud的微服务体系中，大多是使用eureka做为注册中心，ribbon做为负载均衡，hystrix做为断路器。但是在国内网络中却鲜少关于spring-cloud的接口多版本控制的开源项目，而在国内，spring cloud做为越来越被创业公司认同的微服务框架，多版本控制的需求也越来越明显，于是就有了fm-cloud-bamboo这个多版本控制的项目。在开发这个项目的过程，发现只要再做一些扩展，就可以实现灰度管理，于是又有了 fm-cloud-graybunny。



#### 金版本控制
* [spring-cloud-bamboo](spring-cloud-bamboo/README.md)
* spring-cloud-start-multi-version
* [spring-cloud-mult-version-samples](spring-cloud-mult-version-samples/README.md)


#### 灰度发布
* [spring-cloud-gray-core](spring-cloud-gray-core/README.md)
* spring-cloud-gray-client
* spring-cloud-gray-server
* spring-cloud-start-gray
* spring-cloud-start-gray-server
* [spring-cloud-gray-samples](spring-cloud-gray-samples/README.md)



#### 不足
gray目前只有灰度管理的基本功能， 像数据持久化，高可用，推送灰度调整消息等， 都没有实现。 也没有界面化， 仅仅只有接口列表。


#### 扩展思考
gray目前仅仅只支持spring cloud eureka， 但是在spring cloud中，eureka只是做为其中一个注册中心， 如果要做spring cloud的灰度管理， 就还需要兼容其中的注册中心， 比如zookeeper, consul等。

