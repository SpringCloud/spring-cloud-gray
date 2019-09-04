# 管控端界面使用手册
## 设置实例的灰度策略
### 场景
设置service-a的ip为192.168.0.10的实例为灰度实例，并设置灰度策略version = 2.1.0 

### 效果
url参数version 等于2.1.0的请求，都将被转到192.168.0.10这台实例上。

### 操作
#### 第一步-添加灰度实例
添加灰度实例，在服务列表，点击service-a那一行的**<font color=blue>在线实例</font>**按钮，打开service-a的在线实例列表，选中ip为192.168.0.10的那台实例，然后点击**<font color=blue>Add</font>**按钮，把实例添加为灰度实例。

#### 第二步-设置灰度追踪
设置灰度追踪的目的是为了将用户请求的最初的信息透传到服务链，比如version参数能够从网关一直透传到后面的服务中。

操作步骤： 服务列表 -> <font color=blue>追踪</font>按钮(从网关服务进) -> 灰度追踪列表
点击左上角的**<font color=blue>Add</font>**按钮，弹出添加面板，输入追踪类型(Name)和追踪字段(Infos)。

Name: HttpParameter
Infos: version

<font color=red>Infos 可以追踪多个字段，多个字段用逗号(,)分隔</font>

**更多的灰度追踪配置可以查看后面的<font color=red>灰度追踪格式</font>**


#### 第三步-设置灰度策略
设置灰度策略，需要添加灰度策略以及灰度决策。进入灰度策略列表的操作步骤：服务列表 -> <font color=blue>实例</font>按钮 -> 灰度实例列表 -> <font color=blue>策略</font>按钮 -> 灰度策略列表。

点击左上角的**<font color=blue>Add</font>**按钮，弹出添加面板，输入策略标识(Alias)，这个字估由用户自定义。


然后再为这个策略添加灰度决策，步骤是: 灰度策略列表 -> <font color=blue>决策</font>按钮 -> 灰度决策列表

同样是点击左上角的**<font color=blue>Add</font>**按钮，弹出添加面板，输入灰度决策名称(Name)，和灰度决策条件(Infos);

**如设置version 等于2.1.0的请求，都将被转到这台实例上**
Name: HttpTrackParameter
Infos: {"name":"version","compareMode":"EQUAL","values":"2.1.0"}

**更多的灰度决策配置可以查看后面的<font color=red>灰度决策格式</font>**






## 修改实例的灰度状态
**操作步骤**
服务列表 -> <font color=blue>实例</font>按钮 -> 灰度实例列表 -> <font color=blue>Edit</font>按钮
以上操作是打开灰度实例的信息面板，修改`Gray Status`字段，该字段有两个选项：OPEN, CLOSE，分别对应打开和关闭，修改完成后，点击保存即可。

**灰度状态的作用**
灰度状态是用来控制实例是被灰度，当灰度状态打开时，只有匹配该实例的任意灰度策略的请求，才会被转到到该实例上。


## 灰度锁定
**操作步骤**
服务列表 -> <font color=blue>实例</font>按钮 -> 灰度实例列表 -> <font color=blue>Edit</font>按钮
以上操作是打开灰度实例的信息面板，修改`Gray Lock`字段，该字段有两个选项：LOCK, UNLOCK，分别对应锁定和非锁定，修改完成后，点击保存即可。

**灰度锁定的作用**
灰度锁定是为了当灰度状态为打开时，即使实例的状态为DOWN(非正常状态)了，其它的灰度客户端也能从管控端获取到该实例的灰度信息。
这样在发布过程，发布实例的灰度信息不会因为实例状态的变动而导致其它灰度客户端获取不到该发布实例的灰度信息，造成正常请求被转发到该发布实例上。



## 灰度决策的格式
### 在度决策
* HttpHeaderGrayDecisionFactory
	**Name:** HttpHeader
	**Infos:** 
	``` json
	{"compareMode":"EQUAL","header":"headerName","values":"headerValue"}
	```
	
	**Class:** cn.springcloud.gray.decision.factory.HttpHeaderGrayDecisionFactory
	**Describe:** 根据http请求头的字段进行判断

