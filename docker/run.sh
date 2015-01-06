#!/bin/sh

docker stop $(docker ps -a -q)
docker rm $(docker ps -a -q)

docker run -d -p 8002:80 -P --name="consul1" --hostname="consul1" "untoldwind/dose:consul-boot-v1"
docker run -d -P --name="consul2" --link consul1:consul1 --hostname="consul2" "untoldwind/dose:consul-v1"
docker run -d -P --name="consul3" --link consul1:consul1 --hostname="consul3" "untoldwind/dose:consul-v1"
docker run -d -p 8000:80 -P --name="log" --hostname="log" "untoldwind/dose:log-v2"
docker run -d -p 8001:80 -P --name="zipkin" --hostname="zipkin" "untoldwind/dose:zipkin-v1"

# Uggly but necessary atm
sleep 5

docker run -d -p 3306:3306 -P --name="mysql" --link log:log-collector --hostname="mysql" "untoldwind/dose:mysql-v3" 
docker run -d -P --name="customer" --link mysql:mysql-db --link zipkin:zipkin-collector --link log:log-collector --hostname="customer" "untoldwind/dose:customer-v2" 
docker run -d -p 8090:80 -P --name="product" --link zipkin:zipkin-collector --link log:log-collector --hostname="product" "untoldwind/dose:product-v2" 
docker run -d -P --name="cart" --link product:product-service --link customer:customer-service --link mysql:mysql-db --link zipkin:zipkin-collector --link log:log-collector --hostname="cart" "untoldwind/dose:cart-v2"
docker run -d -P --name="billing" --link cart:cart-service --link product:product-service --link customer:customer-service --link mysql:mysql-db --link zipkin:zipkin-collector --link log:log-collector --hostname="billing" "untoldwind/dose:billing-v2"
docker run -d -p 8080:80 -P --name="web" --link cart:cart-service --link product:product-service --link customer:customer-service --link billing:billing-service --link mysql:mysql-db --link zipkin:zipkin-collector --link log:log-collector --hostname="web" "untoldwind/dose:web-v2"
