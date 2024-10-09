FROM drogonframework/drogon

WORKDIR /app

EXPOSE 4000

COPY . .

ARG COMPILE_OPTIONS="-DCMAKE_EXPORT_COMPILE_COMMANDS=1 -DCMAKE_BUILD_TYPE=Debug"
RUN cmake ${COMPILE_OPTIONS} -S /app/ -B /app/build && cmake --build /app/build

ENTRYPOINT ["/app/build/Identity_Service"]