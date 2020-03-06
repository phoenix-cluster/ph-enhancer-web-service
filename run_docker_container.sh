#########################################################################
# File Name: run.sh
# Created Time: Thu 08 Aug 2019 11:28:00 AM CST
#########################################################################
#!/bin/bash
docker container stop  enhancer_web_service_container;
docker container kill  enhancer_web_service_container;
docker container rm enhancer_web_service_container;
#docker container run \
#  -d \
#  --name enhancer_web_service_container \
#  -p 0.0.0.0:8080:8070 \
#  phoenixenhancer/enhancer-web-service
docker container run -d --name enhancer_web_service_container -p 8080:8070 phoenixenhancer/enhancer-web-service tail -f /dev/null;
#  -it \
#tail -f /dev/null

#copy configure file and run
docker cp  src/main/resources-bak/application-docker.yml enhancer_web_service_container:/code;
docker exec enhancer_web_service_container sh -c "java -jar phoenix-cluster-enhancer-webservice-0.4.0.jar --spring.config.location=/code/application-docker.yml";
