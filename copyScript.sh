#!/bin/bash
rm -r ./target/
ssh pi@192.168.178.46 "rm -f -r /home/pi/BachelorArbeit/AuReTim/*"
scp -r ./* pi@192.168.178.46:/home/pi/BachelorArbeit/AuReTim/
ssh pi@192.168.178.46 "
cd /home/pi/BachelorArbeit/AuReTim/
mvn install
export DISPLAY=:0
bash ./auretim.sh
exit"
