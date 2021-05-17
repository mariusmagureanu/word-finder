FROM openjdk:17-slim
LABEL maintainer="marius@archlinux.live"
LABEL version="0.1"
LABEL about="Word finder application"

COPY ./src/ /src/
WORKDIR /src/
RUN javac com/word/*.java

CMD ["java", "com.word.Main", "/test-data"]
