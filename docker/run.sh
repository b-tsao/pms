#!/bin/bash

docker build --file sql/Dockerfile -t pms_sql ..
docker build --file pms/Dockerfile -t pms ..

docker network create pms_net

docker run -d --name pms_sql --network pms_net -v /home/pewpew/pms/data:/var/lib/mysql pms_sql
docker run -it --rm --name pms --network pms_net -p 8484:8484 -p 8787:8787 -p 8585:8585 -p 8586:8586 -p 8587:8587 -p 8588:8588 -v /home/pewpew/pms/dist:/pms/dist -v /home/pewpew/pms/wz:/pms/wz -v /home/pewpew/pms/scripts:/pms/scripts pms bash

docker rm -vf pms_sql

docker network rm pms_net
