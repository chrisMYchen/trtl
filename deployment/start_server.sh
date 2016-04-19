#!/bin/bash
mvn package
nohup ./run --port 4000 --gui &
PID=$!
echo $PID > pid.txt