---

* HttpMethodGrayDecisionFactory
	**Name:** HttpMethod
	**Infos:** 
	``` json
	{"compareMode":"EQUAL","method":"POST"}
	```
	
	**Class:** cn.springcloud.gray.decision.factory.HttpMethodGrayDecisionFactory
	**Describe:** 根据http请求方法的字段进行判断

---

* HttpParameterGrayDecisionFactory
	**Name:** HttpParameter
	**Infos:** 
	``` json
	{"compareMode":"EQUAL","name":"paramName","values":"paramValue"}
	```
	
	**Class:** cn.springcloud.gray.decision.factory.HttpParameterGrayDecisionFactory
	**Describe:** 比对http url参数

---

* HttpTrackHeaderGrayDecisionFactory
	**Name:** HttpTrackHeader
	**Infos:** 
	``` json
	{"compareMode":"EQUAL","header":"headerName","values":"headerValue"}
	```
	
	**Class:** cn.springcloud.gray.decision.factory.HttpTrackHeaderGrayDecisionFactory
	**Describe:** 根据灰度追踪记录的http请求头的字段进行判断
	
	---
	
* HttpTrackParameterGrayDecisionFactory
	**Name:** HttpTrackParameter
	**Infos:** 
	``` json
	{"compareMode":"EQUAL","name":"paramName","values":"paramValue"}
	```
	**Class:** cn.springcloud.gray.decision.factory.HttpTrackParameterGrayDecisionFactory
	**Describe:** 根据灰度追踪记录的http url参数进行判断
	
	---
	
* TraceIpGrayDecisionFactory
	**Name:** TraceIp
	**Infos:** 
	``` json
	{"ip":"192\.168\.0\.*"}
	```
	**Class:** cn.springcloud.gray.decision.factory.TraceIpGrayDecisionFactory
	**Describe:** 根据灰度追踪记录的请求ip进行判断, ip可以是正常表达式
	
	---
	
* TrackAttributeGrayDecisionFactory
	**Name:** TrackAttribute
	**Infos:** 
	``` json
	{"compareMode":"EQUAL","name":"attributeName","values":"attributeValue"}
	```
	**Class:** cn.springcloud.gray.decision.factory.TrackAttributeGrayDecisionFactory
	**Describe:** 根据灰度追踪记录的属性值进行判断
	
	---
	
* FlowRateGrayDecisionFactory
	**Name:** FlowRate
	**Infos:** 
	``` json
	{"type":"TrackAttribute","field":"userid","salt":"","rate":"10"}
	```
	**Class:** cn.springcloud.gray.decision.factory.FlowRateGrayDecisionFactory
	**Describe:** 指定字段按百分比放量进行判断
	


### CompareMode
比较模式，共有6种

Mode | Support | Describe
--- | --- | ---
GT | String | 大于
GTE | String | 大于等于
LT | String | 小于
LTE | String | 小于等于
EQUAL | String, List | 一致
UNEQUAL | String, List | 不一致
CONTAINS_ALL | List | 配置中的值包含请求中的所有值
CONTAINS_ANY | List | 配置中的值至少包含请求值中的一个
NOT_CONTAINS_ANY | List | 配置中的值没有包含请求值中的任何一个
NOT_CONTAINS_ALL | List | 配置中的值全部不包含请求值，与NOT_CONTAINS_ANY相同




## 灰度追踪的格式
名称 | 描述
--- | ---
HttpReceive | 接收调用端传递过来的灰追踪信息(必须)
HttpHeader | 获取http请求的header并记录到灰度追踪的Header中
HttpIP | 获取http请求的ip并记录到灰度追踪中
HttpMethod | 获取http请求的请求方法并记录到灰度追踪中
HttpParameter | 获取http请求的url参数并记录到灰度追踪的parameter中
HttpURI | 获取http请求的URI并记录到灰度追踪中


