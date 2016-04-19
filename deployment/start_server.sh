#!/bin/bash
/usr/local/maven/current/bin/mvn package -f /var/trtl/pom.xml -DskipTests=true
sudo nohup /var/trtl/run --port 80 --gui &
PID=$!
echo $PID > /var/trtl/pid.txt
