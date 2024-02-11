val ktorVersion: String by project
val kotlinVersion: String by project
val logbackVersion: String by project
val koinVersion: String by project
val ktormVersion: String by project
val komapperVersion: String by project

plugins {
  kotlin("jvm") version "1.9.22"

  id("io.ktor.plugin") version "2.3.7"
  id("org.jetbrains.kotlin.plugin.serialization") version "1.9.22"
  id("org.graalvm.buildtools.native") version "0.9.28"
  id("com.google.devtools.ksp") version "1.9.22-1.0.16"
}

group = "dev.lfstech"
version = "0.0.1"

application {
  mainClass.set("io.ktor.server.cio.EngineMain")
}

graalvmNative {
  binaries {
    named("main") {
      fallback.set(false)
      verbose.set(true)

      buildArgs.add("--initialize-at-build-time=kotlinx.serialization.modules.SerializersModuleKt")
      buildArgs.add("--initialize-at-build-time=kotlinx.serialization.json.Json")
      buildArgs.add("--initialize-at-build-time=ch.qos.logback")
      buildArgs.add("--initialize-at-build-time=io.ktor,kotlin")
      buildArgs.add("--initialize-at-build-time=org.slf4j.LoggerFactory")

      buildArgs.add("--gc=G1")
      buildArgs.add("-H:+InstallExitHandlers")
      buildArgs.add("-H:+ReportUnsupportedElementsAtRuntime")
      buildArgs.add("-H:+ReportExceptionStackTraces")

      imageName.set("graalvm-server")
    }

//    all {
//      resources.autodetect()
//    }
  }
}

repositories {
  mavenCentral()
}

dependencies {
  implementation("io.ktor:ktor-server-core-jvm:$ktorVersion")
  implementation("io.ktor:ktor-server-content-negotiation-jvm:$ktorVersion")
  implementation("io.ktor:ktor-serialization-kotlinx-json-jvm:$ktorVersion")
  implementation("io.ktor:ktor-server-cio-jvm:$ktorVersion")
  implementation("io.ktor:ktor-server-status-pages-jvm:$ktorVersion")
  implementation("io.ktor:ktor-server-request-validation:$ktorVersion")
  implementation("ch.qos.logback:logback-classic:$logbackVersion")

  platform("org.komapper:komapper-platform:$komapperVersion").let {
    implementation(it); ksp(it)
  }

  implementation("org.komapper:komapper-starter-jdbc:$komapperVersion")
  implementation("org.komapper:komapper-dialect-postgresql-jdbc:$komapperVersion")
  ksp("org.komapper:komapper-processor:$komapperVersion")

  implementation("com.zaxxer:HikariCP:5.1.0")
  implementation("org.postgresql:postgresql:42.7.1")

  implementation("io.insert-koin:koin-core:$koinVersion")
  implementation("io.insert-koin:koin-ktor:$koinVersion")

  testImplementation("io.ktor:ktor-server-tests-jvm:$logbackVersion")
  testImplementation("org.jetbrains.kotlin:kotlin-test-junit:$kotlinVersion")
}
