
# SpringCloudGray 配置参数
### 管控端
#### gray.server.discovery

Property Name | Default Value | Remarks
--- | --- | ---
evictionEnabled | true | 定时同步注册中心实例状态的开关
evictionIntervalTimerInMs | 60000 | 定时同步注册中心实例状态的时间间隔(默认60秒)， 0表示不创建定时任务

examp
``` yaml
gray:
	server:
		discovery:
			evictionEnabled: true
			evictionIntervalTimerInMs: 60000
```

#### gray.server.instance

Property Name | Default Value | Remarks
--- | --- | ---
normalInstanceStatus | STARTING,UP | 正常的实例状态,默认为STARTING, UP。<br/>实例状态分别是STARTING, UP, OUT_OF_SERVICE, DOWN, UNKNOWN

examp
``` yaml
gray:
  server:
    instance:
      normalInstanceStatus: STARTING,UP
```

#### gray.server.instance.eviction

Property Name | Default Value | Remarks
--- | --- | ---
enabled | false | 是否启用实例记录清除任务(默认为false)
evictionIntervalTimerInMs | 86400000 | 任务间隔时间,单位为毫秒(默认1天)
evictionInstanceStatus | DOWN,UNKNOWN | 需要清徐的实例状态<br/>默认DOWN, UNKNOWN
lastUpdateDateExpireDays | 1 | 最后更新时间过期天数<br/>默认1天

examp
``` yaml
gray:
  server:
    instance:
      eviction:
        enabled: true
        evictionIntervalTimerInMs: 86400000
        evictionInstanceStatus: DOWN,UNKNOWN
        lastUpdateDateExpireDays: 1
```
