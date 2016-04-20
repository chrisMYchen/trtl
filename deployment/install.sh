#!/bin/bash
sudo chmod 775 /var/trtl/
sudo chgrp ec2-user /var/trtl/
sudo chgrp ec2-user /var/trtl/
sudo setfacl -dm u::rwx,g::rwx,o::r /var/trtl/
