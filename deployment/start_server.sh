#!/bin/bash
sudo rm -f /var/trtl/.mvn-classpath
sudo /usr/local/maven/current/bin/mvn package -f /var/trtl/pom.xml -DskipTests=true
sudo nohup sudo /var/trtl/deployment/server_run.sh --port 80 --gui &
