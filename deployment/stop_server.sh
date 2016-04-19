PID=$(cat pid.txt)
kill $PID
rm -f pid.txt
