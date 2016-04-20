#!/bin/bash
PID_FILE=/var/trtl/trtl-pid
sudo rm -f /var/trtl/.mvn-classpath
sudo /usr/local/maven/current/bin/mvn package -f /var/trtl/pom.xml -DskipTests=true >> /var/trtl/trtl.log
if [ ! -f $PID_FILE ]; then
  nohup /var/trtl/deployment/server_run.sh --port 80 --gui 2>> /var/trtl/trtl.log >> /var/trtl/trtl.log &
    sudo echo $! > $PID_FILE
fi
