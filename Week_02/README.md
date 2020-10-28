学习笔记
#GC日志
java -XX:+PrintGCDetails GCLogAnalysis
java -Xloggc:gc.demo.log -XX:+PrintGCDetails -XX:+PrintGCDateStamps GCLogAnalysis
2020-10-23T19:51:37.339+0800: 0.225: [GC (Allocation Failure) [PSYoungGen: 49152K->8188K(57344K整个年轻代大小)] 49152K->17636K(188416K整个堆大小)部分对象清理，部分对象进入老年区, 0.0119127 secs] [Times: user=0.02 sys=0.05, real=0.01 secs] 
....
多次GC后发生fullGC
2020-10-23T19:51:37.503+0800: 0.393: [Full GC (Ergonomics) [PSYoungGen: 8184K->0K(204800K)] [ParOldGen: 92630K->95369K(195072K)old区大小] 100814K->95369K(399872K), [Metaspace: 2598K->2598K(1056768K)], 0.0129050 secs] [Times: user=0.00 sys=0.00, real=0.01 secs] 

major GC指old GC minor GC指young GC
java -XX:+UseSerialGC -Xms512m -Xmx512m -Xloggc:gc.demo.log -XX:+PrintGCDetails -XX:+PrintGCDateStamps GCLogAnalysis
java -XX:+UseParallelGC -Xms512m -Xmx512m -Xloggc:gc.demo.log -XX:+PrintGCDetails -XX:+PrintGCDateStamps GCLogAnalysis
java -XX:+UseConcMarkSweepGC -Xms512m -Xmx512m -XX:+PrintGCDetails -XX:+PrintGCDateStamps GCLogAnalysis
java -XX:+UseG1GC -Xms512m -Xmx512m -Xloggc:gc.demo.log -XX:+PrintGCDetails -XX:+PrintGCDateStamps GCLogAnalysis
- 日志中old+young！= heap? old 和young在变化，减小？
#线程
- TLAB[?]
####JVM内部线程
- VM 线程    ：单例的VMThread 对象，负责执行VM 操作，下文将对此进行讨论;
- 定时任务线程：单例的WatcherThread 对象， 模拟在VM 中执行定时操作的计时器中断；
- GC 线程    ：垃圾收集器中，用于支持并行和并发垃圾回收的线程;
- 编译器线程  ： 将字节码编译为本地机器代码;
- 信号分发线程：等待进程指示的信号，并将其分配给Java级别的信号处理方法。
####安全点 [?]
1. 方法代码中被植入的安全点检测入口；
2. 线程处于安全点状态：线程暂停执行，这个时候线程栈不再发生改变；
3. JVM 的安全点状态：所有线程都处于安全点状态。
#内存
||||
|----|----|----|
|OOM:Java heap space|创建新的对象时，堆内存中的空间不足以存放新创建的对象|超出预期的访问量/数据量：应用系统设计时，一般是有“容量” 定义的，部署这么多机器，用来 处理一定流量的数据/业务。如果访问量突然飙升，超过预期的阈值，类似于时间坐标系中针尖形 状的图谱。那么在峰值所在的时间段，程序很可能就会卡死、并触发 java.lang.OutOfMemoryError: Java heap space错误<br>内存泄露(Memory leak)：由于代码中的某些隐蔽错误，导致系统占 用的内存越来越多。如果某个方法/某段代码存在内存泄漏，每执行一次，就会（有更多的垃圾对象）占用更多的内存。随着运行时间的推移，泄漏的对象耗光了堆中的所有内存，OOM|
|OOM: PermGen space/OOM: Metaspace|是加载到内存中的class数量太多或体积太大，超过了PermGen 区的大小。|增大PermGen/Metaspace <br>-XX:MaxPermSize=512m<br>-XX:MaxMetaspaceSize=512m<br>高版本JVM 也可以： -XX:+CMSClassUnloadingEnabled|
|OOM: Unable to create new native thread java.lang.OutOfMemoryError: Unable to create new native thread |错误是程序创 建的线程数量已达到上限值的异常信息。| 1. 调整系统参数ulimit -a，echo 120000 > /proc/sys/kernel/threads-max<br>2. 降低xss 等参数<br>3. 调整代码，改变线程创建和使用方式|
###分析工具 
- Eclipse MAT
- jhat
#JVM调优
###高分配速率(High Allocation Rate)
分配速率(Allocation rate)表示单位时间内分配的内存量。通常 使用MB/sec 作为单位。上一次垃圾收集之后，与下一次GC开始之前的年轻代使用量，两者的差值除以时间,就是分配速率。
- 分配速率影响minor GC的频率
- 分配速率要低于回收速率
###提升速率（promotion rate）
用于衡量单位时间内从年轻代提 升到老年代的数据量。一般使用MB/sec作为单位, 和分配速率类似。
- 提升速率影响major GC的频率
#####过早提升
对象存活时间还 不够长的时候就被提升到了老年代。根据分代假 设，可能存在一种情况，老年代中不仅有存活时间长的对象,， 也可能有存活时间短的对象。
#####过早提升的表现：
1. 短时间内频繁地执行full GC
2. 每次full GC 后老年代的使用率都很低，在10-20%或以下
3. 提升速率接近于分配速率
#####解决方案
- 增加年轻代的大小，设置JVM 启动参数，类似这样：- Xmx64m -XX:NewSize=32m，程序在执行时，Full GC 的次数 自然会减少很多，只会对minor GC 的持续时间产生影响。 
- 减少程序每次批处理的数量，也能得到类似的结果。

#NIO
