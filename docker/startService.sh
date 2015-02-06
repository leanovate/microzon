#!/bin/sh

CMD="docker run -d -P --name=$2 --hostname=$2 --link consul1:consul1 --link consul2:consul2 --link consul3:consul3 untoldwind/dose:$1-v5"
echo "$2: $CMD"
$CMD
