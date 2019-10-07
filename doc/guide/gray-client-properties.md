
# SpringCloudGray 配置参数

### 客户端
#### gray
Property Name | Default Value | Remarks
--- | --- | ---
enabled | false | 是否启用实例记录清除任务(默认为false)
grayRouting | true | 是否灰度路由，如果开启，将优先对灰度实例进行路由，如果没有匹配的灰度实例，再对正常实现进行路由

examp
``` yaml
gray:
	enabled: true
	grayRouting: true
```

#### gray.server
灰度管控端相关的配置

Property Name | Default Value | Remarks
--- | --- | ---
url |  | 灰度管控端的url <br/>可以http://192.168.0.1:8080/uri <br/>也可以是ribbon的风格 http://gray-server/uri
loadbalanced | false | url是否是负载均衡的路径
retryable | true | 在和灰度服务器通信时，如果交互失败，是否重试
retryNumberOfRetries | 3 | 重试次数

examp
``` yaml
gray:
  server:
    url: http://gray-server
    loadbalanced: false
    retryable: true
    retryNumberOfRetries: 3
```


#### gray.client

Property Name | Default Value | Remarks
--- | --- | ---
runenv | web | 运行环境(默认web)
serviceInitializeDelayTimeInMs | 40000 | 实始化灰度信息的延迟时间<默认40秒>
serviceUpdateIntervalTimerInMs | 60000 | 定时从管控端更新灰度实例信息的间隔时间(默认60秒)<br/>0表示不创建定时任务

examp
``` yaml
gray:
	client:
    runenv: web
    serviceInitializeDelayTimeInMs: 40000
    serviceUpdateIntervalTimerInMs: 60000
```

#### gray.client.instance
实例自身相关的灰度参数，比如自动注册灰度实例。

Property Name | Default Value | Remarks
--- | --- | ---
grayEnroll | false | 是否自动注册为灰度服务,默认false
grayEnrollDealyTimeInMs | 40000 | 触发自动注册的延迟时间

examp
``` yaml
gray:
	client:
    instance:
    	grayEnroll: true
    	grayEnrollDealyTimeInMs: 40000
```

#### gray.client.caches
缓存的配置属性，目前实例的灰度决策缓存策略(grayDecision)已应用

Property Name | Default Value | Remarks
--- | --- | ---
maximumSize | 1000 | 最大缓存数
expireSeconds | 60 | 过期时间(秒)

examp
``` yaml
gray:
	client:
		caches:
			grayDecision:
				maximumSize: 1000
				expireSeconds: 60
```


#### gray.request
http request相关的配置

Property Name | Default Value | Remarks
--- | --- | ---
loadBody | false | 是否记录请求body，不推荐

examp
``` yaml
gray:
	request:
		loadBody: false
```


#### gray.request.track
灰度追踪相关的配置

Property Name | Default Value | Remarks
--- | --- | ---
enabled | true | 是否启用灰度追踪
trackType | web | 追踪类型: web环境
definitionsUpdateIntervalTimerInMs | 60000 | 定时从拉取最新的追踪信息的间隔时间
definitionsInitializeDelayTimeInMs | 40000 | 初始化追踪信息的延迟时间

examp
``` yaml
gray:
	request:
		track:
			enabled: true
			trackType:  web
			definitionsUpdateIntervalTimerInMs: 60000
			definitionsInitializeDelayTimeInMs: 40000
```


#### gray.request.track.web

Property Name | Default Value | Remarks
--- | --- | ---
pathPatterns | /* | Filter拦截的uri,多个用逗号分隔
excludePathPatterns | | Filter拦截排除的uri,多个用逗号分隔

examp
``` yaml
gray:
	request:
		track:
			web:
				pathPatterns: /*
				excludePathPatterns: /static/*
```



#### gray.request.track.web.trackDefinitions
追踪项配置。
这部分可以在项目中配置好，也可以在管控端编辑。优先级以管控端优先，项目配置次之。

Property Name | Default Value | Remarks
--- | --- | ---
name | | 追踪项的名称
value | | 追踪项的内容(字段),多个用逗号分隔

examp
``` yaml
gray:
	request:
		track:
			web:
				trackDefinitions:
					name: HttpHeader
					value: varsion
```


#### gray.holdoutServer
以ribbon的举例，ribbon在路由转发时，只会将请求转发给实例状态为up的Server。
如果在新版本上线过程中，希望上线的实例不影响正常用户，同时又能在线上进行测试，这就需要ribbon能够将特定的请求转发给不是UP状态的实例。
通过该配置，可以实现。

Property Name | Default Value | Remarks
--- | --- | ---
enabled | false | 是否开启
services | | 配置服务可以被转发的实例状态,该参数为Map<String, List>类型
zoneAffinity | false | 是否开启区域亲和
cacheable | false | 是否缓存

examp
``` yaml
gray:
	holdout-server:
		enabled: false
		zoneAffinity: false
		cacheable: false
		services:
			service-a: STARTING
```


#### gray.hystrix
Hystrix的相关配置

Property Name | Default Value | Remarks
--- | --- | ---
enabled | false | 是否加载灰度hystrix的相关配置与实现类
threadTransmitStrategy | WRAP_CALLABLE | 线程变量传递策略，分别WRAP_CALLABLE,HYSTRIX_REQUEST_LOCAL_STORAGE。<br />{WRAP_CALLABLE: 使用com.netflix.hystrix.strategy.concurrency.HystrixConcurrencyStrategy实现, HYSTRIX_REQUEST_LOCAL_STORAGE: 使用com.netflix.hystrix.strategy.concurrency.HystrixRequestVariableDefault实现}

examp
``` yaml
gray:
	hystrix:
		enabled: true
		threadTransmitStrategy: WRAP_CALLABLE
```

