#!/bin/bash
apt-get -y update
apt-get -y upgrade
apt-get -y install default-jdk
apt-get -y install openjfx
apt-get -y install espeak-ng
cp -r ./raspberrypi/boot/* /boot
mkdir /home/pi/.config/autostart
cp ./raspberrypi/auretim.desktop /home/pi/.config/autostart/
mkdir /usr/share/mbrola
cp -r ./mbrola/* /usr/share/mbrola/ 
reboot
