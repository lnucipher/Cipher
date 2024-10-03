FROM ubuntu:22.04 AS build

WORKDIR /app

EXPOSE 4000

RUN apt-get update -yqq \
&& apt-get install -yqq --no-install-recommends cmake curl g++ gcc git \
libbrotli-dev libc-ares-dev libhiredis-dev libjsoncpp-dev libssl-dev locales \
make openssl pkg-config postgresql-server-dev-all software-properties-common \
sudo uuid-dev wget zlib1g-dev \
&& rm -rf /var/lib/apt/lists/* \
&& locale-gen en_US.UTF-8

COPY ./Identity.Service .

ARG COMPILE_OPTIONS="-DCMAKE_EXPORT_COMPILE_COMMANDS=1 -DCMAKE_BUILD_TYPE=Debug"
RUN cmake ${COMPILE_OPTIONS} -S /app/ -B /app/build && cmake --build /app/build

ENTRYPOINT ["/app/build/Identity_Service"]