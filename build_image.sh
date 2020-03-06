docker container kill  enhancer_web_service_container

docker container rm enhancer_web_service_container

docker image rm phoenixenhancer/enhancer-web_service

docker build  --network=host -t  phoenixenhancer/enhancer-web-service .
#docker build   -t  phoenixenhancer/enhancer-webservice .
