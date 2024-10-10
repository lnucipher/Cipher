#!/bin/bash

mkdir -p /app/build/log && touch /app/build/identity.service.log

sudo /app/monitor_logs.sh &

/app/build/Identity_Service 4000