#!/usr/bin/env sh

mvn clean install
unzip target/message-app-0.0.1-SNAPSHOT.jar -d target/message-app
native-image --delay-class-initialization-to-runtime=io.netty.handler.codec.http.HttpObjectEncoder,org.springframework.core.io.VfsUtils,io.netty.handler.ssl.JdkNpnApplicationProtocolNegotiator,io.netty.handler.ssl.ReferenceCountedOpenSslEngine,io.netty.handler.codec.http.websocketx.WebSocket00FrameEncoder -H:ReflectionConfigurationFiles=graal.json -Dio.netty.noUnsafe=true -Dio.netty.noJdkZlibDecoder=true -Dio.netty.noJdkZlibEncoder=true -H:+ReportUnsupportedElementsAtRuntime -Dfile.encoding=UTF-8 -cp ".:$(echo target/message-app/BOOT-INF/lib/*.jar | tr ' ' ':')":target/message-app/BOOT-INF/classes sample.MessageAppApplicationKt
