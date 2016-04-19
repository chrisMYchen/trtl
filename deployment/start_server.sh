#!/bin/bash
/usr/local/maven/current/bin/mvn package -DskipTests=true
sudo nohup ./run --port 80 --gui &
PID=$!
echo $PID > pid.txt
