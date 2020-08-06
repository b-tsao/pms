#!/bin/bash

docker build --file sql/Dockerfile -t vintage_sql ..
docker build --file vintage/Dockerfile -t vintage ..

docker network create vintage_net

docker run -d --network vintage_net --name vintage_sql vintage_sql
docker run -it --rm --network vintage_net -p 8484:8484 -p 7575:7575 -p 7576:7576 --name vintage vintage bash

docker rm -f vintage_sql

docker network rm vintage_net
