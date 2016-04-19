#!/bin/bash
/usr/local/maven/current/bin/mvn package -DskipTests=true
nohup ./run --port 4000 --gui &
PID=$!
echo $PID > pid.txt
