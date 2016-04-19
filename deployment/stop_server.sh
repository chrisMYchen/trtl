PID=$(cat /var/trtl/pid.txt)
kill $PID
rm -f /var/trtl/pid.txt
