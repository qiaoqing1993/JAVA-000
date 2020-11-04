学习笔记
#Netty
###关键对象
- Bootstrap: 启动线程，开启socket
- EventLoopGroup: 线程池
- EventLoop: 线程
- SocketChannel: 连接
- ChannelInitializer: 初始化
- ChannelPipeline: 处理器链
- ChannelHandler: 处理器
####入站事件：
- 通道激活和停用
- 读操作事件
- 异常事件
- 用户事件
####出站事件：
- 打开连接
- 关闭连接
- 写入数据
- 刷新数据

MTU: Maxitum Transmission Unit最大传输单元

MSS: Maxitum Segment Size 最大分段大小
####nagle算法
任意时刻，最多只能有一个未被确认的小段。 所谓“小段”，指的是小于MSS尺寸的数据块，所谓“未被确认”，是指一个数据块发送出去后，没有收到对方发送的ACK确认该数据已收到。
######Nagle算法的规则
- 如果包长度达到MSS，则允许发送；
- 如果该包含有FIN，则允许发送；
- 设置了TCP_NODELAY选项，则允许发送；
- 未设置TCP_CORK选项时，若所有发出去的小数据包（包长度小于MSS）均被确认，则允许发送；
- 上述条件都未满足，但发生了超时（一般为200ms），则立即发送。

##网关
网管职能
请求接入
业务聚合
中介策略（安全、验证、路由、流控等策略）
统一管理

####流量网关
关注稳定与安全
- 全局性流控
- 日志统计
- 防止SQL 注入
- 防止Web 攻击
- 屏蔽工具扫描
- 黑白IP 名单
- 证书/加解密处理
####业务网关
提供更好的服务
- 服务级别流控
- 服务降级与熔断
- 路由与负载均衡、灰度策略
- 服务过滤、聚合与发现
- 权限验证与用户等级策略
- 业务规则与参数校验
- 多级缓存策略

zuul<br>
zuul2(基于netty reactor模型)<br>
spring cloud gateway(基于netty reactor模型)<br>