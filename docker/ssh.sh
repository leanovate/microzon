#!/bin/bash

port=$(docker port $1 22)
ssh -o UserKnownHostsFile=/dev/null -o StrictHostKeyChecking=no -l root $(boot2docker ip) -p ${port#0.0.0.0:} $2 $3 $4 $5
