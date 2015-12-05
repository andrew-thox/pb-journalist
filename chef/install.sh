#!/bin/bash
cd ~/
wget https://opscode-omnibus-packages.s3.amazonaws.com/ubuntu/12.04/x86_64/chefdk_0.9.0-1_amd64.deb
sudo dpkg -i chefdk_*.deb
