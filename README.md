# raft Java实现的详细设计文档

原文地址：https://www.yuque.com/aidt/tq712e/bhhyno

# 概述
第一期，只实现raft一致性算法的核心功能，先不实现集群成员变化、日志压缩等功能。
本文为raft实现的设计文档，对raft算法进行抽象，将关键逻辑用图形和表格梳理清楚，从而给使用Java代码进行实现提供设计文档。
# 主要概念

- server：服务器，可能为leader、candidate、follower中的任意一方
- leader：主节点
- candidate：候选节点
- follower：从节点
- RPC：远程过程调用，在这里指通信接口
- election：选举leader的过程
- client：客户端，发起请求，传输数据的使用方
- entry：条目，=term + 状态机指令(数据) + logIndex(日志索引)。
- term：任期号，即一个数字。如下图，多个term组成了整个生命周期的时间轴。

![image.png](https://cdn.nlark.com/yuque/0/2020/png/129807/1585474228592-d639c3f1-ac06-4166-befe-9429dcebbeb7.png#align=left&display=inline&height=204&margin=%5Bobject%20Object%5D&name=image.png&originHeight=204&originWidth=519&size=34126&status=done&style=none&width=519)


# 关键设计
## 领域模型
### term介绍
核心的数据结构即为entry，多个entry组成了本地的数据模型，如下图：
![image.png](https://cdn.nlark.com/yuque/0/2020/png/129807/1585476025778-c1e4ec11-71ab-4d0f-ab83-b4b2c432ed75.png#align=left&display=inline&height=171&margin=%5Bobject%20Object%5D&name=image.png&originHeight=276&originWidth=720&size=84196&status=done&style=none&width=447)

- term：任期号
- index：日志索引，从1开始递增
- data：保存的数据

体现在一个集群中，则如下图：
![image.png](https://cdn.nlark.com/yuque/0/2020/png/129807/1585474097159-58d6df44-a5da-4feb-ba0b-44d6a2463cc8.png#align=left&display=inline&height=367&margin=%5Bobject%20Object%5D&name=image.png&originHeight=475&originWidth=633&size=207483&status=done&style=none&width=489)
### 整体模型
![image.png](https://cdn.nlark.com/yuque/0/2020/png/129807/1586963816465-2bebb66d-58e7-40e7-bec2-79500db38ee1.png#align=left&display=inline&height=460&margin=%5Bobject%20Object%5D&name=image.png&originHeight=1164&originWidth=1556&size=202312&status=done&style=none&width=615)
## 用例图
![image.png](https://cdn.nlark.com/yuque/0/2020/png/129807/1586964166688-32fdd9d9-14d8-4f3c-9b09-5da8757565bf.png#align=left&display=inline&height=603&margin=%5Bobject%20Object%5D&name=image.png&originHeight=834&originWidth=707&size=197509&status=done&style=none&width=511)
## 模块划分
![image.png](https://cdn.nlark.com/yuque/0/2020/png/129807/1586963892263-490ac7fb-d470-4924-9f00-3b5f7e6f79c6.png#align=left&display=inline&height=413&margin=%5Bobject%20Object%5D&name=image.png&originHeight=938&originWidth=1336&size=89828&status=done&style=none&width=588)
## 关键类设计
### 整体类图如下
![image.png](https://cdn.nlark.com/yuque/0/2020/png/129807/1586963936053-179c57ae-3942-4f3e-8537-257573dc8108.png#align=left&display=inline&height=550&margin=%5Bobject%20Object%5D&name=image.png&originHeight=1100&originWidth=1252&size=150070&status=done&style=none&width=626)
### Server状态流转类图
![image.png](https://cdn.nlark.com/yuque/0/2020/png/129807/1586964012137-d5f3472c-790c-4eb9-b626-f9d9747d043a.png#align=left&display=inline&height=215&margin=%5Bobject%20Object%5D&name=image.png&originHeight=430&originWidth=1102&size=41898&status=done&style=none&width=551)
## server状态流转
![image.png](https://cdn.nlark.com/yuque/0/2020/png/129807/1588560247069-8cc2d1cb-988b-4008-adcf-74e9685a88cd.png#align=left&display=inline&height=198&margin=%5Bobject%20Object%5D&name=image.png&originHeight=620&originWidth=1710&size=87332&status=done&style=none&width=546)
备注：跟随者只响应来自其他服务器的请求。如果跟随者接收不到消息，那么他就会变成候选人并发起一次选举。获得集群中大多数选票的候选人将成为领导者。在一个任期内，领导人一直都会是领导人直到自己宕机了。
## Entry状态转换
从客户端submit到最终apply到状态机：
![image.png](https://cdn.nlark.com/yuque/0/2020/png/129807/1588561273413-1aa6fdd3-c4e5-4128-a68e-3d97af7041bc.png#align=left&display=inline&height=394&margin=%5Bobject%20Object%5D&name=image.png&originHeight=788&originWidth=678&size=52898&status=done&style=none&width=339)
# 核心实现流程
## leader选举
### follower投票
![image.png](https://cdn.nlark.com/yuque/0/2020/png/129807/1588560317486-05008a80-634e-4153-a836-1227e237fcd8.png#align=left&display=inline&height=402&margin=%5Bobject%20Object%5D&name=image.png&originHeight=804&originWidth=1194&size=69838&status=done&style=none&width=597)
约束转换：


1. 如果term < currentTerm返回 false （5.2 节）
1. 如果 votedFor 为空或者为 candidateId，并且候选人的日志至少和自己一样新，那么就投票给他（5.2 节，5.4 节）
1. 如果接收到的 RPC 请求或响应中，任期号T > currentTerm，那么就令 currentTerm 等于 T，并切换状态为跟随者（5.1 节）
### candidate发起投票(广播)
![image.png](https://cdn.nlark.com/yuque/0/2020/png/129807/1588560360242-e5e401a7-0747-4c48-bf67-9914930b3c60.png#align=left&display=inline&height=440&margin=%5Bobject%20Object%5D&name=image.png&originHeight=880&originWidth=486&size=50172&status=done&style=none&width=243)
### candidate发起投票(单个)
![image.png](https://cdn.nlark.com/yuque/0/2020/png/129807/1588560414102-242ad7b4-b094-4704-a3b4-c86f334d951b.png#align=left&display=inline&height=331&margin=%5Bobject%20Object%5D&name=image.png&originHeight=804&originWidth=1250&size=83555&status=done&style=none&width=515)
通过ifLeaderChannel方式通知状态转换模块，由candidate转换为leader
## 日志复制
### follower接受条目
![image.png](https://cdn.nlark.com/yuque/0/2020/png/129807/1588560546366-aded0a23-15d0-4503-8e6e-4f214db2ee31.png#align=left&display=inline&height=514&margin=%5Bobject%20Object%5D&name=image.png&originHeight=1028&originWidth=1392&size=179589&status=done&style=none&width=696)
![image.png](https://cdn.nlark.com/yuque/0/2020/png/129807/1588560571684-123e283d-8a72-4cdf-858d-50a5f5faa647.png#align=left&display=inline&height=474&margin=%5Bobject%20Object%5D&name=image.png&originHeight=948&originWidth=1422&size=158037&status=done&style=none&width=711)


![image.png](https://cdn.nlark.com/yuque/0/2020/png/129807/1588560599239-4aa0ced5-b121-45b2-95c9-5cb73a39b4bd.png#align=left&display=inline&height=385&margin=%5Bobject%20Object%5D&name=image.png&originHeight=770&originWidth=1078&size=239430&status=done&style=none&width=539)
### leader请求条目(广播)
![image.png](https://cdn.nlark.com/yuque/0/2020/png/129807/1588560642248-cd8285cd-56c6-4168-b636-3804e82a903a.png#align=left&display=inline&height=592&margin=%5Bobject%20Object%5D&name=image.png&originHeight=1184&originWidth=1654&size=221583&status=done&style=none&width=827)
![image.png](https://cdn.nlark.com/yuque/0/2020/png/129807/1588560659662-1c350154-3707-463a-ad32-f00ddc4128ff.png#align=left&display=inline&height=308&margin=%5Bobject%20Object%5D&name=image.png&originHeight=616&originWidth=812&size=244968&status=done&style=none&width=406)
![image.png](https://cdn.nlark.com/yuque/0/2020/png/129807/1588560676249-c15a7e6a-fc3c-4365-9182-7f73e85e67d8.png#align=left&display=inline&height=247&margin=%5Bobject%20Object%5D&name=image.png&originHeight=494&originWidth=754&size=212343&status=done&style=none&width=377)
### leader请求条目(单个)
![image.png](https://cdn.nlark.com/yuque/0/2020/png/129807/1588560737487-8b2a5da6-fede-49a3-ab42-4c6007a54ca7.png#align=left&display=inline&height=558&margin=%5Bobject%20Object%5D&name=image.png&originHeight=1116&originWidth=1290&size=191715&status=done&style=none&width=645)
## 客户端submit
### 接收客户端submit
![image.png](https://cdn.nlark.com/yuque/0/2020/png/129807/1588560781329-b5486a0f-a054-4f33-8223-ed8e4875a1e2.png#align=left&display=inline&height=479&margin=%5Bobject%20Object%5D&name=image.png&originHeight=958&originWidth=1198&size=148591&status=done&style=none&width=599)
### 客户端submit
![image.png](https://cdn.nlark.com/yuque/0/2020/png/129807/1588560813733-db5652c1-07ee-4371-91c6-5e21db22a9ad.png#align=left&display=inline&height=382&margin=%5Bobject%20Object%5D&name=image.png&originHeight=764&originWidth=946&size=94464&status=done&style=none&width=473)


## 提交过程：append-commit-apply-sm
### append-commit-apply-sm概述
![image.png](https://cdn.nlark.com/yuque/0/2020/png/129807/1588560867276-96000036-756e-40f4-b4ca-1c5f5925d731.png#align=left&display=inline&height=418&margin=%5Bobject%20Object%5D&name=image.png&originHeight=836&originWidth=944&size=66754&status=done&style=none&width=472)
### commit2Apply
![image.png](https://cdn.nlark.com/yuque/0/2020/png/129807/1588560975605-4912b7df-28bc-4a1f-ad99-d377c9cbfdf3.png#align=left&display=inline&height=555&margin=%5Bobject%20Object%5D&name=image.png&originHeight=1110&originWidth=1266&size=119373&status=done&style=none&width=633)
### 状态机的一种实现（apply2sm）
![image.png](https://cdn.nlark.com/yuque/0/2020/png/129807/1588561029493-e8623425-2a19-4446-884a-aaee79211fd2.png#align=left&display=inline&height=447&margin=%5Bobject%20Object%5D&name=image.png&originHeight=894&originWidth=1232&size=110272&status=done&style=none&width=616)


## server角色流转的详细实现
基于上面的server状态流转的状态机，详细描述如下：
![image.png](https://cdn.nlark.com/yuque/0/2020/png/129807/1588561076821-3994d2d2-c1f7-48a0-bf62-23d559dd78c1.png#align=left&display=inline&height=544&margin=%5Bobject%20Object%5D&name=image.png&originHeight=1088&originWidth=1786&size=199293&status=done&style=none&width=893)
# 实现参考
[MIT 6.824 2020 Raft 实现细节汇总](https://zhuanlan.zhihu.com/p/39105353)


# 其他
## 通信模块
dubbo参考：

- [Dubbo不使用zk](https://www.jianshu.com/p/f5ddbde05813)
- [dubbo-quick-start](http://dubbo.apache.org/zh-cn/docs/user/quick-start.html)

grpc参考：

- [google grpc 快速入门](https://www.jianshu.com/p/ff354ccbde08)
- [grpc Java Quick Start](https://grpc.io/docs/quickstart/java/)

