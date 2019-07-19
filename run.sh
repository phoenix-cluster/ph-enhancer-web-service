#########################################################################
# File Name: run.sh
# Created Time: Mon 21 Jan 2019 05:11:21 PM CST
#########################################################################
#!/bin/bash
cp src/main/resources-bak/application-dev.yml src/main/resources/. ;
mvn spring-boot:run
