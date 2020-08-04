#!/bin/bash

docker rmi pms_data

docker build --file Dockerfile -t pms_data ..

docker run -it --rm --name pms_data pms_data bash
