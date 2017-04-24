# JVM Flags

```
-server
-Xms512m
-Xmx512m
-XX:ReservedCodeCacheSize=256m
-XX:InitialCodeCacheSize=256m
-XX:+UseG1GC 
-XX:+UnlockExperimentalVMOptions 
-XX:MaxGCPauseMillis=100 
-XX:+DisableExplicitGC 
-XX:TargetSurvivorRatio=90 
-XX:G1NewSizePercent=50 
-XX:G1MaxNewSizePercent=80 
-XX:InitiatingHeapOccupancyPercent=10 
-XX:G1MixedGCLiveThresholdPercent=50 
-XX:+AggressiveOpts 
-XX:+AlwaysPreTouch
-XX:+UseStringDeduplication
-XX:CompileThreshold=5000
-XX:+OptimizeStringConcat
-XX:InlineSmallCode=2500
-XX:MaxInlineLevel=32
-XX:MaxInlineSize=64
-XX:MinInliningThreshold=64
-ea
-Dsun.io.useCanonCaches=false
-Djava.net.preferIPv4Stack=true
-XX:+HeapDumpOnOutOfMemoryError
-XX:-OmitStackTraceInFastThrow

```