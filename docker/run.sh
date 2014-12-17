#!/bin/sh

docker stop $(docker ps -a -q)
docker rm $(docker ps -a -q)

docker run -d -p 8000:80 -P --name="log" "untoldwind/dose:log-v1"
docker run -d -p 8001:80 -P --name="zipkin" "untoldwind/dose:zipkin-v1"

# Uggly but necessary atm
sleep 5

docker run -d -p 3306:3306 -P --name="mysql" --link log:log-collector "untoldwind/dose:mysql-v2" 
docker run -d -P --name="customer" --link mysql:mysql-db --link zipkin:zipkin-collector --link log:log-collector "untoldwind/dose:customer-v1" 
docker run -d -p 8090:80 -P --name="product" --link zipkin:zipkin-collector --link log:log-collector "untoldwind/dose:product-v1" 
docker run -d -P --name="cart" --link product:product-service --link customer:customer-service --link mysql:mysql-db --link zipkin:zipkin-collector --link log:log-collector "untoldwind/dose:cart-v1"
docker run -d -P --name="billing" --link cart:cart-service --link product:product-service --link customer:customer-service --link mysql:mysql-db --link zipkin:zipkin-collector --link log:log-collector "untoldwind/dose:billing-v1"
docker run -d -p 8080:80 -P --name="web" --link cart:cart-service --link product:product-service --link customer:customer-service --link billing:billing-service --link mysql:mysql-db --link zipkin:zipkin-collector --link log:log-collector "untoldwind/dose:web-v1"
