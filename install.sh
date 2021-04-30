#!/bin/bash
apt-get -y update
apt-get -y upgrade
apt-get -y install default-jdk
apt-get -y install openjfx
cp ./raspberrypi/boot/ /boot
mdkir ~/.config/autostart
cp ./raspberrypi/auretim.desktop ~/.config/autostart/ 
reboot
