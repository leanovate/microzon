#!/bin/sh

docker build -t "untoldwind/dose:base-v3" base
docker build -t "untoldwind/dose:consul-v1" consul
docker build -t "untoldwind/dose:consul-boot-v1" consul-boot
docker build -t "untoldwind/dose:log-v3" log
docker build -t "untoldwind/dose:zipkin-v3" zipkin
docker build -t "untoldwind/dose:mysql-v4" mysql
docker build -t "untoldwind/dose:customer-v3" customer
docker build -t "untoldwind/dose:product-v3" product
docker build -t "untoldwind/dose:cart-v3" cart
docker build -t "untoldwind/dose:billing-v3" billing
docker build -t "untoldwind/dose:web-v3" web
