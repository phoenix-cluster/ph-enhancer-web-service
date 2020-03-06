From openjdk:8-jdk-alpine 

RUN mkdir -p /code
WORKDIR /code/
COPY ./target/*.jar ./
# apk add openjdk8 curl busybox tzdata && 
# cp /usr/share/zoneinfo/Asia/Shanghai /etc/localtime && 
# echo Asia/Shanghai > /etc/timezone && 
# apk del tzdata && 

