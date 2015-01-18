#!/bin/sh

docker build -t "untoldwind/dose:base-v4" base
docker build -t "untoldwind/dose:consul-v1" consul
docker build -t "untoldwind/dose:consul-boot-v1" consul-boot
docker build -t "untoldwind/dose:log-v4" log
docker build -t "untoldwind/dose:zipkin-v4" zipkin
docker build -t "untoldwind/dose:mysql-v4" mysql
docker build -t "untoldwind/dose:mongo-v1" mongo
docker build -t "untoldwind/dose:customer-v4" customer
docker build -t "untoldwind/dose:product-v4" product
docker build -t "untoldwind/dose:cart-v4" cart
docker build -t "untoldwind/dose:billing-v4" billing
docker build -t "untoldwind/dose:web-v4" web

docker images --no-trunc | grep none | awk '{print $3}' | xargs docker rmi
