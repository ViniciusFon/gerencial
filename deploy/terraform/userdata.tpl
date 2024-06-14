#!/bin/bash
#install docker
    sudo apt-get update -y &&
    sudo apt-get install -y \
        apt-transport-https \
        ca-certificates \
        curl \
        gnupg-agent \
        software-properties-common &&
        curl -fsSL https://download.docker.com/linux/ubuntu/gpg | sudo apt-key add - &&
    sudo add-apt-repository "deb [arch=amd64] https://download.docker.com/linux/ubuntu $(lsb_release -cs) stable" &&
    sudo apt-get update -y &&
    sudo sudo apt-get install docker-ce docker-ce-cli containerd.io -y &&
    sudo usermod -aG docker ubuntu
    sudo systemctl enable docker.service
    sudo systemctl enable containerd.service

#install docker compose
    sudo apt install docker-compose -y

#access user root
    sudo su

#create project folder
    mkdir ~/project
    cd ~/project

    git clone https://github.com/ViniciusFon/gerencial.git
    cd gerencial
    git checkout testeAcesso
    git pull
    cd deploy/docker

#run your docker compose file
    docker-compose -f docker-compose-aws.yml up -d
