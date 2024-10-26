FROM drogonframework/drogon:latest

WORKDIR /app

EXPOSE 4000

RUN apt-get update -yqq && apt-get install -yqq inotify-tools dos2unix

COPY . .

RUN dos2unix /app/monitor_logs.sh /app/start_service.sh

ARG COMPILE_OPTIONS="-DCMAKE_EXPORT_COMPILE_COMMANDS=1 -DCMAKE_BUILD_TYPE=Debug"
RUN cmake ${COMPILE_OPTIONS} -S /app/ -B /app/build && cmake --build /app/build

CMD ["/bin/bash", "/app/start_service.sh"]