#!/bin/bash
PID_FILE=/var/trtl/trtl-pid
sudo rm -f /var/trtl/.mvn-classpath
sudo /usr/local/maven/current/bin/mvn package -f /var/trtl/pom.xml -DskipTests=true
if [ ! -f $PID_FILE ]; then
  sudo nohup sudo /var/trtl/deployment/server_run.sh --port 80 --gui 2>> /dev/null >> /dev/null &
    echo $! > $PID_FILE
fi
