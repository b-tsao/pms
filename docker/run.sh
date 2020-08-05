#!/bin/bash

docker build --file sql/Dockerfile -t pms_sql ..
docker build --file pms/Dockerfile -t pms ..

docker network create pms_net

docker run -d --network pms_net --name pms_sql pms_sql
docker run -it --rm --network pms_net -p 8383:8383 --name pms pms bash

docker rm -f pms_sql

docker network rm pms_net
