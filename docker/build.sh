#!/bin/sh

docker build -t "untoldwind/dose:base-v1" base
docker build -t "untoldwind/dose:log-v1" log
docker build -t "untoldwind/dose:zipkin-v1" zipkin
docker build -t "untoldwind/dose:mysql-v2" mysql
docker build -t "untoldwind/dose:customer-v1" customer
docker build -t "untoldwind/dose:product-v1" product
docker build -t "untoldwind/dose:cart-v1" cart
docker build -t "untoldwind/dose:billing-v1" billing
docker build -t "untoldwind/dose:web-v1" web
