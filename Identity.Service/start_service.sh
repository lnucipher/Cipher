#!/bin/bash

mkdir -p /app/build/log && touch /app/build/identity.service.log

sudo chmod +x /app/monitor_logs.sh
sudo /app/monitor_logs.sh &

/app/build/service/Identity_Service 4000