#!/bin/bash

docker build --file sql/Dockerfile -t pms_sql ..
docker build --file pms/Dockerfile -t pms ..

docker network create pms_net

docker run -d --network pms_net -p 3306:3306 --name pms_sql pms_sql
docker run -it --rm --network pms_net -p 8484:8484 -p 8787:8787 -p 8383:8383 -p 8585:8585 -p 8586:8586 -p 8587:8587 -p 8588:8588 --name pms pms bash

docker rm -f pms_sql

docker network rm pms_net
