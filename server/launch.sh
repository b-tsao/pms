#!/bin/bash

LOGS_DIR="logs"

mkdir -p $LOGS_DIR

timestamp=$(date +%Y%m%d%H%M%S)

./launch_center.sh > ${LOGS_DIR}/center-${timestamp}.log 2>&1 &
./launch_login.sh > ${LOGS_DIR}/login-${timestamp}.log 2>&1 &
./launch_shop.sh > ${LOGS_DIR}/shop-${timestamp}.log 2>&1 &
./launch_game0.sh > ${LOGS_DIR}/game0-${timestamp}.log 2>&1 &
