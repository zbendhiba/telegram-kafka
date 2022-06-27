
Check memory :
-Xmx20M
-- Native mode :
``` 
ps -o pid,rss,command -p $(pgrep quarkus)
```
-- JVM mode
```
ps -o pid,rss,command -p $(jps| grep quarkus | awk '{print $1}')
```