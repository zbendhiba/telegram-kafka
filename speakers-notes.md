
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


$ docker run -d --name=redpanda-1 --rm \
-p 9092:9092 \
-p 9644:9644 \
docker.vectorized.io/vectorized/redpanda:v22.1.2 \
redpanda start \
--overprovisioned \
--smp 1  \
--memory 1G \
--reserve-memory 0M \
--node-id 0 \
--check=false
