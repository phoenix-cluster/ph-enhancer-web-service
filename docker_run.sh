#########################################################################
# File Name: run.sh
# Created Time: Thu 08 Aug 2019 11:28:00 AM CST
#########################################################################
#!/bin/bash
docker container kill  enhancer_web_service_container

docker container rm enhancer_web_service_container

docker container run \
  -it \
  -d \
  --name enhancer_web_service_container \
  -p 8080:8070 \
  phoenixenhancer/enhancer-web-service


docker cp application-docker.yml enhancer_web_service_container:/code/application.yml

docker exec enhancer_web_service_container sh -c "java -jar phoenix-cluster-enhancer-webservice-0.4.0.jar --spring.config.location=/code/application.yml"
