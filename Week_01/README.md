学习笔记
#GC算法
##串行
##并行
##CMS
##G1 
- 垃圾优先 
- 每次只清理回收集里的垃圾，垃圾最多的小块优先处理
- G1之前的回收算法，触发时间是某个区写满or某个对象无法存放，G1之后可以按阈值启动，该阈值会按照历史统计数据自行调整，优化自身行为
- G1 触发full GC时，会退回为串行GC，GC 暂停时间增长为秒级别

|参数|含义|默认值|备注|
|----|-----|-----|----|
|-XX：G1NewSizePercent|初始年轻代占整个Java Heap 的大小|默认5%||
|-XX：G1MaxNewSizePercent|最大年轻代占整个Java Heap 的大小|默认60%||
|-XX：G1HeapRegionSize|设置每个Region 的大小，单位MB，需要为1，2，4，8，16，32 中的某个值|堆内存的1/2000|如果这个值设置比较大，那么大对象就可以进入Region 了。|
|-XX：ConcGCThreads|与Java 应用一起执行的GC 线程数量|默认是Java 线程的1/4|减少这个参数的数值可能会提升并行回收的效率，提高系统内部吞吐量。如果这个数值过低，参与回收垃圾的线程不足，也会导致并行回收机制耗时加长|
|-XX：+InitiatingHeapOccupancyPercent（简称IHOP）|G1 内部并行回收循环启动的阈值|默认为Java Heap的45%|这个可以理解为老年代使用大于等于45% 的时候，JVM 会启动垃圾回收。这个值非常重要，它决定了在什么时间启动老年代的并行回收。|
|-XX：G1HeapWastePercent|G1停止回收的最小内存大小|堆大小的5%||
|-XX：+GCTimeRatio|这个参数就是计算花在Java 应用线程上和花在GC 线程上的时间比率|默认是9，跟新生代内存的分 配比例一致|
 |-XX：MaxGCPauseMills|预期G1 每次执行GC 操作的暂停时间，单位是毫秒|默认值是200 毫秒|
 
 ###G1处理
 1、年轻代模式转移暂停（Evacuation Pause）
 2、并发标记（Concurrent Marking） 类似CMS：
 3、 转移暂停：混合模式（Evacuation Pause (mixed)）
 