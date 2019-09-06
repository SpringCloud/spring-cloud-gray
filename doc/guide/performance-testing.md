
# 测试结果

## 性能测试


* 测试场景

提供方service-a接口sleep 50ms。
测试机器均为4C8G。
一个策略，两个决策(HttpParameter)。


测试场景 | 服务 | 接口 
--- | --- | ---
zuul | gateway | /gateway/service-a/api/test/get?version=${version}&platform={platform}
zuul+feign | gateway | /gateway/service-b/api/test/feignGet?version=${version}&platform={platform}
feign | service-b | /api/test/feignGet?version=${version}&platform={platform}


压测准备：zuul-feign 预热5分钟

 
场景：feign
接口：/api/test/feignGet
压测5分钟

灰度环境

connections | RPS | Avg | Max | 90%Line | Error
--- | --- | --- | --- | --- | --- 
200连接 | 3.32k | 61.11ms | 642.61ms | 69.06ms | 0/996349
300连接 | 4.17k| 72.30ms | 523.08ms | 86.03ms | 0/1252191
 
正常环境

connections | RPS | Avg | Max | 90%Line | Error
--- | --- | --- | --- | --- | --- 
200连接 | 3.70k | 54.12ms | 180.54ms | 57.08ms | 0/1111624
300连接 | 5.14k | 58.82ms | 1.03s | 66.62ms | 2/1540693
 
 
场景：Zuul
接口：/gateway/service-a/api/test/get
压测5分钟

灰度环境

connections | RPS | Avg | Max | 90%Line | Error
--- | --- | --- | --- | --- | --- 
200连接 | 3.61k | 55.69ms | 383.83ms | 58.64ms | 0/1082715
300连接 | 4.80k | 62.87ms | 442.96ms | 69.38ms | 0/1439638
 
正常环境

connections | RPS | Avg | Max | 90%Line | Error
--- | --- | --- | --- | --- | --- 
200连接 | 3.71k | 53.95ms | 161.71ms | 56.58ms | 0/1113616
300连接 | 5.35k | 56.32ms | 207.53ms | 60.69ms | 0/1605155
 
 
场景：Zuul-feign
接口：/gateway/service-b/api/test/feignGet
压测5分钟，超时时间8s

灰度环境

connections | RPS | Avg | Max | 90%Line | Error
--- | --- | --- | --- | --- | --- 
200连接 | 3.08k | 66.10ms | 727.43ms | 77.56ms | 0/922887
300连接 | 3.56k | 85.61ms | 1.37s | 108.50ms | 11/1067908
 
正常环境

connections | RPS | Avg | Max | 90%Line | Error
--- | --- | --- | --- | --- | --- 
200连接 | 3.57k | 56.35ms | 188.72ms | 60.71ms | 0/1070078
300连接 | 4.95k | 63.21ms | 1.16s | 70.76ms | 401/1485965


## 稳定性测试
灰度环境以100测试zuul+feign的调用场景，连接数持续运行12小时，未发现泄漏风险。

