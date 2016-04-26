#!/bin/bash
sudo rm -f /var/trtl/.mvn-classpath
sudo /usr/local/maven/current/bin/mvn package -f /var/trtl/pom.xml -DskipTests=true >> /var/trtl/trtl.log
/var/trtl/deployment/server_run.sh --port 80 --gui --database /home/ec2-user/turtlDB.sqlite3 2>> /var/trtl/trtl.log >> /var/trtl/trtl.log
