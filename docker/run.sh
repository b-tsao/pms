#!/bin/bash

SERVER_DIR="/home/pewpew/pms/server"
DB_DIR="/home/pewpew/pms"

docker build --file sql/Dockerfile -t pms_sql $SERVER_DIR
docker build --file server/Dockerfile -t pms $SERVER_DIR

docker network create pms_net

docker run -d --name pms_sql --network pms_net -v ${DB_DIR}/db:/var/lib/mysql pms_sql
docker run -t -d --name pms --network pms_net \
	-p 8484:8484 -p 8787:8787 -p 8585:8585 -p 8586:8586 -p 8587:8587 -p 8588:8588 \
	-v ${SERVER_DIR}/dist:/pms/dist -v ${SERVER_DIR}/wz:/pms/wz -v ${SERVER_DIR}/scripts:/pms/scripts \
	pms bash

docker exec -u 0 -it pms bash
