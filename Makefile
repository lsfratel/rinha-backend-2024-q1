SHELL = /bin/bash
.ONESHELL:
.DEFAULT_GOAL: jar.build

jar.build:
	@sh gradlew buildFatJar

native.build:
	@native-image \
     	-H:ConfigurationFileDirectories=native/configuration \
     	-H:+InstallExitHandlers \
     	-H:+ReportUnsupportedElementsAtRuntime \
     	-H:+ReportExceptionStackTraces \
     	-R:MaxRAMPercentage=90 \
     	--pgo=native/default.iprof \
     	-march=native \
     	--gc=G1 \
     	--no-fallback \
     	--enable-sbom \
     	--initialize-at-build-time=io.ktor,kotlin,org.slf4j,ch.qos.logback,kotlinx.serialization \
     	-jar build/libs/rinha-2024-q1-all.jar \
     	native/rinha-2024-q1

native.start:
	@PORT=9999 native/rinha-2024-q1

docker.stats:
	@docker stats --format "table {{.Name}}\t{{.CPUPerc}}\t{{.MemUsage}}\t{{.MemPerc}}"

docker.up:
	@docker compose down
	@docker compose up

stress.it:
	@sh executar-teste-local.sh

docker.build:
	@docker buildx build --platform linux/amd64 -t lsfratel/rinha-2024-q1:latest .

docker.push:
	@docker push lsfratel/rinha-2024-q1
