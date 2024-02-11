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
  id("com.google.devtools.ksp") version "1.9.22-1.0.16"
}

group = "dev.lfstech"
version = "0.0.1"

application {
  mainClass.set("io.ktor.server.cio.EngineMain")
}

repositories {
  mavenCentral()
}

dependencies {
  implementation("io.ktor:ktor-server-core-jvm")
  implementation("io.ktor:ktor-server-content-negotiation-jvm")
  implementation("io.ktor:ktor-serialization-kotlinx-json-jvm")
  implementation("io.ktor:ktor-server-cio-jvm")
  implementation("io.ktor:ktor-server-status-pages-jvm")
  implementation("io.ktor:ktor-server-request-validation")
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
