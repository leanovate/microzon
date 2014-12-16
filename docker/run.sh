#!/bin/sh

docker stop $(docker ps -a -q)
docker rm $(docker ps -a -q)

docker run -d -p 8000:80 -P --name="log" "untoldwind/dose:log-v1"
docker run -d -p 8001:80 -P --name="zipkin" "untoldwind/dose:zipkin-v1"
docker run -d -P --name="mysql" --link log:log-collector "untoldwind/dose:mysql-v1" 
docker run -d -P --name="customer" --link mysql:mysql-db --link zipkin:zipkin-collector --link log:log-collector "untoldwind/dose:customer-v1" 
docker run -d -p 8090:80 -P --name="product" --link zipkin:zipkin-collector --link log:log-collector "untoldwind/dose:product-v1" 
