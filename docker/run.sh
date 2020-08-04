#!/bin/bash

docker build --file sql/Dockerfile -t pms_sql ..
docker build --file pms/Dockerfile -t pms ..

docker network create pms_net

docker run -d --network pms_net -p 3306:3306 --name pms_sql pms_sql
docker run -it --rm --network pms_net -p 7575:7575 -p 7576:7576 -p 7577:7577 -p 7578:7578 -p 8484:8484 --name pms pms bash

docker rm -f pms_sql

docker network rm pms_net
