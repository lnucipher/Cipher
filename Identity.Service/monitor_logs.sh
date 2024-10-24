#!/bin/bash

MONITOR_DIR="/app/build/log/"
OUTPUT_FILE="/app/build/identity.service.log"

ln -sf /proc/1/fd/1 $OUTPUT_FILE

for FILE in "$MONITOR_DIR"/*
do
    if [ -f "$FILE" ]; then
        tail -n0 -f "$FILE" >> "$OUTPUT_FILE" &
    fi
done

inotifywait -m -e create "$MONITOR_DIR" --format '%w%f' | while read NEW_FILE
do
    tail -f "$NEW_FILE" >> "$OUTPUT_FILE" &
done