#!/bin/sh

docker build -t "untoldwind/dose:base-v4" base
docker build -t "untoldwind/dose:consul-v2" consul
docker build -t "untoldwind/dose:consul-boot-v2" consul-boot
docker build -t "untoldwind/dose:log-v5" log
docker build -t "untoldwind/dose:zipkin-v4" zipkin
docker build -t "untoldwind/dose:mysql-v5" mysql
docker build -t "untoldwind/dose:mongo-v2" mongo

docker build -t "untoldwind/dose:customer-v5" customer
docker build -t "untoldwind/dose:product-v5" product
docker build -t "untoldwind/dose:cart-v5" cart
docker build -t "untoldwind/dose:billing-v5" billing
docker build -t "untoldwind/dose:web-v5" web

docker images --no-trunc | grep none | awk '{print $3}' | xargs docker rmi
