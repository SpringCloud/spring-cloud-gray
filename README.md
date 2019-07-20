# Spring Cloud Gray - 微服务灰度路由中间件


[![License](https://img.shields.io/badge/license-Apache%202-blue.svg)](https://www.apache.org/licenses/LICENSE-2.0.html)
[![Release](https://img.shields.io/badge/release-A.10.10-blue.svg)](https://github.com/SpringCloud/spring-cloud-gray/releases/tag/A.1.0.10)


## 介绍
Spring Cloud Gray 是一套开源的微服务灰度路由解决方案，它由spring-cloud-gray-client，spring-cloud-gray-client-netflix 和 spring-cloud-tray-server，spring-cloud-gray-webui组成。<br/>
spring-cloud-gray-client定义了一套灰度路由决策模型，灰度信息追踪模型，以及和spring-cloud-gray-server的基本通信功能。<br/>
spring-cloud-gray-client-netflix在spring-cloud-gray-client的基础上集成了微服务注册中心eureka，扩展ribbon的负载均衡规则，提供了对zuul,feign,RestTemplate的灰度路由能力，并且无缝支持hystrix线程池隔离。<br/>
spring-cloud-gray-server负责灰度决策、灰度追踪等信息的管理以及持久化。<br/>
spring-cloud-gray-webui提供操作界面。


#### 设计
在微服务架构中，接口的调用通常是服务消费方按照某种负载均衡策略去选择服务实例；但这无法满足线上更特殊化的一些路由逻辑，比如根据一次请求携带的请求头中的信息路由到某一个服务实例上。Spring Cloud Gray正是为此而创建。<br/>
在Spring Cloud Gray中定义了几个角色灰度客户端(gray-client)、灰度管控端(gray-server)、注册中心。<br/>
![Role](./doc/img/gray.png)

**注册中心**
负责服务的注册和发现。

**灰度客户端**
灰度的客户端是指依赖了spring-cloud-gray-client的服务，一般是指服务消费方。

**灰度管控端**
负责灰度信息的管理、持久化等维护工作。

灰度客户端会从灰度管控端拉取一份灰度信息的清单，并在内存中维护这份清单信息，清单中包含服务，服务实例，灰度策略，灰度追踪字段等。
当请求达到网关时，网关就会在灰度追踪中将需要透传的信息记录下来，并将传递给转发的服务实例，后面的接口调用也会按照同样的逻辑将追踪信息透传下去，从而保证所有一个请求在微服务调用链中的灰度路由。<br/>
如下图所示：

![](./doc/img/gray-all.png)


## 操作界面

### 灰度服务
![](./doc/img/springcloud-gray-service.png)

### 灰度实例
![](./doc/img/springcloud-gray-instance.png)

### 灰度策略
![](./doc/img/springcloud-gray-policy.png)
![](./doc/img/springcloud-gray-decision.png)

### 灰度追踪
![](./doc/img/springcloud-gray-track.png)
