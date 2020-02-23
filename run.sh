#########################################################################
# File Name: run.sh
# Created Time: Thu 08 Aug 2019 11:28:00 AM CST
#########################################################################
#!/bin/bash


cp src/main/resources-bak/application-dev.yml ./application.yml

java -jar target/phoenix-cluster-enhancer-webservice-0.4.0.jar --spring.config.location=./application.yml
