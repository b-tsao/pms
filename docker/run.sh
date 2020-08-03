#!/bin/bash

docker build --file sql/Dockerfile -t pms_sql ..
docker build --file pms/Dockerfile -t pms ..

PMS_MYSQL_CONTAINER_ID=`docker run -d -p 3306:3306 --name pms_sql pms_sql`

PMS_MYSQL_CONTAINER_IP_ADDRESS=`docker inspect --format '{{ .NetworkSettings.IPAddress }}' $PMS_MYSQL_CONTAINER_ID`

docker run -it --rm -e PMS_MYSQL_CONTAINER_IP_ADDRESS=$PMS_MYSQL_CONTAINER_IP_ADDRESS -p 7575:7575 -p 7576:7576 -p 7577:7577 -p 7578:7578 -p 8484:8484 --name pms pms bash

docker rm -f $PMS_MYSQL_CONTAINER_ID
