#!/bin/sh

PROJ="/var/trtl/"

# To run our code, we must have it in our classpath.
# We use $(ls target/*.jar) instead of hardcoding the name so that
# this script can be reused in other projects more easily.

JAR=$PROJ"target/*.jar"

TARGET=$(ls $JAR 2>/dev/null)
if [ -z "$TARGET" ]; then
    echo "No jar file in target/, try 'mvn package'".
    exit 1
fi

# In the pom.xml, we've already explained what other libraries we
# depend on. Maven downloaded them, and put them "somewhere" (our
# repository). Now we ask maven to build up the CLASSPATH that lets us
# run against those libraries.

# We store the CLASSPATH in .mvn-classpath if the pom.xml is newer
# than our existing .mvn-classpath file. (We avoid this command if
# .mvn-classpath is fresh, since it's rather slow.)
sudo /usr/local/maven/current/bin/mvn -f $PROJ dependency:build-classpath -Dmdep.outputFile=${PROJ}.mvn-classpath -q

# Now, we set $CP to the contents of the .classpath file.
 CP=$(cat ${PROJ}.mvn-classpath)

# Again, we're trying to make the script more reusable by guessing the
# package name from the current directory, instead of hard coding.
# But this relies on some conventions in naming.  You'll have to call
# your class "Main" and use our recommended package structure.
# (You're free to change that, and then change the ./run script in
# your handin to match.)

 PROJECT=ebwhite
 PKG=scavenger
 PID_FILE=/var/trtl/trtl-pid

# Find the package that Main is in. TAs use "staff", students should
# rename it to their own username. In the file names and in the source!
MAIN=$(ls ${PROJ}src/main/java/edu/brown/cs/$PROJECT/$PKG/Main.java)
 [ -z "$MAIN" ] && echo "You need a Main.java" && exit 1
 [ ! -e "$MAIN" ] && echo "You can only have one Main.java" && exit 1

# The funny symbol: "$@" passes the command-line arguments on from
# this script to your Java program.
cd /var/trtl/;
if [ ! -f $PID_FILE ]; then
  nohup sudo java -ea -cp $TARGET:$CP edu.brown.cs.$PROJECT.$PKG.Main "$@" 2>> /var/trtl/trtl.log >> /var/trtl/trtl.log &
  sudo echo $! > $PID_FILE
fi
