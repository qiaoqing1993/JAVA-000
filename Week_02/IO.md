#网络
#socket
- socket 被翻译为“套接字”，它是计算机之间进行通信的一种约定或一种方式。通过 socket 这种约定，一台计算机可以接其他计算机的数据，也可以向其他计算机发送数据
- socket起源于Unix，socket是一种特殊的文件，一些socket函数就是对其进行的操作（读/写IO、打开、关闭）。
- Socket()函数返回一个整型的Socket描述符，随后的连接建立、数据传输等操作都是通过该Socket实现的。
-socket创建后，分配输入、输出缓冲区。

|函数|注释|
|----|----|
|socket()|加载套接字库，创建套接字|
|bind()|绑定套接字到指定IP地址、端口上|
|connect()|向服务器发出连接请求|
|listen()|监听端口，等待连接请求|
|accept()|请求到来后，接受连接请求，返回一个新的对应于此次连接的套接字|
|send()/write()/sendto()/sendmsg()/writev()|向socket中写入信息|
|recv()/read()/recvfrom()/recvmsg()/readv()|从socket中读取字符|
|close()|关闭socket|
|shutdown()||
|getpeername()||
|gethostname()||
#IO
###同步IO
- BIO阻塞IO：read时一直等到数据到达
- NIO非阻塞IO：轮询，read时无数据则返回
- IO复用：select/poll轮询多个socket直到有数据到达，有epoll在poll基础上优化
- SIGIO信号驱动：数据准备好后，内核信号触发用户线程的read

###异步IO
内核负责等待数据完成并将数据复制到用户进程缓冲区，然后信号触发用户进程（与SIGIO 相比，一个是发送信号告诉用户进程数据准备完毕，一个是IO执行完毕）

##Netty
Netty 是一个广泛使用的 客户端/服务器、异步、基于事件驱动的Java 网络编程框架
- 基于NIO————高并发
- 零拷贝（数据直接写入应用缓冲区，越过内核缓冲和socket缓冲区）————传输快
- 能通过编程自定义各种协议（Tomcat是基于Http协议的）
- 封装好
###应用场景
- HTTP Server
- HTTPS Server
- WebSocket Server
- TCP Server
- UDP Server
- In VM Pipe
###Netty基本概念
|||
|----|----|
|Channel |通道，Java NIO 中的基础概念,代表一个打开的连接,可执行读取/写入IO 操作。Netty 对Channel 的所有IO 操作都是非阻塞的。|
|ChannelFuture|Java 的Future 接口，只能查询操作的完成情况, 或者阻塞当前线程等待操作完成。Netty 封装一个ChannelFuture 接口。我们可以将回调方法传给ChannelFuture，在操作完成时自动执行。|
|Event & Handler|Netty 基于事件驱动，事件和处理器可以关联到入站和出站数据流。|
|Encoder &Decoder|处理网络IO 时，需要进行序列化和反序列化, 转换Java 对象与字节流。对入站数据进行解码, 基类是ByteToMessageDecoder。对出站数据进行编码, 基类是MessageToByteEncoder。|
|ChannelPipeline|数据处理管道就是事件处理器链。有顺序、同一Channel 的出站处理器和入站处理器在同一个列表中。|
|ChannelHandlerContext|用于传输业务数据|
|Codec|netty的编解码器|