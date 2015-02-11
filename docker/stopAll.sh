#!/bin/bash

CMD="docker stop $(docker ps -a -q | tr "\\n" " ")"
echo "Stop all: $CMD"
$CMD
CMD="docker rm $(docker ps -a -q | tr "\\n" " ")"
echo "Remove all: $CMD"
$CMD
