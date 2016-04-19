#!/bin/bash
rm -f /var/trtl/.mvn-classpath
/usr/local/maven/current/bin/mvn package -f /var/trtl/pom.xml -DskipTests=true
sudo nohup /var/trtl/deployment/server_run.sh --port 80 --gui &
PID=$!
echo $PID > /var/trtl/pid.txt
