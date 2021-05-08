#!/bin/bash
apt-get -y update
apt-get -y upgrade
apt-get -y install default-jdk
apt-get -y install openjfx
apt-get -y install espeak-ng
apt-get -y install maven
cp -r ./raspberrypi/boot/* /boot
mkdir /home/pi/.config/autostart
cp ./raspberrypi/auretim.desktop /home/pi/.config/autostart/
mkdir /usr/share/mbrola
cp -r ./mbrolaVoices/* /usr/share/mbrola/ 
apt-get -y install git make gcc
git clone https://github.com/numediart/MBROLA.git
cd MBROLA
make
cp Bin/mbrola /usr/bin/mbrola
mv ../AuReTimExtension/ ../AuReTim
reboot
