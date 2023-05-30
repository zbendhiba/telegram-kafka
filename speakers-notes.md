
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


$ cat src/main/resources/application.properties | envsubst > src/main/resources/application_new.properties
$ mv src/main/resources/application_new.properties src/main/resources/application.properties
