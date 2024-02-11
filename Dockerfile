FROM ubuntu:mantic

COPY build/native/nativeCompile/graalvm-server /graalvm-server

CMD ["/graalvm-server"]
