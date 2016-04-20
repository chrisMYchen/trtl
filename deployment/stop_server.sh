PID_FILE=/var/trtl/trtl-pid
if [ -f $PID_FILE ]; then
  PID=$(sudo cat $PID_FILE);
  sudo kill $PID
  sudo rm -f $PID_FILE
fi
